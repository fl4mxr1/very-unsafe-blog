<script lang="ts">
	import { marked } from "marked";
	import Button from "../Button.svelte";

	let { id = "" } = $props();

	let value = $state("");
	let viewMode: "edit" | "preview" = $state("edit");
</script>

<div
	class="w-full h-[16lh] text-md border border-gray-300 bg-white rounded-lg flex flex-col"
>
	<div class="flex gap-1 p-1 border-b border-gray-300">
		<Button
			onclick={() => (viewMode = "edit")}
			primary={viewMode == "edit"}
			secondary={viewMode != "edit"}
			sm>Edit</Button
		>
		<Button
			onclick={() => (viewMode = "preview")}
			primary={viewMode == "preview"}
			secondary={viewMode != "preview"}
			sm>Preview</Button
		>
	</div>
	<div class="grow">
		{#if viewMode == "edit"}
			<textarea
				name="markdown-editor"
				class="w-full h-min min-h-full resize-none py-2 px-3 font-mono whitespace-nowrap focus:outline-2 outline-black -outline-offset-2"
				placeholder="..."
				{id}
				bind:value
			></textarea>
		{:else if viewMode == "preview"}
			<div class="font-serif overflow-auto whitespace-nowrap py-2 px-3">
				{#if value.trim().length < 1}
					Post content is empty. Type something and it'll reflect
					here! <br />
					P.S. You can also use Markdown.
				{:else}
					{@html marked(value)}
				{/if}
			</div>
		{/if}
	</div>
</div>
