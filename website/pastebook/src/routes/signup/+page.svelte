<script lang="ts">
    import NamedInput from "../../components/account/NamedInput.svelte";
    import {onMount} from "svelte";

    let username = "";
    let email = "";
    let password = "";
    let confirmPassword = "";

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

    function onConfirmPasswordChange() {
        if (confirmPassword != password) {
            (document.getElementsByClassName("confirm-password")[0] as HTMLElement).style.setProperty("--outline-color", "red");
        } else {
            if (confirmPassword != "" && password != "")
                (document.getElementsByClassName("confirm-password")[0] as HTMLElement).style.setProperty("--outline-color", "green");
            else {
                resetColor("confirm-password");
            }
        }
    }

    function onEmailType() {
        if (email == "") {
            resetColor("email");
            return
        }

        if (isValidEmail(email)) {
            (document.getElementsByClassName("email")[0] as HTMLElement).style.setProperty("--outline-color", "green");
        } else {
            (document.getElementsByClassName("email")[0] as HTMLElement).style.setProperty("--outline-color", "red");
        }
    }

    function onUsernameType() {
        if (username == "") {
            resetColor("username");
            return
        }

        if (isValidUsername(username)) {
            (document.getElementsByClassName("username")[0] as HTMLElement).style.setProperty("--outline-color", "green");
        } else {
            (document.getElementsByClassName("username")[0] as HTMLElement).style.setProperty("--outline-color", "red");
        }
    }

    function resetColor(clazz: string) {
        if (document.body.classList.contains("dark-mode"))
            (document.getElementsByClassName(clazz)[0] as HTMLElement).style.setProperty("--outline-color", "#333333");
        else
            (document.getElementsByClassName(clazz)[0] as HTMLElement).style.setProperty("--outline-color", "#c9c9c9");
    }

    function isValidEmail(email: string) {
        return /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(email);
    }

    function isValidUsername(username: string) {
        return /^[a-zA-Z0-9_]{3,16}$/.test(username);
    }
</script>

<h1 id="title">Sign Up</h1>
<div id="forms-container">
    <div id="forms">
        <div id="username">
            <NamedInput name="Username" type="text" fieldID="username" index={0} onTypeHandler={function (value) {username = value; onUsernameType()}} />
        </div>
        <div id="email">
            <NamedInput name="Email" type="email" fieldID="email" index={1} onTypeHandler={function (value) {email = value; onEmailType()}} />
        </div>
        <div id="password">
            <NamedInput name="Password" type="password" fieldID="password" index={2} onTypeHandler={function (value) {password = value; onConfirmPasswordChange()}} />
        </div>
        <div id="confirm-password">
            <NamedInput name="Confirm Password" type="password" fieldID="confirm-password" index={3} submitButtonHandler={function () {}} onTypeHandler={function (value) {confirmPassword = value; onConfirmPasswordChange()}} />
        </div>
    </div>
</div>

<style lang="scss">
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

  #forms-container {
    border-radius: 10px;
    width: 460px;
    padding: 155px 20px 20px;
    min-width: 300px;
    height: 600px;
    margin: auto;

    display: flex;
    flex-direction: column;
    justify-content: center;
    animation: fadeLeft 0.5s ease;

    @media (max-width: 800px) {
      padding: 205px 20px 20px;
      width: 80%;

      animation: fadeDown 0.5s ease;
    }
  }

  #forms {
    display: flex;
    flex-direction: column;
    gap: 40px;
  }
</style>