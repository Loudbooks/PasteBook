<script lang="ts">
  import { onMount } from "svelte";
  import { title } from "$lib/stores";
  import { disableNew } from "$lib/stores";
  import { goto } from "$app/navigation";

  onMount(() => {
    const toolbar = document.querySelector("toolbar") as HTMLElement;

    toolbar.style.transform = "translate(0, 50%)";
    toolbar.style.opacity = "1";
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

<div id="toolbar-container">
  <toolbar>
    <button id="main" on:click={home}>{$title.toUpperCase()}</button>
    <buttons>
      {#if !$disableNew}
        <button on:click={settings}>SETTINGS</button>
        <button on:click={newPaste}>NEW</button>
      {/if}
    </buttons>
  </toolbar>
</div>

<style lang="scss">
  #toolbar-container {
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

  toolbar {
    position: fixed;
    width: calc(100% - 20px);
    height: 30px;

    box-shadow: var(--box-shadow);

    border-bottom-left-radius: var(--border-radius);
    border-bottom-right-radius: var(--border-radius);
    
    transition:
      opacity 0.5s ease,
      background-color 0.5s ease,
      border 0.5s ease;

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
      margin: 0;
      font-family: var(--font-family), sans-serif;
      border: none;
      color: var(--color-text-secondary);
      
      margin-left: 20px;
      margin-right: 30px;
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
        margin: 0 0 0 20px;
      }
    }
    
    buttons {
      display: flex;
      
      @media (max-width: 600px) {
        font-size: 0.6rem;
        margin-right: 20px;
      }
    }

    @media (max-width: 600px) {
      height: 20px;
    }
  }

  #main {
    margin-left: 30px;

    @media (max-width: 600px) {
      margin-left: 20px;
    }
  }
</style>
