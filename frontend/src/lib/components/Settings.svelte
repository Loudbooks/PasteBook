<script>
	import Checkbox from "./setting/Checkbox.svelte";
	import MultiSelect from "./setting/MultiSelect.svelte";

</script>
<div id="settings">
    <MultiSelect selected="24h" values={["1h", "24h", "7d", "31d"]} onChange={() => {}}/>
    <Checkbox
        title="Text Wrap"
        checked={true}
        onChange={() => {}}
        description="Wrap the contents of this paste."
    />
    <Checkbox
        title="Burn"
        checked={true}
        onChange={() => {}}
        description="Burn this paste after it has been viewed."
    />
</div>

<style lang="scss">
    #settings {
        display: flex;
        flex-direction: column;
        gap: 1rem;
        padding: 1.4rem;
        background-color: var(--color-background-secondary);
        border-radius: 15px;
    }

    h1 {
        font-size: 3rem;
        color: var(--color-primary);
        margin: 0;
        font-family: var(--font-family);
        font-weight: 800;
    }
</style>