<script lang="ts">
    import {onMount} from "svelte";
    import google from "$lib/assets/google.png";
    import discord from "$lib/assets/discord.png";
    import NamedInput from "../../components/account/NamedInput.svelte";
    import Mode from "../../components/Mode.svelte";

    let id = ""
    let password = ""

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

    function onIdChange(value) {
        id = value;
        resetColor("login-username")
    }

    function onPasswordChange(value) {
        password = value;
        resetColor("login-password")
    }

    function resetColor(clazz: string) {
        if (document.body.classList.contains("dark-mode"))
            (document.getElementsByClassName(clazz)[0] as HTMLElement).style.setProperty("--outline-color", "#333333");
        else
            (document.getElementsByClassName(clazz)[0] as HTMLElement).style.setProperty("--outline-color", "#c9c9c9");
    }

    function logIn() {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', "http://localhost:25658/api/profile/login", true);

        xhr.onreadystatechange = async function () {
            if (xhr.status === 400) {
                (document.getElementsByClassName("login-username")[0] as HTMLElement).style.setProperty("--outline-color", "red");
                (document.getElementsByClassName("login-password")[0] as HTMLElement).style.setProperty("--outline-color", "red");

                return;
            }

            if (xhr.status === 200) {
                let response = JSON.parse(await xhr.response);
                document.cookie = `token=${response.token} ;path=/`;
                document.cookie = `id=${response.identification} ;path=/`;
                document.cookie = `username=${response.username} ;path=/`;

                window.location.href = '/panel';
            }
        }

        xhr.send(
            JSON.stringify({
                identification: id,
                password: password
            })
        )
    }
</script>

<div id="background">\
    <Mode></Mode>
    <h1 id="title">Log In</h1>
    <div id="options">
        <div id="login-basic">
            <NamedInput name="Username or Email" index={0} type="username" fieldID="login-username"
                        onTypeHandler={function (value) {onIdChange(value)}}/>
            <div id="spacer"/>
            <NamedInput name="Password" index={1} type="password" fieldID="login-password"
                        onTypeHandler={function (value) {onPasswordChange(value)}}
                        submitButtonHandler={function () {logIn()}}/>
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
        <div id="border-horizontal">
            <svg id="Border" xmlns="http://www.w3.org/2000/svg" width="430" height="24" viewBox="0 0 430 24">
                <line id="Line_1" data-name="Line 1" x2="193" transform="translate(0 12)" fill="none" stroke="#707070"
                      stroke-width="1"/>
                <text id="or" transform="translate(215 -5)" fill="#707070" font-size="25" font-family="Gabarito"
                      font-weight="600">
                    <tspan x="-11.492" y="24">or</tspan>
                </text>
                <line id="Line_7" data-name="Line 7" x2="193" transform="translate(237 12)" fill="none" stroke="#707070"
                      stroke-width="1"/>
            </svg>
        </div>
        <div id="login-withs">
            <div id="button-container">
                <a id="discord"
                   href="https://discord.com/oauth2/authorize?client_id=1240490933376647220&response_type=code&redirect_uri=http%3A%2F%2Flocalhost%3A5173%2Flogin%2Fdiscord&scope=identify">
                    <img src={discord} alt="Log in with Discord" width="30px" height="30px"/>
                    Log in with Discord
                </a>
            </div>
            <div id="spacer-exclude"/>
            <div id="button-container">
                <a id="google">
                    <img src={google} alt="Coming Soon" width="30px" height="30px"/>
                    Coming Soon
                </a>
            </div>
        </div>
    </div>
    <div id="sign-up">
        <button on:click={function () {window.location.href = "/signup"}}>Sign Up</button>
    </div>
</div>

<style lang="scss">
  #spacer {
    height: 76px;

    @media (max-width: 800px) {
      height: 27.5px;
    }
  }

  #spacer-exclude {
    height: calc(76px + 27.5px);

    @media (max-width: 800px) {
      height: calc(27.5px + 27.5px);
    }
  }

  #options {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    width: 100%;
    top: 0;
    gap: 300px;

    @media (max-width: 1100px) {
      gap: 100px;
    }

    @media (max-width: 800px) {
      flex-direction: column;
      gap: 0;
    }
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
    animation: fadeLeft 0.5s ease;

    @media (max-width: 1100px) {
      width: 400px;
    }

    @media (max-width: 800px) {
      padding: 150px 20px 20px;
      width: 80%;

      animation: fadeDown 0.5s ease;
    }
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

    animation: fadeRight 0.5s ease;

    @media (max-width: 1100px) {
      width: 400px;
    }

    @media (max-width: 800px) {
      padding: 20px 20px 20px;
      width: 80%;

      animation: fadeUp 0.5s ease;
    }
  }

  #border {
    display: block;
    width: 24px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -40%);
    overflow: hidden;

    @media (max-width: 800px) {
      display: none;
    }

    animation: fade 0.5s ease;
  }

  #border-horizontal {
    transform: scale(0.6);

    @media (min-width: 800px) {
      display: none;
    }

    animation: fade 0.5s ease;
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

    @media (max-width: 800px) {
      font-size: 50px;
    }

    animation: fadeUp 0.5s ease;
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
    font-weight: 450;
    font-size: 19px;
    border-radius: 23px;
    transition: background-color 0.5s ease, transform 0.5s ease, opacity 0.5s ease, color 0.5s ease, border 0.5s ease;
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
      left: 20px;
      top: 50%;
      transform: translateY(-50%);
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
    opacity: 0.5;
    border: solid 3px #c9c9c9;
    color: black;

    cursor: not-allowed;

    :global(.dark-mode) & {
      background-color: #1a1a1a;
      border: solid 3px #333333;
      color: white;
    }

    :active & {
      transform: scale(1);
    }
  }

  button {
    all: unset;

    transition: transform 0.5s ease;
    color: white;
    position: absolute;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    color: gray;
    font-family: Gabarito, sans-serif;
    text-decoration: underline;
    font-size: 20px;
    cursor: pointer;
    font-weight: 800;

    &:active {
      transform: translateX(-50%) scale(0.95);
    }
  }


  @keyframes fadeLeft {
    from {
      transform: translate(10%, 0);
      opacity: 0;
    }
    to {
      transform: translate(0, 0);
      opacity: 1;
    }
  }

  @keyframes fadeRight {
    from {
      transform: translate(-10%, 0);
      opacity: 0;
    }
    to {
      transform: translate(0, 0);
      opacity: 1;
    }
  }

  @keyframes fadeUp {
    from {
      transform: translate(0, 10%);
      opacity: 0;
    }
    to {
      transform: translate(0, 0);
      opacity: 1;
    }
  }

  @keyframes fadeDown {
    from {
      transform: translate(0, -10%);
      opacity: 0;
    }
    to {
      transform: translate(0, 0);
      opacity: 1;
    }

  }

  @keyframes fade {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
</style>