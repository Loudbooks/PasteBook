import { error } from '@sveltejs/kit';

export async function load({ params, cookies }) {
    if (process.env.DISABLE_NEW?.toLowerCase() === 'true') {
        error(403, {
            message: 'Forbidden',
        });
    }
}