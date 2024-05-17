<script lang="ts">
    import Toolbar from "../components/Toolbar.svelte";
    import { currentProfile } from "$lib/stores.ts"

    export let data;
    const { profile } = data;

    if (profile != null) {
        $currentProfile = profile;
    } else {
        $currentProfile = null;
    }
</script>

<main>
    <Toolbar isLogged={profile != null}></Toolbar>
    <slot></slot>
</main>