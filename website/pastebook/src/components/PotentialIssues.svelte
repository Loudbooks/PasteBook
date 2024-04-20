<script lang="ts">
    import {severes, warnings} from "$lib/stores.ts";
    import type {Issue} from "$lib/issue";
    import Header from "./Header.svelte";
    import {onMount} from "svelte";

    let severIssues: Array<Issue> = $severes;
    let warningIssues: Array<Issue> = $warnings;

    let filteredSevere: Issue[] = []
    let filteredWarning: Issue[] = []

    for (let severIssue of severIssues) {
        if (!filteredSevere.some(issue => issue.visual === severIssue.visual)) {
            filteredSevere.push(severIssue);
        }
    }

    for (let warningIssue of warningIssues) {
        if (!filteredWarning.some(issue => issue.visual === warningIssue.visual)) {
            filteredWarning.push(warningIssue);
        }
    }


    let open = false;
    let cooldown = false;

    onMount(() => {
        let issues = document.querySelector("issues") as HTMLDivElement;

        issues.style.top = `calc(100% + ${issues.clientHeight / 2}px)`;
    });


    function toggleClick() {
        let issues = document.querySelector("issues") as HTMLDivElement;
        let blur = document.querySelector(".blur") as HTMLDivElement;
        let tab = document.querySelector(".tab") as HTMLButtonElement;

        if (open) {
            issues.style.top = `calc(100% + ${issues.clientHeight / 2}px)`;

            blur.style.backgroundColor = "rgba(0, 0, 0, 0)"

            cooldown = true;
            setTimeout(() => {
                blur.style.display = "none";
                tab.classList.add("non-active");
                cooldown = false;
            }, 500)

            open = false;
        } else {
            if (cooldown) return;
            issues.style.top = `50%`;

            blur.style.display = "block";

            setTimeout(() => {
                blur.style.backgroundColor = "rgba(0, 0, 0, 0.5)";
            }, 1)

            tab.classList.remove("non-active");

            open = true;
        }
    }
</script>
<issuescontainer>
    <issues>
        <button class="tab non-active" on:click={toggleClick}>Potential Issues</button>

        {#if !(filteredSevere.length === 0)}
            <header>
                <Header class="severeheader" title="Severe" created="{filteredSevere.length}"></Header>
            </header>
            <sever>
                {#each filteredSevere as issue}
                    <span>{issue.visual + ":"}</span>
                    <li>{issue.description}</li>
                    <br>
                {/each}
            </sever>
        {/if}
        {#if !(filteredWarning.length === 0)}
            <header>
                <Header class="warnheader" title="Warnings" created="{filteredWarning.length}"></Header>
            </header>
            <warn>
                {#each filteredWarning as issue}
                    <span>{issue.visual + ":"}</span>
                    <li>{issue.description}</li>
                    <br>
                {/each}
            </warn>
        {/if}
    </issues>
    <button class="blur" on:click={toggleClick}></button>
</issuescontainer>

<style lang="scss">
  .blur {
    transition: background-color 0.5s ease;
    position: fixed;
    top: 0;
    background-color: rgba(0, 0, 0, 0);
    width: 100vw;
    height: 100vh;
    z-index: 0;
    outline: none;
    border: none;
    display: none;
  }

  .tab {
    transition: all 0.5s ease-in-out;

    font: inherit;
    cursor: pointer;
    outline: inherit;

    position: fixed;
    margin: 0;
    top: -35px;
    height: 35.5px;
    width: 140px;
    bottom: 0;
    padding: 5px 5px 10px;
    left: calc(50% - 70px);
    border-top-right-radius: 10px;
    border-top-left-radius: 10px;
    color: black;
    z-index: 1;

    border-left: 1px solid #c9c9c9;
    border-right: 1px solid #c9c9c9;
    border-top: 1px solid #c9c9c9;
    border-bottom: none;

    background-color: rgb(230, 230, 230, 1);

    :global(body.dark-mode) & {
      border-top: 1px solid #333;
      border-left: 1px solid #333;
      border-right: 1px solid #333;
      border-bottom: none;

      background-color: rgb(20, 20, 20, 1);
      color: white;
    }

    :hover & {
      &.non-active {
        background-color: rgb(200, 200, 200, 1);

        top: -32px;

        :global(body.dark-mode) & {
          background-color: rgb(40, 40, 40, 1);
        }
      }
    }
  }

  issues {
    z-index: 1;
    width: 50vw;
    position: fixed;
    top: 150%;
    left: 50%;
    background-color: rgb(230, 230, 230, 1);
    transition: all 0.5s ease;

    @media (max-width: 500px) {
      width: 91vw;
    }

    border: 1px solid #c9c9c9;

    :global(body.dark-mode) & {
      border: 1px solid #333;
      background-color: rgb(20, 20, 20, 1);
    }

    border-radius: 20px;
    transform: translate(-50%, -50%);
    padding: 10px;
    font-family: Gabarito, sans-serif;

    header {
      margin-bottom: 5px;
    }

    span {
      font-weight: bold;
      padding-left: 10px;
      padding-right: 10px;
      font-family: JetBrains Mono NL, monospace;

      @media (max-width: 800px) {
        font-size: 1em;
      }
    }

    sever, warn {
      transition: all 0.5s ease;

      display: block;
      list-style-type: none;
      padding: 10px;
      border-radius: 15px;
      margin: 1px 10px 10px;
      color: black;
      opacity: 0;

      @media (max-width: 800px) {
        font-size: 0.8em;
      }

      animation: fadeIn 0.5s ease-in-out forwards;
      animation-delay: 0.1s;

      :global(body.dark-mode) & {
        color: white;
      }

      li {
        left: 50%;
        display: inline-block;
      }
    }

    sever {
      background-color: rgb(255, 0, 0, 0.4);
      border: 2px solid rgb(255, 0, 0, 0.4);
    }

    warn {
      background-color: rgb(#ff8c00, 0.4);
      border: 2px solid rgb(#ff8c00, 0.4);
    }
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
</style>