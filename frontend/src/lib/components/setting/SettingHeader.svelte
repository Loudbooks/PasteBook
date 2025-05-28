<script lang="ts">
    let { title } = $props();
</script>
<div id="setting-header">
    <h1 id="setting-header-title">{title}</h1>
</div>

<style lang="scss">
    #setting-header-title {
		font-size: 1.2rem;
        color: var(--color-primary);
        margin: 0;
        font-family: var(--font-family);
        font-weight: 600;
        line-height: .9;
    }
</style>