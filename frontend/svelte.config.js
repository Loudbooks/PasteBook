import adapter from '@sveltejs/adapter-node';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

/** @type {import('@sveltejs/kit').Config} */
const config = {
	preprocess: vitePreprocess(),
	onwarn: (warning, handler) => {
		if (warning.code == 'a11y_click_events_have_key_events') return;
		if (warning.code == 'a11y_mouse_events_have_key_events') return;
		if (warning.code == 'a11y_no_noninteractive_element_interactions') return;
		if (warning.code == 'a11y_no_static_element_interactions') return;
		
		handler(warning);
	},
	kit: {
		adapter: adapter()
	}
};

export default config;
