<script lang="ts">
	import { scrollToMiddle } from '$lib/scrolltomiddle';
	import { wrap, writableContent } from '$lib/stores';
	import { onMount } from 'svelte';

	// svelte-ignore non_reactive_update
    let textArea: HTMLTextAreaElement;
    // svelte-ignore non_reactive_update
    let contentContainer: HTMLElement;

	onMount(() => {
		if (textArea) {
			wrap.subscribe((value) => {
				textArea.style.textWrap = value ? 'normal' : 'nowrap';
			});
		}

		const hash = window.location.hash;

        if (textArea) {
            textArea.addEventListener('input', () => {
                writableContent.set(textArea.value);
            });
        }

		if (contentContainer && hash) {
			const id = hash.substring(1);
			const element = document.getElementById(id);

			if (element) {
                setTimeout(() => {
				    scrollElementToMiddleInContainer(contentContainer, element);
                })
			} else {
                console.error(`Element with id ${id} not found.`);
            }
		}
	});

	let { content = null } = $props();

	function padIndex(index: number): string {
		let lineNumbers = content.split('\n').length;
		let padding = Math.ceil(Math.log10(lineNumbers));
		let paddedIndex = index.toString().padStart(padding, '0');

		return paddedIndex;
	}

	function scrollElementToMiddleInContainer(container: HTMLElement, element: HTMLElement): void {
		const containerRect = container.getBoundingClientRect();
		const elementRect = element.getBoundingClientRect();

		const offsetTop = elementRect.top - containerRect.top + container.scrollTop;
		const scrollTo = offsetTop - container.clientHeight / 2 + elementRect.height / 2;

		container.scrollTo({
			top: scrollTo,
			behavior: 'smooth'
		});
	}
</script>

{#if content}
	<div id="content" bind:this={contentContainer}>
		{#each content.split('\n') as line, index}
			<div id="line-container">
				<p class="number" id={(index + 1).toString()} use:scrollToMiddle>
					{padIndex(index + 1)}
				</p>
				<p id="line" style="text-wrap: {$wrap ? 'initial' : 'nowrap'}">{line}</p>
			</div>
		{/each}
	</div>
{:else}
	<div id="input">
		<textarea
			id="input-textarea"
			placeholder="Paste your content here..."
			style="text-wrap: {$wrap ? 'initial' : 'nowrap'}"
			bind:this={textArea}
		></textarea>
	</div>
{/if}

<style lang="scss">
	#input {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 1rem;
		padding: 1.6rem;
		background-color: var(--color-background);
		border-radius: 0.5rem;

        @media (max-width: 650px) {
            padding: 1rem;
            padding-bottom: 0;
        }
	}

	#input-textarea {
		flex: 1;
		resize: none;
		border-radius: 0.5rem;
		background-color: var(--color-background);
		color: var(--color-primary);
		font-size: 1rem;
		font-family: var(--font-family);
		border: none;
		font-weight: 400;
		text-wrap: nowrap;

        @media (max-width: 650px) {
            font-size: 0.9rem;
        }
	}

	#input-textarea::placeholder {
		color: var(--color-text-secondary);
		font-size: 1rem;
		font-family: var(--font-family);
		font-weight: 200;
	}

	#input-textarea:focus {
		outline: none;
	}

	#content {
		display: flex;
		flex-direction: column;
		padding: 1.6rem;
		background-color: var(--color-background);
		border-radius: 0.5rem;
		overflow: auto;
	}

	#content #line {
		font-size: 1rem;
		color: var(--color-primary);
		font-family: var(--font-family-mono);
		font-weight: 400;
		margin: 0;
		display: inline-block;

        @media (max-width: 650px) {
            font-size: 0.9rem;
        }
	}

	#content #line::selection {
		background-color: var(--color-primary);
		color: var(--color-background);
	}

	#line-container {
		display: flex;
		flex-direction: row;
		gap: 1.5rem;
		padding-right: 1rem;
	}

	.number {
		font-size: 1rem;
		font-family: var(--font-family-mono);
		display: inline-block;
		margin: 0;
		color: var(--color-text-secondary);
		font-weight: 200;
		user-select: none;
		-webkit-user-select: none;
		cursor: pointer;

        @media (max-width: 650px) {
            font-size: 0.9rem;
        }
	}
</style>
