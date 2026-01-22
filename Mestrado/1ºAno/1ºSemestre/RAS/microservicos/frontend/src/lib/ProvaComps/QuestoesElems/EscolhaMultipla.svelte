<script lang="ts">
	import { applyAction } from '$app/forms';
	import Button from '$lib/FormElements/Button.svelte';
	import MultilineTextBox from '$lib/FormElements/MultilineTextBox.svelte';
	import TextBox from '$lib/FormElements/TextBox.svelte';
	import { errStore, loading } from '$lib/stores';
	import type { Pergunta } from '$lib/types';
	import { openModal } from '$lib/utils';
	import type { SubmitFunction } from '@sveltejs/kit';
    import { enhance } from '$app/forms';
	export let order: number = 0;
	export let perg: Pergunta;
	export let allowEditing: Boolean = false;
	export let Editing: Boolean = false;
    export let versaoId: number;
    export let provaId: number;
	console.log(Editing);
	let toggleEditing = () => {
		Editing = !Editing;
	};
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
                    perg.enunciado = result.data?.enunciado;
                    perg.Pergunta.options = result.data?.Pergunta.options;
                    perg.Pergunta.resposta_certa = result.data?.Pergunta.resposta_certa;
                    perg.cotacao = result.data?.cotacao;
                    console.log(perg);
					Editing = false;
				} else {
					await applyAction(result);
				}
			};
		};
</script>

<form method="post" class="w-full flex flex-col gap-2" use:enhance={submit}>
	<input type="hidden" name="id" value={perg.id} />
	<input type="hidden" name="tipo" value={perg.tipo} />
    <input type="hidden" name="versaoId" value={versaoId} />
    <input type="hidden" name="provaId" value={provaId} />
	<div class="flex flex-row justify-between items-center">
		<p>
			Questao {order}
		</p>
		<div class="flex items-center gap-2">
			Cotacao:
			{#if Editing}
				<input
					type="number"
					name="cotacao"
					min="0"
					max="200"
					class="input input-bordered h-12 w-[4.75rem]"
					value={perg.cotacao} />
			{:else}
				{perg.cotacao}
			{/if}
		</div>
		<div class="">
			{#if allowEditing}
				{#if Editing}
					<Button isFormButton={true} FormAction="?/saveQuestion" css_type="btn-primary">
						Guardar
					</Button>
				{:else}
					<Button isFormButton={false} css_type="btn-success" on:btnclicked={toggleEditing}>
						Editar
					</Button>
				{/if}
			{/if}
		</div>
	</div>
	{#if Editing}
		<MultilineTextBox name="enunciado" text={perg.enunciado}>Enunciado</MultilineTextBox>
	{:else}
		{perg.enunciado}
	{/if}
	{#each perg.Pergunta.options as option, index}
		{#if Editing}
			<div class="flex items-center justify-center gap-2 w-full px-8">
				<input type="radio" value={index} class="radio radio-primary mx-2" />
				R{index + 1}:
				<div class="w-4/5">
					<TextBox
						name="option{index + 1}"
						showHeader={false}
						text={option}
						css_type="input-primary w-full" />
				</div>
			</div>
		{:else}
			<div class="flex items-center justify-center gap-2 w-full px-8 h-12">
				<input
					type="radio"
					name="resposta"
					value={index}
					class="radio radio-primary"
					disabled={true} />
				R{index + 1}:
				<div class="w-4/5">
					{option}
				</div>
			</div>
		{/if}
	{/each}
	<div class="flex justify-center items-center gap-2">
		Resposta Correta:
		{#if Editing}
			<label class="form-control w-2/3">
				<select class="select select-primary select-bordered" name="RespostaCorreta" value={perg.Pergunta.resposta_certa}>
					{#each [1, 2, 3, 4] as i}
						<option value={i}>R{i}</option>
					{/each}
				</select>
			</label>
		{:else}
			<div class="form-control w-2/3">
				R{perg.Pergunta.resposta_certa}
			</div>
		{/if}
	</div>
</form>
