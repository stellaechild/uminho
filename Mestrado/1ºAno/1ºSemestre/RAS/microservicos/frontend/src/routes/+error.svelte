
<script lang="ts">
	import { page } from '$app/stores';
</script>


<div class="flex items-center justify-center h-full">
	<div class="h-4/5 w-5/6">
	  <div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
		<div class="h-[20%] flex flex-col items-center justify-center">
			<h1 class="text-8xl font-bold align-middle text-error">
			  Erro
			</h1>
		</div>
		<div class="h-[80%] w-full flex flex-col py-12 px-32">
			<div class="w-full">
				<h1 class="text-5xl px-40 mb-6">{$page.error?.title}</h1>
				<p class="text-xl px-32">{$page.error?.message}</p>
			</div>
			<div class="grow"></div>
			<a href={$page.error?.redirect} class="btn btn-neutral mx-20 my-8 h-14 btn-lg">Continue</a>
			<div class="grow"></div>
		</div>
	  </div>
	</div>
  </div>
