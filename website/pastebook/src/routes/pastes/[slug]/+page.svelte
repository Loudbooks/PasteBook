<script lang="ts">
    import Content from "../../../components/Content.svelte";
    import Mode from "../../../components/Mode.svelte";
    import Header from "../../../components/Header.svelte";
    import PotentialIssues from "../../../components/PotentialIssues.svelte";
    import {severes, warnings, pasteURL} from "$lib/stores";
    import {formatTimeSince} from "$lib/timehandler";
    import {error} from "@sveltejs/kit";
    import {onMount, tick} from 'svelte';

    export let data

    const {title, created, wrap, reportBook, url} = data

    let percent = 0
    let loading = true

    onMount(() => {
        setTimeout(() => {
            if (loading) {
                let loadContainer = document.getElementById("loadcontainer")
                loadContainer.style.opacity = "1"
            }
        }, 200)
    })

    let promise = new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.responseType = 'text';

        let loadBar = document.getElementById("loadbar")
        let statusContainer = document.getElementById("status")

        xhr.addEventListener("progress", (e) => {
            if (e.lengthComputable) {
                statusContainer.innerText = "Receiving..."
                loadBar.style.opacity = "1"
                percent = (e.loaded / e.total) * 100
            }

            if (percent == 0) {
                loadBar.style.width = "0"
            } else {
                loadBar.style.width = "calc(" + percent + "% + 1px)"
            }
        });

        xhr.onload = async () => {
            if (xhr.status === 200) {
                loadBar.style.width = "calc(100% + 1px)"
                statusContainer.innerText = "Rendering..."

                resolve(xhr.response);
                await tick()

                clearLoading()
            } else {
                error(xhr.status, xhr.statusText)
            }
        };
        xhr.onerror = () => {
            error(xhr.status, xhr.statusText)
        };

        xhr.send();
    });

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

    function clearLoading() {
        let loadContainer = document.getElementById("loadcontainer")
        loadContainer.style.opacity = "0"
        loading = false

        setTimeout(() => {
            loadContainer.style.display = "none"
        }, 500)
    }
</script>

<main>
    <div></div>
    <Mode/>
    <Header title="{title}" created="{timeSinceStr}"></Header>
    <loadcontainer id="loadcontainer">
        <loadbarcontainer>
            <loadbar id="loadbar">
            </loadbar>
            <status id="status">Resolving...</status>
        </loadbarcontainer>
    </loadcontainer>

    {#await promise then response}
        <Content  content="{response}" reportBook="{reportBook}" wrapPre="{wrap}"></Content>

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

  loadcontainer {
    transition: all 0.5s ease-in-out;

    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    height: 100vh;
    position: absolute;
    width: 100%;
    top: 0;
  }

  loadbarcontainer {
    transition: background-color 0.2s ease-in-out, border-color 0.2s ease-in-out;

    width: 30vw;

    @media (max-width: 600px) {
        width: 80vw;
    }

    border-radius: 20px;
    background-color: #eeeeee;
    border: 1px solid #c9c9c9;
    overflow: hidden;
    align-self: center;

    :global(.dark-mode) & {
      background-color: #1a1a1a;
      border: 1px solid #333;
    }
  }

  loadbar {
    transition: background-color 0.2s ease-in-out;
    width: 0;
    display: block;
    opacity: 0;
    height: 25px;
    background-color: #c9c9c9;
    content: '';
    align-self: center;

    :global(.dark-mode) & {
      background-color: #555;
    }
  }

  status {
    transition: color 0.2s ease-in-out;

    font-size: 15px;
    color: black;
    font-family: 'Gabarito', sans-serif;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);

    :global(.dark-mode) & {
        color: white;
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