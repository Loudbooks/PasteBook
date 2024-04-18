import type { MetaTagsProps } from 'svelte-meta-tags';

export const load = ({ url }) => {
    const baseMetaTags = Object.freeze({
        title: "PasteBook",
        description: "Simplistic pastebin that supports automated scanning of logs and reports.",
        openGraph: {
        title: "PasteBook",
            description: "PasteBook is a simplistic pastebin that supports automated scanning of logs and reports.",
            url: "https://pastebook.dev",
        }
    }) satisfies MetaTagsProps;

    return {
        baseMetaTags
    }
}