<script lang="ts">
    export let link: string;
</script>

<div class="w-full px-4 py-2">
    <a href={link} class="btn btn-ghost btn-sm h-16 w-full">
        <slot>
            <span>Default button text. CHANGE THIS</span>
        </slot>
    </a>
</div>