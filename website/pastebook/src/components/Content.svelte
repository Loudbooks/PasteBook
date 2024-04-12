<script>
    export let content = undefined

    if (content === undefined) {
        let element = document.querySelector('form')
        if (element != null) {
            element.contentEditable = true
        }
    }
</script>

<contentcontainer>
    <p>
        {#if content === undefined}
            <form contenteditable="true">
            </form>
        {:else}
            {content}
        {/if}
    </p>
</contentcontainer>

<style>
    contentcontainer {
        display: block;
        background-color: #eeeeee;
        margin: 10px;
        border-radius: 20px;
        animation: fadeIn ease 0.7s;
        animation-delay: 0.3s;
        animation-iteration-count: 1;
        animation-fill-mode: forwards;
        opacity: 0;
        overflow: auto;
    }

    form {
        display: inline-block;
        font-size: 13px;
        white-space: pre;
        font-family: "JetBrains Mono", monospace;
        margin: 20px;
        padding: 10px;
        width: 100%;
    }

    p {
        display: inline-block;
        font-size: 13px;
        white-space: pre;
        font-family: "JetBrains Mono", monospace;
        margin: 20px;
        padding: 10px;
        width: 86%;
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