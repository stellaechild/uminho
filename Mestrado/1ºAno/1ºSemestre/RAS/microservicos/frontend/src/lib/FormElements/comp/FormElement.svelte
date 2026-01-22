<script lang="ts">
    import {type IFormElement, FormElementTypes} from "../tsInterfaces";
    import Checkbox from "$lib/FormElements/Checkbox.svelte";
    import FileInput from "$lib/FormElements/FileInput.svelte";
    import TextBox from "$lib/FormElements/TextBox.svelte";
    import MultilineTextBox from "$lib/FormElements/MultilineTextBox.svelte";
    export let form_element: IFormElement;
</script>

{#if form_element.type === FormElementTypes.CHECKBOX}
    <Checkbox name={form_element.name} bind:checked={form_element.value} readonly={form_element.readonly}>
        {form_element.label}
    </Checkbox>
{:else if form_element.type === FormElementTypes.FILE}
    <FileInput name={form_element.name} bind:files={form_element.value} readonly={form_element.readonly}>
        {form_element.label}
    </FileInput>
{:else if form_element.type === FormElementTypes.TEXT}
    <TextBox name={form_element.name} bind:text={form_element.value} readonly={form_element.readonly} obfuscated={form_element.obfuscated}>
        {form_element.label}
    </TextBox>
{:else if form_element.type === FormElementTypes.TEXTAREA}
    <MultilineTextBox name={form_element.name} bind:text={form_element.value} readonly={form_element.readonly}>
        {form_element.label}
    </MultilineTextBox>
{:else if form_element.type === FormElementTypes.HIDDEN}
    <input type="hidden" name={form_element.name} bind:value={form_element.value} />
{/if}