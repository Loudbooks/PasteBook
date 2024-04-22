<script lang="ts" context="module">
    import themes from '$lib/assets/themes.json';

    export function setTheme(themeKey: string) {
        localStorage.setItem('theme', themeKey);
        loadTheme();
    }

    export function getTheme() {
        return localStorage.getItem('theme') || 'standard_dark';
    }

    export function getThemes() {
        let themeKeys = Object.keys(themes);
        return themeKeys.map(key => {
            return {
                key: key,
                name: themes[key].name
            }
        });
    }

    export function loadTheme() {
        let themeKey = localStorage.getItem('theme');
        if (themeKey == null) {
            if (window.matchMedia('(prefers-color-scheme: dark)').matches) {
                localStorage.setItem('theme', 'standard_dark');
            } else {
                localStorage.setItem('theme', 'standard_light');
            }
        }
        let theme = themes[themeKey || 'standard_dark'].colors;
        console.log(theme)
        document.body.style.setProperty('background-color', theme.background);

        document.body.style.setProperty('--toolbar-background', theme.toolbar.background);
        document.body.style.setProperty('--toolbar-text', theme.toolbar.text);
        document.body.style.setProperty('--toolbar-text-hover', theme.toolbar.text_hover);
        document.body.style.setProperty('--toolbar-border', theme.toolbar.border);

        document.body.style.setProperty('--header-title', theme.header.title);
        document.body.style.setProperty('--header-subtitle', theme.header.subtitle);

        document.body.style.setProperty('--settings-section-title', theme.settings.section_title);
        document.body.style.setProperty('--settings-option-title', theme.settings.option_title);

        document.body.style.setProperty('--pane-background', theme.pane.background);
        document.body.style.setProperty('--pane-background-hover', theme.pane.background_hover);
        document.body.style.setProperty('--pane-border', theme.pane.border);

        document.body.style.setProperty('--content-text', theme.content.text);
        document.body.style.setProperty('--content-line-numbers', theme.content.line_numbers);
        document.body.style.setProperty('--content-line-numbers-hover', theme.content.line_numbers_hover);
        document.body.style.setProperty('--content-severity-1', theme.content.severity_1);
        document.body.style.setProperty('--content-severity-2', theme.content.severity_2);

        document.body.style.setProperty('--editor-default-title', theme.editor.default_title);
        document.body.style.setProperty('--editor-text', theme.editor.text);
    }
</script>

<script lang="ts">
    import {onMount} from "svelte";

    onMount(() => {
        loadTheme();
    });
</script>