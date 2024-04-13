/** @type {import('./$types').PageServerLoad} */
export async function load({ params }) {
    let path = params.slug
    let { created, content, title} = await fetch("https://pastebook.dev/get/" + path).then(res => res.json()).catch(() => "");

    return {
        created: created,
        content: content,
        title: title
    }
}