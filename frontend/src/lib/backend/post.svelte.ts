const CACHE_EXPIRES_AFTER_MILLIS = 60 * 60 * 1000 // 1 hour in milliseconds
const BACKEND_URL = "http://localhost:8080";

export interface PostData {
    id?: string, 
    title?: string, 
    preview?: string, 
    author?: string, 
    postedAt?: number,
}

function getPostDataFromId(id: string): Promise<PostData> {
    if (id === null) {
        throw new InvalidArgumentException("Cannot get post data with a null ID.");
    }
    return new Promise((fulfill, reject) => {
        fetch(`${BACKEND_URL}/post/get-data?id=${id}`, {
            method: "GET", 
        })
            .then((res => {
                res.json()
                    .then(fulfill)
                    .catch(reject)
            }))
            .catch(reject)
    })
}

function getPostContentFromId(id: string): Promise<string> {
    if (id === null) {
        throw new InvalidArgumentException("Cannot get post content with a null ID.");
    }
    return new Promise((fulfill, reject) => {
        fetch(`${BACKEND_URL}/post/get-content?id=${id}`, {
            method: "GET", 
        })
            .then((res => {
                res.text()
                    .then(fulfill)
                    .catch(reject)
            }))
            .catch(reject)
    })
}

// get rid of this maybe?
class InvalidArgumentException extends Error {};

// Class which is used to get post data from the backend. Also handles caching and stuff
export default class Post {
    #id?: string;
    #title?: string = $state();
    #author?: string = $state();
    #postedAt?: number = $state();
    #preview?: string = $state();

    //TODO: Save cache data to localStorage?
    #cachedPostContent?: string;
    #cacheExpirationDate?: number;

    constructor(id: string) {
        this.#id = id;
    }

    async fetchData(): Promise<Post | undefined> {
        if (this.#id === undefined) return; // too lazy to do error handling..
        const postData: PostData = await getPostDataFromId(this.#id);
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