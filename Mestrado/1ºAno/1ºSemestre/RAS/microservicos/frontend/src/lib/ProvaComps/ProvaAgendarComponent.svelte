<script lang="ts">
	import Button from '$lib/FormElements/Button.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces';
	import { errStore, loading, novaProvaStore, ProvaStore } from '$lib/stores';
	import { enhance, applyAction } from '$app/forms';
	import type { SubmitFunction } from '@sveltejs/kit';
	import { openModal } from '$lib/utils';
	import type { Sala } from '$lib/types';
	let suggestions: { date: string; startime: string; endtime: string; salas: string[] }[] = [];
	let submit: SubmitFunction = async function submit(form) {
		loading.set(true);
		return async ({ result, update }) => {
			loading.set(false);
			if (result.type === 'failure') {
				$errStore.error_title = result.data?.error_title;
				$errStore.error_message = result.data?.error_message;
				openModal();
			} else if (result.type === 'success') {
				switch (form.action.search) {
					case '?/sugerirData':
						suggestions = result.data?.suggestions;
						break;
				}
			} else if (form.action.search === '?/createProva') {
				$ProvaStore.id = 5;
				$ProvaStore.name = $novaProvaStore.name;
				$ProvaStore.students = $novaProvaStore.students;
				$ProvaStore.versoes = [{ id: 1, perguntas: []}];
				$ProvaStore.randomize = false;
				$ProvaStore.blockReturn = false;
				let sugg = form.formData.get('agendamento') as string;
				let sug = JSON.parse(sugg) as {
					date: string;
					startime: string;
					endtime: string;
					salas: string[];
				};
				$ProvaStore.date = sug.date;
				$ProvaStore.starttime = sug.startime;
				$ProvaStore.endtime = sug.endtime;
				$ProvaStore.salas = sug.salas.map((sala) => {
					return { nome: sala, versao: 1 } as Sala;
				})
				await applyAction(result);
			} else {
				await applyAction(result);
			}
		};
	};
	let buttons: IButton[] = [
		{
			label: 'Proximo',
			isFormButton: true,
			formAction: '?/createProva',
			style: 'btn-success'
		}
	];
</script>

<form method="POST" class="flex flex-col h-full gap-2" use:enhance={submit}>
	<div class="flex items-end">
		<label class="form-control w-full pb-4">
			<div class="label">
				<span class="label-text"> Data e hora pretendidos para a prova </span>
			</div>
			<input
				type="datetime-local"
				class="input w-full border-primary border-[1px] focus:outline focus:outline-2 focus:outline-offset-2 focus:outline-primary"
				id="DataProvaInp"
				name="DataProva" />
		</label>
		<div class="h-[80px]">
			<Button isFormButton={true} FormAction="?/sugerirData" css_type="btn-primary">
				Pedir Sugestoes
			</Button>
		</div>
	</div>
	<input type="hidden" name="NomeProva" value={$novaProvaStore.name} />
	<input type="hidden" name="Alunos" value={JSON.stringify($novaProvaStore.students)} />
	{#if suggestions.length > 0}
		<div class="max-h-[calc(100%-80px)] overflow-auto">
			<table class="table">
				<thead>
					<tr>
						<th></th>
						<th>Data</th>
						<th>Hora Inicio</th>
						<th>Hora Fim</th>
						<th>Salas</th>
					</tr>
				</thead>
				<tbody>
					{#each suggestions as sug}
						<label for={JSON.stringify(sug)} class="table-row hover:bg-base-100/20 cursor-pointer">
							<td>
								<div class="">
									<input
										type="radio"
										class="radio"
										id={JSON.stringify(sug)}
										name="agendamento"
										value={JSON.stringify(sug)} />
								</div>
							</td>
							<td>{sug.date}</td>
							<td>{sug.startime}</td>
							<td>{sug.endtime}</td>
							<td>{sug.salas}</td>
						</label>
					{/each}
				</tbody>
			</table>
		</div>
		<div class="flex justify-evenly justify-items-end h-[80px] gap-4">
			{#each buttons as button}
				<div class="w-[40%] h-[80px]">
					<Button
						isFormButton={button.isFormButton}
						FormAction={button.formAction}
						css_type={button.style}>
						{button.label}
					</Button>
				</div>
			{/each}
		</div>
	{/if}
</form>
