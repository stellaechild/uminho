import { TipoPerguntas, type Pergunta, type Sala } from '$lib/types';
import type { PageServerLoad } from './$types';
import { error, redirect, type Actions } from '@sveltejs/kit';
import { UserTypes } from '$lib/types';
export const load: PageServerLoad = async ({ params, locals }) => {
	if (locals.user.type == UserTypes.NotAuthenticated)
		throw error(403, {
			message: 'Não autorizado. Tenta fazer login primeiro.',
			title: 'Proibido',
			redirect: '/login'
		});
	else if (locals.user.type == UserTypes.TECNICO)
		throw error(403, {
			message: 'Não autorizado. Tens de ser docente ou aluno para aceder a esta página.',
			title: 'Proibido',
			redirect: '/'
		});
	//todo check if docwnte is the owner of the prova
	//todo fetch prova
	return {
		isOwner: locals.user.type == UserTypes.DOCENTE,
		provas: [
			{
				id: 1,
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
			},
			{
				id: 2,
				name: 'proava2',
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
				status: 1
			},
			{
				id: 3,
				name: 'proava3',
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
				status: 2
			},
			{
				id: 4,
				name: 'proava4',
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
				status: 3
			}
		]
	};
};

export const actions: Actions = {
	edit: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.DOCENTE) {
			throw redirect(303, `/provas/${provaID}/editar`);
		}
	},
	share: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.DOCENTE) {
			throw redirect(303, `/provas/${provaID}/partilhar`);
		}
	},
	start: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.DOCENTE) {
			//todo start prova
		}
	},
	end: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.DOCENTE) {
			//todo end prova
		}
	},
	publish: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.DOCENTE) {
			//todo publish prova
		}
	},
	realize: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.ESTUDANTE) {
			throw redirect(303, `/provas/${provaID}/responder`);
		}
	},
	results: async ({ request, locals }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		if (locals.user.type == UserTypes.ESTUDANTE) {
			throw redirect(303, `/provas/${provaID}/consultar`);
		}
	},
	create: async ({ request, locals }) => {
		if (locals.user.type == UserTypes.DOCENTE) {
			throw redirect(303, `/provas/nova`);
		}
	}
} as Actions;
