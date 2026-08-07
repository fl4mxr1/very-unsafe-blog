<script lang="ts">
	import { getRelativeTime } from "@feelinglovelynow/get-relative-time";
	let { id, name, postedBy, postedAtTimestamp } = $props();
</script>

<a href="/post?id={id}" class="w-full px-2 py-1 odd:bg-gray-100">
	<h3 class="text-2xl font-display font-bold -mb-1">{name}</h3>
	<ul
		class="flex flex-row gap-1.5 *:not-last:after:content-['⋄'] *:not-last:after:ml-1.5 *:not-last:after:text-gray-400 text-md text-gray-600 font-serif font-medium"
	>
		<li>
			Posted by <span class="font-bold text-black">{postedBy}</span>
		</li>
		<li>{getRelativeTime(new Date(postedAtTimestamp))}</li>
	</ul>
</a>
