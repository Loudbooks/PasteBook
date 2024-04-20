import {error} from "@sveltejs/kit";
import type {Paste} from "$lib/paste";

export async function load({ params }) {
    const response = await fetch("http://localhost:25658/list")
    const json = await response.json()

    let pastes: Paste[] = []

    json.forEach(paste => {
        pastes.push({
            id: paste.id,
            title: paste.title,
            created: paste.created,
            reportBook: paste.reportBook,
        })
    })


    let current = Date.now();

    pastes = pastes.filter(paste => {
        return paste.created < current;
    })

    pastes.sort((a, b) => {
        return b.created - a.created;
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