const express = require('express');
const path = require('path');
const fs = require('fs');
const crypto = require('crypto');
const nodemailer = require('nodemailer');
const { Pool } = require('pg');

const app = express();
const PORT = Number(process.env.PORT || 3000);
const ROOT = __dirname;
const DATA_DIR = path.join(ROOT, 'data');
const CONTENT_FILE = path.join(DATA_DIR, 'content.json');
const ENQUIRIES_FILE = path.join(DATA_DIR, 'enquiries.json');
const ANALYTICS_FILE = path.join(DATA_DIR, 'analytics.json');
const SITE_URL = (process.env.SITE_URL || `http://localhost:${PORT}`).replace(/\/$/, '');
const AUTH_SECRET = process.env.AUTH_SECRET || 'development-only-change-me';
const OWNER_PASSWORD = process.env.OWNER_PASSWORD || '';
const COOKIE_NAME = 'bsm_owner';
const isProduction = process.env.NODE_ENV === 'production';

app.disable('x-powered-by');
app.set('trust proxy', 1);
app.use(express.json({ limit: '300kb' }));
app.use(express.urlencoded({ extended: false, limit: '300kb' }));
app.use((req, res, next) => {
  res.setHeader('X-Content-Type-Options', 'nosniff');
  res.setHeader('X-Frame-Options', 'SAMEORIGIN');
  res.setHeader('Referrer-Policy', 'strict-origin-when-cross-origin');
  res.setHeader('Permissions-Policy', 'camera=(), microphone=(), geolocation=()');
  next();
});
app.use(express.static(path.join(ROOT, 'public'), { maxAge: isProduction ? '7d' : 0, extensions: ['html'] }));

fs.mkdirSync(DATA_DIR, { recursive: true });
if (!fs.existsSync(ENQUIRIES_FILE)) fs.writeFileSync(ENQUIRIES_FILE, '[]\n');
if (!fs.existsSync(ANALYTICS_FILE)) fs.writeFileSync(ANALYTICS_FILE, '[]\n');

const pool = process.env.DATABASE_URL ? new Pool({ connectionString: process.env.DATABASE_URL, ssl: process.env.PGSSLMODE === 'disable' ? false : { rejectUnauthorized: false } }) : null;

async function initDb() {
  if (!pool) return;
  await pool.query(`
    CREATE TABLE IF NOT EXISTS app_content (
      key TEXT PRIMARY KEY,
      value JSONB NOT NULL,
      updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );
    CREATE TABLE IF NOT EXISTS enquiries (
      id UUID PRIMARY KEY,
      kind TEXT NOT NULL,
      name TEXT NOT NULL,
      business_name TEXT,
      email TEXT NOT NULL,
      phone TEXT,
      preferred_contact TEXT,
      project_type TEXT,
      features JSONB,
      budget TEXT,
      timeline TEXT,
      existing_website TEXT,
      description TEXT NOT NULL,
      status TEXT NOT NULL DEFAULT 'New',
      private_note TEXT NOT NULL DEFAULT '',
      fingerprint TEXT NOT NULL,
      created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
      updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );
    CREATE INDEX IF NOT EXISTS enquiries_created_at_idx ON enquiries(created_at DESC);
    CREATE INDEX IF NOT EXISTS enquiries_status_idx ON enquiries(status);
    CREATE UNIQUE INDEX IF NOT EXISTS enquiries_fingerprint_recent_idx ON enquiries(fingerprint, created_at);
    CREATE TABLE IF NOT EXISTS analytics_events (
      id BIGSERIAL PRIMARY KEY,
      event TEXT NOT NULL,
      path TEXT NOT NULL,
      meta JSONB NOT NULL DEFAULT '{}'::jsonb,
      created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );
    CREATE INDEX IF NOT EXISTS analytics_event_idx ON analytics_events(event, created_at DESC);
  `);
  const existing = await pool.query(`SELECT value FROM app_content WHERE key='content' LIMIT 1`);
  if (!existing.rowCount) {
    const initial = JSON.parse(fs.readFileSync(CONTENT_FILE, 'utf8'));
    await pool.query(`INSERT INTO app_content(key,value) VALUES('content',$1::jsonb)`, [JSON.stringify(initial)]);
  }
}

