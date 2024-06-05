<script lang="ts">
    export let callback: (value: number) => void;

    function handleCallback(event: Event) {
        callback(parseInt((event.target as HTMLSelectElement).value));
    }
</script>

<select on:change={handleCallback}>
    <option value="3600000">1 hour</option>
    <option value="43200000">12 hours</option>
    <option selected="selected" value="86400000">24 hours</option>
    <option value="604800000">1 week</option>
    <option value="1209600000">2 weeks</option>
    <option value="2592000000">1 month</option>
</select>

<style lang="scss">
  select {
    transition: background-color 0.5s ease, color 0.5s ease, border 0.5s ease;

    width: 110px;
    padding: 5px 5px 5px 15px;
    font-size: 15px;
    margin: 10px;
    line-height: 1;
    border-radius: 30px;
    height: 40px;
    @media (min-width: 600px) {
      background: url("$lib/assets/arrow.svg") no-repeat right #eeeeee;
      background-size: 15%;
      background-position-x: 80px;
    }
    @media (max-width: 600px) {
      background: url("$lib/assets/arrow.svg") no-repeat right #eeeeee;
      background-size: 15%;
      background-position-x: 75px;
    }

    :global(.dark-mode) & {
      border: 1px solid #333;
      @media (min-width: 600px) {
        background: url("$lib/assets/arrow-white.svg") no-repeat right #1a1a1a;
        background-size: 15%;
        background-position-x: 80px;
      }
      @media (max-width: 600px) {
        background: url("$lib/assets/arrow-white.svg") no-repeat right #1a1a1a;
        background-size: 15%;
        background-position-x: 75px;
      }
      color: white;
    }

    border: 1px solid #cfcfcf;
    font-family: Gabarito, sans-serif;

    -webkit-appearance: none;
    float: right;

    @media (max-width: 600px) {
      height: 25px;
      font-size: 12px;
      padding-left: 10px;
      width: 100px;
    }

    &:hover {
      cursor: pointer;
      background-color: #cfcfcf;

      :global(.dark-mode) & {
        background-color: #333;
      }
    }

    &:focus {
      outline: none;
    }

    &:active {
      background-color: #cfcfcf;
    }
  }
</style>