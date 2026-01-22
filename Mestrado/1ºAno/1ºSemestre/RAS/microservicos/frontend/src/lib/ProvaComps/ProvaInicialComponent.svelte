<script lang="ts">
	import FileInput from '$lib/FormElements/FileInput.svelte';
	import TextBox from '$lib/FormElements/TextBox.svelte';
	import Button from '$lib/FormElements/Button.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces';
	import { errStore, loading, novaProvaStore } from '$lib/stores';
	import { enhance, applyAction } from '$app/forms';
	import type { SubmitFunction } from '@sveltejs/kit';
	import { openModal } from '$lib/utils';
	export let state;
	let submit: SubmitFunction =
		async function submit(form) {
			loading.set(true);
			return async ({ result, update }) => {
				loading.set(false);
				if (result.type === 'failure') {
					$errStore.error_title = result.data?.error_title;
					$errStore.error_message = result.data?.error_message;
					openModal();
				} else if (result.type === 'success') {
					$novaProvaStore.name = result.data?.name;
					$novaProvaStore.students = result.data?.students;
					state = "agendamento"
				} else {
					await applyAction(result);
				}
			};
		};
	let buttons: IButton[] = [
		{
			label: 'Cancelar',
			isFormButton: true,
			formAction: '?/cancel',
			style: 'btn-error'
		},
		{
			label: 'Proximo',
			isFormButton: true,
			formAction: '?/start',
			style: 'btn-success'
		}
	];
</script>

<form
	method="POST"
	class="flex flex-col justify-evenly justify-items-end h-1/3 gap-4 pb-4"
	use:enhance={submit}
>
	<TextBox name="NomeProva">Nome da Prova</TextBox>
	<FileInput name="FicheiroProva">Ficheiro da Prova</FileInput>
	<div class="flex justify-evenly justify-items-end h-1/3 gap-4 pb-4">
		{#each buttons as button}
			<div class="w-[40%]">
				<Button
					isFormButton={button.isFormButton}
					FormAction={button.formAction}
					css_type={button.style}
				>
					{button.label}
				</Button>
			</div>
		{/each}
	</div>
</form>
