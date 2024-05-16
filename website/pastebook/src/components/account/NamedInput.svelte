<script lang="ts">
    export let name: string
    export let index: number
    export let type: string = "text"
</script>

<div id="container">
    <p>{name}</p>
    <input type="{type}" id="login-input" class="input-index-{index}" />
</div>

<style lang="scss">
  #container {
    transition: outline 0.3s, transform 0.5s ease;
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

    outline: 3px solid #c9c9c9;
    background-color: #eeeeee;
    border-radius: 20px;
    width: calc(100% - 40px);
    height: 50px;
    padding: 5px 20px;
    font-family: Gabarito, sans-serif;

    :global(.dark-mode) & {
      outline: 3px solid #333333;
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
</style>