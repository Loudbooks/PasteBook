<script lang="ts">
	import { page } from '$app/state';
	import Navbar from '$lib/components/Navbar.svelte';
</script>

<Navbar />
<div id="error-page">
    <h1 id="error-title">{page.status}</h1>
    <p id="error-description">{page.error.message}</p>
</div>

<style lang="scss">
    #error-page {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        background-color: var(--color-background);
    }

    #error-title {
        color: var(--color-primary);
        font-family: var(--font-family);
        margin: 0;
        font-weight: 800;
        font-size: 25vw;
        line-height: .8;
    }

    #error-description {
        color: var(--color-secondary);
        font-family: var(--font-family);
        margin: 0;
        font-weight: 200;
        font-size: 3vw;
    }
</style>