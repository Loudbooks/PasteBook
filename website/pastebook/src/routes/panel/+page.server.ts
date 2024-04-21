import {error} from "@sveltejs/kit";
import type {Paste} from "$lib/paste";

export async function load({ params }) {
    const response = await fetch("http://localhost:25658/list")

    if (response.status === 429) {
        error(429, {
            message: 'Rate Limited'
        });
    }

    const json = await response.json()

    let pastes: Paste[] = []

    json.forEach((paste: Paste) => {
        pastes.push({
            id: paste.id,
            title: paste.title,
            created: paste.created,
            reportBook: paste.reportBook,
        })
    })

    let current = Date.now();

    pastes = pastes.filter(paste => {
        return paste.created as unknown as number < current;
    })

    pastes.sort((a, b) => {
        return (b.created as unknown as number) - (a.created as unknown as number);
    });

    if (pastes.length == 0) {
        error(404, {
            message: 'No pastes found'
        });
    }

    return {
        pastes: pastes,
    }
}