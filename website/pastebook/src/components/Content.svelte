<script lang="ts">
  import {
    severes,
    validScan,
    warnings,
    wrap,
    writableContent,
  } from "$lib/stores";
  import detection from "$lib/detections.json";

  import type { Issue } from "$lib/issue";
  import { onMount } from "svelte";
  import { pushState } from "$app/navigation";

  export let content: string = "No content provided";
  export let reportBook: boolean = false;
  export let newReport: boolean = false;
  export let wrapPre: boolean = false;
  export let inspect: boolean = false;

  const results: Issue[] = detection.map(
    ({ filename, visual, description, severity }) => ({
      id: filename,
      visual,
      description,
      severity,
    }),
  );

  let currentScrolledLine = 0;
  let contentLines = content.split("\n");

  const warn: Issue[] = [];
  const severe: Issue[] = [];

  onMount(() => {
    const wrapValue = localStorage.getItem("wrap");
    if (wrapValue === "true") {
      wrap.set(true);
      wrapPre = true;
    }

    wrap.subscribe((value) => {
      const textArea = document.querySelector(
        ".input",
      ) as HTMLTextAreaElement | null;
      if (textArea) {
        textArea.style.whiteSpace = value ? "break-spaces" : "pre";
      }
    });

    const lineNumber = getLineNumberFromURL();
    if (lineNumber) {
      scrollToLine(lineNumber);
    }
  });

  if (inspect) {
    inspectContent(contentLines);
    warnings.set(warn);
    severes.set(severe);
  }

  if (canScan(contentLines)) {
    validScan.set(true);
  }

  function inspectContent(lines: string[]) {
    for (const line of lines) {
      if (!reportBook) continue;

      for (const result of results) {
        if (line.toLowerCase().includes(result.id.toLowerCase())) {
          result.severity === 1 ? warn.push(result) : severe.push(result);
        }
      }
    }
  }

  function canScan(lines: string[]): boolean {
    return lines.some((line) => {
      return (
        getLineSeverity(line) !== 0 ||
        (reportBook &&
          results.some((result) =>
            line.toLowerCase().includes(result.id.toLowerCase()),
          ))
      );
    });
  }

  function getLineSeverity(line: string): number {
    const trimmed = line.trim().toLowerCase();
    const lowerCase = line.toLowerCase();
    if (trimmed.includes("[warn") || trimmed.includes("/warn]")) return 1;
    if (
      trimmed.includes("[severe]") ||
      trimmed.includes("[error") ||
      trimmed.includes("/error]") ||
      trimmed.startsWith("caused by:") ||
      lowerCase.startsWith("\tat") ||
      (trimmed.includes("exception") && trimmed.includes("provided by"))
    ) {
      return 2;
    }
    return 0;
  }

  function scanContent(line: string): number {
    if (!inspect) return 0;
    if (!reportBook) return getLineSeverity(line);

    const result = results.find((result) =>
      line.toLowerCase().includes(result.id.toLowerCase()),
    );
    return result ? result.severity : 0;
  }

  function onInput(event: InputEvent) {
    writableContent.set((event.target as HTMLInputElement).value);
  }

  function getIndex(index: number): string {
    return index
      .toString()
      .padStart(contentLines.length.toString().length, " ");
  }

  function scrollToLine(lineNumber: number) {
    const lineElement = document.getElementById(`line-number-${lineNumber}`);
    if (lineElement) {
      currentScrolledLine = lineNumber;
      updateLineView(lineNumber);

      const offsetY = $wrap
        ? window.innerHeight / 2 - 200
        : window.innerHeight / 2;
      window.scrollTo(0, lineElement.getBoundingClientRect().y - offsetY);
    }
  }

  function updateLineView(newLine: number) {
    const newLineElement = document.getElementById(`line-container-${newLine}`);
    const newLineNumberElement = document.getElementById(
      `line-number-${newLine}`,
    );
    if (newLineElement && newLineNumberElement) {
      if (currentScrolledLine) {
        const currentLineElement = document.getElementById(
          `line-container-${currentScrolledLine}`,
        );
        const currentLineNumberElement = document.getElementById(
          `line-number-${currentScrolledLine}`,
        );
        if (currentLineElement && currentLineNumberElement) {
          currentLineElement.style.marginTop = "0px";
          currentLineElement.style.marginBottom = "0px";
          currentLineNumberElement.style.fontWeight = "normal";
        }
      }

      newLineElement.style.marginTop = "20px";
      newLineElement.style.marginBottom = "20px";
      newLineNumberElement.style.fontWeight = "1000";

      currentScrolledLine = newLine;
    }
  }

  function getLineNumberFromURL(): number | null {
    const lineParam = new URLSearchParams(window.location.search).get("line");
    return lineParam ? parseInt(lineParam, 10) : null;
  }

  function removeCurrentScrolledLine() {
    const currentLineElement = document.getElementById(
      `line-container-${currentScrolledLine}`,
    );
    const currentLineNumberElement = document.getElementById(
      `line-number-${currentScrolledLine}`,
    );
    if (currentLineElement && currentLineNumberElement) {
      currentLineElement.style.marginTop = "0px";
      currentLineElement.style.marginBottom = "0px";
      currentLineNumberElement.style.fontWeight = "normal";
    }
  }

  function clickNumber(event: MouseEvent) {
    const element = event.currentTarget as HTMLElement;
    const id = parseInt(element.id.replace("line-", ""), 10);

    scrollToElement(`line-container-${id}`);

    if (currentScrolledLine !== id) {
      updateLineView(id);

      const currentURL = new URL(window.location.href);
      const newParams = new URLSearchParams(currentURL.search);
      newParams.set("line", id.toString());

      pushState(currentURL.pathname + "?" + newParams.toString(), {
        replace: true,
      });
    }
  }

  function scrollToElement(elementId: string) {
    const element = document.getElementById(elementId);
    const container = document.querySelector("content-container");
    if (element && container) {
      const currentX = container.scrollLeft;
      element.scrollIntoView({
        block: "center",
        inline: "center",
        behavior: "smooth",
      });
      container.scrollLeft = currentX;
    }
  }

  function removeSpaces(line: string): string {
    return line.replace(/\s/g, "");
  }
