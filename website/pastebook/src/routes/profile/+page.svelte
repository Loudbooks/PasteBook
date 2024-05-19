<script lang="ts">
    import Header from "../../components/Header.svelte";
    import {currentProfile} from "$lib/stores.ts";

    const profile: any = $currentProfile
</script>

<div id="profile-container">
    <div id="spacer"></div>
    <Header title="{$currentProfile.username}'s Profile" />
    <p id="id">{profile.id}</p>
</div>

<style lang="scss">
    #spacer {
      height: 40px;
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