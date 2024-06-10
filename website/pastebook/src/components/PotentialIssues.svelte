<script lang="ts">
    import {severes, warnings} from "$lib/stores.ts";
    import type {Issue} from "$lib/issue";
    import Header from "./Header.svelte";
    import Pulltab from "./pulltab/Pulltab.svelte";

    let severIssues: Array<Issue> = $severes;
    let warningIssues: Array<Issue> = $warnings;

    let filteredSevere: Issue[] = [];
    let filteredWarning: Issue[] = [];

    for (let severIssue of severIssues) {
        if (!filteredSevere.some((issue) => issue.visual === severIssue.visual)) {
            filteredSevere.push(severIssue);
        }
    }

    for (let warningIssue of warningIssues) {
        if (
            !filteredWarning.some((issue) => issue.visual === warningIssue.visual)
        ) {
            filteredWarning.push(warningIssue);
        }
    }
</script>

<Pulltab title="Potential Issues">
    <svelte:fragment slot="content">
        {#if !(filteredSevere.length === 0)}
            <header>
                <Header
                        class="severeheader"
                        title="Severe"
                        created={filteredSevere.length}
                ></Header>
            </header>
            <severe class="severe">
                {#each filteredSevere as issue}
                    <span>{issue.visual + ":"}</span>
                    <li>{issue.description}</li>
                    <br/>
                {/each}
            </severe>
        {/if}
        {#if !(filteredWarning.length === 0)}
            <header>
                <Header
                        class="warnheader"
                        title="Warnings"
                        created={filteredWarning.length}
                ></Header>
            </header>
            <warn class="warn">
                {#each filteredWarning as issue}
                    <span>{issue.visual + ":"}</span>
                    <li>{issue.description}</li>
                    <br/>
                {/each}
            </warn>
        {/if}
    </svelte:fragment>
</Pulltab>

<style lang="scss">
  span {
    font-weight: bold;
    padding-left: 10px;
    padding-right: 10px;
    font-family: JetBrains Mono NL,
    monospace;

    @media (max-width: 800px) {
      font-size: 1em;
    }

    display: inline-block;
  }

  .severe,
  .warn {
    transition: all 0.5s ease;

    display: block;
    list-style-type: none;
    border-radius: 15px;
    margin: 10px 10px 10px;
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

    white-space: nowrap;
    overflow: scroll;
    padding: 10px 20px 10px 10px;
  }

  .severe {
    background-color: rgb(255, 0, 0, 0.4);
    border: 2px solid rgb(255, 0, 0, 0.4);
  }

  .warn {
    background-color: rgb(#ff8c00, 0.4);
    border: 2px solid rgb(#ff8c00, 0.4);
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
