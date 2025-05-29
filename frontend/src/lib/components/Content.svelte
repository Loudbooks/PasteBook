<script lang="ts">
	import { wrap, writableContent } from "$lib/stores";
	import { onMount } from "svelte";

	let textArea = $state<HTMLTextAreaElement | null>(null);
	let contentContainer = $state<HTMLElement | null>(null);

	const { content = null, tokenLines = null } = $props();

	function padIndex(index: number): string {
		let length = content
			? content.split("\n").length
			: tokenLines
				? tokenLines.length
				: 0;
		length++;

		let padding = Math.ceil(Math.log10(length));
		return index.toString().padStart(padding, " ");
	}

	function scrollElementToMiddleInContainer(
		container: HTMLElement,
		element: HTMLElement,
	): void {
		const containerRect = container.getBoundingClientRect();
		const elementRect = element.getBoundingClientRect();

		const offsetTop =
			elementRect.top - containerRect.top + container.scrollTop;
		const scrollTo =
			offsetTop - container.clientHeight / 2 + elementRect.height / 2;

		container.scrollTo({
			top: scrollTo,
			behavior: "smooth",
		});
	}

	$effect(() => {
		if (textArea) {
			textArea.style.textWrap = $wrap ? "normal" : "nowrap";
		}
	});

	onMount(async () => {
		if (textArea) {
			textArea.addEventListener("input", () => {
				writableContent.set((textArea as HTMLTextAreaElement).value);
			});
		}

		const hash = window.location.hash;

		if (contentContainer && hash) {
			const id = hash.substring(1);
			const element = document.getElementById(id);
			if (element) {
				setTimeout(() => {
					scrollElementToMiddleInContainer(
						contentContainer as HTMLElement,
						element,
					);
				});
			} else {
				console.error(`Element with id ${id} not found.`);
			}
		}
	});

	function selectAllContentInContainer(container: HTMLElement) {
		const range = document.createRange();
		range.selectNodeContents(container);

		const selection = window.getSelection();
		if (!selection) return;

		selection.removeAllRanges();
		selection.addRange(range);
	}

	let isFocused = false;

	onMount(() => {
		if (contentContainer) {
			contentContainer.addEventListener("click", () => {
				isFocused = true;
			});
		}

		document.addEventListener("click", (e) => {
			if (!contentContainer?.contains(e.target as Node)) {
				isFocused = false;
			}
		});

		document.addEventListener("keydown", (e) => {
			const isSelectAll =
				(e.ctrlKey || e.metaKey) && e.key.toLowerCase() === "a";

			if (isFocused && isSelectAll) {
				e.preventDefault();
				selectAllContentInContainer(contentContainer as HTMLElement);
			}
		});
	});
</script>

{#if content || tokenLines}
	<div id="content" bind:this={contentContainer}>
		{#if tokenLines}
			{#each tokenLines as line, index}
				<div id="line-container">
					<span
						class="number"
						id={(index + 1).toString()}
						onclick={() => {
							const element = document.getElementById(
								(index + 1).toString(),
							);
							if (element) {
								scrollElementToMiddleInContainer(
									contentContainer as HTMLElement,
									element,
								);
							}
						}}
					>
						{padIndex(index + 1)}
					</span>
					<span
						id="line"
						style="text-wrap: {$wrap ? 'initial' : 'nowrap'}"
					>
						{#if line.length === 0}
							<span style="color: transparent;">{"\u200B"}</span>
						{:else}
						{#each line as token}
							<span style="color: {token.color}"
								>{token.content}</span
							>
						{/each}
						{/if}
				</span>
				</div>
			{/each}
		{:else}
			{#each content.split("\n") as line, index}
				<div id="line-container">
					<span
						class="number"
						id={(index + 1).toString()}
						onclick={() => {
							const element = document.getElementById(
								(index + 1).toString(),
							);
							if (element) {
								scrollElementToMiddleInContainer(
									contentContainer as HTMLElement,
									element,
								);
							}
						}}
					>
						{padIndex(index + 1)}
					</span>
					<span
						id="line"
						style="text-wrap: {$wrap ? 'initial' : 'nowrap'}"
					>
						{line == "" ? "\u200B" : line}
				</span>
				</div>
			{/each}
		{/if}
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
		font-family: var(--font-family-mono);
		border: none;
		font-weight: 400;
		text-wrap: nowrap;
		font-variant-ligatures: none !important;

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
		white-space: pre-wrap;

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

	:global(span) {
		font-family: var(--font-family-mono);
		width: fit-content;
	}

	span {
		margin: 0;
		padding: 0;
		white-space: pre;
	}
</style>
