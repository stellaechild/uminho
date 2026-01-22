<script lang="ts">
	import Checkbox from '$lib/FormElements/Checkbox.svelte';
	import TextBox from '$lib/FormElements/TextBox.svelte';
	import Button from '$lib/FormElements/Button.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces';
	import { errStore, loading, novaProvaStore } from '$lib/stores';
	import { enhance, applyAction } from '$app/forms';
	import type { SubmitFunction } from '@sveltejs/kit';
	import { openModal } from '$lib/utils';
	import type { Prova } from '$lib/types';
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
			label: 'Guardar',
			isFormButton: true,
			formAction: '?/save',
			style: 'btn-success'
		}
	];
    let handleEditVersao = (v: number) => {
        versao = v;
        state = "questoes";
    }
    export let state;
    export let versao: number;
	export let prova: Prova;
</script>

<form method="POST" class="flex flex-col h-full" use:enhance={submit}>
    <input type="hidden" name="id" value={prova.id}  />
    <input type="hidden" name="salas" value={JSON.stringify(prova.salas)}  />
	<div class="max-h-[calc(100%-80px)] overflow-auto flex flex-col gap-2">
		<TextBox name="versionQuantity" isNumber={true} minNumber={1} maxNumber={prova.salas.length || 1} text={prova.versoes.length}>
			Quantidade de versoes (tem que ser menor que quantidade de salas)
		</TextBox>
		<Checkbox name="randomize">Ordem de perguntas aleatoria</Checkbox>
		<Checkbox name="blockReturn">Bloquear retrocesso de perguntas</Checkbox>
        <div class="divider px-4">Editar questoes da versao</div>
        <div class="flex w-full px-4 items-center justify-between gap-2 self-center">
            {#each prova.versoes as v}
                <div class="w-1/5">
                    <Button
                        isFormButton={false}
                        on:btnclicked={() => handleEditVersao(v.id)}
                        css_type="btn-primary"
                    >
                        Versao {v.id}
                    </Button>
                </div>
            {/each}
        </div>
		<div class="divider px-4">Versoes de cada sala</div>
		{#each prova.salas as sala}
            <div class="flex w-1/2 h-[48px] px-4 items-center gap-2 self-center">
                <p class="font-bold grow">Sala: {sala.nome}</p>
                <p class="font-bold w-fit">Versao: </p>
                <label class="form-control w-1/4">
                    <select class="select select-primary select-bordered" name={sala.nome}>
                        {#each prova.versoes.map((v) => v.id) as versao}
                            <option value={versao}>{versao}</option>
                        {/each}
                    </select>
                </label>
            </div>
		{/each}
	</div>
	<div class="flex justify-evenly justify-items-end h-[80px] gap-4">
		{#each buttons as button}
			<div class="w-[40%] h-fit">
				<Button
					isFormButton={button.isFormButton}
					FormAction={button.formAction}
					css_type={button.style}>
					{button.label}
				</Button>
			</div>
		{/each}
	</div>
</form>
