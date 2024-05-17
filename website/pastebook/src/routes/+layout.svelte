<script>
    import Toolbar from "../components/Toolbar.svelte";

    export let data;
    const { profile } = data;
</script>

<main>
    <Toolbar isLogged={profile != null}></Toolbar>
    <slot></slot>
</main>