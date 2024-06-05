<script lang="ts">
    import Header from "../../components/Header.svelte";
    import ListedPaste from "../../components/panel/ListedPaste.svelte";
    import Mode from "../../components/Mode.svelte";
    import {onMount} from "svelte";
    import type {Paste} from "$lib/paste";
    import SVGPasteBook from "../../components/SVGPasteBook.svelte";
    import {loadProgress} from "$lib/stores";

    export let data;

    let {promise} = data;

    let pastes: Paste[] = []

    promise.then((data) => {
        loadProgress.set(100)

        data.forEach((paste: Paste) => {
            pastes.push({
                id: paste.id,
                title: paste.title,
                created: paste.created,
                reportBook: paste.reportBook,
                wrap: paste.wrap,
                user: {
                    hashedIP: paste.user.hashedIP,
                }
            })
        })
        let current = Date.now();

        pastes = pastes.filter(paste => {
            return paste.created as unknown as number < current;
        })

        pastes.sort((a, b) => {
            return (b.created as unknown as number) - (a.created as unknown as number);
        });
    })

    onMount(async () => {
        await promise
        setTimeout(() => {
            let fades = document.getElementsByClassName("fade")

            for (let i = 0; i < fades.length; i++) {
                let fade = fades[i] as HTMLElement

                setTimeout(() => {
                    fade.style.opacity = "1"
                    fade.style.transform = "translateY(0)"
                }, i * 60)
            }
        }, 100)
    })
</script>

<panel>
    <div class="padding"></div>
    <SVGPasteBook/>
    {#await promise then data}
        {#if pastes.length === 0}
            <Header title="Pastes" created="0"/>
            <div id="none-container">
                <h1 id="no-pastes">No Pastes Found</h1>
                <acontainer>
                    <button onclick="window.location.href = '/new';">NEW</button>
                </acontainer>
            </div>
        {:else}
            <Header title="Pastes" created="{pastes.length.toString()}"/>
            {#each pastes as paste}
                <div class="fade">
                    <ListedPaste paste="{paste}"/>
                </div>
            {/each}
        {/if}
    {/await}
    <Mode/>
</panel>

<style lang="scss">
  #none-container {
    display: flex;
    justify-content: center;
    flex-direction: column;
    align-items: center;
    height: 100%;
    position: fixed;
    top: 0;
    width: 100%;
  }

  #no-pastes {
    transition: color 0.2s;

    font-size: 5rem;
    font-family: Gabarito, sans-serif;
    font-weight: 900;
    color: black;
    opacity: 0;
    margin-bottom: 0;

    :global(.dark-mode) & {
      color: white;
    }

    animation: fadeUp 0.3s forwards;
    animation-delay: 0.2s;

    @media (max-width: 600px) {
      font-size: 3rem;
    }
  }

  acontainer {
    transition: color 0.2s, transform 0.5s ease;

    &:active {
      transform: scale(0.95);
    }
  }

  #flip {
    transition: color 0.2s, transform 0.2s ease;

    font-size: 1.7rem;
    font-family: Gabarito, sans-serif;
    font-weight: 400;
    color: gray;
    opacity: 0;
    margin-top: 0;
    text-decoration: underline;
    animation: fadeUp 0.3s forwards;
    animation-delay: 0.35s;

    @media (max-width: 600px) {
      font-size: 1rem;
    }
  }

  @keyframes fadeUp {
    from {
      transform: translateY(30%);
      opacity: 0;
    }

    to {
      transform: translateY(0);
      opacity: 1;
    }
  }

  .fade {
    transform: translateY(30%);
    transition: opacity 0.3s, transform 0.3s;
    opacity: 0;
  }

  .padding {
    padding-top: 8px + 30px;

    @media (max-width: 600px) {
      padding-top: 7px + 20px;
    }
  }

  button, a {
    display: inline-block;
    appearance: none;
    border: none;
    padding: 10px 20px;
    font-size: 25px;
    background-color: transparent;
    color: gray;
    cursor: pointer;
    transition: all 0.5s;
    font-family: Gabarito, sans-serif;
    text-decoration: none;
    font-weight: 600;
    opacity: 0;

    animation: fadeUp 0.3s forwards;
    animation-delay: 0.35s;

    &:hover {
      color: darkgray;
      cursor: pointer;
    }

    &:active {
      transform: scale(0.97);
    }

    @media (max-width: 600px) {
      font-size: 15px;
      padding: 5px 0;
    }
  }
</style>

<svelte:head>
    <meta property="og:type" content="website"/>
    <meta property="og:title" content="PasteBook Panel"/>
    <meta property="og:site_name" content="PasteBook"/>
    <meta property="og:url" content="https://pastebook.dev/panel"/>
    <meta property="og:description" content="Click to view all currently created pastes."/>
</svelte:head>