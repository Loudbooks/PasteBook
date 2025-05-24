<script lang="ts">
    import Navbar from "$lib/components/Navbar.svelte";
	import type { Snippet } from "svelte";

    let { children }: { children: Snippet } = $props();
</script>

<div id="layout">
    <Navbar />
    {@render children()}
</div>

<style lang="scss">
    #layout {
        display: flex;
        flex-direction: column;
        height: 100vh;
        background-color: var(--color-background);
    }
</style>