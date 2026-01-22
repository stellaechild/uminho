import { redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { error } from '@sveltejs/kit';

import { UserTypes } from '$lib/types';
export const load: PageServerLoad = async ({ params, locals }) => {
	if (locals.user.type == UserTypes.NotAuthenticated)
		throw error(403, {
			message: 'Não autorizado. Tenta fazer login primeiro.',
			title: 'Proibido',
			redirect: '/login'
		});
	return {
		email: locals.user.email
	};
};

export const actions = {
	cancel: async () => {
		throw redirect(303, '/');
	},
	update: async ({ request, locals }) => {
		const data = await request.formData();
		let email = data.get('email');
		let oldPassword = data.get('old-password');
		let password = data.get('password');
		let logout = false;
		if (email != locals.user.email) {
			let respEmail = await fetch('http://GestaoDeContas:8000/users/editar/email', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					old_email: locals.user.email,
					new_email: email
				})
			});
			if (!respEmail.ok) {
				let err = respEmail.statusText;
				throw error(respEmail.status, {
					message: err,
					title: 'Erro de edicao de email',
					redirect: '/perfil'
				});
			}
			logout = true;
		}
		if (password != '' && oldPassword != '') {
			let resp = await fetch('http://GestaoDeContas:8000/users/editar/password', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					email: email,
					old_password: oldPassword,
					new_password: password
				})
			});
			if (!resp.ok) {
				let err = resp.statusText;
				throw error(resp.status, {
					message: err,
					title: 'Erro de edicao de password',
					redirect: '/perfil'
				});
			}
			logout = true;
		}
		if (logout) throw redirect(303, '/redirects/logout');
		else throw redirect(303, '/perfil');
	}
};
