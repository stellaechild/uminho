<script>
	import Button from "$lib/FormElements/Button.svelte";

	export let data;
	let salas = data.salas;
</script>

<div class="flex items-center justify-center h-full">
	<div class="h-4/5 w-5/6">
		<div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
			<div class="h-[15%] flex flex-col items-center justify-center">
				<h1 class="text-5xl font-bold align-middle">Eliminar Salas</h1>
			</div>
			<div class="h-[85%] w-full flex flex-col gap-4">
				{#each salas as sala}
					<div class="w-full h-16 border-2 rounded-xl border-neutral flex items-center">
						<p class="ml-8 text-xl font-bold">{sala}</p>
            <div class="grow" />
            <div class="w-fit">
              <form method="POST">
                <input type="hidden" name="nomeSala" value={sala}>
                <Button isFormButton={true} FormAction="?/remover" css_type="btn-error">
                  Remover
                </Button>
              </form>
            </div>
            <!-- form per button ...  use an input with default value or the  request -->
					</div>
				{/each}
			</div>
		</div>
	</div>
</div>
