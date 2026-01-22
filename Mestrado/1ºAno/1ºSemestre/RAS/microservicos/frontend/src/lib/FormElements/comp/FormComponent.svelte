<script lang="ts">
	import Button from '$lib/FormElements/Button.svelte';
	import type { IFormElement, IButton } from '$lib/FormElements/tsInterfaces';
	import FormElement from '$lib/FormElements/comp/FormElement.svelte';
	export let questions: IFormElement[] = [];
	export let buttons: IButton[] = [];
</script>


<div class="card w-full h-full bg-base-300/70 shadow-2xl px-24">
    <div class="h-[10%] flex flex-col items-center justify-center">
        <h1 class="text-4xl font-bold align-middle">
            <slot />
        </h1>
    </div>
    <form method="POST" class="flex flex-col justify-between justify-items-end h-[90%] gap-4 pb-4">
        <div class="grow"></div>
        <div class="flex flex-col min-h-fit overflow-y-auto gap-2 pb-1">
            {#each questions as question}
                <FormElement bind:form_element={question} />
            {/each}
        </div>
        <div class="flex min-h-[80px]">
            {#each buttons as button}
            <Button isFormButton={button.isFormButton} FormAction={button.formAction} css_type={button.style}>
                {button.label}
            </Button>
            {/each}
        </div>
        <div class="grow" />
    </form>
</div>
