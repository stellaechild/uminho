<script lang="ts">
  import { SidebarState } from "$lib/stores";
  import { SidebarTypes } from "$lib/types";
  $SidebarState = SidebarTypes.PROVAS_EDITAR;
  import ProvaComponent from "$lib/ProvaComps/ProvaComponent.svelte";
  import { ProvaStore } from "$lib/stores.js";
  export let data;
  $ProvaStore = data.prova
</script>

<ProvaComponent state="detalhes" prova={data.prova}/>