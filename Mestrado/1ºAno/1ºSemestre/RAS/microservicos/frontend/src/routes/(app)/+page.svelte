<script lang="ts">
	import { UserTypes } from "$lib/types.js";
  export let data;
  let role = "";
  switch (data.type) {
    case UserTypes.TECNICO:
      role = "Tecnico";
      break;
    case UserTypes.ESTUDANTE:
      role = "Estudante";
      break;
    case UserTypes.DOCENTE:
      role = "Docente";
      break;
  }
</script>

<div class="flex flex-col items-center justify-center h-full">
  <p class="text-6xl font-bold">Bemvindo {role} {data.email}</p>
</div>
