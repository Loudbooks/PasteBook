import {isLoggedIn} from "$lib/profileutils";

export async function load({ cookies }) {
    if (isLoggedIn(cookies)) {
        return {
            failed: true
        }
    }

    return {
        failed: false
    }
}
