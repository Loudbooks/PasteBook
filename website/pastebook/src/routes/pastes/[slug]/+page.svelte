<script lang="ts">
    import Content from "../../../components/Content.svelte";
    import Mode from "../../../components/Mode.svelte";
    import Header from "../../../components/Header.svelte";
    import PotentialIssues from "../../../components/PotentialIssues.svelte";
    import {severes, warnings, pasteURL, loadProgress} from "$lib/stores";
    import {formatTimeSince} from "$lib/timehandler";
    import {error} from "@sveltejs/kit";
    import {tick} from 'svelte';
    import SVGPasteBook from "../../../components/SVGPasteBook.svelte";

    export let data

    const {metadata, url} = data

    let percent = 0

    let promise = new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.responseType = 'text';

        xhr.addEventListener("progress", (e) => {
            if (e.lengthComputable) {
                percent = (e.loaded / e.total) * 100
            }

            loadProgress.set(percent)
        });

        xhr.onload = async () => {
            if (xhr.status === 200) {
                loadProgress.set(99.9)

                resolve(xhr.response);
                await tick()
                loadProgress.set(100)
            } else {
                error(xhr.status, xhr.statusText)
            }
        };
        xhr.onerror = () => {
            error(xhr.status, xhr.statusText)
        };

        xhr.send();
    });

    let timeSinceStr = "";
    let created = new Date()
    let title = ""
    let reportBook = false
    let wrap = false

    metadata.then((data) => {
        created = new Date(data.created)
        title = data.title
        reportBook = data.reportBook
        wrap = data.wrap

        const reloadTime = () => {
            timeSinceStr = formatTimeSince(created)
        }

        reloadTime()
        let clear
        clearInterval(clear)
        clear = setInterval(reloadTime, 1000)
    });
</script>

<main>
    <div></div>
    <SVGPasteBook/>
    <Mode/>
    <Header title="{title}" created="{timeSinceStr}"></Header>
    {#await promise then response}
        <Content content="{response}" reportBook="{reportBook}" wrapPre="{wrap}"></Content>

        {#if ($warnings.length > 0 || $severes.length > 0)}
            <PotentialIssues/>
        {/if}
    {:catch e}
        <p>Failed to fetch paste: {e}</p>
    {/await}
</main>

<style lang="scss">
  div {
    padding-top: 10px + 30px;

    @media (max-width: 600px) {
      padding-top: 7px + 20px;
    }
  }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="{title}"/>
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="{$pasteURL}"/>
    <meta property="og:description"
          content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes."/>
    <meta name="robots" content="noindex">
</svelte:head>