<script lang="ts">
    import {detections} from "$lib/detections.json";
    import {writableContent} from "$lib/stores.js"

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

    function getIndex(index: number): string {
        let max = contentLines.length
        let maxStringLength = max.toString().length
        return index.toString().padStart(maxStringLength, " ")
    }
</script>

<contentcontainer class="new-{newReport}">
    {#if !newReport}
        <lines>
            {#each contentLines as line, index}
                <linecontainer>
                    <number class="number">
                        {getIndex(index + 1)}
                    </number>
                    <linecontentcontainer class="severity-{scanContent(line)}">
                        {line}
                    </linecontentcontainer>
                </linecontainer>
            {/each}
        </lines>
    {:else}
        <textarea class="input" on:input="{onInput}" />
    {/if}
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
    overflow-x: scroll;

    :global(.dark-mode) & {
      background-color: #1a1a1a;
    }

    transition: all 0.5s ease;
  }

  .input {
    display: inline-block;
    border: none;
    width: calc(100% - 60px);
    height: calc(100% - 65px);
    background-color: transparent;
    color: inherit;
    font-size: 13px;
    font-family: "JetBrains Mono NL", monospace;
    outline: none;
    resize: none;
    margin: 30px;
    white-space: pre;
    padding: 0;

    :global(.dark-mode) & {
      color: white;
    }
  }

  linecontainer {
    display: block;
    &:hover {
      .number {
        color: #919191;
      }
    }
  }

  lines {
    display: block;
    padding: 30px 30px 30px 10px;
    height: calc(100% - 60px);
  }

  .number {
    display: inline-block;
    text-align: right;
    padding-right: 10px;
    color: #9999;
    font-family: "JetBrains Mono NL", monospace;
    font-size: 13px;
    margin: 0;
    opacity: 1;

    .dark-mode & {
      color: #999;
    }

    transition: all 0.2s ease;
  }

  linecontainer {
    display: block;
    transition: color 0.2s ease;

    font-size: 13px;
    white-space: pre;
    font-family: "JetBrains Mono NL", monospace;
    margin: 0;

    :global(body.dark-mode) & {
      color: white;
    }
  }

  linecontentcontainer {
    padding-right: 30px;
    &.severity-1 {
      background-color: rgb(255, 165, 0, 0.7);
    }

    &.severity-2 {
      background-color: rgb(255, 0, 0, 0.6);
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