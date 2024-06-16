<script lang="ts">
    export let externalHandler: ((value: boolean) => void);

    export let isSelected = false;

    let circle: HTMLElement;
    let background: HTMLElement;

    function toggleSelected() {
        isSelected = !isSelected;

        circle.classList.remove("selected");

        if (isSelected) {
            background.classList.remove("bg-non-active");
            background.classList.add("bg-selected");

            circle.classList.remove("non-active");
            circle.classList.add("selected");
        } else {
            background.classList.remove("bg-selected");
            background.classList.add("bg-non-active");

            circle.classList.remove("selected");
            circle.classList.add("non-active");
        }

        externalHandler(isSelected)
    }
</script>

<settings>
    <button bind:this={background} class="container bg-active-{isSelected}" on:click={toggleSelected}>
        <circ bind:this={circle} class="circle active-{isSelected}"></circ>
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
    height: 40px;
    width: 70px;
    margin: 10px;
    border-radius: 30px;
    border: 1px solid #cfcfcf;

    :hover & {
      cursor: pointer;
    }

    :global(.dark-mode) & {
      border: 1px solid #333;
    }

    @media (max-width: 600px) {
      width: 40px;
      height: 25px;
    }
  }

  :global(.bg-active-true) {
    background-color: #cfcfcf;

    :global(.dark-mode) & {
      background-color: #333;
    }
  }

  :global(.bg-active-false) {
    background-color: #eeeeee;

    :global(.dark-mode) & {
      background-color: #1a1a1a;
    }
  }

  .circle {
    transition: all 0.5s ease;
    display: block;
    height: 35px;
    width: 35px;
    margin-top: 0;
    border-radius: 50%;
    background-color: dimgray;

    @media (max-width: 600px) {
      height: 20px;
      width: 20px;
    }

    :global(.dark-mode) & {
      background-color: white;
    }

    padding: 0;
  }

  .active-false {
    margin-left: 2px;

    @media (max-width: 600px) {
      margin-left: 2px;
    }
  }

  :global(.active-true) {
    position: relative;
    margin-left: 31px;

    @media (max-width: 600px) {
      margin-left: 17px;
    }
  }
</style>
