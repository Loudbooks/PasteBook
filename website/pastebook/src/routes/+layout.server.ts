export async function load({ cookies }) {
    let token = cookies.get('token');
    let username = cookies.get('username');
    let id = cookies.get('id');

    console.log(token
        + " " + username
        + " " + id
    )

    if (token == null || username == null || id == null) {
        return {
            profile: null
        }
    }

    return {
        profile: {
            id: id,
            username: username
        }
    }
}