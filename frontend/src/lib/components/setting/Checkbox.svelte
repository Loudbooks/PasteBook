<script lang="ts">
	import { onMount } from 'svelte';
    import InlineSetting from './InlineSetting.svelte';

	let { title, description, checked, onChange } = $props();

    let checkState = $state(checked);
	let checkboxId = $state('');

	onMount(() => {
		checkboxId = `checkbox-${Math.random().toString(36).slice(2, 9)}`;
	});
</script>

<InlineSetting {title} {description}>
	<div class="checkbox-toggle">
		<input
			type="checkbox"
			id={checkboxId}
			bind:checked={checkState}
			onchange={onChange}
		/>

		<label for={checkboxId} class="toggle-label">
			<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
				<path
					d="M4 12.6111L8.92308 17.5L20 6.5"
					stroke="var(--color-background)"
					stroke-width="2.5"
					stroke-linecap="round"
					stroke-linejoin="round"
				/>
			</svg>
		</label>
	</div>
</InlineSetting>

<style lang="scss">
    #checkbox-description {
        cursor: pointer;
    }

	input[type='checkbox'] {
		display: none;
	}

	#checkbox {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		gap: 0;
	}

	.toggle-label {
		display: inline-block;
		width: 20px;
		height: 20px;
		border-radius: 8px;
		background-color: var(--color-background);
		border: 1px solid var(--color-border);
		position: relative;
		cursor: pointer;
		transition: background-color 0.3s ease;
	}

	input:checked + .toggle-label {
		background-color: var(--color-primary);
	}

	input:checked + .toggle-label svg {
		opacity: 1;
	}

	svg {
		width: 12px;
		height: 12px;
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		opacity: 0;
		transition: opacity 0.3s ease;
	}
</style>
