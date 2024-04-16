<script>
    import { writableTitle } from "$lib/stores.js";
    import { writableContent } from "$lib/stores.js";

    function onClick() {
        const xhr = new XMLHttpRequest();
        xhr.open("POST", "https://pastebook.dev/upload");
        xhr.setRequestHeader("Content-Type", "plain/text");
        xhr.setRequestHeader("access-control-allow-origin", "*")
        xhr.setRequestHeader("access-control-allow-methods", "POST")
        xhr.setRequestHeader("title", $writableTitle);
        xhr.send($writableContent);
        xhr.responseType = "text";
        xhr.onload = function() {
            if (xhr.status !== 200) {
                alert(`Error ${xhr.status}: ${xhr.statusText}`);
            }

            window.location.replace(xhr.response)
        }
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
      transition: all 0.3s;
      background-color: #eeeeee;

      :global(.dark-mode) & {
        background-color: #333333;
      }

      &:hover {
        background-color: #cfcfcf;

        :global(.dark-mode) & {
          background-color: #242323;
        }

        cursor: pointer;
        transform: scale(1.05);
        color: #000;
      }

      &:active {
        transform: scale(0.95);
      }

      :global(.dark-mode) & {
        color: lightgray;
      }
    }
  }
</style>