import {error} from "@sveltejs/kit";
import {pasteURL} from "$lib/stores";

export async function load({ params }) {
    let path = params.slug
    let { created, content, title, reportBook} = await fetch("https://pastebook.dev/get/" + path).then(res => res.json()).catch(() => "");

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