function readLocalJson(file, fallback) {
  try { return JSON.parse(fs.readFileSync(file, 'utf8')); } catch { return fallback; }
}
function writeLocalJson(file, value) {
  const temp = `${file}.tmp`;
  fs.writeFileSync(temp, JSON.stringify(value, null, 2) + '\n');
  fs.renameSync(temp, file);
}
async function getContent() {
  if (pool) {
    const r = await pool.query(`SELECT value FROM app_content WHERE key='content' LIMIT 1`);
    return r.rows[0]?.value || {};
  }
  return readLocalJson(CONTENT_FILE, {});
}
async function saveContent(value) {
  if (pool) {
    await pool.query(`INSERT INTO app_content(key,value,updated_at) VALUES('content',$1::jsonb,NOW()) ON CONFLICT(key) DO UPDATE SET value=EXCLUDED.value, updated_at=NOW()`, [JSON.stringify(value)]);
  } else {
    writeLocalJson(CONTENT_FILE, value);
  }
}
async function listEnquiries() {
  if (pool) {
    const r = await pool.query(`SELECT * FROM enquiries ORDER BY created_at DESC`);
    return r.rows.map(row => ({ ...row, createdAt: row.created_at, updatedAt: row.updated_at, businessName: row.business_name, preferredContact: row.preferred_contact, projectType: row.project_type, existingWebsite: row.existing_website, privateNote: row.private_note }));
  }
  return readLocalJson(ENQUIRIES_FILE, []);
}
async function insertEnquiry(e) {
  if (pool) {
    await pool.query(`INSERT INTO enquiries(id,kind,name,business_name,email,phone,preferred_contact,project_type,features,budget,timeline,existing_website,description,status,private_note,fingerprint) VALUES($1,$2,$3,$4,$5,$6,$7,$8,$9::jsonb,$10,$11,$12,$13,'New','',$14)`, [e.id,e.kind,e.name,e.businessName,e.email,e.phone,e.preferredContact,e.projectType,JSON.stringify(e.features||[]),e.budget,e.timeline,e.existingWebsite,e.description,e.fingerprint]);
    return;
  }
  const rows = readLocalJson(ENQUIRIES_FILE, []);
  rows.unshift(e);
  writeLocalJson(ENQUIRIES_FILE, rows.slice(0, 2000));
}
async function updateEnquiry(id, patch) {
  if (pool) {
    const allowedStatus = ['New','Contacted','Quoted','Won','Closed'];
    if (patch.status && !allowedStatus.includes(patch.status)) throw new Error('Invalid status');
    await pool.query(`UPDATE enquiries SET status=COALESCE($2,status), private_note=COALESCE($3,private_note), updated_at=NOW() WHERE id=$1`, [id, patch.status || null, typeof patch.privateNote === 'string' ? patch.privateNote.slice(0,5000) : null]);
    return;
  }
  const rows = readLocalJson(ENQUIRIES_FILE, []);
  const index = rows.findIndex(x => x.id === id);
  if (index < 0) throw new Error('Not found');
  if (patch.status) rows[index].status = patch.status;
  if (typeof patch.privateNote === 'string') rows[index].privateNote = patch.privateNote.slice(0,5000);
  rows[index].updatedAt = new Date().toISOString();
  writeLocalJson(ENQUIRIES_FILE, rows);
}

function parseCookies(req) {
  const out = {};
  String(req.headers.cookie || '').split(';').forEach(part => {
    const i = part.indexOf('=');
    if (i > -1) out[part.slice(0,i).trim()] = decodeURIComponent(part.slice(i+1).trim());
  });
  return out;
}
function signToken(payload) {
  const raw = Buffer.from(JSON.stringify(payload)).toString('base64url');
  const sig = crypto.createHmac('sha256', AUTH_SECRET).update(raw).digest('base64url');
  return `${raw}.${sig}`;
}
function verifyToken(token) {
  if (!token || !token.includes('.')) return null;
  const [raw, sig] = token.split('.');
  const expected = crypto.createHmac('sha256', AUTH_SECRET).update(raw).digest('base64url');
  if (sig.length !== expected.length || !crypto.timingSafeEqual(Buffer.from(sig), Buffer.from(expected))) return null;
  try {
    const payload = JSON.parse(Buffer.from(raw, 'base64url').toString('utf8'));
    if (!payload.exp || Date.now() > payload.exp) return null;
    return payload;
  } catch { return null; }
}
function ownerOnly(req, res, next) {
  const token = verifyToken(parseCookies(req)[COOKIE_NAME]);
  if (!token?.owner) return res.status(401).json({ ok:false, error:'Authentication required.' });
  next();
}

