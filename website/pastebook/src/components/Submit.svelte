<script>
    import {writableTitle} from "$lib/stores.js";
    import {writableContent} from "$lib/stores.js";
    import {onMount} from "svelte";

    onMount(() => {
        writableTitle.subscribe(() => {
            allFieldsFilled()
        });

        writableContent.subscribe(() => {
            allFieldsFilled()
        });
    });

    function onClick() {
        if (!allFieldsFilled()) {
            window.scroll(0, 0);
            return
        }

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "https://pastebook.dev/upload");
        xhr.setRequestHeader("Content-Type", "plain/text");
        xhr.setRequestHeader("onlyPastebook", "true")
        xhr.setRequestHeader("access-control-allow-origin", "*")
        xhr.setRequestHeader("access-control-allow-methods", "POST")
        xhr.setRequestHeader("title", $writableTitle);
        xhr.send($writableContent);
        xhr.responseType = "text";
        xhr.onload = function () {
            if (xhr.status !== 200) {
                alert(`Error ${xhr.status}: ${xhr.statusText}`);
            }

            window.location.replace(xhr.response)
        }
    }

    function allFieldsFilled() {
        let ready = $writableTitle !== "" && $writableContent !== "";
        if (ready) {
            console.log("ready");
            document.querySelector(".submit").classList.add("ready");
            document.querySelector(".submit").classList.remove("not-ready");
        } else {
            console.log("not ready");
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
      border: none;
      border-radius: 20px;
      font-family: Gabarito, sans-serif;
      font-size: 20px;
      font-weight: 800;
      transition: all 0.5s;
      background-color: #eeeeee;
      color: black;
      text-decoration: none;

      animation: appear 1s;

      :global(.dark-mode) & {
        background-color: #1a1a1a;
        color: white;
      }

      &:hover {
        background-color: #cfcfcf;

        :global(.dark-mode) & {
          background-color: #242323;
        }

        cursor: pointer;
        transform: scale(1.05);
      }

      &:active {
        transform: scale(0.95);
      }
    }

    :global(.dark-mode) & {
      color: white;
    }
  }

  @keyframes appear {
    from {
      opacity: 0;
      transform: translate3d(0, 50px, 0) scale(0.5);
    }
    to {
      opacity: 1;
      transform: scale(1);
    }

  }
</style>