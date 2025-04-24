import adapter from "@sveltejs/adapter-node";
import { vitePreprocess } from "@sveltejs/vite-plugin-svelte";

/** @type {import('@sveltejs/kit').Config} */
const config = {
  // Consult https://kit.svelte.dev/docs/integrations#preprocessors
  // for more information about preprocessors
  preprocess: [
    vitePreprocess({ postcss: true }),
  ],

  kit: {
    adapter: adapter({
      precompress: true,
    })
  },
  
  server: {
    host: '0.0.0.0',
  }
};

export default config;
