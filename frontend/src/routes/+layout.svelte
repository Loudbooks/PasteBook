<script lang="ts">
  import Toolbar from "../components/Toolbar.svelte";
  import { title, description } from "$lib/stores";

  let { data }: { data: PageData } = $props();
  const newTitle = data.title || 'PasteBook';
  const newDescription = data.description || 'PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes.';

  title.set(newTitle);
  description.set(newDescription);
</script>

<main>
  <Toolbar></Toolbar>
  <slot></slot>
</main>
