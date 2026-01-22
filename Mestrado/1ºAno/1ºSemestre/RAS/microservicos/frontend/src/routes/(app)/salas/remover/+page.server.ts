import type { PageServerLoad } from './$types';
import { error, redirect } from '@sveltejs/kit';

import { UserTypes } from '$lib/types';
export const load: PageServerLoad = async ({ params, locals }) => {
	if (locals.user.type == UserTypes.NotAuthenticated)
        throw error(403, {
            message: 'Não autorizado. Tenta fazer login primeiro.',
            title: 'Proibido',
            redirect: '/login',
        })
    else if (locals.user.type != UserTypes.TECNICO)
        throw error(403, {
            message: 'Não autorizado. Tens de ser admin para aceder a esta página.',
            title: 'Proibido',
            redirect: '/',
        })
    //todo: get all salas
    let salas:string[] = ["sala1", "sala2", "sala3"];
    return {
        salas: salas
    }
};

export const actions = {
    remover: async ({ request }) => {
        const data = await request.formData();
        let nomeSala = data.get('nomeSala');
        console.log(nomeSala);
        // TODO update the user API CALL
        throw redirect(303, '/salas/remover');
    }
};