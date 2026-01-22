<script>
    export let css_type = "checkbox-primary";
    export let name = "";
    export let checked = false;
    export let readonly = false;
    let pointer = readonly ? "cursor-not-allowed" : "cursor-pointer";
</script>

<div class="px-4">
    <div class="form-control">
        <label class="flex justify-between items-center h-12 bg-base-100 px-4 rounded-lg shadow-sm border-primary border-[1px] focus-within:outline focus-within:outline-2 focus-within:outline-offset-2 outline-primary {pointer}">
            <span class="label-text"><slot /></span>
            <input type="checkbox" name={name} class="checkbox {css_type}" bind:checked disabled={readonly} >
        </label>
    </div>
</div>