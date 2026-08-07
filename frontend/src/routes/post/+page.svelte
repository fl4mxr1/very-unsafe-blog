<script lang="ts">
	import {
		getPostContentFromId,
		getPostDataFromId,
		type PostData,
	} from "$lib/backend-lib";
	import { marked } from "marked";
	import { getRelativeTime } from "@feelinglovelynow/get-relative-time";
	import { onMount } from "svelte";

	let id: string | null = $state("");
	let postData = $derived(getPostDataFromId(id));
	let postContent = $derived(getPostContentFromId(id));

	onMount(() => {
		const urlParams = new URLSearchParams(window.location.search);
		id = urlParams.get("id");
	});
</script>

<div>
	{#if id && postData && postContent}
		{#await postData}
			<h1 class="text-6xl font-display font-black">Loading data...</h1>
		{:then data}
			<h1 class="text-6xl font-display font-black">{data.title}</h1>
			<ul
				class="flex flex-row gap-2 *:not-last:after:content-['⋄'] *:not-last:after:ml-2 *:not-last:after:text-gray-400"
			>
				<li class="text-lg font-serif">
					<span class="text-gray-600">Posted by</span>
					<span class="font-bold">{data.author}</span>
				</li>
				<li class="text-lg font-serif text-gray-600">
					{getRelativeTime(new Date(data.postedAt as number))}
				</li>
			</ul>
		{:catch error}
			{error}
		{/await}
		<hr class="my-3 text-gray-300" />
		{#await postContent}
			<p class="font-serif">Loading content...</p>
		{:then content}
			<p class="text-justify font-serif">
				{@html marked(content)}
			</p>
		{:catch error}
			{error}
		{/await}
	{/if}
</div>
