<script lang="ts">
	import { getRelativeTime } from "@feelinglovelynow/get-relative-time";
	import { ChevronForwardW300 } from "@material-symbols-svg/svelte/icons/chevron-forward";
	import { ShareW300 } from "@material-symbols-svg/svelte/icons/share";
	import Button from "../components/buttons/Button.svelte"
	let { id, title, author, preview, postedAtTimestamp } = $props();
</script>

<div
	class="w-full px-2 py-1 border transition-colors border-amber-300 hover:bg-amber-300 hover:text-black focus:bg-amber-300 focus:text-black group flex flex-row"
>
	<div class="flex flex-col grow max-w-full">
		<header class="flex flex-row gap-1 grow transition-all">
			<a class="flex flex-row gap-1 items-end grow" href="/post?id={id}">
				<div
					class="flex items-center self-stretch transition-all w-0 group-hover:w-7 group-focus:w-7"
				>
					<ChevronForwardW300 />
				</div>
				<h2 class="-mb-2 -mt-1 mr-2 -ml-1">{title}</h2>
				Posted
				<i>{getRelativeTime(new Date(postedAtTimestamp))}</i>
				by
				<b>{author}</b>
			</a>
			<Button aria-label="Share" ghost class="px-0!" onclickcapture={() => {
				navigator.clipboard.writeText(`https://localhost:5173/post?id=${id}`); // only issue is that this website isn't hosted and doesn't have a domain name..
			}}>
				<ShareW300 />
			</Button>
		</header>
		<article
			class="text-nowrap mask-r-from-black mask-r-from-95% mask-r-to-transparent"
		>
			{preview}
		</article>
	</div>
</div>
