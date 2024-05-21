export async function load({ cookies }) {
    console.log(cookies.get("cachedSignup"))
    if (cookies.get("cachedSignup") === undefined) {
        return {
            failed: true
        }
    }

    let data = JSON.parse(cookies.get("cachedSignup"))

    return {
        email: data.email,
        username: data.username,
        password: data.password,
        failed: false
    }
}