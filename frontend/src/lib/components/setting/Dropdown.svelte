<script lang="ts">
    import InlineSetting from "./InlineSetting.svelte";

    let { title, description, options, onChange } = $props();

    let selected = $state(options[0]);
</script>

<InlineSetting {title} {description}>
    <select bind:value={selected} onchange={() => onChange?.(selected)}>
        {#each options as option}
            <option value={option}>{option}</option>
        {/each}
    </select>
</InlineSetting>

<style lang="scss">
    select {
        padding: 5px 10px 5px 10px;
        height: 2.5rem;
        width: 100px;
        line-height: 1;
        border-radius: 10px;
        border: none;
        color: var(--color-primary);
        font-family: var(--font-family), sans-serif;
		font-size: .9rem;
        font-weight: 500;
        background-color: var(--color-background);

        -webkit-appearance: none;
        float: right;
        text-align-last: center;

        &:hover {
            cursor: pointer;
        }

        &:focus {
            outline: none;
        }
    }

    option {
        text-align-last: center;
    }
</style>
