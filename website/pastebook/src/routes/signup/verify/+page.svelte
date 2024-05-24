<script lang="ts">
    import {onMount} from "svelte";

    export let data
    export let { email, username, password, failed } = data

    let submitting = false;
    let allNumbers = "0123456789"

    onMount(() => {
        if (failed == true) {
            window.location.href = "/signup"
        }

        let firstCell = document.getElementById("cell-1") as HTMLElement
        let lastCell = document.getElementById("cell-6") as HTMLElement

        firstCell.addEventListener("paste" , (event) => {
            if (event.clipboardData === null) return

            event.preventDefault()

            let paste = event.clipboardData.getData('text');
            let pasteArray = paste.split("")

            pasteArray = pasteArray.filter((value) => allNumbers.includes(value))
            if (pasteArray.length !== 6) return;

            let cells = document.getElementsByClassName("input-cell") as HTMLCollectionOf<HTMLInputElement>

            for (let i = 0; i < cells.length; i++) {
                cells[i].value = pasteArray[i]
            }

            lastCell.focus()
            submit()
        })

        let allCells = document.getElementsByClassName("input-cell") as HTMLCollectionOf<HTMLInputElement>
        for (let i = 2; i <= allCells.length; i++) {
            allCells[i - 1].addEventListener("paste", (event) => {
                event.preventDefault()
            })
        }

        document.addEventListener("keydown", (e) => {
            let current = document.activeElement;
            if (current === null) return

            if (!current.classList.contains("input-cell")) return;

            for (let i = 1; i <= 6; i++) {
                resetColor("cell-" + i)
            }

            if (e.key === "Backspace") {
                (current as HTMLInputElement).value = ""
                previous()
                return;
            }

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

            if (!(current.classList.contains("input-cell"))) return;

            let currentIndex = current.id.replace("cell-", "");
            let nextIndex = parseInt(currentIndex) + 1;
            let next = document.getElementById("cell-" + nextIndex) as HTMLElement

            try {
                next.focus();
                (next as HTMLInputElement).value = ""
            } catch (e) {
                // submit()
            }
        }

        function previous() {
            let current = document.activeElement;

            if (!(current.classList.contains("input-cell"))) return;

            let currentIndex = current.id.replace("cell-", "");
            let nextIndex = parseInt(currentIndex) - 1;
            let next = document.getElementById("cell-" + nextIndex) as HTMLElement

            try {
                next.focus();
            } catch (e) {
            }
        }

        function submit() {
            if (submitting) return;

            submitting = true
            let string = ""

            document.cookie = `cachedSignup=`

            for (let i = 1; i <= 6; i++) {
                string += (document.getElementById("cell-" + i) as HTMLInputElement).value
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
                        (document.getElementById(`cell-${i}`) as HTMLElement).style.setProperty("outline", "3px solid #87011a");
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
            (document.getElementById(clazz) as HTMLElement).style.setProperty("outline", "2px solid #333333");
        else
            (document.getElementById(clazz) as HTMLElement).style.setProperty("outline", "2px solid #c9c9c9");
    }

    function resendEmail() {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', "http://localhost:25658/api/profile/login/requestEmail", true);

        xhr.send(
            JSON.stringify({
                email: email,
                username: username,
            })
        )
    }
</script>

<div id="verification-container">
    <h1 id="title">Verify Email</h1>
    <div id="input-container">
        <p>Type in the six digit code sent to <strong>{email}</strong></p>
        <div id="cells">
            <input id="cell-1" class="input-cell">
            <input id="cell-2" class="input-cell">
            <input id="cell-3" class="input-cell">
            <input id="cell-4" class="input-cell">
            <input id="cell-5" class="input-cell">
            <input id="cell-6" class="input-cell">
        </div>
        <button id="resend" on:click={resendEmail}>Resend Email</button>
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