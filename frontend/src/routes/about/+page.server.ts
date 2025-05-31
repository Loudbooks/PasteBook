export async function load({ }) {
    return {
        title: process.env.TITLE || "PasteBook",
        description: process.env.DESCRIPTION || "An easy on the eyes, portable, lightning fast pastebin built with Svelte and Rust.",
        commitHash: process.env.COMMIT_HASH || "unknown",
    }
}