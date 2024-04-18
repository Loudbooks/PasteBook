<script lang="ts">
    import Content from "../../../components/Content.svelte";
    import Mode from "../../../components/Mode.svelte";
    import Header from "../../../components/Header.svelte";
    import PotentialIssues from "../../../components/PotentialIssues.svelte";
    import {pasteName, severes, warnings, pasteURL} from "$lib/stores";
    export let data

    const { created, content, title, reportBook } = data

    const reloadTime = () => {
        let timeSince = Date.now() - created;
        if (timeSince < 1000) {
            timeSinceStr = "Just now";
        } else if (timeSince < 60000) {
            if (Math.floor(timeSince / 1000) == 1) {
                timeSinceStr = "1 second ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 1000) + " seconds ago";
            }
        } else if (timeSince < 3600000) {
            if (Math.floor(timeSince / 60000) == 1) {
                timeSinceStr = "1 minute ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 60000) + " minutes ago";
            }
        } else if (timeSince < 86400000) {
            if (Math.floor(timeSince / 3600000) == 1) {
                timeSinceStr = "1 hour ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 3600000) + " hours ago";
            }
        } else {
            if (Math.floor(timeSince / 86400000) == 1) {
                timeSinceStr = "1 day ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 86400000) + " days ago";
            }
        }
    }

    let timeSinceStr = "";

    reloadTime()
    let clear
    $: {
        clearInterval(clear)
        clear = setInterval(reloadTime, 1000)
    }
</script>

<main>
    <Mode/>
    <Header title="{title}" created="{timeSinceStr}"></Header>
    <Content content="{content}" reportBook="{reportBook}"></Content>
    {#if ($warnings.length > 0 || $severes.length > 0)}
    <PotentialIssues/>
    {/if}
</main>

<svelte:head>
    <title>{title}</title>
    <meta name="description" content="Pastebook is a simplistic pastebin the supports automatic file scanning."/>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="{title}" />
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="{$pasteURL}"/>
    <meta property="og:description" content="Click to view this paste on PasteBook"/>
</svelte:head>