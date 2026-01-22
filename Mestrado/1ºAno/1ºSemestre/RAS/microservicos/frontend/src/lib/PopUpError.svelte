<script>
	import { errStore } from "./stores";
</script>

<dialog id="error_modal" class="modal">
	<div class="modal-box">
		<form method="dialog">
			<button class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2">✕</button>
		</form>
		<h3 class="font-bold text-lg">Erro: {$errStore.error_title}</h3>
		<p class="py-4">{$errStore.error_message}</p>
	</div>
</dialog>
