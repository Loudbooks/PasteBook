<script lang="ts">
  export let externalHandler = "aaaa"

  console.log(externalHandler)

  let isSelected = false;
  function toggleSelected() {
    let circle = document.querySelector(".circle") as HTMLDivElement;

    isSelected = !isSelected;

    circle.classList.remove("selected");

    if (isSelected) {
      circle.classList.remove("non-active");
      circle.classList.add("selected");
    } else {
      circle.classList.remove("selected");
      circle.classList.add("non-active");
    }
  }
</script>

<settings>
  <container on:click={externalHandler}>
    <button class="circle non-active"></button>
  </container>
</settings>

<style lang="scss">
  container {
    display: block;
    height: 30px;
    background-color: #1a1a1a;
    width: 90px;
    margin: 10px;
    border-radius: 30px;
    border: 1px solid #333;

    :hover & {
      cursor: pointer;
    }
  }

  .circle {
    transition: all 1s ease;
    display: inline-block;
    height: 30px;
    width: 30px;
    background-color: #333;
    border-radius: 50%;
    transform: scale(1.3);

    padding: 0;
  }

  .non-active {
    border: 5px solid red;
  }

  :global(.selected) {
    position: relative;
    margin-left: 60px;
    border: 5px solid green;
  }
</style>
