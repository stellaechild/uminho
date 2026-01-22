<script lang="ts">
  import { SidebarState } from "$lib/stores";
  import { SidebarTypes } from "$lib/types";
  $SidebarState = SidebarTypes.PROVAS_CORRIGIR;
  import FormComponent from "$lib/FormElements/comp/FormComponent.svelte";
  import { type IFormElement, type IButton, FormElementTypes } from "$lib/FormElements/tsInterfaces";
  let fields: IFormElement[] = [
    {
      name: "email",
      type: FormElementTypes.TEXT,
      label: "Email",
      value: "",
      required: true,
    }
  ];
  let buttons: IButton[] = [
    {
      label: "Cancelar",
      isFormButton: true,
      formAction: "?/cancel",
      style: "btn-error",
    },
    {
      label: "Partilhar",
      isFormButton: true,
      formAction: "?/share",
      style: "btn-success",
    },
  ];
</script>
<div class="flex items-center justify-center h-full">
  <div class="h-4/5 w-5/6">
    <FormComponent questions={fields} buttons={buttons}>
      Partilhar
    </FormComponent>
  </div>
</div>
