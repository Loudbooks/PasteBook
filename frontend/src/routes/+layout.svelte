<script lang="ts">
  import Toolbar from "../components/Toolbar.svelte";
  import { title, description, disableNew } from "$lib/stores";
  import { onMount } from "svelte";
  import type { Snippet } from "svelte";

  let { data, children }: { data: any, children: Snippet } = $props();

  const newTitle = data.title || 'PasteBook';
  const newDescription = data.description || 'PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes.';
  const newDisableNew = data.disableNew || false;
  const newFaviconUrl = data.faviconUrl || null;

  title.set(newTitle);
  description.set(newDescription);
  disableNew.set(newDisableNew);

  onMount(() => {
    document.title = newTitle;

    if (newFaviconUrl) {
      const favicon = document.querySelector('link[rel="icon"]');
      // @ts-ignore
      favicon.href = newFaviconUrl;
    }
  });
</script>

<main>
  <Toolbar></Toolbar>
  <div id="padding-container"></div>
  {@render children()}
</main>

<style lang="scss">

  #padding-container {
    padding-top: 30px;

    @media (max-width: 600px) {
      padding-top: 20px;
    }
  }
</style>