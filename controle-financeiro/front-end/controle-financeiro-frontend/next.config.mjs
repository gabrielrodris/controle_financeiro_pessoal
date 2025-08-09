const withPWA = require('next-pwa');

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  compress: true,
  experimental: {
    allowedDevOrigins: [
      'http://localhost:3000',
      'http://172.19.0.1:3000',
      'http://192.168.1.161:3000',
    ],
  },
  webpack: (config) => {
    config.resolve.fallback = { fs: false, net: false, tls: false };
    return config;
  },
  async headers() {
    return [
      {
        source: '/(.*)',
        headers: [
          { 
            key: 'Cache-Control', 
            value: 'public, max-age=31536000, immutable' 
          },
          {
            key: 'X-DNS-Prefetch-Control',
            value: 'on'
          }
        ],
      },
    ];
  },
  images: {
    domains: ['localhost'],
    minimumCacheTTL: 60,
    formats: ['image/webp'],
    deviceSizes: [640, 750, 828, 1080, 1200, 1920, 2048, 3840],
  },
};

module.exports = withPWA({
  ...nextConfig,
  pwa: {
    dest: 'public',
    register: true,
    skipWaiting: true,
    disable: process.env.NODE_ENV === 'development',
    dynamicStartUrl: false,
    runtimeCaching: [
      {
        urlPattern: /^https?.*/,
        handler: 'NetworkFirst',
        options: {
          cacheName: 'offlineCache',
          expiration: {
            maxEntries: 200,
          },
        },
      },
    ],
  },
});