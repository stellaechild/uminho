import type { PageServerLoad } from './$types';
import { error, redirect, type Actions } from '@sveltejs/kit';

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
    //todo check if docwnte is the owner of the prova, fetch prova
    let prova = {
        id: params.id,
        name: 'proava1',
        students: ['aaa', 'bbb', 'ccc'],
        date: '2021-05-05',
        starttime: '10:00',
        endtime: '12:00',
        salas: [
            { nome: 'A1', versao: 1 },
            { nome: 'A2', versao: 2 },
            { nome: 'A3', versao: 3 },
            { nome: 'A4', versao: 4 }
        ],
        versoes: [
            { id: 1, perguntas: [] },
            { id: 2, perguntas: [] },
            { id: 3, perguntas: [] },
            { id: 4, perguntas: [] }
        ],
        randomize: false,
        blockReturn: false,
        status: 0
    };
    return prova;
};

export const actions: Actions = {
	cancel: async ({ request, locals }) => {
        throw redirect(303, `/provas`);
	},
	share: async ({ request, locals }) => {
		//share
	}
};