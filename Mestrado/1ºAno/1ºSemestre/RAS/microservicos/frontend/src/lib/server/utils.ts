import { error } from "@sveltejs/kit"


export function validatePasswordStrength(password?: FormDataEntryValue | null){
    if (password == null){
        throw error(400, {
            message: 'Password não pode estar vazia',
            title: 'Bad Request',
            redirect: '/registar/docente',
        })
    } else if (password.toString().length < 8){
        throw error(400, {
            message: 'Password tem de ter pelo menos 8 caracteres',
            title: 'Bad Request',
            redirect: '/registar/docente',
        })
    }
}

export function validateRegister(nome?: FormDataEntryValue | null, numero?: FormDataEntryValue | null, email?: FormDataEntryValue | null, password?: FormDataEntryValue | null){
    if (nome == null){
        throw error(400, {
            message: 'Nome não pode estar vazio',
            title: 'Bad Request',
            redirect: '/registar/docente',
        })
    } else if (numero == null){
        throw error(400, {
            message: 'Numero não pode estar vazio',
            title: 'Bad Request',
            redirect: '/registar/docente',
        })
    } else if (email == null){
        throw error(400, {
            message: 'Email não pode estar vazio',
            title: 'Bad Request',
            redirect: '/registar/docente',
        })
    }
    validatePasswordStrength(password);
}

