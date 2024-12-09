<script lang="ts">
  import { onMount } from "svelte";
  import { title } from "$lib/stores";
  import { disableNew } from "$lib/stores";

  onMount(() => {
    const toolbar = document.querySelector("toolbar") as HTMLElement;

    toolbar.style.transform = "translate(10px, 0)";
    toolbar.style.opacity = "1";
  });

  function newPaste() {
    window.location.href = "/new";
  }

  function settings() {
    window.location.href = "/settings";
  }

  function home() {
    window.location.href = "/";
  }
</script>

<toolbar>
  <button id="main" on:click={home}>{$title.toUpperCase()}</button>
  <buttons>
    {#if !$disableNew}
      <button on:click={settings}>SETTINGS</button>
      <button on:click={newPaste}>NEW</button>
    {/if}
  </buttons>
</toolbar>

<style lang="scss">
  toolbar {
    z-index: 999;

    position: absolute;
    width: calc(100% - 20px);
    height: 30px;

    border-bottom-left-radius: 20px;
    border-bottom-right-radius: 20px;
    transform: translate(10px, -100%);
    transition:
      opacity 0.5s ease,
      background-color 0.5s ease,
      border 0.5s ease;

    background-color: #eeeeee;
    opacity: 0;

    
    display: flex;
    justify-content: space-between;
    
    color: gray;
    border: 1px solid #c9c9c9;
    
    :global(.dark-mode) & {
      border: 1px solid #333;
      background-color: #1a1a1a;
    }
    
    &:active {
      transform: scale(0.95);
    }
    
    button {
      transition:
      opacity 0.7s,
      transform 0.5s,
      color 0.5s;
      align-self: center;
      
      font-size: 1rem;
      font-weight: 700;
      margin: 0;
      font-family: Gabarito, sans-serif;
      border: none;
      color: gray;
      
      margin-left: 20px;
      margin-right: 30px;
      padding: 0;
      background-color: transparent;
      outline: none;
      
      &:hover {
        color: darkgray;
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
