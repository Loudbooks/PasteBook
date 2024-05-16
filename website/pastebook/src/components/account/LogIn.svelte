<script lang="ts">
    import NamedInput from "./NamedInput.svelte";
    import {onMount} from "svelte";
    import google from "$lib/assets/google.png";
    import discord from "$lib/assets/discord.png";

    onMount(() => {
        document.addEventListener("keydown", (e) => {
            if (e.key === "Enter") {
                let current = document.activeElement;

                if (current.id !== "login-input") return;

                let currentIndex = current.classList[0].replace("input-index-", "");
                let nextIndex = parseInt(currentIndex) + 1;
                let next = document.getElementsByClassName("input-index-" + nextIndex)[0] as HTMLElement

                try {
                    next.focus();
                } catch (e) {
                }
            }
        });
    })

</script>

<div id="background">
    <h1 id="title">Log In</h1>
    <div id="options">
        <div id="login-basic">
            <NamedInput name="Username or Email" index={0} type="username"/>
            <div id="spacer"/>
            <NamedInput name="Password" index={1} type="password"/>
            <div id="spacer"/>
        </div>
        <div id="border">
            <svg id="Border" xmlns="http://www.w3.org/2000/svg" width="24" height="430" viewBox="0 0 24 430">
                <line id="Line_1" data-name="Line 1" y2="193" transform="translate(12)" fill="none" stroke="#707070"
                      stroke-width="1"/>
                <text id="or" transform="translate(11 198)" fill="#707070" font-size="25" font-family="Gabarito"
                      font-weight="600">
                    <tspan x="-11.492" y="24">or</tspan>
                </text>
                <line id="Line_7" data-name="Line 7" y2="193" transform="translate(12 237)" fill="none" stroke="#707070"
                      stroke-width="1"/>
            </svg>
        </div>
        <div id="login-withs">
            <div id="button-container">
                <a id="discord" href="login/discord">
                    <img src={discord} alt="Log in with Discord" width="40px" height="40px"/>
                    Log in with Discord
                </a>
            </div>
            <div id="spacer-exclude"/>
            <div id="button-container">
                <a id="google" href="login/google">
                    <img src={google} alt="Log in with Google" width="40px" height="40px"/>
                    Log in with Google
                </a>
            </div>
        </div>
    </div>
</div>

<style lang="scss">
  #background {
    height: 100vh;
    width: 100vw;
    margin: 0;
    overflow: hidden;

    background-color: white;

    :global(.dark-mode) & {
      background-color: black;
    }
  }

  #spacer {
    height: 76px;
  }

  #spacer-exclude {
    height: calc(76px + 27.5px);
  }

  #options {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    top: 0;
    gap: 250px;
  }

  #login-basic {
    border-radius: 10px;
    width: 460px;
    padding: 155px 20px 20px;
    min-width: 300px;
    height: 600px;

    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  #login-withs {
    padding: 105px 20px 20px;
    border-radius: 10px;
    width: 460px;
    min-width: 300px;
    height: 600px;

    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  #border {
    display: block;
    width: 24px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -40%);
    overflow: hidden;
  }

  #title {
    color: black;
    font-size: 70px;
    text-align: center;
    padding-top: 80px;
    display: block;
    position: absolute;
    width: 100%;
    top: 0;
    margin: 0;
    font-family: Gabarito, sans-serif;
    font-weight: 900;

    :global(.dark-mode) & {
      color: white;
    }
  }

  /*
    button {
      all: unset;
      transition: background-color 0.5s ease, transform 0.5s ease;
      background-color: #eeeeee;
      color: black;
      text-decoration: none;
      border: 3px solid #c9c9c9;
      padding: 4px 25px;
      border-radius: 23px;
      cursor: pointer;
      display: block;
      margin: 0 auto;
      font-family: Gabarito, sans-serif;
      font-weight: 900;
      font-size: 19px;
      height: 40px;

      &:hover {
        background-color: #e0e0e0;
      }

      &:active {
        transform: scale(0.97);
      }
    }*/

  a {
    display: block;
    text-align: center;
    text-decoration: none;
    color: white;
    font-family: Gabarito, sans-serif;
    font-weight: 900;
    font-size: 19px;
    border-radius: 23px;
    transition: background-color 0.5s ease, transform 0.5s ease;
    cursor: pointer;
    height: 60px;
    line-height: 60px;
    position: relative;

    &:hover {
      background-color: #e0e0e0;
    }

    &:active {
      transform: scale(0.97);
    }

    img {
      vertical-align: middle;
      margin-right: 10px;
      position: absolute;
      left: 15px;
      top: 10px;
    }
  }

  #discord {
    background-color: rgba(54, 71, 239, 1);
    border: solid 3px rgba(87, 101, 236, 1);

    &:hover {
      background-color: rgba(54, 71, 239, 0.8);
    }
  }

  #google {
    background-color: #eeeeee;
    border: solid 3px #c9c9c9;
    color: black;

    &:hover {
      background-color: #e0e0e0;

      :global(.dark-mode) & {
        background-color: #141414;
        border: solid 3px #333333;
        color: white;

      }
    }

    :global(.dark-mode) & {
      background-color: #1a1a1a;
      border: solid 3px #333333;
      color: white;
    }
  }
</style>