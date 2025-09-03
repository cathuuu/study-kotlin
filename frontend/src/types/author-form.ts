// types.ts
export interface Author {
    id: string;
    name: string;
    birthYear?: number | null;
    nationality?: string | null;
}

export type AuthorFormEmits = {
    (e: "saved", author: Author): void;
    (e: "cancel"): void;
    (e: "search", filter: {
        keyword?: string;
        nationality?: string;
        birthYear?: number | null;
    }): void;
};
