<script lang="ts">
	import { getPostDataFromId } from "$lib/backend-lib";
	import PostItem from "../components/PostItem.svelte";
	import PostList from "../components/PostList.svelte";

	getPostDataFromId("1");
</script>

<h1 class="mb-2">Browse All Posts</h1>
<PostList>
	<PostItem
		id="1"
		name="hello"
		postedBy="neighbour"
		postedAtTimestamp={65656564}
	/>
	<PostItem
		id="1"
		name="hello"
		postedBy="neighbour"
		postedAtTimestamp={65656564}
	/>
	<PostItem
		id="1"
		name="hello"
		postedBy="neighbour"
		postedAtTimestamp={65656564}
	/>
	<PostItem
		id="1"
		name="hello"
		postedBy="neighbour"
		postedAtTimestamp={65656564}
	/>
	<PostItem
		id="1"
		name="hello"
		postedBy="neighbour"
		postedAtTimestamp={65656564}
	/>
</PostList>
