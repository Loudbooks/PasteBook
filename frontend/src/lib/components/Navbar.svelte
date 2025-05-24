<script>
	import NewPaste from "./button/NewPaste.svelte";

</script>
<div id="navbar">
    <h1>PasteBook</h1>
    <div id="buttons">
        <div id="new">
            <NewPaste />
        </div>
    </div>
</div>

<style lang="scss">
    #navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 1.4rem;
        margin-bottom: 4px;
        height: 52px;
        padding-bottom: 1.6rem;
        background-color: var(--color-background);
        background-image: url("data:image/svg+xml,%3Csvg width='100%25' height='5' xmlns='http://www.w3.org/2000/svg'%3E%3Cline x1='0' y1='0' x2='100%25' y2='0' stroke='%235F5F5FFF' stroke-width='4' stroke-dasharray='5%2C12' stroke-linecap='square' /%3E%3C/svg%3E");
        background-repeat: no-repeat;
        background-position: bottom;
        background-size: 100% 5px;
    }
    
    h1 {
        font-size: 3rem;
        color: var(--color-primary);
        margin: 0;
        font-family: var(--font-family);
        font-weight: 800;
    }

    #buttons {
        display: flex;
        gap: 1rem;
    }
</style>

