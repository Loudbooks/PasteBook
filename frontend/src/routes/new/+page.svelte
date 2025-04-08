<script lang="ts">
  import Content from "../../components/Content.svelte";
  import Header from "../../components/Header.svelte";
  import Submit from "../../components/new/Submit.svelte";
  import Mode from "../../components/Mode.svelte";
  import Switch from "../../components/settings/Switch.svelte";
  import Pulltab from "../../components/pulltab/Pulltab.svelte";
  import Setting from "../../components/settings/Setting.svelte";

  import { expire, wrap, title, description } from "$lib/stores";
  import DropDown from "../../components/settings/DropDown.svelte";
  import { onMount } from "svelte";

  function handleCallback(value: number) {
    expire.set(value);
  }

  let defaultExpire = 86400000;
  let forceWrap = false;

  onMount(() => {
    defaultExpire = parseInt(
      localStorage.getItem("default-expire") ?? "86400000",
    );
    forceWrap = localStorage.getItem("wrap") === "true";
  });
</script>

<main>
  <Mode />
  <Header newReport="true"></Header>
  
  <Content newReport={true}></Content>
  <Pulltab title="Upload Options">
    <svelte:fragment slot="content">
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

  <div id="submit-container">
    <Submit></Submit>
  </div>
</main>

<svelte:head>
  <meta property="og:type" content="website" />
  <meta property="og:title" content="New Paste" />
  <meta property="og:site_name" content="{$title}" />
  <meta
    property="og:description"
    content="{$description}"
  />
</svelte:head>

<style lang="scss">
main {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: calc(100vh - 35px);
  padding: 0;
  margin: 0;
  align-items: center;
  justify-content: flex-start;
}

#submit-container {
  flex-shrink: 0;
  margin-top: 1rem;
  margin-bottom: 0rem;
}

</style>
