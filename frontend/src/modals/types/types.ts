// types.ts
// import type { Author } from "./types";

export interface Author {
    id: string;
    name: string;
    birthYear?: number | null;
    nationality?: string | null;
}

// ✅ định nghĩa emits option
export interface AuthorFormEmits {
    saved: [author: Author];
    cancel: [];
    search: [
        filter: {
            keyword?: string;
            nationality?: string;
            birthYear?: number | null;
        }
    ];
}
