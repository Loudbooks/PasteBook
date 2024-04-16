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
    display: block;
    background-color: #eeeeee;
    width: calc(100% - 20px);
    margin: 10px;
    border-radius: 20px;
    animation: fadeIn ease 0.7s;
    animation-delay: 0.3s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    opacity: 0;
    height: 100%;
    overflow: scroll;

    :global(.dark-mode) & {
      background-color: #333333;
    }

    transition: all 0.5s ease;
  }

  .input {
    height: 98%;
    width: 100%;
    border: none;
    background-color: transparent;
    color: inherit;
    font-size: 13px;
    font-family: "JetBrains Mono NL", monospace;
    outline: none;
    resize: none;
    white-space: nowrap;
    overflow: auto;
  }

  p {
    display: block;
    transition: color 0.2s ease;

    font-size: 13px;
    white-space: pre;
    font-family: "JetBrains Mono NL", monospace;
    padding: 30px;
    margin: 0;

    height: 92%;

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