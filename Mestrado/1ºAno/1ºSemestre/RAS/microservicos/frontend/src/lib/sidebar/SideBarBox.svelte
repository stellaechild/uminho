<script lang="ts">
    import { SidebarState } from "$lib/stores";
    import { SidebarTypes } from "$lib/types";
</script>


<div class="w-1/5 h-full bg-primary { $SidebarState === SidebarTypes.HIDDEN ? "hidden" : ""}">
    <slot />
</div>
