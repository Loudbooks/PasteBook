<script lang="ts">
	import { pushState } from "$app/navigation";
	import { wrap, writableContent } from "$lib/stores";
	import { onMount } from "svelte";

	let textArea = $state<HTMLTextAreaElement | null>(null);
	let contentContainer = $state<HTMLElement | null>(null);

	let { content = "", tokenLines = null, newPaste = false } = $props();
	let length = $state(0);

	function padIndex(index: number): string {
		let useLength = length + 1;

		let padding = Math.ceil(Math.log10(useLength));
		return index.toString().padStart(padding, " ");
	}

	function getLeftInputPadding(): string {
		let padding = Math.ceil(Math.log10(length + 1));

		if (padding < 1) {
			padding = 1;
		}

		return `${padding + 2}ch`;
	}

	function getWidth(): string {
		if ($wrap) {
			return "initial";
		}

		let characterWidth = 0;
		if (content) {
			characterWidth = Math.max(
				...content.split("\n").map((line: string) => line.length),
			);
		} else if (tokenLines) {
			let rawLines = tokenLines.map((line: any) =>
				line.map((token: any) => token.content).join(""),
			);

			characterWidth = Math.max(
				...rawLines.map((line: string) => line.length),
			);
		}

		if (characterWidth > 0) {
			return `${characterWidth + 3}ch`;
		}

		return 0 + "ch";
	}

	function getHeight(): string {
		if (length > 0) {
			return `${length * 1.5}rem`;
		}

		return "1.5rem";
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

		pushState(`#${element.id}`, {});

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

	async function updateContent(newContent: string) {
		length = newContent.split("\n").length;

		content = newContent;
		writableContent.set(newContent);
	}

	onMount(async () => {
		const hash = window.location.hash;

		length = content
			? content.split("\n").length
			: tokenLines
				? tokenLines.length
				: 0;

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

		textArea?.addEventListener("keydown", (event) => {
			if (!textArea) return;

			if (event.key === "Tab") {
				event.preventDefault();

				textArea.setRangeText(
					"\t",
					textArea.selectionStart,
					textArea.selectionEnd,
					"end",
				);
			}
		});
	});

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
	});
</script>

<div id="content" bind:this={contentContainer}>
	{#if newPaste}
		<textarea
			oninput={(event) => {
				if (!textArea) return;
				const newContent = (event.target as HTMLTextAreaElement).value;
				updateContent(newContent);
			}}
			onkeydown={(event) => {
				if (event.key === "Enter") {
					const newContent = textArea.value + "\n";
					window.scrollTo({
						left: 0,
						behavior: "smooth",
					});
				}
			}}
			id="input-textarea"
			placeholder="Paste your content here..."
			style="text-wrap: {$wrap
				? 'initial'
				: 'nowrap'}; left: {getLeftInputPadding()}; width: {getWidth()}; height: {getHeight()};"
			bind:this={textArea}
		></textarea>
	{/if}
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
							<span
								class={$wrap ? "wrap" : ""}
								style="color: {token.color}; text-wrap: {$wrap
									? 'initial'
									: 'nowrap'}">{token.content}</span
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
					class={$wrap ? "wrap" : ""}
					style="text-wrap: {$wrap ? 'initial' : 'nowrap'}"
				>
					{line == "" ? "\u200B" : line}
				</span>
			</div>
		{/each}
	{/if}
</div>

<style lang="scss">
	#input-textarea {
		flex: 1;
		resize: none;
		border-radius: 0.5rem;
		color: var(--color-primary);
		background: transparent;
		font-size: 1rem;
		font-family: var(--font-family-mono);
		border: none;
		font-weight: 400;
		text-wrap: nowrap;
		font-variant-ligatures: none !important;
		-webkit-text-fill-color: transparent;
		z-index: 2;
		border: 1x solid green;
		position: absolute;
		top: 0;
		left: 3ch;
		right: 3ch;
		bottom: 0;
		padding: 1.6rem;
		user-select: text;
		-webkit-user-select: text;
		min-width: calc(100% - 3.2rem - 6ch);
		min-height: calc(100% - 3.2rem);

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
		height: 100%;
		position: relative;
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
		gap: 2ch;
		font-family: var(--font-family-mono);
		padding-right: 1.6rem;
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

		&.wrap {
			display: inline-block;
			overflow-wrap: anywhere;
			word-break: normal;
		}
	}
</style>
