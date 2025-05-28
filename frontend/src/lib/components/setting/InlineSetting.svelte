<script lang="ts">
	import SettingHeader from './SettingHeader.svelte';

	let { title, description, children } = $props();
</script>

<div id="inline-setting">
	<div id="inline-setting-header">
		<SettingHeader {title} />
		{#if description}
			<p>{description}</p>
		{/if}
	</div>
	<div>
		{@render children?.()}
	</div>
</div>

<style lang="scss">
	p {
		font-size: 0.8rem;
		color: var(--color-text-secondary);
		width: 100%;
		margin: 0;
		font-family: var(--font-family);
		font-weight: 200;
	}

    #inline-setting-header {
        display: flex;
        flex-direction: column;
        gap: 0.2rem;
    }

	#inline-setting {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		gap: 0;
	}
</style>
