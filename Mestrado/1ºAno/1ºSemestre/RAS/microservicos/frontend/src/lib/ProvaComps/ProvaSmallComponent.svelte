<script lang="ts">
	import type { IButton } from '$lib/FormElements/tsInterfaces';
	import EasyBtn from '$lib/FormElements/EasyBtn.svelte';
	import { StatusProva, type Prova } from '$lib/types';
	let buttons = {
		edit: {
			label: 'Editar',
			isFormButton: true,
			formAction: '?/edit',
			style: 'btn-neutral'
		} as IButton,
		share: {
			label: 'Partilhar',
			isFormButton: true,
			formAction: '?/share',
			style: 'btn-neutral'
		} as IButton,
		start: {
			label: 'Comecar',
			isFormButton: true,
			formAction: '?/start',
			style: 'btn-neutral'
		} as IButton,
		end: {
			label: 'Terminar',
			isFormButton: true,
			formAction: '?/end',
			style: 'btn-neutral'
		} as IButton,
		publish: {
			label: 'Publicar classificacoes',
			isFormButton: true,
			formAction: '?/publish',
			style: 'btn-neutral'
		} as IButton,
		realizar: {
			label: 'Realizar prova',
			isFormButton: true,
			formAction: '?/realize',
			style: 'btn-neutral'
		} as IButton,
		resultados: {
			label: 'Resultados',
			isFormButton: true,
			formAction: '?/results',
			style: 'btn-neutral'
		} as IButton,
		hidden: {
			label: '',
			isFormButton: true,
			formAction: '?/results',
			style: 'hidden'
		} as IButton
	};
	let statusText = {
		[StatusProva.CRIADA]: 'Por comecar',
		[StatusProva.INICIADA]: 'Em Andamento',
		[StatusProva.TERMINADA]: 'Terminada',
		[StatusProva.PUBLICADA]: 'Publicada'
	};
	export let prova: Prova;
	export let isOwner: boolean;
</script>

<form method="post" class="card bg-base-100 w-full">
	<input type="hidden" name="id" value="{prova.id}" />
	<div class="pt-6">
		<div class="text-2xl font-bold pb-4 pl-24">
			{prova.name}
			{#if prova.status === StatusProva.CRIADA}
				<span class="text-base text-gray-500">({statusText[prova.status]})</span>
			{:else if prova.status === StatusProva.INICIADA}
				<span class="text-base text-yellow-500">({statusText[prova.status]})</span>
			{:else if prova.status === StatusProva.TERMINADA}
				<span class="text-base text-red-500">({statusText[prova.status]})</span>
			{:else if prova.status === StatusProva.PUBLICADA}
				<span class="text-base text-green-500">({statusText[prova.status]})</span>
			{/if}
		</div>
		<div class="pl-32 flex flex-col gap-[2px]">
			<p>
				Data: {prova.date}
			</p>
			<p>
				Hora de inicio: {prova.starttime}
			</p>
			<p>
				Hora de fim: {prova.endtime}
			</p>
			<p>
				Salas: {prova.salas.map((sala) => sala.nome).join('||')}
			</p>
		</div>
	</div>
	<div class="flex">
		{#if isOwner}
			{#if prova.status === StatusProva.CRIADA}
				<EasyBtn details={buttons.start} />
			{:else if prova.status === StatusProva.INICIADA}
				<EasyBtn details={buttons.end} />
			{:else if prova.status === StatusProva.TERMINADA}
				<EasyBtn details={buttons.publish} />
			{/if}
			<EasyBtn details={buttons.edit} />
			<EasyBtn details={buttons.share} />
		{:else if prova.status === StatusProva.PUBLICADA}
			<EasyBtn details={buttons.resultados} />
		{:else if prova.status === StatusProva.INICIADA}
			<EasyBtn details={buttons.realizar} />
		{:else}
			<EasyBtn details={buttons.hidden} />
		{/if}
	</div>
</form>
