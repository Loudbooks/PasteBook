import {error, redirect} from "@sveltejs/kit";

export async function load({ url, cookies }) {
    const code = url.searchParams.get('code');

    if (code == null) {
        error(400, {
            message: 'Missing code parameter'
        })
    }

    const response = await fetch("http://localhost:25658/api/profile/login/discord?code=" + code)

    if (response.status === 429) {
        error(429, {
            message: 'Rate Limited'
        });
    }

    if (response.status != 200) {
        if (response.body != null) {
            const body = await response.text()
            error(response.status, body);
        }

        error(response.status, {
            message: 'Server Error'
        });
    }

    const jsonResponse: any = await response.json()

    const token: string = jsonResponse.token
    const username: string = jsonResponse.username
    const id: string = jsonResponse.identification

    cookies.set('token', token, {path: '/'})
    if (username != null) cookies.set('username', username, {path: '/'})
    cookies.set('id', id, {path: '/'})

    redirect(301, '/panel')
}