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
    <br>
    <br>
    <p>
        {content}
    </p>
</main>

<style lang="scss">
    name {
      display: block;
      font-size: 5vw;
      font-family: Gabarito, sans-serif;
      font-weight: 900;
      text-align: center;
      width: 100%;
      white-space: nowrap;
    }

    created {
      display: block;
      font-size: 1.5vw;
      font-family: Gabarito, sans-serif;
      text-align: center;
      font-weight: 200;
      color:gray;
    }

    p {
      font-size: 15 px;
      white-space: pre;
      font-family: "JetBrains Mono", monospace;
    }
</style>