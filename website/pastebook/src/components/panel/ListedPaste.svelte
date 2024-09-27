<script lang="ts">
    import type {Paste} from "$lib/paste";
    import {formatTimeSince} from "$lib/timehandler";

    export let paste: Paste

    let title = paste.title
    let created = paste.created
    let id = paste.id

    const reloadTime = () => {
        timeSinceStr = formatTimeSince(created)
    }

    let timeSinceStr = formatTimeSince(created)

    reloadTime()
    let clear
    $: {
        clearInterval(clear)
        clear = setInterval(reloadTime, 1000)
    }

    function onClick() {
        window.location.href = `/p/${paste.id}`
    }
</script>

<pastebackground>
    <button class="pastecontainer" on:click={onClick}>
        <name>
            <h1>{title}</h1>
            <p>{id}</p>
        </name>
        <created>
            {timeSinceStr}
        </created>
    </button>
</pastebackground>


<style lang="scss">
  .pastecontainer {
    transition: background-color 0.5s, color 0.5s, border 0.5s, transform 0.4s;

    display: flex;
    padding: 1rem;
    border-radius: 15px;
    color: black;
    width: calc(100% - 20px);
    outline: none;
    align-content: space-between;
    background-color: #eeeeee;
    margin: 10px;
    border: 1px solid #c9c9c9;

    &:hover {
      background-color: #cfcfcf;

      :global(.dark-mode) & {
        background-color: #333;
      }

      cursor: pointer;
    }

    &:active {
      transform: scale(0.995);
    }

    :global(.dark-mode) & {
      background-color: #1a1a1a;
      border: 1px solid #333;
      color: white;
    }

    name {
      display: inline-block;
      text-overflow: ellipsis;
      overflow: hidden;

      h1 {
        font-size: 1.5rem;
        align-self: center;
        font-family: Gabarito, sans-serif;
        font-weight: 400;
        text-overflow: ellipsis;
        padding-right: 10px;
        overflow: hidden;
        white-space: nowrap;
        text-align: left;
        margin: 0;
      }

      p {
        font-size: 0.8rem;
        color: gray;
        margin: 0;
        text-align: left;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        font-family: Gabarito, sans-serif;
      }

      @media screen {
        @media (max-width: 600px) {
          p {
            font-size: 0.5rem;
          }

          h1 {
            font-size: 1rem;
          }
        }
      }
    }

    created {
      display: inline-block;
      font-size: 1rem;
      align-self: center;
      text-align: right;
      margin-left: auto;
      white-space: nowrap;

      font-family: Gabarito, sans-serif;
      color: gray;

      :global(.dark-mode) & {
        color: lightgray;
      }

      @media screen {
        @media (max-width: 600px) {
          font-size: 0.6rem;
        }
      }
    }
  }
</style>
