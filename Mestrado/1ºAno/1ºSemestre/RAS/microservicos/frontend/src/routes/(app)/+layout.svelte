<script>
    import SideBar from "$lib/sidebar/SideBar.svelte";
    import SideBarDetalhesProva from "$lib/sidebar/SideBarDetalhesProva.svelte";
    import { SidebarState } from "$lib/stores";
    import { SidebarTypes } from "$lib/types";
    export let data;
</script>

<div class="flex w-full h-full">
    {#if $SidebarState == SidebarTypes.NORMAL || $SidebarState == SidebarTypes.HIDDEN}
        <SideBar type={data.type}/>
    {:else}
        <SideBarDetalhesProva />
    {/if}
    <div class="w-full h-full">
        <slot />
    </div>
</div>
