module.exports = {
  globDirectory: 'public/',
  globPatterns: [
    '**/*.{html,json,js,css,png,jpg,jpeg,svg,gif,ico,webp,woff,woff2,ttf,eot}'
  ],
  swDest: 'public/sw.js',
  clientsClaim: true,
  skipWaiting: true
};