<script lang="ts">
	import ProvaSmallComponent from '$lib/ProvaComps/ProvaSmallComponent.svelte';
	import EasyBtn from '$lib/FormElements/EasyBtn.svelte';
	import type { IButton } from '$lib/FormElements/tsInterfaces.js';
	let btn = {
		label: 'Criar Prova',
		isFormButton: true,
		formAction: '?/create',
		style: 'btn-primary'
	} as IButton;
	export let data;
</script>

<div class="flex items-center justify-center h-full">
	<div class="h-4/5 w-5/6">
		<div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
			<div class="h-[15%] flex flex-col items-center justify-center">
				<h1 class="text-5xl font-bold align-middle">Provas</h1>
			</div>
			<div class="h-[85%] w-full flex flex-col justify-center items-center">
				<div class="w-full max-h-[100%-80px] flex flex-col gap-2 overflow-auto px-2">
					{#each data.provas as prova}
						<ProvaSmallComponent {prova} isOwner={data.isOwner} />
					{/each}
				</div>
				{#if data.isOwner}
					<form method="post" class="w-1/3 h-[80px]">
						<EasyBtn details={btn} />
					</form>
				{/if}
			</div>
		</div>
	</div>
</div>