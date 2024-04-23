<script lang="ts">
    export let externalHandler: ((value: boolean) => void);

    let isSelected = false;

    let circle: HTMLElement;
    let background: HTMLElement;

    function toggleSelected() {
        isSelected = !isSelected;

        circle.classList.remove("selected");

        if (isSelected) {
            background.style.backgroundColor = "#333";
            circle.classList.remove("non-active");
            circle.classList.add("selected");
        } else {
            background.style.backgroundColor = "#1a1a1a";
            circle.classList.remove("selected");
            circle.classList.add("non-active");
        }

        externalHandler(isSelected)
    }
</script>

<settings>
    <button bind:this={background} class="container" on:click={toggleSelected}>
        <circ bind:this={circle} class="circle non-active"></circ>
    </button>
</settings>

<style lang="scss">

  .container {
    transition: all 0.5s ease;
    display: block;
    color: inherit;
    padding: 0;
    font: inherit;
    cursor: pointer;
    height: 30px;
    width: 60px;
    margin: 10px;
    border-radius: 30px;
    border: 1px solid #333;
    background-color: #1a1a1a;

    :hover & {
      cursor: pointer;
    }

    @media (max-width: 600px) {
        width: 40px;
      height: 25px;
    }
  }

  .circle {
    transition: all 0.5s ease;
    display: block;
    height: 25px;
    width: 25px;
    margin-top: 0;
    background-color: white;
    border-radius: 50%;

    @media (max-width: 600px) {
      height: 20px;
      width: 20px;
    }

    padding: 0;
  }

  .non-active {
    margin-left: 2px;

    @media (max-width: 600px) {
      margin-left: 2px;
    }
  }

  :global(.selected) {
    position: relative;
    margin-left: 31px;

    @media (max-width: 600px) {
      margin-left: 17px;
    }
  }
</style>
