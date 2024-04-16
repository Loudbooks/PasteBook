<script>
    import Content from "../../components/Content.svelte";
    import Header from "../../components/Header.svelte";
    import Submit from "../../components/Submit.svelte";
    import Mode from "../../components/Mode.svelte";
</script>

<main>
    <Mode/>
    <Header newReport="true"></Header>
    <Content newReport="true"></Content>
    <Submit></Submit>
</main>