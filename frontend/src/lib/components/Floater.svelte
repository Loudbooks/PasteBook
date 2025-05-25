<script lang="ts">
	import Submit from "./button/Submit.svelte";
	import Settings from "./Settings.svelte";
</script>
<div id="floater">
    <Settings />
    <Submit />
</div>

<style lang="scss">
    #floater {
        position: fixed;
        bottom: 0;
        right: 0;
        margin: 1.4rem;
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }
</style>