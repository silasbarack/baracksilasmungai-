// Renders the README preview images (docs/previews/*.jpg) from the project screenshots in public/assets/projects.
// Run: node tools/gen-previews.js   (needs Playwright with Chromium installed)
const fs = require('fs');
const path = require('path');
const { chromium } = require('playwright');

const root = path.join(__dirname, '..');
const out = path.join(root, 'docs', 'previews');
const shot = f => 'data:image/jpeg;base64,' + fs.readFileSync(path.join(root, 'public/assets/projects', f)).toString('base64');
const logo = 'data:image/png;base64,' + fs.readFileSync(path.join(root, 'public/assets/logo-mark.png')).toString('base64');

const projects = [
  { slug: 'ticketflow-kenya', no: '01', title: 'TicketFlow Kenya', kind: 'Event ticketing platform', tag: 'Personal product · Active development',
    stack: ['React', 'TypeScript', 'NestJS', 'PostgreSQL', 'Prisma'], desktop: 'ticketflow-kenya-desktop.jpg', mobile: 'ticketflow-kenya-mobile.jpg', accent: '#e8384f' },
  { slug: 'neurooption', no: '02', title: 'NeuroOption', kind: 'Fintech trading platform', tag: 'Personal product · Ongoing development',
    stack: ['React', 'Node.js', 'Prisma', 'REST API', 'WebSockets'], desktop: 'neurooption-desktop.jpg', mobile: 'neurooption-mobile.jpg', accent: '#3b82f6' },
  { slug: 'zawadimart', no: '03', title: 'ZawadiMart', kind: 'E-commerce storefront', tag: 'Demonstration project',
    stack: ['HTML', 'CSS', 'JavaScript', 'Responsive UI'], desktop: 'zawadimart-desktop-2026-11.jpg', mobile: 'zawadimart-mobile-2026-11.jpg', accent: '#2bb673' },
];

const base = `
@import url('https://fonts.googleapis.com/css2?family=Instrument+Serif:ital@0;1&family=Manrope:wght@500;600;700;800&display=swap');
*{box-sizing:border-box;margin:0}
body{width:var(--w);height:var(--h);overflow:hidden;font-family:Manrope,system-ui,sans-serif;color:#e9edf3;
 background:radial-gradient(900px 600px at 85% 15%,rgba(201,164,92,.20),transparent 60%),
 radial-gradient(700px 500px at 10% 100%,var(--glow),transparent 60%),linear-gradient(160deg,#0b1220,#070b14 60%)}
body:before{content:"";position:absolute;inset:0;background-image:linear-gradient(rgba(255,255,255,.035) 1px,transparent 1px),linear-gradient(90deg,rgba(255,255,255,.035) 1px,transparent 1px);background-size:56px 56px;mask-image:radial-gradient(ellipse at 70% 40%,#000 30%,transparent 75%)}
.serif{font-family:'Instrument Serif',Georgia,serif;font-weight:400}
.gold{color:#e6cc93}
.laptop .screen{background:#05070c;border-radius:18px 18px 4px 4px;padding:12px;border:1px solid #2b3445;box-shadow:0 40px 80px rgba(0,0,0,.55),0 0 0 1px rgba(255,255,255,.04) inset}
.laptop .screen img{display:block;width:100%;aspect-ratio:16/10;object-fit:cover;object-position:top;border-radius:6px}
.laptop .base{height:18px;margin:0 -6%;background:linear-gradient(180deg,#d5d9df,#7d8590);border-radius:0 0 20px 20px;position:relative}
.laptop .base:after{content:"";position:absolute;left:42%;right:42%;top:0;height:6px;background:#5d646d;border-radius:0 0 8px 8px}
.phone{position:absolute;border:8px solid #05070c;border-radius:34px;overflow:hidden;background:#fff;box-shadow:0 30px 60px rgba(0,0,0,.6),0 0 0 1px #2b3445}
.phone img{display:block;width:100%;height:100%;object-fit:cover;object-position:top}
.chip{display:inline-block;padding:7px 14px;border:1px solid rgba(255,255,255,.14);border-radius:999px;font-size:15px;font-weight:600;color:#cfd6e0;background:rgba(255,255,255,.04)}
`;

