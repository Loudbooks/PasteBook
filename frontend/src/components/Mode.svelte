<script>
  import { onMount } from "svelte";

  export let darkMode = false;

  onMount(() => {
    darkMode = localStorage.getItem("dark-mode") === "true";
  });

  function toggleStyle() {
    darkMode = !darkMode;

    document.documentElement.setAttribute('data-theme', darkMode ? 'dark' : 'light');

    if (darkMode) {
      localStorage.setItem("dark-mode", "true");
    } else {
      localStorage.setItem("dark-mode", "false");
    }
  }
</script>

<container>
  {#if darkMode}
    <button class="style" style="background-color: #fff; color: #000" on:click={toggleStyle}>LIGHT</button>
  {:else}
    <button class="style" style="background-color: #000; color: #fff" on:click={toggleStyle}>DARK</button>
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

      border: none;
      font: inherit;
      cursor: pointer;
      outline: inherit;

      position: fixed;
      margin: 0;
      right: 30px;
      bottom: 0;

      color: var(--color-text-primary);

      padding: 4px 8px;
      border-top-left-radius: 5px;
      border-top-right-radius: 5px;
      font-size: 12px;
      font-weight: bold;
      z-index: 9999;
      font-family: var(--font-family), sans-serif;
      text-decoration: none;
      opacity: 0;

      animation: flyIn ease 0.7s forwards;
      animation-delay: 0.3s;
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
