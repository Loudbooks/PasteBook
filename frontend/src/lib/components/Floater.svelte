<script lang="ts">
	import Submit from "./button/Submit.svelte";
	import Settings from "./Settings.svelte";
</script>
<div id="floater-container">
<div id="floater">
    <Settings />
    <Submit />
</div>
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
        width: 25rem;

        @media (max-width: 650px) {
            position: relative;
            bottom: 0;
            right: 50%;
            transform: translateX(50%);
            margin: 0;
            padding: 1rem;
            width: calc(100% - 2rem);
        }
    }
</style>