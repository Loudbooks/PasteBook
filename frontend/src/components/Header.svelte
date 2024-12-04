<script lang="ts">
  import { pasteName, writableTitle } from "$lib/stores.ts";

  export let title = "";
  export let created = "";
  export let newReport: string = "false";

  pasteName.set(title);

  function onType(event: InputEvent) {
    writableTitle.set((event.target as HTMLInputElement).value);
  }
</script>

<topcontainer class="new-{newReport}">
  {#if newReport === "true"}
    <input id="title" placeholder="Title..." on:input={onType} />
  {:else}
    <name>
      {title}
    </name>
    <created>
      {created}
    </created>
  {/if}
</topcontainer>

<style lang="scss">
  topcontainer {
    display: flex;
    overflow: hidden;

    input {
      display: inline-block;
      font-size: 35px;
      font-family: Gabarito, sans-serif;
      font-weight: 750;
      text-align: left;
      word-break: break-word;
      align-self: end;
      flex: 100%;
      padding-left: 30px;

      animation: fadeIn ease 0.7s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;

      :global(.dark-mode) & {
        color: white;
      }

      transition:
        opacity 0.5s ease,
        transform 0.5s ease;
      outline: none;
      background-color: transparent;
      border: none;
      resize: none;
      overflow: scroll;
    }

    name {
      display: inline-block;
      font-size: 35px;
      font-family: Gabarito, sans-serif;
      font-weight: 750;
      text-align: left;
      word-break: break-word;
      align-self: end;
      flex: 70%;
      padding-left: 30px;

      animation: fadeIn ease 0.7s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;
      transition: color 0.2s ease;

      :global(.dark-mode) & {
        color: white;
      }
    }

    created {
      font-size: 18px;
      font-family: Gabarito, sans-serif;
      text-align: right;
      font-weight: 200;
      opacity: 0;
      color: grey;
      align-self: end;

      padding-right: 30px;

      flex: 30%;

      animation: fadeIn ease 0.7s;
      animation-delay: 0.1s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;

      transition: color 0.2s ease;

      :global(.dark-mode) & {
        color: lightgray;
      }
    }

    @keyframes fadeIn {
      0% {
        opacity: 0;
      }
      100% {
        opacity: 1;
      }
    }

    @media only screen and (max-width: 600px) {
      name {
        font-size: 22px;
      }
    }

    @media only screen and (max-width: 600px) {
      created {
        font-size: 13px;
      }
    }
  }
</style>
