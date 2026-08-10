<script lang="ts">
	import { getRelativeTime } from "@feelinglovelynow/get-relative-time";

	let { title = null, author = null, postedAt = null, ...other } = $props();
</script>

<header {...other}>
	<h1
		lang="en"
		class="text-7xl max-w-full font-display text-wrap hyphens-auto font-light mb-1 transition-colors duration-200 {(title &&
			'bg-transparent text-amber-300') ||
			'h-lh w-88 bg-amber-300 text-black'}"
	>
		{title || "Loading..."}
	</h1>
	Posted
	<i class=""
		>{(postedAt && getRelativeTime(new Date(postedAt as number))) || "..."}
	</i>
	by
	<b>{author || "...?"}</b>
	<hr class="mt-2" />
</header>
