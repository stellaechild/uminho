<script lang="ts">
    export let css_type = "file-input-primary";
	export let name = "";
    export let files: FileList | null = null;
	export let readonly = false;
    let pointer = readonly ? "cursor-not-allowed" : "cursor-pointer";
</script>

<div class="px-4">
	<label class="form-control w-full {pointer}">
		<div class="label">
			<span class="label-text">
				<slot>
					<span>Default file input text. CHANGE THIS</span>
				</slot>
			</span>
		</div>
		<input type="file" name={name} class="file-input file-input-bordered {css_type} w-full" bind:files disabled={readonly} />
	</label>
</div>
