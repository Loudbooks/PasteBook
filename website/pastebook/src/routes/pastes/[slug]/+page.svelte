<script lang="ts">
    import Content from "../../../components/Content.svelte";
    import Mode from "../../../components/Mode.svelte";
    import Header from "../../../components/Header.svelte";
    import PotentialIssues from "../../../components/PotentialIssues.svelte";
    import {loadProgress, severes, warnings} from "$lib/stores";
    import {formatTimeSince, formatTimeUntil} from "$lib/timehandler";
    import {error} from "@sveltejs/kit";
    import {onMount, tick} from 'svelte';
    import SVGPasteBook from "../../../components/svg/SVGPasteBook.svelte";
    import Highlight from "../../../components/Highlight.svelte";

    export let data

    const {metadata, url} = data

    let percent = 0

    let promise = new Promise((resolve) => {
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
    let expires = new Date()
    let title = ""
    let reportBook = false
    let wrap = false
    let hashedIP = ""
    let untilExpire = ""

    metadata.then((data) => {
        created = new Date(data.created)
        title = data.title
        reportBook = data.reportBook
        wrap = data.wrap
        hashedIP = data.user.hashedIP
        expires = new Date(data.expiresAt)

        const reloadTime = () => {
            timeSinceStr = formatTimeSince(created)
            untilExpire = formatTimeUntil(expires)
        }

        reloadTime()
        let clear
        clearInterval(clear)
        clear = setInterval(reloadTime, 1000)
    });

    let x = null;
    let y = null;

    onMount(() => {
        document.addEventListener('mousemove', onMouseUpdate, false);
        document.addEventListener('mouseenter', onMouseUpdate, false);

        function onMouseUpdate(e) {
            x = e.pageX;
            y = e.pageY;
        }
    });

    let isShifting = false

    onMount(() => {
        window.addEventListener("keydown", (e) => {
            if (e.key === "Shift") {
                isShifting = true

                let hoveredElement = document.elementFromPoint(x, y).parentElement.parentElement
                if (hoveredElement === null) return

                if (hoveredElement.id === "hoverable") {
                    handler()
                }
            }
        })

        window.addEventListener("keyup", (e) => {
            if (e.key === "Shift") {
                isShifting = false
            }
        })
    })

    function handler() {
        if (!isShifting) {
            return
        }

        let idElement = document.getElementById("hash")
        if (idElement === null) return

        idElement.style.opacity = "1"

        if (window.innerWidth < 600) {
            idElement.style.height = "7px"
        } else {
            idElement.style.height = "13px"
        }
    }

    function undoHandler() {
        let idElement = document.getElementById("hash")
        if (idElement === null) return

        idElement.style.opacity = 0
        idElement.style.height = "0"
    }

    function loadHandler() {
        document.title = title + " • PasteBook"
    }
</script>

<main>
    <div id="padding-container">
        <div id="padding"></div>
    </div>
    <SVGPasteBook/>
    <Mode/>
    <Highlight/>
    <div id="hoverable" on:load={loadHandler} on:mouseenter={handler} on:mouseleave={undoHandler} role="tooltip">
        <Header title="{title}" created="{timeSinceStr}"></Header>
        <p id="hash">{hashedIP}</p>
    </div>
    {#await promise then response}
        <Content content="{response}" reportBook="{reportBook}" wrapPre="{wrap}"></Content>
        {#if untilExpire !== ''}
            <p id="expire" class="extra-padding-{$warnings.length > 0 || $severes.length > 0}">Expires in
                <strong>{untilExpire}</strong></p>
        {/if}

        {#if ($warnings.length > 0 || $severes.length > 0)}
            <PotentialIssues/>
        {/if}
    {:catch e}
        <p>Failed to fetch paste: {e}</p>
    {/await}
</main>

<style lang="scss">
  #padding {
    padding-top: 10px + 30px;

    @media (max-width: 600px) {
      padding-top: 7px + 20px;
    }
  }

  #hash {
    transition: transform 0.3s, opacity 0.2s, height 0.3s;

    padding-left: 30px;
    margin: 0;
    font-size: 13px;
    font-family: Gabarito, sans-serif;
    color: grey;
    opacity: 0;
    height: 0;

    @media (max-width: 600px) {
      font-size: 6px;
    }
  }

  #expire {
    color: gray;
    margin: 0;
    padding: 0 0 10px;
    font-size: 14px;
    font-family: Gabarito, sans-serif;
    text-align: center;

    @media (max-width: 600px) {
      font-size: 10px;
    }

    &.extra-padding-true {
      padding: 0 0 40px;
    }
  }
</style>

<svelte:head>
    <meta name="robots" content="noindex">
</svelte:head>
