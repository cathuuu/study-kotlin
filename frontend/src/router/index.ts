// src/router/index.ts
import { createRouter, createWebHistory } from "vue-router"
import Login from "../views/authentication/Login.vue"
import BookForm from "../components/BookForm.vue"
import BookList from "../components/BookList.vue";
import BookSearch from "../components/BookSearch.vue";
import AuthorForm from "../components/AuthorForm.vue";
import AuthorList from "../components/AuthorList.vue";
import AuthorSearchPopup from "../components/AuthorSearchPopup.vue";


const routes = [
    { path: "/login", component: Login },
    { path: "/booksForm", component: BookForm, meta: { requiresAuth: true }},
    { path: "/booksList", component: BookList, meta: { requiresAuth: true }},
    { path: "/booksSearch", component: BookSearch, meta: { requiresAuth: true }},
    { path: "/authorForm", component: AuthorForm, meta: { requiresAuth: true }},
    { path: "/authorList", component: AuthorList, meta: { requiresAuth: true }},
    { path: "/authorSearchPopup", component: AuthorSearchPopup, meta: { requiresAuth: true }},
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

router.beforeEach((to, _, next) => {
    const token = localStorage.getItem("jwtToken")

    if (to.meta.requiresAuth && !token) {
        next("/login") // chưa có token → về login
    } else if (to.path === "/login" && token) {
        next("/booksForm")
    } else if (to.path === "/login" && token) {
        next("/booksList")
    } else if (to.path === "/login" && token) {
        next("/booksSearch")
    } else if (to.path === "/login" && token) {
        next("/authorForm")
    } else if (to.path === "/login" && token) {
        next("/authorList")
    } else if (to.path === "/login" && token) {
        next("/authorSearchPopup")// đã login → không cho vào lại login
    } else {
        next()
    }
})

export default router
