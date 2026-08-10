<script lang="ts">
	let { children, ...rest } = $props();
</script>

<div class="flex flex-col gap-2" {...rest}>
	{@render children()}
</div>
