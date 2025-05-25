<script>
	import { goto } from "$app/navigation";

</script>
<div id="new-paste">
    <button class="new-paste-button" on:click={() => {goto("/new")}}>
        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M4 12H20M12 4V20" stroke="#000000" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
        New Paste
    </button>
</div>

<style lang="scss">
    #new-paste {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 50px;
        width: 100%;
        background-color: var(--color-primary);
        border-radius: 15px;

        transition: background-color 0.3s ease,
            transform 0.2s ease,
            filter 0.2s ease;
    }

    svg {
        width: .75rem;
        height: .75rem;
    }

    .new-paste-button {
        background-color: var(--color-button);
        color: var(--color-text);
        border: none;
        cursor: pointer;
        height: 2rem;
        font-size: 1.05rem;
        padding: 0.5rem 1.2rem;
        font-family: var(--font-family);
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 0.8rem;
        border-radius: 15px;
    }

    #new-paste:hover {
        filter: brightness(0.9);
    }

    #new-paste:active {
        transform: scale(0.98);
    }

</style>