<script lang="ts">
  import { loadProgress } from "$lib/stores";
  import { onMount } from "svelte";
  import { title } from "$lib/stores";

  let fullContainer: HTMLElement;
  let svgContainer: HTMLElement;

  onMount(() => {
    fullContainer.style.opacity = "1";

    loadProgress.subscribe((value) => {
      if (value) {
        fullContainer.style.opacity = "0";
        svgContainer.style.transform = "scale(1.2)";

        setTimeout(() => {
          fullContainer.style.display = "none";
        }, 300);
      }
    });
  });
</script>

<full-container id="full-container" bind:this={fullContainer}>
  <div id="title-container" bind:this={svgContainer}>
    <h1>{$title}</h1>
  </div>
</full-container>

<style lang="scss">
  * {
    padding: 0;
    margin: 0;
    overflow: hidden;
  }

  full-container {
    transition: opacity 0.35s ease-in-out;

    position: absolute;
    overflow: hidden;
    height: 100vh;
    width: 100vw;
    top: 0;

    opacity: 0;
  }

  #title-container {
    transition:
      background-color 0.5s ease-in-out,
      opacity 0.35s ease,
      transform 0.5s ease;
    height: 100vh;
    width: 100vw;
    position: absolute;
    top: 0;
    z-index: 998;
    background-color: var(--color-background);
  }

  h1 {
    transition:
      stroke-dasharray 0.5s ease-in-out,
      stroke-dashoffset 0.5s ease-in-out,
      fill 0.3s ease-in-out,
      stroke 0.5s ease-in-out,
      stroke-opacity 0.5s ease-in-out;

    color: var(--color-background-secondary);
    font-size: 150px;
    font-family: var(--font-family), serif;
    font-weight: 1000;
    text-align: center;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);

    width: 100vw;

    animation: blink 2s ease infinite alternate;

    @media (max-width: 1000px) {
      transform: translate(-50%, -50%) scale(1);
      animation: blinkMedium 2s ease infinite alternate;
    }

    @media (max-width: 600px) {
      transform: translate(-50%, -50%) scale(0.8);
      animation: blinkSmall 2s ease infinite alternate;
    }

    @keyframes blink {
      0% {
        opacity: 1;
        transform: translate(-50%, -50%) scale(0.98);
      }
      100% {
        opacity: 0.8;
        transform: translate(-50%, -50%) scale(1);
      }
    }

    @keyframes blinkSmall {
      0% {
        opacity: 1;
        transform: translate(-50%, -50%) scale(0.38);
      }
      100% {
        opacity: 0.8;
        transform: translate(-50%, -50%) scale(0.4);
      }
    }

    @keyframes blinkMedium {
      0% {
        opacity: 1;
        transform: translate(-50%, -50%) scale(0.58);
      }
      100% {
        opacity: 0.8;
        transform: translate(-50%, -50%) scale(0.6);
      }
    }
  }
</style>
