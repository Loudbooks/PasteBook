<script lang="ts">
    export let title: string = "Settings";
</script>

<settingssection>
    <div class="section-title">{title}</div>
    <div class="section-content"><slot /></div>
</settingssection>

<style lang="scss">
    .section-title {
        font-size: 1.5rem;
        align-self: center;
        font-family: Gabarito, sans-serif;
        font-weight: 600;
        text-overflow: ellipsis;
        padding-right: 10px;
        overflow: hidden;
        white-space: nowrap;
        text-align: left;
        margin: 0;
        padding: 10px;
        padding-left: 20px;
        color: var(--settings-section-title);
    }

    .section-content {
        color: var(--settings-option-title);
        padding: 5px;
        padding-left: 20px;
        font-family: JetBrains Mono NL, monospace;
        font-size: 1.2rem;
    }
</style>