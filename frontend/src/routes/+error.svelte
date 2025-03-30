<script>
  import { page } from "$app/stores";
  import Mode from "../components/Mode.svelte";
</script>

<error>
  <h1>{$page.status}</h1>
  <p>{$page.error.message}</p>
  <Mode></Mode>
</error>

<svelte:head>
  <title>Error</title>
  <meta name="description" content="Paste not found." />
  <meta name="theme-color" content="#EE4B2B" />
</svelte:head>

<style lang="scss">
  error {
    overflow: hidden;
    overscroll-behavior: none;
    display: flex;
    flex-direction: column;
    height: 100vh;
    justify-content: center;

    h1 {
      text-align: center;
      font-size: 30vw;
      font-family: Comic Neue, sans-serif;
      font-weight: 800;
      line-height: 25vw;
      margin: 0;

      :global(body.dark-mode) & {
        color: #b21807;
      }
    }

    p {
      text-align: center;
      font-size: 5vw;
      font-family: Comic Neue, sans-serif;
      color: #f87162;
      font-weight: 500;
      margin: 0;
    }
  }
</style>
