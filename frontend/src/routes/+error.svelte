<script lang="ts">
	import { page } from '$app/state';
</script>

<div id="error-page">
    <h1>{page.status}</h1>
    <p>{page.error.message}</p>
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

    h1 {
        color: var(--color-primary);
        font-family: var(--font-family);
        margin: 0;
        font-weight: 800;
        font-size: 20vw;
        line-height: .8;
    }

    p {
        color: var(--color-secondary);
        font-family: var(--font-family);
        margin: 0;
        font-weight: 200;
        font-size: 2vw;
    }
</style>