
export interface Sala{
    nome: string;
    versao: number;
}

export interface PerguntaEscolhaMultipla {
    options: string[];
    resposta_dada: number;
    resposta_certa: number;
}

export interface PerguntaRespostaMultipla {
    resposta_dada: string;
}

export enum TipoPerguntas {
    ESCOLHA_MULTIPLA = 0,
    RESPOSTA_LONGA,
    COMPLETAR,
    VERDADEIRO_FALSO,
}

export interface Pergunta {
    id: number;
    tipo: TipoPerguntas;
    Pergunta: PerguntaEscolhaMultipla;
    enunciado: string;
    cotacao: number;
}

export interface Versao {
    id: number;
    perguntas: Pergunta[];
}

export enum StatusProva {
    CRIADA = 0,
    INICIADA,
    TERMINADA,
    PUBLICADA,
}

export interface Prova {
    id: number;
	name: string;
	students: string[];
	date: string;
	starttime: string;
	endtime: string;
	salas: Sala[];
	versoes: Versao[];
	randomize: boolean;
	blockReturn: boolean;
    status: StatusProva;
}

export enum SidebarTypes {
    NORMAL = 0,
    PROVAS_EDITAR,
    PROVAS_CONSULTAR,
    PROVAS_CORRIGIR,
    HIDDEN
}

export enum UserTypes {
    NotAuthenticated = 0,
    DOCENTE = 1,
    ESTUDANTE = 2,
    TECNICO = 3
}

export interface User {
    id: string;
    email: string;
    type: UserTypes;
}

export interface ProvaEstudante {
    studentId:string;
    provaId: number;
    versaoId: number;
    prova: Prova
}