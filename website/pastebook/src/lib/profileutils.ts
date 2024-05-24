import type {Cookies} from "@sveltejs/kit";

export function isLoggedIn(cookies: Cookies) {
    return cookies.get('token') != null && cookies.get('username') != null && cookies.get('id') != null;
}