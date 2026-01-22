<script lang="ts">
	import Checkbox from '$lib/FormElements/Checkbox.svelte';
	import TextBox from '$lib/FormElements/TextBox.svelte';
	import Button from '$lib/FormElements/Button.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces';
	import { errStore, loading } from '$lib/stores';
	import { enhance, applyAction } from '$app/forms';
	import type { SubmitFunction } from '@sveltejs/kit';
	import { openModal } from '$lib/utils';
	import type { Pergunta, Prova } from '$lib/types';
	import { TipoPerguntas } from '$lib/types';
	import EscolhaMultipla from './QuestoesElems/EscolhaMultipla.svelte';
	let submit: SubmitFunction = async function submit(form) {
		loading.set(true);
		return async ({ result, update }) => {
			loading.set(false);
			if (result.type === 'failure') {
				$errStore.error_title = result.data?.error_title;
				$errStore.error_message = result.data?.error_message;
				openModal();
			} else {
				await applyAction(result);
			}
		};
	};
	let buttons: IButton[] = [
		{
			label: '+Criar Escolha Multipla',
			isFormButton: true,
			formAction: '?/addEscolhaMultipla',
			style: 'btn-success'
		}
	];
	export let prova: Prova;
	export let versao: number;
	let perguntas: Pergunta[];
	let enumeratedPerguntas: { pergunta: Pergunta; index: number }[] = [];
	for (let i = 0; i < prova.versoes.length; i++) {
		if (prova.versoes[i].id === versao) {
			perguntas = prova.versoes[i].perguntas;
			for (let i = 1; i <= perguntas.length; i++) {
				enumeratedPerguntas.push({ pergunta: perguntas[i], index: i });
			}
			break;
		}
	}
	enumeratedPerguntas.push({
		index: enumeratedPerguntas.length + 1,
		pergunta: {
			id: -1,
			tipo: 0,
			Pergunta: {
				options: ['46wsj', 'taWENARW', '3N756YQ46', 'Y5WERTTNYt'],
				resposta_certa: 2,
				resposta_dada: -1
			},
			cotacao: 7,
			enunciado: 'wsu6hby'
		}
	});
	let createEscolhaMultipla = () => {
		let pergunta: Pergunta = {
			id: 90,
			cotacao: 20,
			tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
			enunciado: '',
			Pergunta: {
				options: ['', '', '', ''],
				resposta_certa: -1,
				resposta_dada: -1
			}
		};
		enumeratedPerguntas.push({ pergunta: pergunta, index: enumeratedPerguntas.length + 1 });
		enumeratedPerguntas = enumeratedPerguntas;
	};
</script>

<div class="w-full h-full">
	<div class="max-h-[calc(100%-80px)] overflow-auto flex flex-col gap-2">
		{#each enumeratedPerguntas as perguntaEnumerated}
			{#if perguntaEnumerated.pergunta.tipo == TipoPerguntas.ESCOLHA_MULTIPLA}
				<div class="w-full">
					<EscolhaMultipla
						perg={perguntaEnumerated.pergunta}
						order={perguntaEnumerated.index}
						allowEditing={true}
						Editing={perguntaEnumerated.pergunta.id >= 0}
                        versaoId={versao} 
                        provaId={prova.id}/>
					<div class="divider px-2 my-2" />
				</div>
			{/if}
		{/each}
	</div>
	<div class="flex justify-evenly justify-items-end h-[80px] gap-2">
		<div class="flex justify-around w-full">
			<div class="w-1/4">
				<Button isFormButton={false} css_type="btn-success" on:btnclicked={createEscolhaMultipla}>
					+Criar Escolha Multipla
				</Button>
			</div>
		</div>
	</div>
</div>
