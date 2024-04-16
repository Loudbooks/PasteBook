<script>
    import Content from "../../components/Content.svelte";
    import Header from "../../components/Header.svelte";
</script>

<main>
    <Header newReport="true"></Header>
    <Content newReport="true"></Content>
</main>

<style lang="scss">
  main {
    resize: both;
    height: 100vh
  }

  :global(body) {
    height: 100vh;
  }
</style>