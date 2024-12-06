  <script lang="ts">
    import Mode from "../components/Mode.svelte";
    import { onMount } from "svelte";
    import { title, description } from "$lib/stores";

    export const commitHash = import.meta.env.VITE_COMMIT_HASH || 'unknown';
    let width = 0;

    onMount(() => {
      width = window.innerWidth;

      window.onresize = () => {
        width = window.innerWidth;
      };
    });
  </script>

  <about>
    <h1>{$title}</h1>
    <p class="description">
      {$description}
    </p>

    <p class="footer">
      <a href="https://github.com/Loudbooks/PasteBook" target="_blank" aria-label="button">pastebook</a>@<a href="https://github.com/Loudbooks/PasteBook/commit/{commitHash}" target="_blank" aria-label="button">{commitHash}</a>
    </p>

    <Mode></Mode>
  </about>

  <svelte:head>
    <meta
      name="description"
      content="{$description}"
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
    }

    .footer {
      position: fixed;
      bottom: 5px;
      font-size: 11px;
      font-family: "Jetbrains Mono", sans-serif;
      color: gray;

      animation: fadeUp 1s var(--animation) forwards;
      animation-delay: 0.4s;
      opacity: 0;

      @media (max-width: 768px) {
        font-size: 8px;
      }

      a {
        color: gray;
        text-decoration: none;
        font-weight: 600;

        &:hover {
          text-decoration: underline;
        }
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
  </style>
