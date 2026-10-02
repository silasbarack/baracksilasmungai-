// Generates public/tech.js from the official simple-icons brand data.
// Re-runnable: `npm install && node tools/gen-tech.js` after adding a brand to BRANDS.
const fs = require('fs');
const si = require('simple-icons');
const OUT = require('path').join(__dirname, '..', 'public', 'tech.js');

// label(s) the owner may write in content.json  ->  official simple-icons slug
const BRANDS = {
  react: ['React', 'React.js', 'ReactJS'],
  typescript: ['TypeScript', 'TS'],
  javascript: ['JavaScript', 'JS'],
  html5: ['HTML5', 'HTML'],
  css: ['CSS3', 'CSS'],
  nodedotjs: ['Node.js', 'NodeJS', 'Node'],
  nestjs: ['NestJS', 'Nest.js'],
  postgresql: ['PostgreSQL', 'Postgres'],
  prisma: ['Prisma ORM', 'Prisma'],
  express: ['Express', 'Express.js'],
  git: ['Git'],
  github: ['GitHub'],
  render: ['Render'],
  figma: ['Figma'],
  tailwindcss: ['Tailwind CSS', 'TailwindCSS', 'Tailwind'],
  docker: ['Docker'],
  python: ['Python'],
  mysql: ['MySQL'],
  mongodb: ['MongoDB'],
  firebase: ['Firebase']
};

// Concepts are not brands and have no official mark: reuse the site's own line icons.
const CONCEPTS = {
  'REST APIs': 'plug', 'REST API': 'plug', 'APIs': 'plug',
  'WebSockets': 'dev', 'WebSocket': 'dev',
  'Authentication': 'user', 'Authentication & Authorization': 'user',
  'Cloud deployment': 'rocket', 'Deployment': 'rocket',
  'Responsive UI/UX': 'monitor', 'Responsive UI': 'monitor', 'Responsive design': 'monitor',
  'Payments': 'card', 'M-Pesa': 'card', 'Dashboards': 'chart',
  'Testing': 'check', 'Accessibility': 'user', 'SEO': 'chart'
};

const norm = s => String(s).toLowerCase().replace(/[^a-z0-9]/g, '');
// WCAG relative luminance: light marks get a dark disc so the official colour stays legible
const lum = hex => {
  const v = [0, 2, 4].map(i => {
    const c = parseInt(hex.slice(i, i + 2), 16) / 255;
    return c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
  });
  return 0.2126 * v[0] + 0.7152 * v[1] + 0.0722 * v[2];
};
const rgb = hex => [0, 2, 4].map(i => parseInt(hex.slice(i, i + 2), 16)).join(',');

const out = {};
const missing = [];
for (const [slug, labels] of Object.entries(BRANDS)) {
  const icon = si['si' + slug[0].toUpperCase() + slug.slice(1)];
  if (!icon) { missing.push(slug); continue; }
  const entry = { t: icon.title, c: '#' + icon.hex, s: rgb(icon.hex), d: lum(icon.hex) > 0.45 ? 1 : 0, p: icon.path };
  for (const label of labels) out[norm(label)] = entry;
}
if (missing.length) { console.error('MISSING slugs: ' + missing.join(', ')); process.exit(1); }

const concepts = {};
for (const [label, ico] of Object.entries(CONCEPTS)) concepts[norm(label)] = ico;

const pkgPath = require.resolve('simple-icons').replace(/[\\/]index\.js$/, '/package.json');
const version = JSON.parse(fs.readFileSync(pkgPath, 'utf8')).version;

const header = '/* Official brand marks from simple-icons v' + version + ' (CC0). Trademarks belong to their owners;\n'
  + '   used here only to identify the technologies in use. Generated file - edit gen-tech.js instead.\n'
  + '   t=title c=official hex s=rgb d=needs dark disc for contrast p=path (viewBox 0 0 24 24) */\n';
// assigned onto window, not declared with const: a top-level const is a global *lexical*
// binding, which app.js can only see because both are classic scripts. An explicit global
// keeps working if either file is ever bundled or loaded as a module.
fs.writeFileSync(OUT, header + 'window.TECH=' + JSON.stringify(out) + ';\nwindow.TECH_CONCEPT=' + JSON.stringify(concepts) + ';\n');

const uniq = [...new Set(Object.values(out).map(x => x.t))];
console.log('wrote public/tech.js - ' + uniq.length + ' brands, ' + Object.keys(out).length + ' labels, '
  + Object.keys(concepts).length + ' concepts, ' + fs.statSync(OUT).size + ' bytes');
console.log('brands: ' + uniq.join(', '));
console.log('dark-disc (light marks): ' + [...new Set(Object.values(out).filter(x => x.d).map(x => x.t))].join(', '));
