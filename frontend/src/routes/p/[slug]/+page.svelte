<script lang="ts">
	import Content from "$lib/components/Content.svelte";
	import { formatTimeSince, formatTimeUntil } from "$lib/timehandler.js";
	import { wrap, burn } from "$lib/stores";
	import Navbar from "$lib/components/Navbar.svelte";

	const { data } = $props();
	const { metadata, content, highlightedContent } = data;

	let timeSinceStr = $state("");
	let created = $state<Date | null>(null);
	let expires = $state<Date | null>(null);
	let untilExpire = $state("");
	let title = $state("");
	let language = $state("none");

	metadata.then((data) => {
		created = new Date(data.created);
		$wrap = data.wrap;
		$burn = data.burn;
		title = data.title;
		expires = new Date(data.expires_at);
		language = data.language;

		const reloadTime = () => {
			timeSinceStr = formatTimeSince(created as unknown as number);
			untilExpire = formatTimeUntil(expires as unknown as number);
		};

		reloadTime();
		let clear;
		clearInterval(clear);
		clear = setInterval(reloadTime, 1000);
	});
</script>

{#await metadata then}
	<Navbar
		editable={false}
		{title}
		createdAt={timeSinceStr}
		expiresAt={untilExpire}
		burn={$burn}
		language={language}
		loadingPromise={highlightedContent}
	/>
{/await}
{#await content then response}
	{#await highlightedContent then highlightedResponse}
		{#if highlightedResponse}
			<Content tokenLines={highlightedResponse} />
		{:else}
			<Content content={response} />
		{/if}
	{/await}
{:catch error}
	<div class="error">
		<p>Failed to load content: {error.message}</p>
	</div>
{/await}