const rateBuckets = new Map();
function rateLimit(key, max, windowMs) {
  const now = Date.now();
  const list = (rateBuckets.get(key) || []).filter(t => now - t < windowMs);
  if (list.length >= max) return false;
  list.push(now); rateBuckets.set(key, list); return true;
}
function clean(value, max=2000) { return String(value ?? '').trim().replace(/\u0000/g,'').slice(0,max); }
function validEmail(s) { return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(s); }
const CONTACT_METHODS = ['Email', 'Phone', 'WhatsApp'];
function fingerprint(body) {
  return crypto.createHash('sha256').update([clean(body.email,200).toLowerCase(),clean(body.name,200).toLowerCase(),clean(body.description,1000).toLowerCase()].join('|')).digest('hex');
}

function getTransport() {
  if (!process.env.SMTP_HOST || !process.env.SMTP_USER || !process.env.SMTP_PASS) return null;
  return nodemailer.createTransport({
    host: process.env.SMTP_HOST,
    port: Number(process.env.SMTP_PORT || 587),
    secure: String(process.env.SMTP_SECURE).toLowerCase() === 'true',
    auth: { user: process.env.SMTP_USER, pass: process.env.SMTP_PASS }
  });
}
async function sendNotifications(e) {
  const transport = getTransport();
  const from = process.env.SMTP_FROM || process.env.SMTP_USER;
  if (!transport || !from) return { configured:false };
  const ownerEmail = process.env.OWNER_EMAIL || '';
  const jobs = [];
  if (ownerEmail) jobs.push(transport.sendMail({from,to:ownerEmail,subject:`New ${e.kind === 'quote' ? 'quotation request' : 'website enquiry'} — ${e.name}`,text:`Name: ${e.name}\nBusiness: ${e.businessName || '—'}\nEmail: ${e.email}\nPhone: ${e.phone || '—'}\nProject: ${e.projectType || '—'}\nBudget: ${e.budget || '—'}\nTimeline: ${e.timeline || '—'}\n\n${e.description}`}));
  jobs.push(transport.sendMail({from,to:e.email,subject:'Your enquiry has been received',text:`Hello ${e.name},\n\nYour enquiry has been received and stored successfully. This confirmation does not create a binding quotation or delivery commitment. Project scope, pricing and timing are confirmed separately after requirements are reviewed.\n\n— baracksilasmungai`}));
  const results = await Promise.allSettled(jobs);
  return { configured:true, sent:results.filter(x=>x.status==='fulfilled').length, failed:results.filter(x=>x.status==='rejected').length };
}

app.get('/api/content', async (req,res,next) => {
  try {
    const content = await getContent();
    const safe = JSON.parse(JSON.stringify(content));
    safe.articles = (safe.articles || []).filter(a => a.published);
    safe.testimonials = (safe.testimonials || []).filter(t => t.published);
    res.json({ok:true,content:safe});
  } catch (e) { next(e); }
});

