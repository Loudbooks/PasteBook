export async function load({ params, cookies }) {
    console.log(cookies.get("cachedSignup"))
    if (cookies.get("cachedSignup") === undefined) {
        console.log("retu")
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