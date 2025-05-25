<script lang="ts">
	import Content from '$lib/components/Content.svelte';
	import Spacer from '$lib/components/Spacer.svelte';
	import { formatTimeSince, formatTimeUntil } from '$lib/timehandler.js';
    import { wrap, burn } from '$lib/stores';

	export let data;

	const { metadata, content } = data;

	let timeSinceStr = '';
	let created = new Date();
	let expires = new Date();
	let untilExpire = '';

	metadata.then((data) => {
		created = new Date(data.created);
		$wrap = data.wrap;
        $burn = data.burn;
		expires = new Date(data.expires_at);

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

<Spacer />

{#await content then response}
	<Content content={response} />
{:catch error}
	<div class="error">
		<p>Failed to load content: {error.message}</p>
	</div>
{/await}
