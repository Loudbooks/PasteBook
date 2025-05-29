import { error } from '@sveltejs/kit';

export async function load({ }) {
    if (process.env.DISABLE_NEW?.toLowerCase() === 'true') {
        error(403, {
            code: 403,
            message: 'New posts are disabled',
        } satisfies App.Error);
    }

    let loadedLanguages = [
        "None",
        "Angular",
        "C",
        "C#",
        "C++",
        "CSS",
        "Go",
        "HTML",
        "Java",
        "JavaScript",
        "JSX",
        "Kotlin",
        "Lua",
        "Markdown",
        "PHP",
        "Python",
        "R",
        "Rust",
        "Ruby",
        "Sass",
        "SCSS",
        "Shell",
        "Svelte",
        "Swift",
        "SQL",
        "TSX",
        "TypeScript",
        "Vue",
        "XML",
        "YAML",
        "JSON",
        "Handlebars",
        "Less",
        "Zig"
    ]

    return {
        loadedLanguages: loadedLanguages,
    }
}