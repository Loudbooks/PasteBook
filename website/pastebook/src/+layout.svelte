<script>
    import {pasteName, pasteURL} from "$lib/stores.js";

    let title = $pasteName
    let url = $pasteURL

    if (title === "") {
        title = "PasteBook"
    }

    if (url === "") {
        url = "https://pastebook.dev"
    }
</script>

<svelte:head>
    <meta property="og:title" content="{pasteName}" />
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="{url}"/>
    <meta property="og:description" content="Pastebook is a simplistic pastebin the supports automatic file scanning."/>
</svelte:head>

<slot />