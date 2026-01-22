<script>
    export let css_type = "btn-primary";
    export let isFormButton = false;
    export let FormAction = "submit";

    import { createEventDispatcher } from 'svelte';
    const dispatch = createEventDispatcher();
    function handleClick() {
        dispatch('btnclicked', {});
    }
</script>

<div class="w-full h-full px-4 py-4">
    {#if isFormButton}
        <button class="w-full h-full btn {css_type}" formaction={FormAction}>
            <slot />
        </button>
    {:else}
        <button class="w-full h-full btn {css_type}" on:click={handleClick}>
            <slot />
        </button>
    {/if}
</div>