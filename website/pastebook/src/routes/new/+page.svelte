<script>
    import Content from "../../components/Content.svelte";
    import Header from "../../components/Header.svelte";
    import Submit from "../../components/new/Submit.svelte";
    import Mode from "../../components/Mode.svelte";
</script>

<main>
    <div></div>
    <Mode/>
    <Header newReport="true" class="header"></Header>
    <Content newReport="true"></Content>
    <Submit></Submit>
</main>

<style>
    div {
        padding-top: 37px;

        @media (max-width: 600px) {
            padding-top: 27px;
        }
    }

    main {
        flex-direction: column;
        justify-content: center;
        padding: 0;
        height: calc(100vh - 30px);
        margin: 0;
        flex-shrink: inherit;
    }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="New Paste" />
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/new"/>
    <meta property="og:description" content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes."/>
</svelte:head>