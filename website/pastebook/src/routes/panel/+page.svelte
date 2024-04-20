<script lang="ts">
    import Header from "../../components/Header.svelte";
    import ListedPaste from "../../components/panel/ListedPaste.svelte";
    import Mode from "../../components/Mode.svelte";

    export let data;

    let { pastes } = data;
</script>

<panel>
    <Header title="Pastes" created="{pastes.length}"/>
    {#each pastes as paste}
        <ListedPaste paste="{paste}"/>
    {/each}
    <Mode />
</panel>