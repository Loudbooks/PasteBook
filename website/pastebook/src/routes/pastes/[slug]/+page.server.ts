/** @type {import('./$types').PageServerLoad} */
export async function load({ params }) {
    let path = params.slug
    let { created, content, title} = await fetch("http://127.0.0.1:25658/get/" + path).then(res => res.json()).catch(() => "");

    return {
        created: created,
        content: content,
        title: title
    }
}