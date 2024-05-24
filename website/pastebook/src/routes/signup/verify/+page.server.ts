import {isLoggedIn} from "$lib/profileutils";

export async function load({ cookies }) {
    if (cookies.get("cachedSignup") === undefined) {
        return {
            failed: true
        }
    }

    if (isLoggedIn(cookies)) {
        return {
            failed: true
        }
    }

    let data = JSON.parse(cookies.get("cachedSignup") as string)

    return {
        email: data.email,
        username: data.username,
        password: data.password,
        failed: false
    }
}