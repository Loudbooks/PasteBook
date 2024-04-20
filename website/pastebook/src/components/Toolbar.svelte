<script lang="ts">
    import {onMount} from "svelte";

    onMount(() => {
        const toolbar = document.querySelector('toolbar') as HTMLElement;

        toolbar.style.transform = 'translate(10px, 0)';
    });

    function newPaste() {
        window.location.href = '/new';
    }

    function panel() {
        window.location.href = '/panel';
    }
</script>

<toolbar>
    <h1>PASTEBOOK</h1>
    <buttons>
        <button on:click={panel}>PANEL</button>
        <button on:click={newPaste}>NEW</button>
    </buttons>
</toolbar>

<style lang="scss">
  toolbar {
    position: absolute;
    width: calc(100% - 20px);

    border-bottom-left-radius: 20px;
    border-bottom-right-radius: 20px;
    transform: translate(10px, -200%);
    transition: all 0.5s;

    height: 30px;
    background-color: #eeeeee;

    @media (max-width: 600px) {
      height: 20px;
    }

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

    h1, button {
      transition: all 0.5s;
      align-self: center;

      font-size: 1rem;
      font-weight: 700;
      margin: 0;
      padding: 0 0 0 30px;
      font-family: Gabarito, sans-serif;
      border: none;
      color: gray;

      @media (max-width: 600px) {
        font-size: 0.6rem;
      }
    }

    buttons {
      display: flex;

      button {
        padding-left: 20px;
        padding-right: 30px;
        background-color: transparent;
        border: none;
        outline: none;

        &:hover {
          color: darkgray;

          cursor: pointer;
          transform: scale(1.05);
        }
      }
    }
  }
</style>