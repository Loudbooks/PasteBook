<script lang="ts">
    import Content from "../../../components/Content.svelte";
    import Mode from "../../../components/Mode.svelte";
    import Header from "../../../components/Header.svelte";
    import PotentialIssues from "../../../components/PotentialIssues.svelte";
    import {severes, warnings, pasteURL} from "$lib/stores";
    import {formatTimeSince} from "$lib/timehandler";
    export let data

    const { created, content, title, reportBook, wrap } = data

    const reloadTime = () => {
        timeSinceStr = formatTimeSince(created)
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
    <div></div>
    <Mode/>
    <Header title="{title}" created="{timeSinceStr}"></Header>
    <Content content="{content}" reportBook="{reportBook}" wrapPre="{wrap}"></Content>
    {#if ($warnings.length > 0 || $severes.length > 0)}
        <PotentialIssues/>
    {/if}
</main>

<style lang="scss">
    div {
        padding-top: 7px + 30px;

        @media (max-width: 600px) {
            padding-top: 7px + 20px;
        }
    }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="{title}" />
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="{$pasteURL}"/>
    <meta property="og:description" content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes."/>
    <meta name="robots" content="noindex">
</svelte:head>