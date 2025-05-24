<script>
	import MultiSelect from "./setting/MultiSelect.svelte";
	import SettingHeader from "./setting/SettingHeader.svelte";

</script>
<div id="settings">
    <SettingHeader title="Expiration" />
    <MultiSelect selected="24h" values={["1h", "24h", "7d", "31d"]} onChange={() => {}}/>
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