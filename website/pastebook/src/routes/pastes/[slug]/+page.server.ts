import {error} from "@sveltejs/kit";
import {pasteURL} from "$lib/stores";

export async function load({ params }) {
    let path = params.slug

    let response = await fetch("http://localhost:25658/api/get/" + path + "/metadata");

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

    pasteURL.set("https://pastebook.dev/pastes/" + path);

    let title = response.headers.get("title");
    let created = response.headers.get("created");
    let wrap = response.headers.get("wrap");
    let reportBook = response.headers.get("reportBook");

    return {
        title: title,
        created: created,
        wrap: wrap,
        reportBook: reportBook,
        url: "http://localhost:25658/api/get/" + path + "/content",
    }
}