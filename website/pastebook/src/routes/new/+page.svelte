<script>
    import Content from "../../components/Content.svelte";
    import Header from "../../components/Header.svelte";
    import Submit from "../../components/Submit.svelte";
    import Mode from "../../components/Mode.svelte";
</script>

<main>
    <Mode/>
    <Header newReport="true" class="header"></Header>
    <Content newReport="true"></Content>
    <Submit></Submit>
</main>

<style>
    main {
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: 100vh;
    }
</style>