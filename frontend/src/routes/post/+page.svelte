<script lang="ts">
	import Post from "$lib/backend/post.svelte";
	import { marked } from "marked";
	import { getRelativeTime } from "@feelinglovelynow/get-relative-time";
	import { onMount } from "svelte";
	import { fade } from "svelte/transition";
	import { page } from "$lib/page.svelte";

	import PostHeader from "../../components/post/PostHeader.svelte";

	let id: string | null = $state("");
	let post: Post | null = $derived(id != "" ? new Post(id) : null);

	onMount(() => {
		const urlParams = new URLSearchParams(window.location.search);
		id = urlParams.get("id");
	});

	$effect(async () => {
		if (post) {
			await post.fetchData();
			page.name = `"${post.title}"`;
		} else {
			page.name = "loading post...";
		}
	})
</script>

<!-- TODO: Make it semantic -->
<!-- TODO: Show a PROPER skeleton when loading -->

<div>
	{#await post?.fetchData()}
		<PostHeader />
	{:then}
		<PostHeader
			title={post?.title}
			author={post?.author}
			postedAt={post?.postedAt}
		/>
	{/await}

	{#await post?.fetchContent()}
		<article></article>
	{:then content}
		<article class="[&>p]:indent-5">{@html marked(content || "")}</article>
	{/await}
</div>