<script lang="ts">
    import { writableTitle } from "$lib/stores.js";

    export let title = ""
    export let created = ""
    export let newReport: boolean = false


    function onType(event: InputEvent) {
        writableTitle.set((event.target as HTMLInputElement).value)
    }
</script>

<topcontainer class="new-{newReport}">
    {#if newReport}
        <input placeholder="Title" on:input="{onType}">
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
    padding-top: 7px;
    display: flex;

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

      transition: all 0.5s ease;
      outline: none;
      background-color: transparent;
      border: none;
      resize: none;

      &:hover {
        cursor: pointer;
        transform: scale(1.01);
      }

      &:active {
        transform: scale(0.98);
      }
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

      :global(.dark-mode) & {
        color: white;
      }

      transition: color 1s ease;
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

      :global(.dark-mode) & {
        color: lightgray;
      }

      transition: color 1s ease;
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