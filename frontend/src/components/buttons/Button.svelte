<script lang="ts">
	let {
		children,
		class: className = "", 
		onclick = null,
		primary = true,
		secondary = false,
		ghost = false,
		md = true,
		sm = false,
		...props
	} = $props();
</script>

<button
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
		cursor-pointer font-display text-xl flex flex-row items-center
	"
	{onclick}
	{...props}
>
	{@render children()}
</button>
