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
	else if (locals.user.type != UserTypes.DOCENTE)
		throw error(403, {
			message: 'Não autorizado. Tens de ser docente para aceder a esta página.',
			title: 'Proibido',
			redirect: '/'
		});
	//todo check if docwnte is the owner of the prova
	//todo fetch prova
	return {
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
				{ id: 1, perguntas: [
					{
						id: 1,
						tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
						Pergunta: {
							options: ['a', 'b', 'c', 'd'],
							resposta_certa: 1,
							resposta_dada: -1
						},
						cotacao: 1,
						enunciado: 'enunciado1'
					},
					{
						id: 2,
						tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
						Pergunta: {
							options: ['a1', 'b2', 'c2', 'd2'],
							resposta_certa: 3,
							resposta_dada: -1
						},
						cotacao: 1,
						enunciado: 'enunciado2'
					},
					{
						id: 3,
						tipo: TipoPerguntas.ESCOLHA_MULTIPLA,
						Pergunta: {
							options: ['a4', 'b3', '1gsf', '113r4'],
							resposta_certa: 2,
							resposta_dada: -1
						},
						cotacao: 1,
						enunciado: 'Qual e coisa qual e ela'
					},
				] },
				{ id: 2, perguntas: [] },
				{ id: 3, perguntas: [] },
				{ id: 4, perguntas: [] }
			],
			randomize: false,
			blockReturn: false,
			status: 0
		}
	};
};

export const actions = {
	back: async () => {
		throw redirect(303, '/provas');
	},
	save: async ({ request, cookies }) => {
		const data = await request.formData();
		let provaID = Number(data.get('id') as string);
		let salas: Sala[] = JSON.parse(data.get('salas') as string);
		let versionQuantity = Number(data.get('versionQuantity') as string);
		for (let i = 0; i < salas.length; i++) {
			salas[i].versao = Number(data.get(`${salas[i].nome}`) as string);
		}
		console.log(provaID, salas, versionQuantity);
		//todo save prova
	},
	saveQuestion: async ({ request, cookies }) => {
		const data = await request.formData();
		let versionId = Number(data.get('versionId') as string);
		let provaId = Number(data.get('provaId') as string);
		let options: string[] = [];
		for (let i = 1; i < 5; i++) {
			const option = data.get(`option${i}`) as string;
			if (option != null && option != '') options.push(option);
		}
		const pergunta: Pergunta = {
			id: Number(data.get('id') as string),
			tipo: TipoPerguntas[data.get('tipo') as keyof typeof TipoPerguntas],
			Pergunta: {
				options: options,
				resposta_certa: Number(data.get('RespostaCorreta') as string),
				resposta_dada: -1
			},
			cotacao: Number(data.get('cotacao') as string),
			enunciado: data.get('enunciado') as string
		};
		console.log(pergunta, versionId, provaId);
		//todo save question
		return pergunta;
	}
} satisfies Actions;
