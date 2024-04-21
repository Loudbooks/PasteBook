<script lang="ts">
    import {writableTitle} from "$lib/stores.ts";
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
        if (!allFieldsFilled()) {
            window.scroll(0, 0);
            return
        }

        if (alreadyUploading) {
            return;
        }

        if ($writableTitle === "") {
            $writableTitle = "Untitled";
        }

        let submit = document.getElementsByClassName("submit")[0] as HTMLElement

        submit.style.animation = "blink 3s infinite";

        alreadyUploading = true;
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:25658/upload");
        xhr.setRequestHeader("Content-Type", "plain/text");
        xhr.setRequestHeader("onlyPastebook", "true")
        xhr.setRequestHeader("access-control-allow-origin", "*")
        xhr.setRequestHeader("access-control-allow-methods", "POST")
        xhr.setRequestHeader("title", $writableTitle);
        xhr.send($writableContent);
        xhr.responseType = "text";
        xhr.onload = function () {
            if (xhr.status !== 200) {
                submit.style.animation = "none";
                alert(`Error ${xhr.status}: ${xhr.statusText}`);
            }

            window.location.replace(xhr.response)

            alreadyUploading = false;
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
    padding-top: 3px;

    .submit {
      margin: 0;
      width: 200px;
      height: 50px;
      border-radius: 17px;
      font-family: Gabarito, sans-serif;
      font-size: 20px;
      font-weight: 800;
      transition: all 0.5s;
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

      &:hover {
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