</script>

<content-container class="new-{newReport}">
  {#if !newReport}
    <div style="display: table">
      <lines>
        {#each contentLines as line, index}
          <linecontainer
            id="line-container-{removeSpaces(getIndex(index + 1))}"
            class="wrap-{wrapPre}"
          >
            <a
              role="button"
              id="line-{removeSpaces(getIndex(index + 1))}"
              on:click={clickNumber}
              tabindex="0"
              on:keydown={() => {}}
            >
              <number
                class="number"
                id="line-number-{removeSpaces(getIndex(index + 1))}"
              >
                {getIndex(index + 1)}
              </number>
            </a>
            <line-content-container class="severity-{scanContent(line)}">
              {line}
            </line-content-container>
          </linecontainer>
        {/each}
      </lines>
    </div>
  {:else}
    <textarea class="input" on:input={onInput} />
  {/if}
</content-container>

<style lang="scss">
  content-container {
    display: block;
    background-color: #eeeeee;
    width: calc(100% - 20px);
    margin: 10px;
    border-radius: 20px;
    animation: fadeIn 0.5s;
    animation-iteration-count: 1;
    animation-fill-mode: forwards;
    opacity: 0;
    height: calc(100% - 140px);
    overflow-x: scroll;
    border: 1px solid #c9c9c9;

    @media (max-width: 600px) {
      height: calc(100% - 130px);
      margin-top: 6px;
    }

    :global(.dark-mode) & {
      border: 1px solid #333;
      background-color: #1a1a1a;
    }

    &.new-true {
      height: calc(100% - 175px);
    }

    transition: all 0.5s ease;
  }

  .input {
    display: inline-block;
    border: none;
    width: calc(100% - 60px);
    height: calc(100% - 64px);
    background-color: transparent;
    color: inherit;
    font-size: 13px;
    font-family: "JetBrains Mono NL", monospace;
    outline: none;
    resize: none;
    margin: 30px;
    padding: 0;

    :global(.dark-mode) & {
      color: white;
    }

    @media (max-width: 600px) {
      font-size: 10px;
    }
  }

  linecontainer {
    display: block;
    margin: 0;
    &:hover {
      .number {
        color: #919191;
      }
    }

    user-select: none;
  }

  lines {
    display: block;
    padding: 30px 30px 30px 18px;
    height: calc(100% - 60px);
  }

  .number {
    display: inline-block;
    text-align: right;
    padding-right: 20px;
    color: #9999;
    font-family: "JetBrains Mono NL", monospace;
    font-size: 13px;
    margin: 0;
    opacity: 1;
    user-select: none;
    -webkit-user-select: none;

    &::selection {
      background-color: orange;
    }

    @media (max-width: 600px) {
      font-size: 10px;
      padding-right: 12px;
    }

    .dark-mode & {
      color: #999;
    }

    transition:
      all 0.2s ease,
      font-weight 0.5s ease;
  }

  linecontainer {
    display: block;
    transition:
      color 0.2s ease,
      margin 0.5s ease;

    font-size: 13px;
    white-space: pre;
    font-family: "JetBrains Mono NL", monospace;
    margin: 0;
    padding-left: 51px;
    text-indent: -26px;

    &.wrap-true {
      white-space: break-spaces;
    }

    @media (max-width: 600px) {
      font-size: 10px;
      padding-left: 30px;
      text-indent: -18px;
    }

    :global(body.dark-mode) & {
      color: white;
    }
  }

  line-content-container {
    margin-right: 30px;
    &.severity-1 {
      background-color: rgb(255, 165, 0, 0.7);
    }

    &.severity-2 {
      background-color: rgb(255, 0, 0, 0.6);
    }

    // enable selection
    user-select: text;
  }

  @keyframes fadeIn {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }

  a {
    &:hover {
      cursor: pointer;
    }
  }
</style>
