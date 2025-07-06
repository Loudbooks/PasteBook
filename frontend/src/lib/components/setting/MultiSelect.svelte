<script lang="ts">
	import SettingHeader from "./SettingHeader.svelte";

	let { values, selected, onChange } = $props();

	let selectedIndex = $state(values.indexOf(selected));

    function updateSelectedIndex(value: string) {
        selectedIndex = values.indexOf(value);
        onChange?.(value);
    }
</script>

<div id="multi-select-container">
    <SettingHeader title="Expiration" />
    <div id="multi-select">
        <div
            id="selection"
            style="width: calc(calc(100% - 0.8rem) / {values.length}); transform: translateX(calc({selectedIndex} * 100%));"
        ></div>

        {#each values as value}
            <div
                class="multi-select-option selected-{selected === value}"
                style="color: {values[selectedIndex] == value ? 'var(--color-background)' : 'var(--color-primary)'}; font-weight: {values[selectedIndex] == value ? '500' : 'normal'}"
                onclick={() => updateSelectedIndex(value)}
            >
                <label for={value}>{value}</label>
            </div>
        {/each}
    </div>
</div>

<style lang="scss">
	#multi-select-container {
		display: flex;
		flex-direction: column;
		gap: 0.2rem;
	}

	#multi-select {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		gap: 0;
		position: relative;
		background-color: var(--color-background);
        border-radius: 10px;
        height: 2.5rem;
		width: calc(100% - 0.8rem);
		font-family: var(--font-family);
		overflow: hidden;
        padding-left: 0.4rem;
        padding-right: 0.4rem;
        margin-top: .3rem;
	}

	.multi-select-option {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		text-align: center;
		padding: 0.75rem;
		cursor: pointer;
		z-index: 2;
		position: relative;
		font-size: 1rem;
        transition: color 0.2s ease-in-out,
            font-weight 0.2s ease-in-out;
	}

    label {
        cursor: pointer;
    }

	#selection {
		position: absolute;
        height: calc(100% - 0.8rem);
		top: 0;
		left: 0;
		background-color: var(--color-primary);
		border-radius: 8px;
		z-index: 1;
		transition: transform 0.2s ease-in-out;
        margin: 0.4rem;
	}
</style>
