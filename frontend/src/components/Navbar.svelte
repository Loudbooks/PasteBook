<script lang="ts">
  import { onMount } from "svelte";
  import { title } from "$lib/stores";
  import { disableNew } from "$lib/stores";
  import { goto } from "$app/navigation";

  let navbar: HTMLElement;

  onMount(() => {
    navbar.style.transform = "translate(0, 50%)";
    navbar.style.opacity = "1";

    window.addEventListener("scroll", () => {
      if (window.scrollY > 0) {
        navbar.style.boxShadow = "var(--box-shadow)";
      } else {
        navbar.style.boxShadow = "none";
      }
    });
  });

  function newPaste() {
    goto("/new");
  }

  function settings() {
    goto("/settings");
  }

  function home() {
    goto("/");
  }
</script>

<div id="navbar-container">
  <div id="navbar" bind:this={navbar}>
    <button id="main" on:click={home}>{$title.toUpperCase()}</button>
    <buttons>
      {#if !$disableNew}
        <button on:click={settings}>SETTINGS</button>
        <button on:click={newPaste}>NEW</button>
      {/if}
    </buttons>
  </div>
</div>

<style lang="scss">
  #navbar-container {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    height: max-content;

    z-index: 998;

    display: flex;
    justify-content: center;
    align-items: center;

    background-color: transparent;
  }

  #navbar {
    position: fixed;
    width: calc(100% - 20px);
    height: 30px;

    box-shadow: none;

    border-bottom-left-radius: var(--border-radius);
    border-bottom-right-radius: var(--border-radius);
    
    transition:
      opacity 0.5s ease,
      background-color 0.5s ease,
      border 0.5s ease,
      box-shadow 0.5s ease;

    background-color: var(--color-background-secondary);
    opacity: 0;
    
    display: flex;
    justify-content: space-between;
    
    color: var(--color-text-primary);
    border: var(--border-standard);
    border-top: none;

    &:active {
      transform: scale(0.95);
    }
    
    button {
      transition:
      opacity 0.3s,
      transform 0.5s,
      color 0.5s,
      filter 0.3s;

      align-self: center;
      
      font-size: 1rem;
      font-weight: 700;
      font-family: var(--font-family), sans-serif;
      border: none;
      color: var(--color-text-secondary);
      
      padding: 0;
      background-color: transparent;
      outline: none;
      
      &:hover {
        filter: var(--button-hover-effect);

        cursor: pointer;
      }
      
      &:active {
        transform: scale(0.95);
      }
      
      @media (max-width: 600px) {
        font-size: 0.6rem;
      }
    }
    
    buttons {
      display: flex;
      gap: 40px;
      margin-right: 20px;
      margin-left: 20px;
      
      @media (max-width: 600px) {
        font-size: 0.6rem;
        gap: 20px;
        margin-right: 15px;
        margin-left: 15px;
      }
    }

    @media (max-width: 600px) {
      height: 20px;
    }
  }

  #main {
    margin-left: 20px;

    @media (max-width: 600px) {
      margin-left: 15px;
    }
  }
</style>
