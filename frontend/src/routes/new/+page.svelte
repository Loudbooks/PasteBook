<script lang="ts">
	import Content from "$lib/components/Content.svelte";
	import Floater from "$lib/components/Floater.svelte";
	import Spacer from "$lib/components/Spacer.svelte";

    let floater: Floater;
</script>
<Spacer />
<Floater bind:this={floater} />
<Content />