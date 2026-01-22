import { error, redirect } from '@sveltejs/kit';
export async function load({ cookies }) {
	
}

export const actions = {
    cancel: async () => {
		throw redirect(303, '/');
	},
	login: async ({ request, cookies }) => {
		const data = await request.formData();
        let email = data.get('email');
        let password = data.get('password');
        let body = {
            email: email,
            password: password
        }
        let resp = await fetch('http://GestaoDeContas:8000/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        })
        if (resp.status == 403) {
			let err = resp.statusText;
			throw error(403, {
				message: "",
				title: 'Erro de Login - Credenciais inválidas',
                redirect: '/login'
			});
		}
        let jwt = await resp.json();
        let expires_in = 60 * 60; // One week.
        cookies.set('AuthToken', `Bearer ${jwt.token}`, {
            httpOnly: true,
            path: '/',
            secure: true,
            sameSite: 'strict',
            maxAge: expires_in
        });
        throw redirect(303, '/')
	},
};