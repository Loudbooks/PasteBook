<script lang="ts">
	import Checkbox from "./setting/Checkbox.svelte";
	import MultiSelect from "./setting/MultiSelect.svelte";
    import { wrap, burn, time } from "$lib/stores";

</script>
<div id="settings">
    <MultiSelect selected="24h" values={["1h", "24h", "7d", "31d"]} onChange={(value: string) => {
        $time = value;
    }}/>
    <Checkbox
        title="Text Wrap"
        checked={$wrap}
        onChange={() => {
            $wrap = !$wrap;
        }}
        description="Wrap the contents of this paste."
    />
    <Checkbox
        title="Burn"
        checked={$burn}
        onChange={() => {
            $burn = !$burn;
        }}
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
</style>