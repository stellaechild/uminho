<script>
    import { page } from '$app/stores';
</script>

  
<div class="card w-96 bg-neutral text-neutral-content">
    <div class="card-body items-center text-center">
        <h2 class="card-title">TODO!</h2>
        <p>Path = {$page.url.pathname}  </p>
    </div>
</div>