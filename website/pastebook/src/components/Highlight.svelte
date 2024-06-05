<script>
    import {onMount} from "svelte";
    import {validScan} from '$lib/stores.ts';

    export let highlight = false;

    onMount(() => {
        let currentUrl = window.location.href;
        validScan.subscribe(() => {
            if ($validScan) {
                let style = document.getElementById("highlight")
                style.style.display = "block";
            } else {
                let style = document.getElementById("highlight")
                style.style.display = "none";
            }
        })

        if (localStorage.getItem('auto-highlight') === null) {
            localStorage.setItem('auto-highlight', 'false');
        }

        highlight = localStorage.getItem('auto-highlight') === 'true';
        if (currentUrl.includes("?inspect")) highlight = true;

        if (highlight) {
            if (!currentUrl.includes("?inspect")) window.location.href = currentUrl + `?inspect`
        }
    });

    function toggleHighlight() {
        highlight = !highlight;
        let currentUrl = window.location.href;

        if (highlight) {
            localStorage.setItem('auto-highlight', 'true');
            window.location.href = currentUrl + `?inspect`
        } else {
            localStorage.setItem('auto-highlight', 'false');
            window.location.href = currentUrl.replace('?inspect', '');
        }
    }
</script>

<container>
    {#if highlight}
        <button id="highlight" class="style" on:click="{toggleHighlight}">UNHIGHLIGHT</button>
    {:else}
        <button id="highlight" class="style" on:click="{toggleHighlight}">HIGHLIGHT</button>
    {/if}
</container>

<style lang="scss">
  container {
    transition: background 0.5s ease, color 0.5s ease, transform 0.5s ease, bottom 0.5s ease;

    .style {
      all: unset;
      transition: background 0.5s ease, color 0.5s ease, transform 0.5s ease, bottom 0.5s ease;

      background: none;
      border: none;
      font: inherit;
      cursor: pointer;
      outline: inherit;
      display: none;

      position: fixed;
      margin: 0;
      right: 100px;

      @media (max-width: 600px) {
        left: 30px;
        right: unset;
      }

      bottom: 0;

      background: #ffcc00;
      color: black;

      padding: 4px 8px;
      border-top-left-radius: 5px;
      border-top-right-radius: 5px;
      font-size: 12px;
      font-weight: bold;
      z-index: 9999;
      font-family: Gabarito, sans-serif;
      text-decoration: none;
      opacity: 0;

      animation: flyIn ease 0.7s forwards;
      animation-delay: 0.5s;
      animation-iteration-count: 1;

      &:hover {
        cursor: pointer;
        bottom: -2px
      }
    }
  }

  @keyframes flyIn {
    0% {
      transform: translateY(90%);
      opacity: 0;
    }
    100% {
      transform: translateY(0%);
      opacity: 1;
    }
  }
</style>