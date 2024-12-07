import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async () => {
	return {
		title: process.env.TITLE,
		description: process.env.DESCRIPTION,
		disableNew : process.env.DISABLE_NEW,
	};
};