// src/apollo.ts
import { ApolloClient, InMemoryCache, createHttpLink } from "@apollo/client/core"

const httpLink = createHttpLink({
    uri: "http://localhost:8080/graphql", // đổi URI theo BE của bạn
})

export const apolloClient = new ApolloClient({
    link: httpLink,
    cache: new InMemoryCache(),
})
