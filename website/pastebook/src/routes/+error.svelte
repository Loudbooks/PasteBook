<script>
    import { page } from '$app/stores';
</script>

<error>
    <h1>{$page.status}</h1>
    <p>{$page.error.message}</p>
</error>

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
        font-family: Gabarito, sans-serif;
        font-weight: 800;
        line-height: 25vw;
        margin: 0;
      }

      p {
        text-align: center;
        font-size: 5vw;
        font-family: Gabarito, sans-serif;
        color: gray;
        font-weight: 500;
        margin: 0;
      }
    }
</style>