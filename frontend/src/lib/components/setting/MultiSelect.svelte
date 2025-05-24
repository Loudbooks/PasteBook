<script lang="ts">
	import { onMount } from "svelte";

	let { values, selected, onChange } = $props();

	let selectedIndex = $state(values.indexOf(selected));

    onMount(() => {
        console.log("Selected index:", values[selectedIndex]);
    });

    function updateSelectedIndex(value: string) {
        selectedIndex = values.indexOf(value);
        onChange?.(value);
    }
</script>

<div id="multi-select">
	<div
		id="selection"
		style="width: calc(calc(100% - 0.8rem) / {values.length}); transform: translateX(calc({selectedIndex} * 100%));"
	></div>

	{#each values as value}
		<div
			class="multi-select-option selected-{selected === value}"
            style="color: {values[selectedIndex] == value ? 'var(--color-background)' : 'var(--color-primary)'}; font-weight: {values[selectedIndex] == value ? 'bold' : 'normal'}"
			on:click={() => updateSelectedIndex(value)}
		>
			<label for={value}>{value}</label>
		</div>
	{/each}
</div>

<style lang="scss">
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
	}

	.multi-select-option {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		text-align: center;
		padding: 0.75rem;
		cursor: pointer;
        text-align: center;
		z-index: 2;
		position: relative;
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
		border-radius: 7px;
		z-index: 1;
		transition: transform 0.2s ease-in-out;
        margin: 0.4rem;
	}
</style>
