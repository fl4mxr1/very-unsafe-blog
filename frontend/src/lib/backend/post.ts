import { getPostDataFromId, getPostContentFromId, type PostData } from "$lib/backend/backend-http";

const CACHE_EXPIRES_AFTER_MILLIS = 60 * 60 * 1000 // 1 hour in milliseconds

//class CouldNotFetchPostDataError extends Error {};

// Class which is used to get post data from the backend. Also handles caching and stuff
export default class Post {
    #id?: string;
    #title?: string;
    #author?: string;
    #postedAt?: number;
    #preview?: string;

    //TODO: Save cache data to localStorage?
    #cachedPostContent?: string;
    #cacheExpirationDate?: number;

    constructor(id: string) {
        this.#id = id;
    }

    async fetchPostData(): Promise<Post | undefined> {
        if (this.#id === undefined) return; // too lazy to do error handling..
        const postData: PostData = await getPostDataFromId(this.#id);
        console.log(postData);
        // if (postData.title === undefined || postData.author === undefined || postData.postedAt === undefined || postData.preview === undefined) {
        //     throw new CouldNotFetchPostDataError("Unexpected error occured when fecthing post data.");
        // }
        this.#title = postData.title;
        this.#author = postData.author;
        this.#postedAt = postData.postedAt;
        this.#preview = postData.preview;

        return this;
    }

    async fetchContent(): Promise<string | undefined> {
        if (this.#id === undefined) return;
        if (this.#cachedPostContent != undefined && (this.#cacheExpirationDate != undefined && this.#cacheExpirationDate < Date.now())) {
            return this.#cachedPostContent;
        }
        const postContent: string = await getPostContentFromId(this.#id);
        this.#cachedPostContent = postContent;
        this.#cacheExpirationDate = Date.now() + CACHE_EXPIRES_AFTER_MILLIS;
        return postContent;
    }

    get id(): string | undefined {
        return this.#id;
    }

    get title(): string | undefined { 
        return this.#title;
    }

    get author(): string | undefined {
        return this.#author;
    }

    get postedAt(): number | undefined {
        return this.#postedAt;
    }

    get preview(): string | undefined {
        return this.#preview;
    }
}