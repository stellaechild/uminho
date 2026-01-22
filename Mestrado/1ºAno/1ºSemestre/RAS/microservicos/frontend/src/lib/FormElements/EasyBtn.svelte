<script lang="ts">
	import Button from './Button.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces';
	export let details: IButton;
</script>

{#if details.isFormButton}
	<Button
		isFormButton={details.isFormButton}
		FormAction={details.formAction}
		css_type={details.style}>
		{details.label}
	</Button>
{:else}
	<Button isFormButton={details.isFormButton} on:btnclicked={details.onClick} css_type={details.style}>
		{details.label}
	</Button>
{/if}
