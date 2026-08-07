<script lang="ts">
	let { children, ...rest } = $props();
</script>

<div class="flex flex-col rounded-sm overflow-hidden" {...rest}>
	{@render children()}
</div>
