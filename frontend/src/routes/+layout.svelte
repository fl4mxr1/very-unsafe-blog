<script lang="ts">
	import "./layout.css";
	import favicon from "$lib/assets/favicon.svg";

	let { children } = $props();
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>
<div class="w-9/10 max-w-150 absolute top-0 left-1/2 -translate-x-1/2">
	<div
		class="sticky top-0 left-0 w-full bg-white border-b border-gray-300 py-2 flex flex-row"
	>
		<a class="text-4xl font-display font-black select-none" href="/">
			VeryUnsafeBlog
			<span class="text-lg -ml-1 font-display">.COM</span>
		</a>
	</div>
	<div class="w-full pt-4">
		{@render children()}
	</div>
</div>
