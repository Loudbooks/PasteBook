<script lang="ts">
    import { goto } from '$app/navigation';
    import { onMount } from 'svelte';
    import { wrap, burn, writableContent } from '$lib/stores';

    let alreadyUploading = false;


	function onClick() {
		if (alreadyUploading) {
			return;
		}

		alreadyUploading = true;
		const xhr = new XMLHttpRequest();

		let domain = window.location.host;

		if (domain.includes('localhost')) {
			xhr.open('POST', `http://localhost/api/upload`);
		} else {
			if (domain.match(/192\.168\.\d+\.\d+/)) {
				domain = domain.replace(/:\d+/, '');

				xhr.open('POST', `http://${domain}/api/upload`);
			} else {
				xhr.open('POST', `https://${domain}/api/upload`);
			}
		}

		xhr.setRequestHeader('Content-Type', 'plain/text');
		xhr.setRequestHeader('access-control-allow-methods', 'POST');
		xhr.setRequestHeader('wrap', String($wrap));
        xhr.setRequestHeader('burn', String($burn));

		xhr.send($writableContent);
		xhr.responseType = 'text';
        
		xhr.onload = function () {
			if (xhr.status !== 200) {
				alreadyUploading = false;

				return;
			}

			goto('/p/' + xhr.response);
		};
	}
</script>

<div id="submit">
	<button class="submit-button" on:click={onClick}>
		<svg width="17" height="15" viewBox="0 0 17 15" fill="none" xmlns="http://www.w3.org/2000/svg">
			<path
				d="M11 9.99999L8.50004 7.49999M8.50004 7.49999L6.00004 9.99999M8.50004 7.49999V13.125M13.7438 11.4937C14.3534 11.1614 14.8349 10.6355 15.1125 9.99913C15.39 9.36272 15.4477 8.65201 15.2764 7.97917C15.1052 7.30633 14.7147 6.70968 14.1667 6.28339C13.6187 5.8571 12.9443 5.62545 12.25 5.62499H11.4625C11.2734 4.89327 10.9208 4.21395 10.4313 3.63811C9.94175 3.06227 9.32806 2.6049 8.63635 2.30037C7.94463 1.99584 7.19288 1.85209 6.43761 1.87992C5.68234 1.90774 4.9432 2.10643 4.27577 2.46103C3.60834 2.81563 3.02998 3.31693 2.58417 3.92723C2.13837 4.53753 1.83672 5.24095 1.7019 5.98461C1.56708 6.72827 1.6026 7.49282 1.80579 8.22078C2.00899 8.94873 2.37456 9.62116 2.87504 10.1875"
				stroke="black"
				stroke-width="1.8"
				stroke-linecap="round"
				stroke-linejoin="round"
			/>
		</svg>

		Submit
	</button>
</div>

<style lang="scss">
	svg {
		width: 1.6rem;
		height: 1.6rem;
	}

	.submit-button {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 60px;
		background-color: var(--color-primary);
		border-radius: 15px;
		width: 400px;

		color: var(--color-text);
		border: none;
		cursor: pointer;
		font-size: 1.4rem;
		padding: 0.5rem 1.2rem;
		font-family: var(--font-family);
		line-height: 1;
		font-weight: 600;
		gap: 0.8rem;
		border-radius: 15px;
		transition:
			background-color 0.3s ease,
			transform 0.2s ease,
			filter 0.2s ease;
	}

	.submit-button:hover {
		filter: brightness(0.9);
	}

	.submit-button:active {
		transform: scale(0.98);
	}
</style>
