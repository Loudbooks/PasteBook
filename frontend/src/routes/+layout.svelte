<script lang="ts">
  import Toolbar from "../components/Toolbar.svelte";
  import { title, description, disableNew } from "$lib/stores";
  import { onMount } from "svelte";
  import type { Snippet } from "svelte";

  let { data, children }: { data: PageData, children: Snippet } = $props();

  const newTitle = data.title || 'PasteBook';
  const newDescription = data.description || 'PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes.';
  const newDisableNew = data.disableNew || false;

  title.set(newTitle);
  description.set(newDescription);
  disableNew.set(newDisableNew);

  onMount(() => {
    document.title = newTitle;
  });
</script>

<main>
  <Toolbar></Toolbar>
  {@render children()}
</main>
