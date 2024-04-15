<script lang="ts">
    import {detections} from "./detections.json";

    export let content: string = "No content provided"
    export let reportBook: boolean = false

    let contentLines = content.split("\n")

    function scanContent(content: String) {
        if (reportBook === false) {
            return false;
        }

        for (let string of detections) {
            if (content.toLowerCase().includes(string.toLocaleLowerCase())) {
                return true;
            }
        }

        return false;
    }
</script>

<contentcontainer>
    <p>
        {#each contentLines as line}
            <linecontainer class="flagged-{scanContent(line)}">
                {line}
            </linecontainer>
            <br>
        {/each}
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
    overflow: auto;
  }

  p {
    display: inline-block;
    font-size: 13px;
    white-space: pre;
    font-family: "JetBrains Mono NL", monospace;
    margin: 20px;
    padding: 10px;

    .flagged-true {
      color: red;
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