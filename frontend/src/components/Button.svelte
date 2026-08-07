<script lang="ts">
	let {
		children,
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
	{sm ? 'px-3 py-1' : md ? 'px-4 py-2' : ''}
	{ghost
		? 'bg-transparent'
		: secondary
			? 'border border-gray-300 text-gray-600'
			: primary
				? 'bg-black text-white'
				: ''}
	rounded-sm cursor-pointer
	"
	{onclick}
	{...props}
>
	{@render children()}
</button>
