<script lang="ts">
    import Header from "../../components/Header.svelte";
    import {currentProfile} from "$lib/stores.ts";
    import ListedPaste from "../../components/panel/ListedPaste.svelte";

    const profile: any = $currentProfile
</script>

<div id="profile-container">
    <div id="spacer"></div>
    <Header title="{$currentProfile.username}" created="10 pastes" />
    <p id="id">Created April 16, 2024</p>
    <div id="id-spacer"></div>
    <ListedPaste paste={
    {
        "id": "1234",
        "title": "Hello World",
        "created": new Date()
    }
    }/>
</div>

<style lang="scss">
    #spacer {
      height: 40px;
    }

    #id-spacer {
      height: 20px;
    }

    #profile-container {
      @media (max-width: 600px) {
        padding-top: 7px + 20px;
      }
    }

    #id {
      transition: transform 0.3s, opacity 0.2s, height 0.3s;

      padding-left: 30px;
      margin: 0;
      font-size: 13px;
      font-family: Gabarito, sans-serif;
      color: grey;
      height: 0;

      @media (max-width: 600px) {
        font-size: 6px;
      }
    }
</style>