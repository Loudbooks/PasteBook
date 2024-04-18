import {error} from "@sveltejs/kit";

/** @type {import('./$types').PageServerLoad} */
export async function load({ params }) {
    let path = params.slug
    let { created, content, title, reportBook} = await fetch("https://pastebook.dev/get/" + path).then(res => res.json()).catch(() => "");

    let pageMetaTags = {
        title: title.toString(),
        description: "Simplistic pastebin that supports automated scanning of logs and reports.",
        openGraph: {
            title: title.toString(),
            description: "PasteBook is a simplistic pastebin that supports automated scanning of logs and reports.",
            url: "https://pastebook.dev/pastes/" + path,
        }
    }

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
        pageMetaTags: pageMetaTags
    }
}