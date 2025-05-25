<script lang="ts">
	import { afterNavigate } from '$app/navigation';
	import { onMount, type Snippet } from 'svelte';
	import { writableContent, writableTitle } from '../lib/stores';

	let { data, children }: { data: any, children: Snippet } = $props();
    let { title, faviconUrl } = data;

    afterNavigate(() => {
        writableTitle.set("");
        writableContent.set("");
    });

	onMount(() => {
		document.title = title ? title : 'PasteBook';

		if (faviconUrl) {
			const favicon = document.querySelector('link[rel="icon"]');
			// @ts-ignore
			favicon.href = newFaviconUrl;
		}
	});
</script>

<div id="layout">
	{@render children()}
</div>

<style lang="scss">
	#layout {
		display: flex;
		flex-direction: column;
		height: 100vh;
		background-color: var(--color-background);
	}
</style>
