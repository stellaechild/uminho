import { validateRegister } from '$lib/server/utils';
import type { PageServerLoad } from './$types';
import { error, redirect } from '@sveltejs/kit';

import { UserTypes } from '$lib/types';
export const load: PageServerLoad = async ({ params, locals }) => {
	if (locals.user.type == UserTypes.NotAuthenticated)
		throw error(403, {
			message: 'Não autorizado. Tenta fazer login primeiro.',
			title: 'Proibido',
			redirect: '/login'
		});
	else if (locals.user.type == UserTypes.ESTUDANTE)
		throw error(403, {
			message: 'Não autorizado. Tens de ser professor ou admin para aceder a esta página.',
			title: 'Proibido',
			redirect: '/'
		});
};

export const actions = {
	cancel: async () => {
		throw redirect(303, '/');
	},
	registar: async ({ request }) => {
		const data = await request.formData();
		let nome = data.get('nome');
		let numero = data.get('numero');
		let email = data.get('email');
		let password = data.get('password');
		console.log(nome, numero, email, password);
		// validateRegister(nome, numero, email, password);
		let resp = await fetch('http://GestaoDeContas:8000/users/registar/aluno', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				name: nome,
				numero: numero,
				email: email,
				password: password
			})
		});
		if (!resp.ok) {
			let err = resp.statusText;
			throw error(resp.status, {
				message: err,
				title: 'Erro de registo',
                redirect: '/registar/aluno'
			});
		}
		throw redirect(303, '/');
	}
};
