<script lang="ts">
    export let name: string
    export let index: number
    export let type: string = "text"
    export let fieldID: string = "login-input"
    export let onTypeHandler: (data: string) => void = () => {};
    export let submitButtonHandler: () => void = undefined;

    let currentData = ""


    let onType = (event: Event) => {
        currentData = (event.target as HTMLInputElement).value
        onTypeHandler(currentData)
    }

</script>

<div id="container" class="{fieldID}">
    <p>{name}</p>
    {#if submitButtonHandler !== undefined}
        <div>
            <input type="{type}" id="login-input" class="input-index-{index} in-div" on:input={onType}>
            <button on:click={submitButtonHandler}>SUBMIT</button>
        </div>
    {:else}
        <input type="{type}" id="login-input" class="input-index-{index}" on:input={onType}/>
    {/if}
</div>

<style lang="scss">
  #container {
    transition: outline 0.3s, transform 0.5s ease;

    --outline-color: #c9c9c9;

    :global(.dark-mode) & {
      --outline-color: #333333;
    }
  }

  p {
    margin: 0;
    padding-left: 18px;
    padding-bottom: 6px;
    color: grey;
    font-family: Gabarito, sans-serif;
    font-size: 18px;
  }

  input {
    all: unset;

    transition: outline 0.3s, transform 0.3s, background-color 0.5s;

    outline: 3px solid var(--outline-color);
    background-color: #eeeeee;
    border-radius: 20px;
    width: calc(100% - 40px);
    height: 50px;
    padding: 5px 20px;
    font-family: Gabarito, sans-serif;

    :global(.dark-mode) & {
      outline: 3px solid var(--outline-color);
      background-color: #1a1a1a;
      color: white;
    }

    &:focus {
      background-color: #e0e0e0;

      :global(.dark-mode) & {
        background-color: #141414;
      }
    }
  }

  .in-div {
    width: calc(100% - 120px);
  }

  button {
    all: unset;

    transition: outline 0.3s ease, transform 0.3s ease, background-color 0.5s ease;

    outline: 3px solid #c9c9c9;
    background-color: #eeeeee;
    border-radius: 20px;
    width: 68px;
    height: 60px;
    font-family: Gabarito, sans-serif;
    font-weight: 800;
    color: gray;
    font-size: 15px;
    margin-left: 12px;
    text-align: center;
    position: absolute;

    :global(.dark-mode) & {
      outline: 3px solid #333333;
      background-color: #1a1a1a;
    }

    &:active {
      transform: scale(0.95);
    }

    &:hover {
      background-color: #e0e0e0;

      :global(.dark-mode) & {
        background-color: #141414;
      }
    }
  }
</style>