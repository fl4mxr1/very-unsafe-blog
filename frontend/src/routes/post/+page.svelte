<script lang="ts">
	import Post from "$lib/backend/post";
	import { marked } from "marked";
	import { getRelativeTime } from "@feelinglovelynow/get-relative-time";
	import { onMount } from "svelte";

	let id: string | null = $state("");
	let post: Post | null = $derived(id != "" ? new Post(id) : null);
	$inspect(post);

	onMount(() => {
		const urlParams = new URLSearchParams(window.location.search);
		id = urlParams.get("id");
	});
</script>

<!-- TODO: Make it semantic -->

<div>
	{#if post != null}
		{#await post.fetchPostData()}
			<p>Loading post...</p>
		{:then post: Post | undefined}
			{#if post != undefined}
				<h1 class="text-6xl font-display font-black">
					{post.title}
				</h1>
				<ul
					class="flex flex-row gap-2 *:not-last:after:content-['⋄'] *:not-last:after:ml-2 *:not-last:after:text-gray-400"
				>
					<li class="text-lg font-serif">
						<span class="text-gray-600">Posted by</span>
						<span class="font-bold">{post.author}</span>
					</li>
					<li class="text-lg font-serif text-gray-600">
						{getRelativeTime(new Date(post.postedAt as number))}
					</li>
				</ul>
				<hr class="my-3 text-gray-300" />
				{#await post.fetchContent()}
					<p>Fetching post content...</p>
				{:then postContent: string | undefined}
					{#if postContent != undefined}
						{@html marked(postContent)}
					{:else}
						<p>
							Could not get post content.. Please reload the page.
						</p>
					{/if}
				{/await}
			{:else}
				<p>Could not get post data... Please reload the page.</p>
			{/if}
		{:catch error}
			{error}
		{/await}
	{:else}
		<p>Loading post...</p>
	{/if}
</div>
