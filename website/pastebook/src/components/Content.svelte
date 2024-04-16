<script lang="ts">
    import { detections } from "$lib/detections.json";
    import { writableContent } from "$lib/stores.js"

    export let content: string = "No content provided"
    export let reportBook: boolean = false
    export let newReport: boolean = false;

    let contentLines = content.split("\n")

    function scanContent(content: String): number {

        if (reportBook === false) {
            return 0;
        }

        for (const key in detections) {
            if (content.toLowerCase().includes(key.toLocaleLowerCase())) {
                return detections[key];
            }
        }

        return 0;
    }

    function onInput(event: InputEvent) {
        writableContent.set((event.target as HTMLInputElement).value)
    }
</script>

<contentcontainer class="new-{newReport}">
    <p>
        {#if !newReport}
            {#each contentLines as line}
                <linecontainer class="severity-{scanContent(line)}">
                    {line}
                </linecontainer>
                <br>
            {/each}
        {:else}
            <textarea class="input" on:input="{onInput}" />
        {/if}
    </p>
</contentcontainer>

<style lang="scss">
  contentcontainer {
    display: inline-block;
    background-color: #eeeeee;
    width: calc(100% - 20px);
    margin: 10px;
    border-radius: 20px;
    animation: fadeIn ease 0.7s;
    animation-delay: 0.3s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    opacity: 0;
    height: 84vh;

    :global(.dark-mode) & {
      background-color: #333333;
    }

    transition: all 0.5s ease;

    &:active {
      transform: scale(0.99);
    }

  }

  .input {
    width: 100%;
    height: 93%;
    border: none;
    background-color: transparent;
    color: inherit;
    font-size: 13px;
    font-family: "JetBrains Mono NL", monospace;
    padding: 10px;
    outline: none;
    resize: none;
  }

  p {
    transition: color 0.2s ease;
    width: 100%;
    height: 80vh;

    display: inline-block;
    font-size: 13px;
    white-space: pre;
    font-family: "JetBrains Mono NL", monospace;
    margin: 20px;
    padding: 10px;

    .severity-1 {
      background-color: rgb(255, 165, 0, 0.7);
    }

    .severity-2 {
      background-color: rgb(255, 0, 0, 0.7);
    }

    :global(body.dark-mode) & {
      color: white;
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
</style>