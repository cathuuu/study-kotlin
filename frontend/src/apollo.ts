import { ApolloClient, InMemoryCache, HttpLink } from '@apollo/client/core';

// Tạo link HTTP, trỏ về /graphql (sẽ proxy qua Vite config sang backend)
const httpLink = new HttpLink({
    uri: '/graphql',
});

// Khởi tạo Apollo Client
const apolloClient = new ApolloClient({
    link: httpLink,                // Nơi gửi request
    cache: new InMemoryCache(),    // Cache để Apollo tự quản lý dữ liệu
});

// Export ra để dùng toàn cục
export default apolloClient;
