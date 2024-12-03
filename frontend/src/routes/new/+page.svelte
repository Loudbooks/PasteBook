<script lang="ts">
  import Content from "../../components/Content.svelte";
  import Header from "../../components/Header.svelte";
  import Submit from "../../components/new/Submit.svelte";
  import Mode from "../../components/Mode.svelte";
  import Switch from "../../components/settings/Switch.svelte";
  import Pulltab from "../../components/pulltab/Pulltab.svelte";
  import Setting from "../../components/settings/Setting.svelte";

  import { expire, unlisted, wrap } from "$lib/stores";
  import DropDown from "../../components/settings/DropDown.svelte";
  import { onMount } from "svelte";

  function handleCallback(value: number) {
    expire.set(value);
  }

  let defaultExpire = 86400000;
  let defaultUnlisted = false;
  let forceWrap = false;

  onMount(() => {
    defaultExpire = parseInt(
      localStorage.getItem("default-expire") ?? "86400000",
    );
    defaultUnlisted = localStorage.getItem("default-unlisted") === "true";
    forceWrap = localStorage.getItem("wrap") === "true";
  });
</script>

<main>
  <div></div>
  <Mode />
  <Header newReport="true" class="header"></Header>
  <Content newReport="true"></Content>
  <Pulltab title="Upload Options">
    <svelte:fragment slot="content">
      <Setting name="Unlisted" description="Hides your paste from the public">
        <svelte:fragment slot="setting">
          <Switch
            isSelected={defaultUnlisted}
            externalHandler={(selected) => {
              $unlisted = selected;
            }}
          ></Switch>
        </svelte:fragment>
      </Setting>
      <Setting
        name="Text Wrap"
        description="Wraps your text to the edges of display"
      >
        <svelte:fragment slot="setting">
          <Switch
            isSelected={forceWrap}
            externalHandler={(selected) => {
              $wrap = selected;
            }}
          ></Switch>
        </svelte:fragment>
      </Setting>
      <Setting
        name="Expire Time"
        description="How long your paste should last before being annihilated"
      >
        <svelte:fragment slot="setting">
          <DropDown callback={handleCallback}>
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
    </svelte:fragment>
  </Pulltab>

  <Submit></Submit>
</main>

<svelte:head>
  <meta property="og:type" content="website" />
  <meta property="og:title" content="New Paste" />
  <meta property="og:site_name" content="PasteBook" />
  <meta property="og:url" content="https://pastebook.dev/new" />
  <meta
    property="og:description"
    content="PasteBook is an aesthetic, effortless way to share your blocks of text, and respects your privacy by automatically deleting your pastes."
  />
</svelte:head>

<style lang="scss">
  div {
    padding-top: 10px + 30px;

    @media (max-width: 600px) {
      padding-top: 27px;
    }
  }

  main {
    flex-direction: column;
    justify-content: center;
    padding: 0;
    height: calc(100vh - 30px);
    margin: 0;
    flex-shrink: inherit;
  }
</style>
