<script lang="ts">
    import { goto } from '$app/navigation';
	import { wrap, burn, writableTitle, writableContent, time } from '$lib/stores';
	import { onMount } from 'svelte';

	let alreadyUploading = false;
	let disabled = true;
	let error = '';
	let uploadProgress = 0;

	onMount(() => {
		writableContent.subscribe((content) => {
			if (content.length > 0 && $writableTitle.length > 0) {
				disabled = false;
			} else {
				disabled = true;
			}
		});

        writableTitle.subscribe((title) => {
            if (title.length > 0 && $writableContent.length > 0) {
				disabled = false;
			} else {
				disabled = true;
			}
        });
	});

	function onClick() {
		if (disabled) {
			return;
		}

		if (alreadyUploading) {
			return;
		}

		alreadyUploading = true;
		disabled = true;
		const xhr = new XMLHttpRequest();

		let domain = window.location.host;

		if (domain.includes('localhost')) {
			xhr.open('POST', `http://backend:8080/api/upload`);
		} else {
			if (domain.match(/192\.168\.\d+\.\d+/)) {
				domain = domain.replace(/:\d+/, '');

				xhr.open('POST', `http://${domain}/api/upload`);
			} else {
				xhr.open('POST', `https://${domain}/api/upload`);
			}
		}

        let expire;

        if ($time == "1h") {
            expire = 3600000;
        } else if ($time == "24h") {
            expire = 86400000;
        } else if ($time == "7d") {
            expire = 604800000;
        } else if ($time == "31d") {
            expire = 2678400000;
        } else {
            expire = 3600000;
        }

		xhr.setRequestHeader('Content-Type', 'plain/text');
		xhr.setRequestHeader('access-control-allow-methods', 'POST');
		xhr.setRequestHeader('wrap', String($wrap));
		xhr.setRequestHeader('burn', String($burn));
		xhr.setRequestHeader('title', $writableTitle);
        xhr.setRequestHeader('expires', String(expire));

		xhr.send($writableContent);
		xhr.responseType = 'text';

		xhr.onerror = function () {
			alreadyUploading = false;
			disabled = false;

			console.error('Upload failed:', xhr.statusText);
			if (xhr.status == 413) {
				error = 'Paste too large. Please reduce the size and try again.';
			} else if (xhr.status == 400) {
				error = 'Invalid request. Please check your input.';
			} else {
				error = 'Failed to upload paste. Please try again.';
			}

			uploadProgress = 0;
		};

		xhr.onprogress = function (event) {
			if (event.lengthComputable) {
				uploadProgress = Math.round((event.loaded / event.total) * 100);
			} else {
				console.warn('Unable to compute upload progress');
				uploadProgress = 0;
			}
		};

		xhr.onload = function () {
			if (xhr.status !== 200) {
				alreadyUploading = false;
				disabled = false;
				console.error('Upload failed with status:', xhr.status, xhr.statusText);
				if (xhr.status == 413) {
					error = 'Paste too large. Please reduce the size and try again.';
				} else if (xhr.status == 400) {
					error = 'Invalid request. Please check your input.';
				} else {
					error = 'Failed to upload paste. Please try again.';
				}

				uploadProgress = 0;

				return;
			}

			console.log('Upload successful:', xhr.response);

			goto('/p/' + xhr.response);
		};
	}
</script>

<div id="submit">
	<button
		class="submit-button {disabled ? 'disabled' : ''}"
		onclick={onClick}
		id="submit-button"
		disabled={alreadyUploading}
	>
		<div id="progress-bar-mask">
			<div id="progress-bar" style="width: {uploadProgress}%"></div>
		</div>
		<div id="icon-row">
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
		</div>
		<p>{error}</p>
	</button>
</div>

<style lang="scss">
	svg {
		width: 1.6rem;
		height: 1.6rem;

		@media (max-width: 650px) {
			width: 1.4rem;
			height: 1.4rem;
		}
	}

	.submit-button {
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
		height: 65px;
		background-color: var(--color-primary);
		border-radius: 15px;
		width: 400px;
		position: relative;

		color: var(--color-text);
		border: none;
		cursor: pointer;
		font-size: 1.4rem;
		padding: 0.5rem 1.2rem;
		font-family: var(--font-family);
		line-height: 1;
		font-weight: 600;
		border-radius: 15px;
		transition:
			background-color 0.3s ease,
			transform 0.2s ease,
			filter 0.2s ease;

        @media (max-width: 650px) {
            width: 100%;
            height: 50px;
            font-size: 1.2rem;
            padding: 0.5rem 1rem;
            border-radius: 10px;
        }
	}

	#icon-row {
		display: flex;
		align-items: center;
		gap: 0.8rem;

		@media (max-width: 650px) {
			gap: 0.5rem;
		}
	}

	p {
		font-size: 0.6rem;
		color: red;
		margin: 0;
		font-family: var(--font-family);
		font-weight: 200;
		height: 0;
	}

	.submit-button.disabled {
		filter: brightness(0.5);
		cursor: not-allowed;
	}

	.submit-button:hover:not(.disabled) {
		filter: brightness(0.9);
	}

	.submit-button:active:not(.disabled) {
		transform: scale(0.98);
	}

	#progress-bar-mask {
		width: 100%;
		height: 100%;
		filter: brightness(0.95);
		border-radius: 15px;
		position: absolute;
		top: 0;
		left: 0;
		overflow: hidden;
	}

	#progress-bar {
		height: 100%;
		border-radius: 15px;
		background-color: var(--color-primary);
		transition: width 0.3s ease;
	}
</style>
