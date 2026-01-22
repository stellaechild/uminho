<script>
    import "../app.css";
    import { loading } from "$lib/stores";
    import PopUpError from "$lib/PopUpError.svelte";
</script>
{#if $loading}
    <div class="w-screen h-screen absolute backdrop-blur z-50">    
        <span class="loading loading-spinner w-20 text-primary absolute z-[51] top-[calc(50%-2.5rem)] left-[calc(50%-2.5rem)]"></span>
    </div>
{/if}
<PopUpError/>
<div class="w-screen h-screen">
    <slot />
</div>
