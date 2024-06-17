<script lang="ts">
    import Header from "../../components/Header.svelte";
    import Switch from "../../components/settings/Switch.svelte";
    import Setting from "../../components/settings/Setting.svelte";
    import {onMount} from "svelte";
    import DropDown from "../../components/settings/DropDown.svelte";

    let darkMode = false;
    let wrap = false;
    let defaultUnlisted = false;
    let defaultExpire = 86400000;
    let theme: string;

    let themeOptions = [
        {value: "dark", text: "Dark"},
        {value: "light", text: "Light"}
    ];
    let defaultExpireOptions = [
        {value: 3600000, text: "1 hour"},
        {value: 43200000, text: "12 hours"},
        {value: 86400000, text: "24 hours"},
        {value: 604800000, text: "1 week"},
        {value: 1209600000, text: "2 weeks"},
        {value: 2592000000, text: "1 month"}
    ];

    onMount(() => {
        darkMode = localStorage.getItem("dark-mode") === "true";

        if (darkMode) {
            document.body.classList.add("dark-mode");
        }

        wrap = localStorage.getItem("wrap") === "true";

        if (wrap) {
            document.body.classList.add("wrap");
        }

        defaultUnlisted = localStorage.getItem("default-unlisted") === "true";

        if (defaultUnlisted) {
            document.body.classList.add("default-unlisted");
        }

        defaultExpire = parseInt(localStorage.getItem("default-expire") ?? "86400000");

        theme = localStorage.getItem("theme") ?? "dark";
    });

    onMount(() => {
        console.log("aaa");
        if (localStorage.getItem('dark-mode') === null) {
            localStorage.setItem('dark-mode', 'true');
        }

        darkMode = localStorage.getItem('dark-mode') === 'true';

        if (darkMode) {
            document.body.classList.add('dark-mode');
            document.body.style.background = '#000000';
        } else {
            document.body.classList.remove('dark-mode');
            document.body.style.backgroundColor = '#ffffff';
        }
    });
</script>

<main>
    <div id="padding-container">
        <div id="padding"></div>
    </div>
    <Header title="Settings" created=""></Header>
    <div id="container">
        <div id="settings">
            <Setting name="Theme" description="Changes the look of PasteBook">
                <svelte:fragment slot="setting">
                    <DropDown callback={(value) => {
                        localStorage.setItem("theme", value.toString());
                    }}>
                        <svelte:fragment slot="options">
                            {#each themeOptions as option}
                                {#if theme === option.value}
                                    <option value={option.value} selected="selected">{option.text}</option>
                                {:else}
                                    <option value={option.value}>{option.text}</option>
                                {/if}
                            {/each}
                        </svelte:fragment>
                    </DropDown>
                </svelte:fragment>
            </Setting>
            <Setting name="Force Text Wrap" description="Forcefully enable text wrap on all pastes">
                <svelte:fragment slot="setting">
                    <Switch isSelected={wrap} externalHandler={(selected) => {
                        localStorage.setItem("wrap", selected.toString());
                        }}></Switch>
                </svelte:fragment>
            </Setting>
            </div>
            </div>
            <br>
    <Header title="Default Paste Options" created="" subtitle></Header>
            <div id="container" class="default-settings">
        <div id="settings">
            <Setting name="Default Unlisted" description="Default paste visibility">
                <svelte:fragment slot="setting">
                    <Switch isSelected={defaultUnlisted} externalHandler={(selected) => {
                        localStorage.setItem("default-unlisted", selected.toString());
                        }}></Switch>
                </svelte:fragment>
            </Setting>
            <Setting name="Default Expire Time" description="Default paste expiration time">
                <svelte:fragment slot="setting">
                    <DropDown callback={(value) => {
                        localStorage.setItem("default-expire", value.toString());
                    }}>
                        <svelte:fragment slot="options">
                            {#each defaultExpireOptions as option}
                                {#if defaultExpire === option.value}
                                    <option value={option.value} selected="selected">{option.text}</option>
                                {:else}
                                    <option value={option.value}>{option.text}</option>
                                {/if}
                            {/each}
                        </svelte:fragment>
                    </DropDown>
                </svelte:fragment>
            </Setting>
        </div>
    </div>
</main>

<style lang="scss">

.default-settings {
    margin-top: 200px;
}

  #container {
    transition: all 0.5s ease;

    display: block;
    background-color: #eeeeee;
    width: calc(100% - 20px);
    margin: 10px;
    border-radius: 20px;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    height: calc(100% - 140px);
    overflow-x: scroll;
    border: 1px solid #c9c9c9;
    box-sizing: border-box;
    padding: 10px 10px 5px;
    opacity: 0;

    @media (max-width: 600px) {
      height: calc(100% - 130px);
      margin-top: 6px;
    }

    :global(.dark-mode) & {
      border: 1px solid #333;
      background-color: #1a1a1a;
    }

    animation: fadeIn 0.5s forwards;
  }

  #padding {
    padding-top: 10px + 30px;

    @media (max-width: 600px) {
      padding-top: 7px + 20px;
    }
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }

    to {
      opacity: 1;
    }
  }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="PasteBook Settings"/>
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/settings"/>
    <meta property="og:description" content="Manage your settings for PasteBook."/>
</svelte:head>