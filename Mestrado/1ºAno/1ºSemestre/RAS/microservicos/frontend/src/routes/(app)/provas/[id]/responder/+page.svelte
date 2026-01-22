<script lang="ts">
  import { SidebarState } from "$lib/stores";
  import { SidebarTypes } from "$lib/types";
  $SidebarState = SidebarTypes.PROVAS_CONSULTAR;
  import Button from "$lib/FormElements/Button.svelte";
  export let data;
  let allowGoBack = !data.prova.blockReturn;
  let perguntasId:number[] = [];
  for (let i = 0; i < data.prova.versoes.length; i++) {
    if (data.prova.versoes[i].id == data.versaoId) {
      for (let j = 0; j < data.prova.versoes[i].perguntas.length; j++) {
        perguntasId.push(j + 1);
      }
    }
  }
  let currentPergunta = 1;
  let perguntas = data.prova.versoes[0].perguntas;
  let clickBtn = (i:number) => {
    if (allowGoBack || i > currentPergunta) {
      currentPergunta = i;
    }
  }
  let success = false;
</script>

<div class="flex flex-col items-center justify-center h-full">
  <div class="h-[10%] w-5/6 flex items-end">
    <div class="w-full  flex justify-center items-center">
      <div class="text-sm breadcrumbs">
        <ul>
          {#each perguntasId as i}
            <li>
              <button on:click={() => clickBtn(i)} class="btn btn-sm {(currentPergunta == i ? "btn-primary": "btn-neutral")}" disabled={!allowGoBack && currentPergunta > i}>
                {i}
              </button>
            </li>
          {/each}
        </ul>
      </div>
    </div>
  </div>
	<div class="h-[90%] w-5/6 pt-8 pb-16">
    <div class="h-full w-full">
      <div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
        <div class="h-full w-full flex flex-col justify-center items-center">
          <div class="w-full h-full flex flex-col overflow-auto px-2">
            <form method="post" class="h-full pt-8 max-w-full">
              {perguntas[currentPergunta - 1].enunciado}
              {#each perguntas[currentPergunta - 1].Pergunta.options as option, index}
                <div class="flex gap-2 w-full pt-4 px-4">
                  <input
                    type="radio"
                    value={index}
                    class="radio radio-primary"
                    name="resposta"
                    id="resposta"
                    required
                  />
                  R{index + 1}:
                  <div class="w-4/5">
                    {option}
                  </div>
                </div>
              {/each}
              <div class="w-1/6">
                <Button isFormButton={true} FormAction="?/submit" css_type="btn-primary btn-small">
                  Submeter
                </Button>
              </div>
              {#if success}
                Resposta submetida com sucesso!
              {/if}
            </form>
          </div>
        </div>
      </div>
		</div>
	</div>
</div>

