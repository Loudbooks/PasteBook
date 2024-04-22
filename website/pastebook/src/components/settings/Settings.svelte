<script>
	import SettingsSection from "./SettingsSection.svelte";
    import { getTheme, getThemes, setTheme } from '../Theme.svelte';
	import { onMount } from "svelte";

    let currentTheme = '';
    let themes = [];

    onMount(() => {
        currentTheme = getTheme();
        themes = getThemes();
    });
</script>
<settingscontainer>
    <settingscontent>
    <SettingsSection title="User Settings">
        <div class="theme option">
            <span style="float: left;">Theme</span>
            <select style="float: right;" value={currentTheme}>
                {#each themes as theme}
                    <option value={theme.key}>{theme.name}</option>
                {/each}
            </select>
        </div>
        </SettingsSection>
        </settingscontent>
</settingscontainer>

<style lang="scss">
    settingscontainer {
        display: block;
        background-color: #eeeeee;
        width: calc(100% - 20px);
        margin: 10px;
        border-radius: 20px;
        animation: fadeIn ease 0.5s;
        animation-delay: 0.2s;
        animation-iteration-count: 1;
        animation-fill-mode: forwards;
        height: calc(100% - 140px);
        overflow-x: scroll;
        border: 1px solid #c9c9c9;

        @media (max-width: 600px) {
        height: calc(100% - 130px);
        }

        :global(.dark-mode) & {
        border: 1px solid #333;
        background-color: #1a1a1a;
        }

        transition: all 0.5s ease;
    }

    settingscontent {
        display: block;
    }
    .option {
        padding-left: 20px;
        padding-right: 40px;
    }

    
</style>
