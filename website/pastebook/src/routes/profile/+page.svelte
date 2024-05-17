<script lang="ts">
    import Header from "../../components/Header.svelte";
    import {currentProfile} from "$lib/stores.ts";

    const profile: any = $currentProfile
</script>

<div id="profile-container">
    <Header title="{$currentProfile.username}'s Profile" />
</div>

<style lang="scss">
    #profile-container {
      padding-top: 10px + 30px;

      @media (max-width: 600px) {
        padding-top: 7px + 20px;
      }
    }
</style>