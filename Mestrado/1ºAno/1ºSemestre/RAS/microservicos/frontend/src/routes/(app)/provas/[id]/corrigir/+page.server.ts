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
    else if (locals.user.type != UserTypes.DOCENTE)
        throw error(403, {
            message: 'Não autorizado. Tens de ser docente para aceder a esta página.',
            title: 'Proibido',
            redirect: '/',
        })
    //todo check if docwnte is the owner of the prova
};
