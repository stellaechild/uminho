<script lang="ts">
  import FormComponent from "$lib/FormElements/comp/FormComponent.svelte";
  import { type IFormElement, type IButton, FormElementTypes } from "$lib/FormElements/tsInterfaces";
  export let data;
  let fields: IFormElement[] = [
    {
      name: "email",
      type: FormElementTypes.TEXT,
      label: "Email",
      value: data.email,
      required: true,
    },
    {
      name: "old-password",
      type: FormElementTypes.TEXT,
      label: "Palavra-passe antiga",
      value: "",
      obfuscated: true,
      required: true,
    },
    {
      name: "password",
      type: FormElementTypes.TEXT,
      label: "Palavra-passe Nova",
      value: "",
      obfuscated: true,
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
      label: "Atualizar",
      isFormButton: true,
      formAction: "?/update",
      style: "btn-success",
    },
  ];
</script>
<div class="flex items-center justify-center h-full">
  <div class="h-4/5 w-5/6">
    <FormComponent questions={fields} buttons={buttons}>
      Perfil
    </FormComponent>
  </div>
</div>