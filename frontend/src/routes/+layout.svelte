<script lang="ts">
  import Toolbar from "../components/Toolbar.svelte";
  import { title, description, disableNew, backendPort } from "$lib/stores";
  import { onMount } from "svelte";
  import type { Snippet } from "svelte";

  let { data, children }: { data: any, children: Snippet } = $props();

  const newTitle = data.title || 'PastaBook';
  const newDescription = data.description || 'PastaBook is an yummy, tasty way to share your pasta dishes, and respects your privacy by automatically expiring old pasta.';
  const newDisableNew = data.disableNew || false;
  const newFaviconUrl = data.faviconUrl || null;
  const newBackendPort = data.backendPort || 8080;

  title.set(newTitle);
  description.set(newDescription);
  disableNew.set(newDisableNew);
  backendPort.set(newBackendPort);

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
  {@render children()}
</main>
