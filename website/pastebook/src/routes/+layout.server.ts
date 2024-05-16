import {parseJwt} from "$lib/jwthandler";

export async function load({ cookies }) {
    let token = cookies.get('token');

    if (token == null) {
        return {
            profile: null
        }
    }

    let decoded = parseJwt(token);
    let id = decoded.id;
    let username = decoded.username;

    return {
        profile: {
            id: id,
            username: username
        }
    }
}