<script lang="ts">
	import Button from "$lib/FormElements/Button.svelte";
  import type {IButton} from "$lib/FormElements/tsInterfaces";
  let buttons: IButton[] = [
    {
      label: "Adicionar",
      isFormButton: true,
      formAction: "?/redirectAdicionar",
      style: "btn-success",
    },
    {
      label: "Remover",
      isFormButton: true,
      formAction: "?/redirectRemover",
      style: "btn-error",
    },
  ];
</script>


<div class="flex items-center justify-center h-full">
  <div class="h-4/5 w-5/6">
    <div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
      <div class="h-[15%] flex flex-col items-center justify-center">
          <h1 class="text-5xl font-bold align-middle">
            Gestao de Salas
          </h1>
      </div>
      <div class="h-[85%] w-full flex flex-col justify-center">
        <form method="POST" class="flex justify-evenly justify-items-end h-1/3 gap-4 pb-4">
          {#each buttons as button}
          <div class="w-[40%]">
            <Button isFormButton={button.isFormButton} FormAction={button.formAction} css_type={button.style}>
              {button.label}
            </Button>
          </div>
          {/each}
        </form>
      </div>
    </div>
  </div>
</div>

