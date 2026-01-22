<script lang="ts">
	import SideBarBox from './SideBarBox.svelte';
	import { ProvaStore } from '$lib/stores';
	import { SidebarState } from '$lib/stores';
	import { SidebarTypes, type Sala } from '$lib/types';
	import Button from '$lib/FormElements/Button.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces';
    let buttons: IButton[] = [
		{
			label: 'Guardar',
			isFormButton: true,
			formAction: '?/save',
			style: 'btn-success'
		}
	];
</script>

<SideBarBox>
	<div class="flex flex-col h-full items-center px-4 pt-4 gap-2">
		<div class="text-xl font-bold">
			{#if $SidebarState === SidebarTypes.PROVAS_EDITAR}
				Editar Prova
			{:else if $SidebarState === SidebarTypes.PROVAS_CONSULTAR}
				{$ProvaStore.name}
			{:else if $SidebarState === SidebarTypes.PROVAS_CORRIGIR}
                Corrigir Prova
            {/if}
		</div>
		<div class="flex w-full gap-1">
			<p class="font-bold">Nome:</p>
			<p>{$ProvaStore.name}</p>
		</div>
		<div class="w-full">
			<p class="font-bold">Salas:</p>
			<ul class="list-disc pl-8">
				{#each $ProvaStore.salas as sala}
					<li>{sala.nome}</li>
				{/each}
			</ul>
		</div>
		<div class="flex w-full gap-1">
			<p class="font-bold">Data:</p>
			<p>{$ProvaStore.date}</p>
		</div>
		<div class="flex w-full gap-1">
			<p class="font-bold">Hora de inicio:</p>
			<p>{$ProvaStore.starttime}</p>
		</div>
		<div class="flex w-full gap-1">
			<p class="font-bold">Hora de fim:</p>
			<p>{$ProvaStore.endtime}</p>
		</div>
		<div class="grow"></div>
		<div class="h-[80px] w-full">
            <form method="post">
                <Button isFormButton={true} FormAction="?/back" css_type="btn-warning">Voltar</Button>
            </form>
		</div>
	</div>
</SideBarBox>
