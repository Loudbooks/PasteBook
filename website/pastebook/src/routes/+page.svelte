<script>
    import Mode from "../components/Mode.svelte";
    import {onMount} from "svelte";

    let width = 0;

    onMount(() => {
        width = window.innerWidth;

        window.onresize = () => {
            width = window.innerWidth;
        }
    })
</script>

<about>
    <h1>PasteBook</h1>
    <p class="author">By Loudbook</p>
    <p class="description">PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your
        privacy by
        automatically deleting your pastes.</p>
    <buttons>
        {#if width > 768}
            <button onclick="window.location.href = '/new';">NEW</button>
            <dot>⎯</dot>
            <button onclick="window.location.href = '/panel';">PANEL</button>
            <dot>⎯</dot>
            <a href="https://github.com/Loudbooks/PasteBook" target="_blank">GITHUB</a>
            <dot>⎯</dot>
            <a href="mailto:contact@pastebook.dev">CONTACT</a>
            <dot>⎯</dot>
            <a href="/privacy">PRIVACY</a>
        {:else}
            <div id="second-container">
                <button onclick="window.location.href = '/new';">NEW</button>
                <dot>⎯</dot>
                <button onclick="window.location.href = '/panel';">PANEL</button>
            </div>
            <div id="second-container">
                <a href="https://github.com/Loudbooks/PasteBook" target="_blank">GITHUB</a>
                <dot>⎯</dot>
                <a href="mailto:contact@pastebook.dev">CONTACT</a>
                <dot>⎯</dot>
                <a href="/privacy">PRIVACY</a>
            </div>
        {/if}
    </buttons>

    <Mode></Mode>
</about>

<style lang="scss">
  :root {
    --animation: ease;
  }

  about {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: white;
    transition: all 0.5s var(--animation);
    overflow: hidden;

    :global(.dark-mode) & {
      background-color: black;
      color: white;
    }

    color: black;

    h1 {
      margin-top: 0;
      text-align: center;
      font-family: Gabarito, serif;
      font-weight: 1000;
      font-size: 120px;
      margin-bottom: 10px;
      height: 120px;

      @media (max-width: 768px) {
        font-size: 50px;
        height: 50px;
      }

      animation: fadeUp 1s var(--animation);
    }

    .author {
      font-family: Gabarito, sans-serif;
      font-size: 20px;
      margin-top: 0;
      color: #333333;
      opacity: 0;

      transition: all 0.5s var(--animation);

      :global(.dark-mode) & {
        color: lightgray;
      }

      @media (max-width: 768px) {
        font-size: 13px;
      }

      animation: fadeUp 1s var(--animation);
      animation-delay: 0.1s;
      animation-fill-mode: forwards;
    }

    .description {
      text-align: center;
      font-size: 25px;
      font-family: Gabarito, sans-serif;
      margin-top: 0;
      padding-top: 20px;
      padding-left: 15px;
      padding-right: 15px;
      color: #333333;
      transition: all 0.5s ease;
      opacity: 0;

      animation: fadeUp 1s var(--animation);
      animation-delay: 0.2s;
      animation-fill-mode: forwards;

      :global(.dark-mode) & {
        color: lightgray;
      }

      @media (max-width: 768px) {
        font-size: 15px;
      }
    }

    buttons {
      opacity: 0;
      animation: fadeUp 1s var(--animation);
      animation-delay: 0.3s;
      animation-fill-mode: forwards;
    }

    dot {
      color: gray;
      font-size: 20px;
      margin: 0 10px;
      transition: all 1s ease;
      text-align: center;

      &:hover {
        margin: 0 13px;
        cursor: default;
      }

      &::selection {
        background-color: transparent;
      }

      @media (max-width: 768px) {
        font-size: 13px;
        margin: 0 5px;
      }
    }

    button, a {
      display: inline-block;
      appearance: none;
      border: none;
      padding: 10px 20px;
      font-size: 25px;
      background-color: transparent;
      color: gray;
      cursor: pointer;
      transition: all 0.5s;
      font-family: Gabarito, sans-serif;
      text-decoration: none;
      font-weight: 600;

      &:hover {
        color: darkgray;
        cursor: pointer;
      }

      &:active {
        transform: scale(0.97);
      }

      @media (max-width: 768px) {
        font-size: 13px;
        padding: 5px 0;
      }
    }

    #second-container {
      display: flex;
      align-items: center;
      justify-content: center;
      width: 100%;

      a {
        display: block;
      }
    }

    @keyframes fadeUp {
      from {
        opacity: 0;
        transform: translateY(25px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }
  }
</style>

<svelte:head>
    <meta name="description"
          content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes. "/>
</svelte:head>