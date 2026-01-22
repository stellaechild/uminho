<script lang="ts">
    export let css_type = "input-primary";
	export let name = "";
    export let placeholder_object = "...";
    export let text: string | number = "";
	export let readonly = false;
	export let obfuscated = false;
	export let isNumber = false;
	export let minNumber = 0;
	export let maxNumber = 1;
	export let showHeader = true;
</script>

<div class="px-4">
	<label class="form-control w-full">
		{#if showHeader}
			<div class="label">
				<span class="label-text">
					<slot>
						<span>Default text input label text. CHANGE THIS</span>
					</slot>
				</span>
			</div>
		{/if}
		{#if obfuscated}
			<input type="password" name={name} placeholder="Type here {placeholder_object}" class="input input-bordered {css_type} w-full" bind:value={text} disabled={readonly}/>
		{:else if isNumber}
			<input type="number" min={minNumber} max={maxNumber} name={name} placeholder="Type here {placeholder_object}" class="input input-bordered {css_type} w-full" bind:value={text} disabled={readonly}/>
		{:else}
			<input type="text" name={name} placeholder="Type here {placeholder_object}" class="input input-bordered {css_type} w-full" bind:value={text} disabled={readonly}/>
		{/if}
	</label>
</div>