const card = p => `<!doctype html><html><head><style>${base}
:root{--w:1600px;--h:900px;--glow:${p.accent}33}
.wrap{position:relative;display:grid;grid-template-columns:520px 1fr;gap:40px;align-items:center;height:100%;padding:0 72px}
.no{font-size:16px;letter-spacing:.3em;font-weight:800;color:#c9a45c}
h1{font-size:92px;line-height:.95;margin:18px 0 14px}
.kind{font-size:26px;color:#aab4c3;margin-bottom:26px}
.tag{display:inline-flex;align-items:center;gap:10px;font-size:15px;font-weight:700;color:#e6cc93;margin-bottom:30px}
.tag:before{content:"";width:9px;height:9px;border-radius:50%;background:#c9a45c;box-shadow:0 0 0 5px rgba(201,164,92,.18)}
.chips{display:flex;flex-wrap:wrap;gap:10px;max-width:470px}
.dev{position:relative;padding:0 110px 70px 0}
.dev .phone{right:0;bottom:0;width:230px;aspect-ratio:390/844}
.brand{position:absolute;left:72px;bottom:48px;display:flex;align-items:center;gap:12px;font-size:15px;font-weight:700;letter-spacing:.18em;color:#7f8a9b}
.brand img{height:28px}
</style></head><body><div class="wrap"><div>
<div class="no">${p.no} — SELECTED WORK</div>
<h1 class="serif">${p.title}</h1>
<div class="kind">${p.kind}</div>
<div class="tag">${p.tag}</div>
<div class="chips">${p.stack.map(s => `<span class="chip">${s}</span>`).join('')}</div>
</div><div class="dev"><div class="laptop"><div class="screen"><img src="${shot(p.desktop)}"></div><div class="base"></div></div>
<div class="phone"><img src="${shot(p.mobile)}"></div></div></div>
<div class="brand"><img src="${logo}">BARACK SILAS MUNGAI</div></body></html>`;

const banner = () => `<!doctype html><html><head><style>${base}
:root{--w:1600px;--h:640px;--glow:rgba(15,138,131,.18)}
.wrap{position:relative;height:100%;padding:0 80px;display:grid;grid-template-columns:1fr 760px;align-items:center;gap:30px}
.eyebrow{display:flex;align-items:center;gap:14px;font-size:15px;font-weight:800;letter-spacing:.28em;color:#c9a45c}
.eyebrow:before{content:"";width:40px;height:2px;background:#c9a45c}
h1{font-size:80px;line-height:1;margin:22px 0 22px}
p{font-size:22px;color:#aab4c3;max-width:600px;line-height:1.5}
.logo{height:46px;margin-bottom:34px}
.stack{position:relative;height:520px}
.stack .laptop{position:absolute;width:520px}
.stack .l1{left:0;top:40px;transform:rotate(-4deg);opacity:.9}
.stack .l2{right:0;top:150px;width:560px;z-index:2}
.stack .phone{width:150px;aspect-ratio:390/844;left:330px;top:250px;z-index:3;border-width:6px;border-radius:26px}
</style></head><body><div class="wrap"><div>
<img class="logo" src="${logo}">
<div class="eyebrow">FULL-STACK WEB DEVELOPER · KENYA</div>
<h1 class="serif">Digital products<br>that <i class="gold">solve real<br>business problems.</i></h1>
<p>Custom websites, booking &amp; ticketing systems, dashboards and payment-ready platforms — designed, built and shipped.</p>
</div><div class="stack">
<div class="laptop l1"><div class="screen"><img src="${shot(projects[1].desktop)}"></div><div class="base"></div></div>
<div class="laptop l2"><div class="screen"><img src="${shot(projects[0].desktop)}"></div><div class="base"></div></div>
<div class="phone"><img src="${shot(projects[2].mobile)}"></div>
</div></div></body></html>`;

(async () => {
  fs.mkdirSync(out, { recursive: true });
  const browser = await chromium.launch();
  const render = async (html, file, w, h) => {
    const page = await browser.newPage({ viewport: { width: w, height: h }, deviceScaleFactor: 1 });
    await page.setContent(html, { waitUntil: 'networkidle' });
    await page.evaluate(() => document.fonts.ready);
    await page.screenshot({ path: path.join(out, file), type: 'jpeg', quality: 86 });
    await page.close();
    console.log('wrote docs/previews/' + file);
  };
  await render(banner(), 'banner.jpg', 1600, 640);
  for (const p of projects) await render(card(p), p.slug + '.jpg', 1600, 900);
  await browser.close();
})();
