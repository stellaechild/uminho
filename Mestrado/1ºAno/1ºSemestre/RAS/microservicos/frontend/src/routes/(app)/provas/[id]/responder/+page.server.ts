import type { PageServerLoad } from './$types';
import { error, redirect } from '@sveltejs/kit';

import { UserTypes, type ProvaEstudante, TipoPerguntas } from '$lib/types';
export const load: PageServerLoad = async ({ params, locals }) => {
	if (locals.user.type == UserTypes.NotAuthenticated)
		throw error(403, {
			message: 'Não autorizado. Tenta fazer login primeiro.',
			title: 'Proibido',
			redirect: '/login'
		});
	else if (locals.user.type != UserTypes.ESTUDANTE)
		throw error(403, {
			message: 'Não autorizado. Tens de ser docente para aceder a esta página.',
			title: 'Proibido',
			redirect: '/'
		});
	let res: ProvaEstudante = {
		studentId: locals.user.id,
		provaId: parseInt(params.id),
		versaoId: 1,
		prova: {
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
				{
					id: 1,
					perguntas: [
						{
							id: 1,
							tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
							Pergunta: {
								options: ['a', 'b', 'c', 'd'],
								resposta_certa: 1,
								resposta_dada: 2
							},
							cotacao: 40,
							enunciado: 'enunciado1'
						},
						{
							id: 2,
							tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
							Pergunta: {
								options: ['a1', 'b2', 'c2', 'd2'],
								resposta_certa: 3,
								resposta_dada: 3
							},
							cotacao: 30,
							enunciado: 'enunciado2'
						},
						{
							id: 3,
							tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
							Pergunta: {
								options: ['a4', 'b3', '1gsf', '113r4'],
								resposta_certa: 2,
								resposta_dada: 2
							},
							cotacao: 20,
							enunciado: 'Qual e coisa qual e ela'
						}
					]
				},
				{ id: 2, perguntas: [] },
				{ id: 3, perguntas: [] },
				{ id: 4, perguntas: [] }
			],
			randomize: false,
			blockReturn: false,
			status: 0
		}
	};
    return res;
};
