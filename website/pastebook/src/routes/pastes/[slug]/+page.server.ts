import {error} from "@sveltejs/kit";
import {pasteURL} from "$lib/stores";

export async function load({ params }) {
    let path = params.slug

    let response = await fetch("https://pastebook.dev/get/" + path);

    if (response.status === 404) {
        error(404, {
            message: 'Content Not Found'
        });
    }

    if (response.status === 500) {
        error(500, {
            message: 'Server Error'
        });
    }

    if (response.status === 429) {
        error(429, {
            message: 'Rate Limited'
        });
    }

    let { created, content, title, reportBook} = await response.json();

    pasteURL.set("https://pastebook.dev/pastes/" + path);

    if (content === undefined) {
        error(404, {
            message: 'Content Not Found'
        });
    }

    return {
        created: created,
        content: content,
        title: title,
        reportBook: reportBook,
    }
}