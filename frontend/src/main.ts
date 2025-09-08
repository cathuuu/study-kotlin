// src/main.ts
import { createApp, h, provide } from "vue"
import App from "./App.vue"
import { DefaultApolloClient } from "@vue/apollo-composable"
import { apolloClient } from "./apollo"

const app = createApp({
    setup() {
        // provide ApolloClient mặc định cho toàn app
        provide(DefaultApolloClient, apolloClient)
    },
    render: () => h(App),
})

app.mount("#app")
