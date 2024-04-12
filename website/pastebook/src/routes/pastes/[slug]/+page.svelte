<script lang="ts">
    export let data

    const { created, content, title } = data

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
    <name>
        {title}
    </name>
    <created>
        {timeSinceStr}
    </created>
    <break></break>
    <p>
        {content}
    </p>
</main>

<style lang="scss">
    name {
      display: block;
      font-size: max(5vw, 30px);
      font-family: Gabarito, sans-serif;
      font-weight: 900;
      text-align: center;
      width: 100vw;
      white-space: wrap;

      animation: fadeIn ease 0.7s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;
    }

    break {
      display: block;
      color: black;
      background-color: grey;
      height: 3px;
      border-radius: 10px;
      width: 90%;
      align-content: center;
      margin: 15px auto auto;
      opacity: 0;

      animation: fadeIn ease 0.7s;
      animation-delay: 0.2s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;
    }

    created {
      display: block;
      font-size: max(1.5vw, 15px);
      font-family: Gabarito, sans-serif;
      text-align: center;
      font-weight: 200;
      color:gray;
      opacity: 0;

      animation: fadeIn ease 0.7s;
      animation-delay: 0.1s;
      animation-iteration-count: 1;
      animation-fill-mode: forwards;
    }

    p {
      font-size: 15 px;
      white-space: pre;
      font-family: "JetBrains Mono", monospace;
      padding: 20px;
      margin: 0;
      opacity: 0;

      animation: fadeIn ease 0.7s;
      animation-delay: 0.3s;
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
</style>