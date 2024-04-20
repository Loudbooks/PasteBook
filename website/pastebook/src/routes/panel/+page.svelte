<script lang="ts">
    import Header from "../../components/Header.svelte";
    import ListedPaste from "../../components/panel/ListedPaste.svelte";
    import Mode from "../../components/Mode.svelte";
    import {pasteURL} from "$lib/stores";

    export let data;

    let { pastes } = data;
</script>

<panel>
    <Header title="Pastes" created="{pastes.length}"/>
    {#each pastes as paste}
        <ListedPaste paste="{paste}"/>
    {/each}
    <Mode />
</panel>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="PasteBook Panel" />
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/panel"/>
    <meta property="og:description" content="Click to view all currently created pastes."/>
</svelte:head>