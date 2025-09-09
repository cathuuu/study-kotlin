import { gql } from "@apollo/client/core";

// --- Queries ---
export const GET_ALL_AUTHORS = gql`
    query GetAllAuthors {
        getAllAuthors {
            id
            name
            birthYear
            nationality
            books {
                id
                title
            }
        }
    }
`;

export const GET_ALL_BOOKS = gql`
    query GetAllBooks {
        getAllBooks {
            id
            title
            publishedYear
            price
            quantity
            author {
                id
                name
            }
        }
    }
`;

export const GET_AUTHOR_BY_ID = gql`
    query GetAuthorById($id: ID!) {
        getAuthorById(id: $id) {
            id
            name
            birthYear
            nationality
            books {
                id
                title
            }
        }
    }
`;

export const GET_BOOK_BY_ID = gql`
    query GetBookById($id: ID!) {
        getBookById(id: $id) {
            id
            title
            publishedYear
            price
            quantity
            author {
                id
                name
            }
        }
    }
`;

export const SEARCH_AUTHOR = gql`
    query SearchAuthors($filter: AuthorSearchInput) {
        searchAuthors(filter: $filter) {
            id
            name
            birthYear
            nationality
        }
    }
`;



export const SEARCH_BOOKS = gql`
    query SearchBooks($filter: BookSearchInput) {
        searchBooks(filter: $filter) {
            id
            title
            publishedYear
            price
            quantity
        }
    }
`;

// --- Mutations ---
export const ADD_AUTHOR = gql`
    mutation AddAuthor($input: AuthorInput!) {
        addAuthor(input: $input) {
            id
            name
            birthYear
            nationality
        }
    }
`;

export const UPDATE_AUTHOR = gql`
    mutation UpdateAuthor($id: ID!, $input: AuthorInput!) {
        updateAuthor(id: $id, input: $input) {
            id
            name
            birthYear
            nationality
        }
    }
`;

export const DELETE_AUTHOR = gql`
    mutation DeleteAuthor($id: ID!) {
        deleteAuthor(id: $id)
    }
`;

export const ADD_BOOK = gql`
    mutation AddBook($authorId: ID!, $input: BookInput!) {
        addBook(authorId: $authorId, input: $input) {
            id
            title
            publishedYear
            price
            quantity
            author {
                id
                name
            }
        }
    }
`;

export const UPDATE_BOOK = gql`
    mutation UpdateBook($id: ID!, $authorId: ID, $input: BookInput!) {
        updateBook(id: $id, authorId: $authorId, input: $input) {
            id
            title
            publishedYear
            price
            quantity
            author {
                id
                name
            }
        }
    }
`;

export const DELETE_BOOK = gql`
    mutation DeleteBook($id: ID!) {
        deleteBook(id: $id)
    }
`;

export const SEARCH_BOOK_PAGE_NATIVE =gql`
   query GetBooksByPage($filter: BookSearchInput, $page: Int!, $size: Int!) {
    getBooksByPage(filter: $filter, page: $page, size: $size) {
      content {
        id
        title
          publishedYear
          price
          quantity
          author {
              name
          }
      }
      totalElements
      totalPages
      number
      size
    }
  }
 `;
export const SEARCH_AUTHOR_PAGE_NATIVE = gql`
    query GetAuthorsByPage($filter: AuthorSearchInput, $page: Int!, $size: Int!) {
        getAuthorsByPage(filter: $filter, page: $page, size: $size) {
            content {
                id
                name
                birthYear
                nationality
            }
            totalElements
            totalPages
            number
            size
        }
    }
`;
export const LOGIN_MUTATION = gql`
    mutation Login($input: LoginInput!) {
        login(input: $input) {
            accessToken
            refreshToken
        }
    }
`;