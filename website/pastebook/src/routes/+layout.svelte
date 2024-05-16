<script>
    import Toolbar from "../components/Toolbar.svelte";

    export let data;
    const { profile } = data;
</script>

<main>
    <Toolbar profile={profile}></Toolbar>
    <slot></slot>
</main>