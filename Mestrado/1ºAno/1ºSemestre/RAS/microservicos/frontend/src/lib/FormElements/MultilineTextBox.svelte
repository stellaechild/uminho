<script lang="ts">
    export let css_type = "textarea-primary";
	export let name = "";
    export let placeholder_object = "...";
    export let text = "";
	export let readonly = false;
</script>

<div class="px-4">
	<label class="form-control w-full">
		<div class="label">
			<span class="label-text">
				<slot>
					<span>Default text input label text. CHANGE THIS</span>
				</slot>
			</span>
		</div>
		<textarea placeholder="Type here {placeholder_object}" name={name} class="textarea textarea-bordered {css_type} w-full" bind:value={text} disabled={readonly}/>
	</label>
</div>
