import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async ({ params }) => {
	return {
		title: process.env.TITLE,
		description: process.env.DESCRIPTION
	};
};