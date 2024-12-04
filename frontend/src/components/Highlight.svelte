<script lang="ts">
  import { onMount } from "svelte";
  import { validScan } from "$lib/stores.ts";

  export let highlight = false;

  function setCookie(name: string, value: string, days: number) {
    const expires = new Date(Date.now() + days * 864e5).toUTCString();
    document.cookie = `${name}=${value}; expires=${expires}; path=/`;
  }

  function getCookie(name: string) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
  }

  onMount(() => {
    if (!getCookie("auto-highlight")) {
      setCookie("auto-highlight", "false", 365);
    }

    highlight = getCookie("auto-highlight") === "true";

    if (getCookie("inspect") === "true") {
      highlight = true;
    }

    validScan.subscribe(() => {
      const style = document.getElementById("highlight");
      style.style.display = $validScan ? "block" : "none";
    });

    if (highlight) {
      setCookie("inspect", "true", 365);
    }
  });

  function toggleHighlight() {
    highlight = !highlight;

    setCookie("auto-highlight", highlight ? "true" : "false", 365);

    if (highlight) {
      setCookie("inspect", "true", 365);
    } else {
      document.cookie = "inspect=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    }

    window.location.reload();
  }
</script>

<container>
  {#if highlight}
    <button id="highlight" class="style" on:click={toggleHighlight}
      >UNHIGHLIGHT</button
    >
  {:else}
    <button id="highlight" class="style" on:click={toggleHighlight}
      >HIGHLIGHT</button
    >
  {/if}
</container>

<style lang="scss">
  container {
    transition:
      background 0.5s ease,
      color 0.5s ease,
      transform 0.5s ease,
      bottom 0.5s ease;

    .style {
      all: unset;
      transition:
        background 0.5s ease,
        color 0.5s ease,
        transform 0.5s ease,
        bottom 0.5s ease;

      background: none;
      border: none;
      font: inherit;
      cursor: pointer;
      outline: inherit;
      display: none;

      position: fixed;
      margin: 0;
      right: 100px;

      @media (max-width: 600px) {
        left: 30px;
        right: unset;
      }

      bottom: 0;

      background: #ffcc00;
      color: black;

      padding: 4px 8px;
      border-top-left-radius: 5px;
      border-top-right-radius: 5px;
      font-size: 12px;
      font-weight: bold;
      z-index: 9999;
      font-family: Gabarito, sans-serif;
      text-decoration: none;
      opacity: 0;

      animation: flyIn ease 0.7s forwards;
      animation-delay: 0.5s;
      animation-iteration-count: 1;

      &:hover {
        cursor: pointer;
        bottom: -2px;
      }
    }
  }

  @keyframes flyIn {
    0% {
      transform: translateY(90%);
      opacity: 0;
    }
    100% {
      transform: translateY(0%);
      opacity: 1;
    }
  }
</style>
