<script lang="ts">
    import Content from "../../../components/Content.svelte";

    export let data

    const { created, content, title, reportBook } = data

    const reloadTime = () => {
        let timeSince = Date.now() - created;
        if (timeSince < 1000) {
            timeSinceStr = "Just now";
        } else if (timeSince < 60000) {
            if (Math.floor(timeSince / 1000) == 1) {
                timeSinceStr = "1 second ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 1000) + " seconds ago";
            }
        } else if (timeSince < 3600000) {
            if (Math.floor(timeSince / 60000) == 1) {
                timeSinceStr = "1 minute ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 60000) + " minutes ago";
            }
        } else if (timeSince < 86400000) {
            if (Math.floor(timeSince / 3600000) == 1) {
                timeSinceStr = "1 hour ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 3600000) + " hours ago";
            }
        } else {
            if (Math.floor(timeSince / 86400000) == 1) {
                timeSinceStr = "1 day ago";
            } else {
                timeSinceStr = Math.floor(timeSince / 86400000) + " days ago";
            }
        }
    }

    let timeSinceStr = "";

    reloadTime()
    let clear
    $: {
        clearInterval(clear)
        clear = setInterval(reloadTime, 1000)
    }

</script>

<main>
    <a class="beta" target="_blank" href="https://github.com/Loudbooks/PasteBook">BETA</a>
    <topcontainer>
        <name>
            {title}
        </name>
        <created>
            {timeSinceStr}
        </created>
    </topcontainer>
    <Content content="{content}" reportBook="{reportBook}"></Content>
</main>

<style lang="scss">
  .beta {
    position: fixed;
    margin: 0;
    right: 30px;
    bottom: 0;
    background: linear-gradient(90deg, #f05a48, #f4518d);
    color: #fff;
    padding: 3px 8px;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    font-size: 12px;
    font-weight: bold;
    z-index: 9999;
    font-family: Gabarito, sans-serif;
    text-decoration: none;

    animation: flyIn ease 0.7s;
    animation-iteration-count: 1;

    transition: transform 0.3s ease;
  }

  .beta:hover {
    cursor: pointer;
    transform: translateY(10%);
  }

  topcontainer {
    padding-top: 7px;
    display: flex;

    name {
      display: inline-block;
      font-size: 35px;
      font-family: Gabarito, sans-serif;
      font-weight: 750;
      text-align: left;
      word-break: break-all;
      align-self: end;
      flex: 75%;
      padding-left: 30px;

      animation: fadeIn ease 0.7s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;
    }

    created {
      font-size: 18px;
      font-family: Gabarito, sans-serif;
      text-align: right;
      font-weight: 200;
      opacity: 0;
      color: grey;
      align-self: end;
      padding-right: 30px;

      flex: 25%;

      animation: fadeIn ease 0.7s;
      animation-delay: 0.1s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;
    }

    @keyframes fadeIn {
      0% {
        opacity: 0;
      }
      100% {
        opacity: 1;
      }
    }

    @keyframes flyIn {
        0% {
            transform: translateY(90%);
        }
        100% {
            transform: translateY(0%);
        }
    }

    @media only screen and (max-width: 600px) {
      name {
        font-size: 22px;
      }
    }

    @media only screen and (max-width: 600px) {
      created {
        font-size: 13px;
      }
    }
  }

</style>