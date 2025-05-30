<script lang="ts">
	import { afterNavigate } from '$app/navigation';
	import { onMount, type Snippet } from 'svelte';
	import { language, writableContent, writableTitle } from '../lib/stores';

	let { data, children }: { data: any, children: Snippet } = $props();
    let { title, faviconUrl } = data;

    afterNavigate(() => {
        writableTitle.set("");
        writableContent.set("");
		language.set("none");
    });

	onMount(() => {
		document.title = title ? title : 'PasteBook';

		if (faviconUrl) {
			const favicon = document.querySelector('link[rel="icon"]');
			// @ts-ignore
			favicon.href = newFaviconUrl;
		}

		window.addEventListener("keydown", (event) => {
			if ((event.metaKey || event.ctrlKey) && event.shiftKey && event.key.toLowerCase() === 'p') {
				event.preventDefault();
				window.location.href = '/';
			}
		})
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
