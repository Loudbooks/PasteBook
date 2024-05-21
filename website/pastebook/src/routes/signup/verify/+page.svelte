<script lang="ts">
    import {onMount} from "svelte";

    export let data
    export let { email, username, password, failed } = data

    let submitting = false;

    onMount(() => {

        if (failed == true) {
            window.location.href = "/signup"
        }

        document.addEventListener("keydown", (e) => {
            let current = document.activeElement;
            if (current === null) return

            if (current.id !== "input-cell") return;

            for (let i = 1; i <= 6; i++) {
                resetColor("cell-" + i)
            }

            if (e.key === "Backspace") {
                (current as HTMLInputElement).value = ""
                previous()
                return;
            }

            let allNumbers = "0123456789"
            if (!allNumbers.includes(e.key)) {
                e.preventDefault()
                return;
            }
            (current as HTMLInputElement).value = e.key

            e.preventDefault()

            next()
        })

        document.addEventListener("keydown", (e) => {
            if (e.key === "Enter") {
                next()
            }
        });

        function next() {
            let current = document.activeElement;

            if (current.id !== "input-cell") return;

            let currentIndex = current.classList[0].replace("cell-", "");
            let nextIndex = parseInt(currentIndex) + 1;
            let next = document.getElementsByClassName("cell-" + nextIndex)[0] as HTMLElement

            try {
                next.focus();
                (next as HTMLInputElement).value = ""
            } catch (e) {
                submit()
            }
        }

        function previous() {
            let current = document.activeElement;

            if (current.id !== "input-cell") return;

            let currentIndex = current.classList[0].replace("cell-", "");
            let nextIndex = parseInt(currentIndex) - 1;
            let next = document.getElementsByClassName("cell-" + nextIndex)[0] as HTMLElement

            try {
                next.focus();
            } catch (e) {
            }
        }

        function submit() {
            if (submitting) return;

            submitting = true
            let string = ""

            for (let i = 1; i <= 6; i++) {
                string += (document.getElementsByClassName("cell-" + i)[0] as HTMLInputElement).value
            }

            const xhr = new XMLHttpRequest();
            xhr.open('POST', "http://localhost:25658/api/profile/signup", true);

            xhr.send(
                JSON.stringify({
                    email: email,
                    username: username,
                    code: string,
                    password: password
                })
            )

            xhr.onreadystatechange = async function () {
                if (xhr.status === 400) {
                    for (let i = 1; i <= 6; i++) {
                        (document.getElementsByClassName(`cell-${i}`)[0] as HTMLElement).style.setProperty("outline", "3px solid red");
                    }

                    submitting = false
                }

                if (xhr.status === 200) {
                    let response = JSON.parse(await xhr.response)

                    document.cookie = `token=${response.token} ;path=/`
                    document.cookie = `username=${response.username} ;path=/`
                    document.cookie = `id=${response.identification} ;path=/`

                    window.location.href = "/profile"
                }
            }
        }
    })

    function resetColor(clazz: string) {
        if (document.body.classList.contains("dark-mode"))
            (document.getElementsByClassName(clazz)[0] as HTMLElement).style.setProperty("outline", "2px solid #333333");
        else
            (document.getElementsByClassName(clazz)[0] as HTMLElement).style.setProperty("outline", "2px solid #c9c9c9");
    }
</script>

<div id="verification-container">
    <h1 id="title">Verify Email</h1>
    <div id="input-container">
        <p>Type in the six digit code sent to <strong>{email}</strong></p>
        <div id="cells">
            <input id="input-cell" class="cell-1">
            <input id="input-cell" class="cell-2">
            <input id="input-cell" class="cell-3">
            <input id="input-cell" class="cell-4">
            <input id="input-cell" class="cell-5">
            <input id="input-cell" class="cell-6">
        </div>
        <button id="resend">Resend Email</button>
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

  #input-container {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;

    animation: fade 1s ease;
  }

  p {
    color: gray;
    font-family: Gabarito, sans-serif;
    display: block;
  }

  input {
    all: unset;
    transition: background-color 0.5s ease, outline 0.5s ease;
    background-color: #1a1a1a;
    color: gray;
    font-family: Gabarito, sans-serif;
    border-radius: 20px;
    outline: 2px solid #333333;

    height: 100px;
    width: 70px;
    margin: 7px;
    text-align: center;
    font-size: 50px;
    font-weight: bold;

    &:focus {
      background-color: #141414;
    }
  }

  button {
    all: unset;
    transition: transform 0.5s ease;
    color: gray;
    font-family: Gabarito, sans-serif;
    font-weight: 600;
    margin-top: 16px;
    text-decoration: underline;
    cursor: pointer;

    &:active {
      transform: scale(0.95);
    }
  }
</style>