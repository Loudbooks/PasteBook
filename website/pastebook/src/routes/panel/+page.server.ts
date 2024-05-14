import {error} from "@sveltejs/kit";
import type {Paste} from "$lib/paste";

export async function load({ params }) {
    const response = await fetch("http://localhost:25658/api/list")

    if (response.status === 429) {
        error(429, {
            message: 'Rate Limited'
        });
    }

    if (response.status === 403) {
        error(403, {
            message: 'Forbidden'
        });
    }

    const json = response.json()

    return {
        promise: json,
    }
}