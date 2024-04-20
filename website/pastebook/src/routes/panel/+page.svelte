<script lang="ts">
    import Header from "../../components/Header.svelte";
    import ListedPaste from "../../components/panel/ListedPaste.svelte";
    import Mode from "../../components/Mode.svelte";
    import { onMount } from "svelte";

    export let data;

    let { pastes } = data;

    onMount(() => {
        let fades = document.getElementsByClassName("fade")

        for (let i = 0; i < fades.length; i++) {
            let fade = fades[i] as HTMLElement
            
            setTimeout(() => {
                fade.style.opacity = "1"
            }, (i) * 50)
        }
    })
</script>

<panel>
    <Header title="Pastes" created="{pastes.length.toString()}"/>
    {#each pastes as paste}
        <div class="fade">
            <ListedPaste paste="{paste}"/>
        </div>
    {/each}
    <Mode />
</panel>

<style lang="scss">
    div {
        transition: opacity 1s;
        opacity: 0;
    }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="PasteBook Panel" />
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/panel"/>
    <meta property="og:description" content="Click to view all currently created pastes."/>
</svelte:head>