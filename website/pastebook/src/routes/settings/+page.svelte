<script>
    import Header from "../../components/Header.svelte";
    import Mode from "../../components/Mode.svelte";
	import Theme from "../../components/Theme.svelte";
	import Settings from "../../components/settings/Settings.svelte";
    
    
</script>


<main>
    <div />
    <Theme/>
    <!-- <Mode/> -->
    <Header title="Settings"></Header>
    <Settings />
</main>

<style lang="scss">
    div {
        padding-top: 7px + 30px;

        @media (max-width: 600px) {
        padding-top: 7px + 20px;
        }
    }
</style>