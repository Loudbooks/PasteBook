<script lang="ts">
  import Header from "../../components/Header.svelte";
  import Switch from "../../components/settings/Switch.svelte";
  import Setting from "../../components/settings/Setting.svelte";
  import { onMount } from "svelte";
  import DropDown from "../../components/settings/DropDown.svelte";

  let darkMode = false;
  let wrap = false;
  let defaultUnlisted = false;
  let defaultExpire = 86400000;

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

    defaultExpire = parseInt(
      localStorage.getItem("default-expire") ?? "86400000",
    );
  });

  onMount(() => {
    console.log("aaa");
    if (localStorage.getItem("dark-mode") === null) {
      localStorage.setItem("dark-mode", "true");
    }

    darkMode = localStorage.getItem("dark-mode") === "true";

    if (darkMode) {
      document.body.classList.add("dark-mode");
      document.body.style.background = "#000000";
    } else {
      document.body.classList.remove("dark-mode");
      document.body.style.backgroundColor = "#ffffff";
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
      <Setting name="Dark Mode" description="Activates the dark side">
        <svelte:fragment slot="setting">
          <Switch
            isSelected={darkMode}
            externalHandler={(selected) => {
              if (selected) {
                document.body.classList.add("dark-mode");
                document.body.style.background = "#000000";
              } else {
                document.body.classList.remove("dark-mode");
                document.body.style.backgroundColor = "#ffffff";
              }

              localStorage.setItem("dark-mode", selected.toString());
            }}
          ></Switch>
        </svelte:fragment>
      </Setting>
      <Setting
        name="Force Text Wrap"
        description="Forcefully enable text wrap on all pastes"
      >
        <svelte:fragment slot="setting">
          <Switch
            isSelected={wrap}
            externalHandler={(selected) => {
              localStorage.setItem("wrap", selected.toString());
            }}
          ></Switch>
        </svelte:fragment>
      </Setting>
      <Setting name="Default Unlisted" description="Default paste visibility">
        <svelte:fragment slot="setting">
          <Switch
            isSelected={defaultUnlisted}
            externalHandler={(selected) => {
              localStorage.setItem("default-unlisted", selected.toString());
            }}
          ></Switch>
        </svelte:fragment>
      </Setting>
      <Setting
        name="Default Expire Time"
        description="Default paste expiration time"
      >
        <svelte:fragment slot="setting">
          <DropDown
            callback={(value) => {
              localStorage.setItem("default-expire", value.toString());
            }}
          >
            <svelte:fragment slot="options">
              {#if defaultExpire === 3600000}
                <option value="3600000" selected="selected">1 hour</option>
              {:else}
                <option value="3600000">1 hour</option>
              {/if}
              {#if defaultExpire === 43200000}
                <option value="43200000" selected="selected">12 hours</option>
              {:else}
                <option value="43200000">12 hours</option>
              {/if}
              {#if defaultExpire === 86400000}
                <option value="86400000" selected="selected">24 hours</option>
              {:else}
                <option value="86400000">24 hours</option>
              {/if}
              {#if defaultExpire === 604800000}
                <option value="604800000" selected="selected">1 week</option>
              {:else}
                <option value="604800000">1 week</option>
              {/if}
              {#if defaultExpire === 1209600000}
                <option value="1209600000" selected="selected">2 weeks</option>
              {:else}
                <option value="1209600000">2 weeks</option>
              {/if}
              {#if defaultExpire === 2592000000}
                <option value="2592000000" selected="selected">1 month</option>
              {:else}
                <option value="2592000000">1 month</option>
              {/if}
            </svelte:fragment>
          </DropDown>
        </svelte:fragment>
      </Setting>
    </div>
  </div>
</main>

<svelte:head>
  <meta property="og:type" content="website" />
  <meta property="og:title" content="PasteBook Settings" />
  <meta property="og:site_name" content="PasteBook" />
  <meta property="og:url" content="https://pastebook.dev/settings" />
  <meta
    property="og:description"
    content="Manage your settings for PasteBook."
  />
</svelte:head>

<style lang="scss">
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
