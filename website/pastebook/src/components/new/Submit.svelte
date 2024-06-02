<script lang="ts">
    import {unlisted, wrap, writableTitle} from "$lib/stores.ts";
    import {writableContent} from "$lib/stores.ts";
    import {onMount} from "svelte";

    onMount(() => {
        writableTitle.subscribe(() => {
            allFieldsFilled()
        });

        writableContent.subscribe(() => {
            allFieldsFilled()
        });

        let submit = document.getElementsByClassName("submit")[0] as HTMLElement

        setTimeout(() => {
          submit.style.opacity = "1"
          submit.style.transform = "translateY(0)"
        }, 400)
    });

    let alreadyUploading = false;

    function onClick() {
        if ($writableTitle === "") {
            let title = document.getElementById("title") as HTMLInputElement
            title.focus()
            shakeElement(title, 500);

            return
        }

        if (!allFieldsFilled()) {
            return
        }

        if (alreadyUploading) {
            return;
        }

        if ($writableTitle === "") {
            $writableTitle = "Untitled";
        }

        let submit = document.getElementsByClassName("submit")[0] as HTMLElement

        alreadyUploading = true;
        submit.classList.add("loading");
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "https://pastebook.dev/api/upload");
        xhr.setRequestHeader("Content-Type", "plain/text");
        xhr.setRequestHeader("onlyPastebook", "true")
        xhr.setRequestHeader("access-control-allow-methods", "POST")
        xhr.setRequestHeader("title", $writableTitle);
        xhr.setRequestHeader("wrap", String($wrap))
        xhr.setRequestHeader("unlisted", String($unlisted))

        xhr.send($writableContent);
        xhr.responseType = "text";
        xhr.onload = function () {
            if (xhr.status !== 200) {
                submit.classList.remove("loading");
                alert(`Error ${xhr.status}: ${xhr.statusText}`);
                alreadyUploading = false;
            }

            window.location.replace(xhr.response)
        }
    }

    function allFieldsFilled() {
        let ready = $writableContent !== "";

        if (ready) {
            document.querySelector(".submit").classList.add("ready");
            document.querySelector(".submit").classList.remove("not-ready");
        } else {
            document.querySelector(".submit").classList.remove("ready");
            document.querySelector(".submit").classList.add("not-ready");
        }

        return ready;
    }

    const shakeElement = (element: HTMLElement, duration: number) => {
        const startTime = Date.now();
        const shakeInterval = 1000 / 60;

        const shake = () => {
            const elapsedTime = Date.now() - startTime;
            const progress = elapsedTime / duration;
            const fullShakes = Math.floor(progress / 0.25);

            if (fullShakes % 2 === 0) {
                element.style.transform = 'translateX(-15px)';
            } else {
                element.style.transform = 'translateX(15px)';
            }

            if (progress < 1) {
                setTimeout(shake, shakeInterval);
            } else {
                element.style.transform = 'translateX(0)';
            }
        };

        shake();
    };
</script>

<container>
    <button class="submit" on:click="{onClick}">SUBMIT</button>
</container>

<style lang="scss">
  container {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 50px;
    padding-bottom: 10px;
    transition: transform 0.5s ease;

    &:active {
      transform: scale(0.96);
    }

    .submit {
      margin: 0;
      width: 200px;
      height: 50px;
      border-radius: 17px;
      font-family: Gabarito, sans-serif;
      font-size: 20px;
      font-weight: 800;
      transition: opacity 0.5s, transform 0.5s, background-color 0.5s;
      background-color: #eeeeee;
      color: black;
      text-decoration: none;
      border: 1px solid #c9c9c9;

      opacity: 0;
      transform: translateY(30%);

      :global(.dark-mode) & {
        background-color: #1a1a1a;
        border: 1px solid #333;
        color: white;
      }

      &:global(.loading) {
        animation: blink 3s infinite;
        color: #999999;
      }

      &:hover:global(.loading) {
        cursor: not-allowed;
      }

      &:hover:not(.loading) {
        background-color: #cfcfcf;

        :global(.dark-mode) & {
          background-color: #333;
        }

        cursor: pointer;
      }

      &:active {
        transform: scale(0.96);
      }
    }

    :global(.dark-mode) & {
      color: white;
    }
  }

  @keyframes blink {
      0% {
        opacity: 0.5;
        transform: scale(1);
      }
      50% {
        opacity: 1;
        transform: scale(1.02);
      }
      100% {
        opacity: 0.5;
        transform: scale(1);
      }
    }
</style>