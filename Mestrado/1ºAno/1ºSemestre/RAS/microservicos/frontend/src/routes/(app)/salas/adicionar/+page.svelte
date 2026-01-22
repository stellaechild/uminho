<script lang="ts">
  import FormComponent from "$lib/FormElements/comp/FormComponent.svelte";
  import { type IFormElement, type IButton, FormElementTypes } from "$lib/FormElements/tsInterfaces";
  let fields: IFormElement[] = [
    {
      name: "nomeSala",
      type: FormElementTypes.TEXT,
      label: "Nome da Sala a Adicionar",
      value: "",
      required: true,
    },
  ];
  let buttons: IButton[] = [
    {
      label: "Submit",
      isFormButton: true,
      formAction: "?/Adicionar",
      style: "btn-secondary",
    },
  ];
</script>
<div class="flex items-center justify-center h-full">
  <div class="h-4/5 w-5/6">
    <FormComponent questions={fields} buttons={buttons}>
      Adicionar Salas
    </FormComponent>
  </div>
</div>
