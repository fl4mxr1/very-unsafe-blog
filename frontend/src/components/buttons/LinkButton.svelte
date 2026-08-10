<script lang="ts">
	import Button from "./Button.svelte";

	let {
		children,
		href = "#", 
		class: className = "", 
		primary = true,
		secondary = false,
		ghost = false,
		md = true,
		sm = false,
		...props
	} = $props();
</script>

<a
	{href}
	class="
	{className}
	{sm ? 'px-3 py-1' : md ? 'px-4 py-2' : ''}
	{ghost
		? 'bg-transparent'
		: secondary
			? 'border border-amber-300 text-amber-300 backdrop-blur-sm'
			: primary
				? 'bg-amber-300 text-black'
				: ''}
		cursor-pointer font-display text-xl flex flex-row items-center h-max
	"
	{...props}
>
	{@render children()}
</a>