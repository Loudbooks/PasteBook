<script lang="ts">
  import Content from "../../../components/Content.svelte";
  import Mode from "../../../components/Mode.svelte";
  import Header from "../../../components/Header.svelte";
  import PotentialIssues from "../../../components/PotentialIssues.svelte";
  import { loadProgress, severes, warnings } from "$lib/stores";
  import { formatTimeSince, formatTimeUntil } from "$lib/timehandler";
  import { onMount, tick } from "svelte";
  import LoadingTitle from "../../../components/image/LoadingTitle.svelte";
  import Highlight from "../../../components/Highlight.svelte";

  export let data;

  const { metadata, content } = data;

  let timeSinceStr = "";
  let created = new Date();
  let expires = new Date();
  let title = "";
  let reportBook = false;
  let wrap = false;
  let userId = "";
  let untilExpire = "";

  metadata.then((data) => {
    created = new Date(data.created);
    title = data.title;
    reportBook = data.reportBook;
    wrap = data.wrap;
    userId = data.user.id;
    expires = new Date(data.expires_at);

    const reloadTime = () => {
      timeSinceStr = formatTimeSince(created as unknown as number);
      untilExpire = formatTimeUntil(expires as unknown as number);
    };

    reloadTime();
    let clear;
    clearInterval(clear);
    clear = setInterval(reloadTime, 1000);
  });

  content.then(() => {
    loadProgress.set(true);
    tick();
  });

  let x = null;
  let y = null;

  onMount(() => {
    document.addEventListener("mousemove", onMouseUpdate, false);
    document.addEventListener("mouseenter", onMouseUpdate, false);

    function onMouseUpdate(e) {
      x = e.pageX;
      y = e.pageY;
    }
  });

  let isShifting = false;

  onMount(() => {
    window.addEventListener("keydown", (e) => {
      if (e.key === "Shift") {
        isShifting = true;

        let hoveredElement = document.elementFromPoint(x, y).parentElement
          .parentElement;
        if (hoveredElement === null) return;

        if (hoveredElement.id === "hoverable") {
          handler();
        }
      }
    });

    window.addEventListener("keyup", (e) => {
      if (e.key === "Shift") {
        isShifting = false;
      }
    });
  });

  function handler() {
    if (!isShifting) {
      return;
    }

    let idElement = document.getElementById("hash");
    if (idElement === null) return;

    idElement.style.opacity = "1";

    if (window.innerWidth < 600) {
      idElement.style.height = "7px";
    } else {
      idElement.style.height = "13px";
    }
  }

  function undoHandler() {
    let idElement = document.getElementById("hash");
    if (idElement === null) return;

    idElement.style.opacity = 0;
    idElement.style.height = "0";
  }

  function loadHandler() {
    document.title = title + " • PasteBook";
  }
</script>

<main>
  <LoadingTitle />
  <Mode />
  <Highlight />
  <div
    id="hoverable"
    on:load={loadHandler}
    on:mouseenter={handler}
    on:mouseleave={undoHandler}
    role="tooltip"
  >
    <Header {title} created={timeSinceStr}></Header>
    <p id="hash">{userId}</p>
  </div>
  {#await content then response}
    <Content
      content={response}
      {reportBook}
      wrapPre={wrap}
      inspect={data.inspect}
    ></Content>
    {#if untilExpire !== ""}
      <p
        id="expire"
        class="extra-padding-{$warnings.length > 0 || $severes.length > 0}"
      >
        Expires in
        <strong>{untilExpire}</strong>
      </p>
    {/if}

    {#if $warnings.length > 0 || $severes.length > 0}
      <PotentialIssues />
    {/if}
  {:catch e}
    <p>Failed to fetch paste: {e}</p>
  {/await}
</main>

<svelte:head>
  <meta name="robots" content="noindex" />
</svelte:head>

<style lang="scss">
  #hash {
    transition:
      transform 0.3s,
      opacity 0.2s,
      height 0.3s;

    padding-left: 30px;
    margin: 0;
    font-size: 13px;
    font-family: var(--font-family), sans-serif;
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
    margin-top: 10px;
    margin-bottom: 10px;
    font-size: 14px;
    font-family: var(--font-family), sans-serif;
    text-align: center;

    animation: fadeIn ease 0.7s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;

    &.extra-padding-true {
      margin-top: 40px;
      margin-bottom: 40px;
    }

    @media (max-width: 600px) {
      font-size: 10px;
    }

    @keyframes fadeIn {
      0% {
        opacity: 0;
      }
      100% {
        opacity: 1;
      }
    }
  }
</style>
