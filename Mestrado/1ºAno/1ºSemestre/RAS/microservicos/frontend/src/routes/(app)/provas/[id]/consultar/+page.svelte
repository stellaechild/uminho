<script lang="ts">
  import { errStore, loading } from "$lib/stores";
  $loading = true;
  import { SidebarState } from "$lib/stores";
  import { SidebarTypes, type Pergunta } from "$lib/types";
  $SidebarState = SidebarTypes.PROVAS_CONSULTAR;
  import { type IButton, FormElementTypes } from "$lib/FormElements/tsInterfaces";

  let buttons: IButton[] = [
    {
      label: "Cancelar",
      isFormButton: true,
      formAction: "?/cancel",
      style: "btn-error",
    },
    {
      label: "Partilhar",
      isFormButton: true,
      formAction: "?/share",
      style: "btn-success",
    },
  ];
  export let data;
  import { ProvaStore } from "$lib/stores";
  $ProvaStore = data.prova;
  $loading = false;
  let perguntas: Pergunta[] = []
  for (let i = 0; i < data.prova.versoes.length; i++) {
    if (data.prova.versoes[i].id == data.versaoId) {
      perguntas = data.prova.versoes[i].perguntas;
    }
  }
  let cotacaoTotal = 0;
  for (let i = 0; i < perguntas.length; i++) {
    if (perguntas[i].Pergunta.resposta_certa == perguntas[i].Pergunta.resposta_dada) {
      cotacaoTotal += perguntas[i].cotacao;
    }
  }
</script>
<div class="flex items-center justify-center h-full">
	<div class="h-4/5 w-5/6">
		<div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
			<div class="h-[15%] flex flex-col items-center justify-center">
				<h1 class="text-5xl font-bold align-middle">Consulta Prova - Nota: {cotacaoTotal}</h1>
			</div>
			<div class="h-[85%] w-full flex flex-col justify-center items-center">
				<div class="w-full max-h-[100%] flex flex-col gap-2 overflow-auto px-2">
					{#each perguntas as perg, index}
          <div class="divider px-2 my-2" />
          <form method="post" class="w-full flex flex-col gap-2">
            <input type="hidden" name="id" value={perg.id} />
            <input type="hidden" name="tipo" value={perg.tipo} />
              <input type="hidden" name="versaoId" value={data.versaoId} />
              <input type="hidden" name="provaId" value={data.provaId} />
            <div class="flex flex-row justify-between items-center text-lg font-bold">
              <p class="grow">
                Questao {index + 1}
              </p>
              <div class="flex items-center gap-2 grow">
                Cotacao:{ perg.Pergunta.resposta_certa == perg.Pergunta.resposta_dada ? perg.cotacao : 0}
              </div>
            </div>
            {perg.enunciado}
            {#each perg.Pergunta.options as option, index}
                <div class="flex items-center justify-center gap-2 w-full px-8 h-12">
                  <input
                    type="radio"
                    name="resposta"
                    value={index}
                    class="radio {perg.Pergunta.resposta_certa == index ? "radio-success" : "radio-error"} disabled:opacity-100"
                    disabled={true} checked={index == perg.Pergunta.resposta_dada} />
                  <div class="w-full flex gap-1 {perg.Pergunta.resposta_certa == index ? "text-success" : "text-error"}">
                    R{index + 1}:
                    <div class="w-4/5">
                      {option}
                    </div>
                  </div>
                </div>
            {/each}
            <div class="flex justify-center items-center gap-2">
              Resposta Correta:
                <div class="form-control w-2/3">
                  R{perg.Pergunta.resposta_certa}
                </div>
            </div>
          </form>
          {/each}
				</div>
			</div>
		</div>
	</div>
</div>