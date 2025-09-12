export interface Book {
    id: string;
    title: string;
    publishedYear?: number | null;
    price?: number | null;
    quantity?: number | null;
    authorId: string;
}