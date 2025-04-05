<script lang="ts">
  export let name: string;
  export let description: string;
</script>

<settingcontainer>
  <settingheading>
    <settingtitle>{name}</settingtitle>
    <settingdescription>{description}</settingdescription>
  </settingheading>
  <settingcontent>
    <slot name="setting"></slot>
  </settingcontent>
</settingcontainer>

<style lang="scss">
  settingcontainer {
    display: flex;

    justify-content: space-between;
  }

  settingheading {
    display: flex;
    margin: 10px;
    align-items: center;
    flex-direction: column;
    justify-self: center;
  }

  settingtitle {
    transition: color 0.5s ease;

    font-size: 1.5em;
    font-weight: bold;
    margin-right: 10px;
    text-align: left;
    align-self: start;
    color: black;
    font-family: var(--font-family), sans-serif;

    :global(.dark-mode) & {
      color: white;
    }

    @media (max-width: 768px) {
      font-size: 1em;
    }
  }

  settingdescription {
    font-size: 1em;
    color: #666;
    text-align: left;
    align-self: start;
    font-family: var(--font-family), sans-serif;

    @media (max-width: 768px) {
      font-size: 0.7em;
    }
  }

  settingcontent {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    margin: 10px;

    @media (max-width: 768px) {
      margin: 5px;
    }
  }
</style>