app.post('/api/enquiries', async (req,res,next) => {
  try {
    const ip = req.ip || req.socket.remoteAddress || 'unknown';
    if (!rateLimit(`enquiry:${ip}`, 5, 15 * 60 * 1000)) return res.status(429).json({ok:false,error:'Too many submissions from this connection. Please try again later.'});
    if (clean(req.body.website,200)) return res.status(400).json({ok:false,error:'Submission rejected.'});
    const e = {
      id: crypto.randomUUID(),
      kind: req.body.kind === 'quote' ? 'quote' : 'contact',
      name: clean(req.body.name,120),
      businessName: clean(req.body.businessName,160),
      email: clean(req.body.email,180).toLowerCase(),
      phone: clean(req.body.phone,60),
      preferredContact: CONTACT_METHODS.includes(clean(req.body.preferredContact,60)) ? clean(req.body.preferredContact,60) : 'Email',
      projectType: clean(req.body.projectType,120),
      features: Array.isArray(req.body.features) ? req.body.features.map(x=>clean(x,100)).slice(0,20) : [],
      budget: clean(req.body.budget,100),
      timeline: clean(req.body.timeline,100),
      existingWebsite: clean(req.body.existingWebsite,300),
      description: clean(req.body.description,6000),
      status: 'New',
      privateNote: '',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      fingerprint: ''
    };
    e.fingerprint = fingerprint(e);
    const errors = {};
    if (e.name.length < 2) errors.name = 'Please enter your name.';
    if (!validEmail(e.email)) errors.email = 'Please enter a valid email address.';
    if (e.description.length < 12) errors.description = 'Please describe the project or enquiry in a little more detail.';
    if (e.kind === 'quote' && !e.projectType) errors.projectType = 'Please select a project type.';
    if (e.preferredContact !== 'Email' && !e.phone) errors.phone = `Please add a phone number so you can be reached on ${e.preferredContact}.`;
    if (Object.keys(errors).length) return res.status(422).json({ok:false,error:'Please check the highlighted fields.',fields:errors});

    const existing = await listEnquiries();
    const duplicate = existing.find(x => x.fingerprint === e.fingerprint && Date.now() - new Date(x.createdAt || x.created_at).getTime() < 10 * 60 * 1000);
    if (duplicate) return res.status(409).json({ok:false,error:'A very similar enquiry was already received recently. Your original submission is still stored.'});

    await insertEnquiry(e);
    let email = { configured:false };
    try { email = await sendNotifications(e); } catch { email = { configured:true, sent:0, failed:1 }; }
    res.status(201).json({ok:true,id:e.id,emailConfigured:email.configured,emailDelivered:email.configured ? email.failed === 0 : false,message: email.configured ? 'Your enquiry was stored successfully. Email delivery was attempted using the configured mail service.' : 'Your enquiry was stored successfully. Email notifications are not configured yet.'});
  } catch (e) { next(e); }
});

app.post('/api/analytics', async (req,res,next) => {
  try {
    const allowed = new Set(['page_view','portfolio_view','live_demo','quote_start','enquiry_success','contact_click']);
    const event = clean(req.body.event, 80);
    const pagePath = clean(req.body.path, 240);
    if (!allowed.has(event) || !pagePath.startsWith('/')) return res.status(422).json({ok:false,error:'Invalid analytics event.'});
    const ip = req.ip || 'unknown';
    if (!rateLimit(`analytics:${ip}`, 120, 60 * 60 * 1000)) return res.status(204).end();
    const metaIn = req.body.meta && typeof req.body.meta === 'object' && !Array.isArray(req.body.meta) ? req.body.meta : {};
    const meta = {};
    if (metaIn.project) meta.project = clean(metaIn.project, 120);
    if (metaIn.kind) meta.kind = clean(metaIn.kind, 40);
    if (pool) {
      await pool.query(`INSERT INTO analytics_events(event,path,meta) VALUES($1,$2,$3::jsonb)`, [event,pagePath,JSON.stringify(meta)]);
    } else {
      const rows = readLocalJson(ANALYTICS_FILE, []);
      rows.push({event,path:pagePath,meta,createdAt:new Date().toISOString()});
      writeLocalJson(ANALYTICS_FILE, rows.slice(-10000));
    }
    res.status(204).end();
  } catch(e){ next(e); }
});

