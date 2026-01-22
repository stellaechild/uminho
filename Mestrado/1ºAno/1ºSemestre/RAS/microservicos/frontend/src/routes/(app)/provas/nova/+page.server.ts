import type { PageServerLoad } from './$types';
import { error, redirect, type ActionResult, fail } from '@sveltejs/kit';
import type { Actions } from './$types';

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
};
import { setTimeout as sleep } from 'timers/promises';
import { s } from 'vitest/dist/types-198fd1d9.js';
export const actions = {
	cancel: async () => {
		throw redirect(303, '/provas');
	},
	start: async ({ request, cookies }) => {
		const data = await request.formData();
		let name = data.get('NomeProva');
		if (name === '') {
			return fail(400, {
				error_message: 'Nome da prova não pode estar vazio',
				error_title: 'Bad Request'
			});
		}
		let file: File = data.get('FicheiroProva') as File;
		if (file.size == 0) {
			return fail(400, {
				error_message: 'Ficheiro não pode estar vazio',
				error_title: 'Bad Request'
			});
		}
		//validate file
		if (file == null) {
			return fail(400, {
				error_message: 'Ficheiro não pode estar vazio',
				error_title: 'Bad Request'
			});
		}
		//check if file is a list of strings in json
		let studentList: string[];
		try {
			studentList = JSON.parse(await file.text()) as string[];
		} catch (error) {
			return fail(400, {
				error_message: 'Ficheiro tem de ser um array de strings em formato json',
				error_title: 'Bad Request'
			});
		}
		//check if all students are different
		let studentSet = new Set(studentList);
		if (studentSet.size != studentList.length) {
			return fail(400, {
				error_message: 'Ficheiro contem alunos repetidos',
				error_title: 'Bad Request'
			});
		}
		//check if all students are valid through regex
		let regex = new RegExp('(a|A|pg|PG)\\d{4,6}');
		for (let student of studentList) {
			console.log(student);
			if (!regex.test(student)) {
				return fail(400, {
					error_message: 'Ficheiro contem alunos com ids inválidos',
					error_title: 'Bad Request'
				});
			}
		}
		return { name: name, students: studentList };
	},
	sugerirData: async ({ request, cookies }) => {
		const data = await request.formData();
		let date = data.get('DataProva');
		if (date === '') {
			return fail(400, {
				error_message: 'Data da prova não pode estar vazia',
				error_title: 'Bad Request'
			});
		}
		//todo get suggestions from backend
		let suggestions: { date: string; startime: string; endtime: string; salas: string[] }[] = [
			{ date: '2021-06-01', startime: '10:00', endtime: '12:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-01', startime: '14:00', endtime: '16:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-02', startime: '10:00', endtime: '12:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-02', startime: '14:00', endtime: '16:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-03', startime: '10:00', endtime: '12:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-03', startime: '14:00', endtime: '16:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-04', startime: '10:00', endtime: '12:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-04', startime: '14:00', endtime: '16:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-05', startime: '10:00', endtime: '12:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-05', startime: '14:00', endtime: '16:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-06', startime: '10:00', endtime: '12:00', salas: ['A1', 'A2'] },
			{ date: '2021-06-06', startime: '14:00', endtime: '16:00', salas: ['A1', 'A2'] }
		];
		return { suggestions: suggestions };
	},
	createProva: async ({ request, cookies }) => {
		const data = await request.formData();
		let name = data.get('NomeProva');
		let students = JSON.parse(data.get('Alunos') as string) as string[];
		let sugg = data.get('agendamento') as string;
		if (sugg === null) {
			return fail(400, {
				error_message: 'Agendamento não pode estar vazio',
				error_title: 'Bad Request'
			});
		}
		let sug = JSON.parse(sugg) as {
			date: string;
			startime: string;
			endtime: string;
			salas: string[];
		};
		let date = sug.date;
		let starttime = sug.startime;
		let endtime = sug.endtime;
		let salas = sug.salas;
		console.log(name, students, date, starttime, endtime, salas);
		//todo criar prova no backend
		let provaId = 1;
		throw redirect(303, `/provas/${provaId}/editar`);
	}
} satisfies Actions;
