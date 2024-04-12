<script>
    export let content = undefined
</script>

<contentcontainer>
    <p>
        {content}
    </p>
</contentcontainer>

<style>
    contentcontainer {
        display: inline-block;
        background-color: #eeeeee;
        width: calc(100% - 20px);
        margin: 10px;
        border-radius: 20px;
        animation: fadeIn ease 0.7s;
        animation-delay: 0.3s;
        animation-iteration-count: 1;
        animation-fill-mode: forwards;
        opacity: 0;
        overflow: auto;
    }

    p {
        display: inline-block;
        font-size: 13px;
        white-space: pre;
        font-family: "JetBrains Mono", monospace;
        margin: 20px;
        padding: 10px;
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