app.post('/api/owner/login', (req,res) => {
  if (!OWNER_PASSWORD) return res.status(503).json({ok:false,error:'Owner login is not configured. Set OWNER_PASSWORD and AUTH_SECRET in the server environment.'});
  const ip = req.ip || 'unknown';
  if (!rateLimit(`login:${ip}`, 8, 15 * 60 * 1000)) return res.status(429).json({ok:false,error:'Too many login attempts. Please try again later.'});
  const supplied = clean(req.body.password,300);
  const a = Buffer.from(supplied), b = Buffer.from(OWNER_PASSWORD);
  if (a.length !== b.length || !crypto.timingSafeEqual(a,b)) return res.status(401).json({ok:false,error:'Incorrect owner password.'});
  const token = signToken({owner:true,exp:Date.now()+12*60*60*1000});
  res.setHeader('Set-Cookie', `${COOKIE_NAME}=${encodeURIComponent(token)}; HttpOnly; SameSite=Strict; Path=/; Max-Age=43200${isProduction ? '; Secure' : ''}`);
  res.json({ok:true});
});
app.post('/api/owner/logout', (req,res) => {
  res.setHeader('Set-Cookie', `${COOKIE_NAME}=; HttpOnly; SameSite=Strict; Path=/; Max-Age=0${isProduction ? '; Secure' : ''}`);
  res.json({ok:true});
});
app.get('/api/owner/session', (req,res) => res.json({ok:true,authenticated:Boolean(verifyToken(parseCookies(req)[COOKIE_NAME])?.owner)}));
app.get('/api/owner/enquiries', ownerOnly, async (req,res,next) => { try { res.json({ok:true,enquiries:await listEnquiries()}); } catch(e){next(e);} });
app.patch('/api/owner/enquiries/:id', ownerOnly, async (req,res,next) => { try { await updateEnquiry(req.params.id, {status:req.body.status,privateNote:req.body.privateNote}); res.json({ok:true}); } catch(e){next(e);} });
app.get('/api/owner/content', ownerOnly, async (req,res,next) => { try { res.json({ok:true,content:await getContent()}); } catch(e){next(e);} });
app.put('/api/owner/content', ownerOnly, async (req,res,next) => {
  try {
    if (!req.body || typeof req.body !== 'object' || Array.isArray(req.body)) return res.status(422).json({ok:false,error:'Content must be a JSON object.'});
    const serialized = JSON.stringify(req.body);
    if (serialized.length > 750000) return res.status(413).json({ok:false,error:'Content payload is too large.'});
    await saveContent(req.body); res.json({ok:true});
  } catch(e){next(e);}
});
app.get('/api/owner/export.csv', ownerOnly, async (req,res,next) => {
  try {
    const rows = await listEnquiries();
    const cols = ['id','createdAt','kind','status','name','businessName','email','phone','preferredContact','projectType','budget','timeline','existingWebsite','description','privateNote'];
    const esc = v => `"${String(v ?? '').replace(/"/g,'""').replace(/\r?\n/g,' ')}"`;
    const csv = [cols.join(','), ...rows.map(r => cols.map(c => esc(r[c] ?? r[c.replace(/[A-Z]/g,m=>'_'+m.toLowerCase())])).join(','))].join('\n');
    res.setHeader('Content-Type','text/csv; charset=utf-8');
    res.setHeader('Content-Disposition','attachment; filename="baracksilasmungai-enquiries.csv"');
    res.send(csv);
  } catch(e){next(e);}
});

app.get('/robots.txt', (req,res) => res.type('text/plain').send(`User-agent: *\nAllow: /\nDisallow: /owner\nDisallow: /api/owner\nSitemap: ${SITE_URL}/sitemap.xml\n`));
app.get('/sitemap.xml', async (req,res,next) => {
  try {
    const c = await getContent();
    const routes = ['','/portfolio','/services','/pricing','/about','/faq','/contact','/request-a-quote','/privacy','/service-terms'];
    for (const p of (c.projects||[]).filter(x=>x.published)) routes.push(`/portfolio/${encodeURIComponent(p.slug)}`);
    const xml = `<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">${routes.map(r=>`<url><loc>${SITE_URL}${r||'/'}</loc></url>`).join('')}</urlset>`;
    res.type('application/xml').send(xml);
  } catch(e){next(e);}
});

app.get('*', (req,res) => res.sendFile(path.join(ROOT,'public','index.html')));
app.use((err, req, res, next) => {
  console.error(err);
  res.status(500).json({ok:false,error:'The server could not complete that request.'});
});

initDb().then(() => app.listen(PORT, () => console.log(`baracksilasmungai running on ${SITE_URL}`))).catch(err => {
  console.error('Database initialisation failed:', err);
  process.exit(1);
});
