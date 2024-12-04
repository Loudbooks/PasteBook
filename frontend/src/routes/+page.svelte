  <script>
    import Mode from "../components/Mode.svelte";
    import { onMount } from "svelte";

    let width = 0;

    onMount(() => {
      width = window.innerWidth;

      window.onresize = () => {
        width = window.innerWidth;
      };
    });
  </script>

  <about>
    <h1>PasteBook</h1>
    <p class="description">
      PasteBook is an aesthetic, effortless way to share your blocks of text, and
      respects your privacy by automatically deleting your pastes.
    </p>
    <buttons>
      <div id="second-container">
        <a href="https://github.com/Loudbooks/PasteBook" target="_blank"
          >GITHUB</a
        >
        <dot>⎯</dot>
        <a href="mailto:contact@pastebook.dev">CONTACT</a>
        <dot>⎯</dot>
        <a href="/privacy">PRIVACY</a>
      </div>
      <div id="second-container">
        <button on:click={() => {window.location.href = '/new'}} aria-label="button">NEW</button>
      </div>
    </buttons>

    <Mode></Mode>
  </about>

  <svelte:head>
    <meta
      name="description"
      content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes. "
    />
  </svelte:head>

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
        
        animation: fadeUp 1s var(--animation);

        @media (max-width: 768px) {
          font-size: 70px;
          height: 60px;
        }
      }

      .description {
        text-align: center;
        font-size: 25px;
        font-family: Gabarito, sans-serif;
        margin-top: 0;
        padding-top: 10px;
        padding-left: 15px;
        padding-right: 15px;
        color: #333333;
        transition: all 0.5s ease;
        opacity: 0;
        max-width: 800px;

        animation: fadeUp 1s var(--animation);
        animation-delay: 0.2s;
        animation-fill-mode: forwards;

        :global(.dark-mode) & {
          color: lightgray;
        }

        @media (max-width: 768px) {
          font-size: 15px;
          max-width: 400px;
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

      button,
      a {
        display: inline-block;
        appearance: none;
        border: none;
        padding: 10px 10px;
        font-size: 20px;
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
