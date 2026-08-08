export interface PostData {
    id?: string, 
    title?: string, 
    preview?: string, 
    author?: string, 
    postedAt?: number,
}

class InvalidArgumentException extends Error {};

const API_URL = "http://localhost:8080";

export function getPostDataFromId(id: string): Promise<PostData> {
    if (id === null) {
        throw new InvalidArgumentException("Cannot get post data with a null ID.");
    }
    return new Promise((fulfill, reject) => {
        fetch(`${API_URL}/post/get-data?id=${id}`, {
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

export function getPostContentFromId(id: string): Promise<string> {
    if (id === null) {
        throw new InvalidArgumentException("Cannot get post content with a null ID.");
    }
    return new Promise((fulfill, reject) => {
        fetch(`${API_URL}/post/get-content?id=${id}`, {
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