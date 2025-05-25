import type { PageServerLoad } from "./p/[slug]/$types";

export const load: PageServerLoad = async () => {
	return {
		disableNew: process.env.DISABLE_NEW?.toLowerCase() === 'true',
		faviconUrl: process.env.FAVICON_URL,
        title: process.env.TITLE,
	};
};