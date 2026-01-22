<script lang="ts">
    import ProvaInicialComponent from "./ProvaInicialComponent.svelte";
    import ProvaAgendarComponent from "./ProvaAgendarComponent.svelte";
    import ProvaDetalhesComponent from "./ProvaDetalhesComponent.svelte";
	import type { Prova } from "$lib/types";
	import QuestoesComponent from "./QuestoesComponent.svelte";
    export let state: string;
    const statesTitles: { [key: string]: string } = {
        start: 'comeco',
        agendamento: 'agendamento',
        detalhes: 'detalhes',
        questoes: 'Gerir Questoes',
    }
    export let prova: Prova = {
        id: 0,
        name: '',
        date: '',
        starttime: '',
        endtime: '',
        salas: [],
        students: [],
        versoes: [],
        randomize: false,
        blockReturn: false,
        status: 0,
    };
    let versaoPraEditar = -1;

</script>

<div class="flex items-center justify-center h-full">
    <div class="h-4/5 w-5/6">
        <div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
            <div class="h-[15%] flex flex-col items-center justify-center">
				<h1 class="text-5xl font-bold align-middle">Editar Prova - {statesTitles[state]}</h1>
			</div>
			<div class="h-[85%] w-full flex flex-col justify-center">
				{#if state === 'start'}
                    <ProvaInicialComponent bind:state={state} />
                {:else if state === 'agendamento'}
                    <ProvaAgendarComponent />
                {:else if state === 'detalhes'}
                    <ProvaDetalhesComponent prova={prova} bind:state={state} bind:versao={versaoPraEditar}/>
                {:else if state === 'questoes'}
                    <QuestoesComponent prova={prova} versao={versaoPraEditar}/>
                {/if}
			</div>
		</div>
	</div>
</div>
