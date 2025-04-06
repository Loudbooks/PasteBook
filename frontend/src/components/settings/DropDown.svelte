<script lang="ts">
    import Arrow from "../Arrow.svelte";

  export let callback: (value: number) => void;

  function handleCallback(event: Event) {
    callback(parseInt((event.target as HTMLSelectElement).value));
  }
</script>

<div id="dropdown" class="dropdown">
  <select on:change={handleCallback}>
    <slot name="options" />
  </select>
  
  <div id="arrow" class="arrow">
    <Arrow />
  </div>
</div>



<style lang="scss">
  select {
    transition:
      background-color 0.5s ease,
      color 0.5s ease,
      border 0.5s ease;

    width: 110px;
    padding: 5px 5px 5px 15px;
    font-size: 15px;
    margin: 10px;
    line-height: 1;
    border-radius: 30px;
    height: 40px;
    
    border: var(--border-standard);
    color: var(--color-text-primary);
    font-family: var(--font-family), sans-serif;
    
    
    -webkit-appearance: none;
    float: right;
    
    
    &:hover {
      cursor: pointer;
      background-color: var(--color-border) !important;
    }
    
    &:focus {
      outline: none;
    }

    color: var(--color-text-primary);
    border: var(--border-standard);
    
    @media (max-width: 600px) {
      height: 25px;
      font-size: 12px;
      padding-left: 10px;
      width: 90px;
    }

    @media (min-width: 600px) {
      background: url("$lib/assets/arrow.svg") no-repeat right var(--color-background-secondary);
      background-size: 15%;
      background-position-x: 80px;
    }
    
    @media (max-width: 600px) {
      background: url("$lib/assets/arrow.svg") no-repeat right var(--color-background-secondary);
      background-size: 15%;
      background-position-x: 65px;
    }
  }

  #arrow {
    position: absolute;
    transform: translate(92px, 22px);

    @media (max-width: 600px) {
      transform: translate(76px, 14px);
    }
  }
</style>
