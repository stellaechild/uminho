import { writable } from 'svelte/store';
import { SidebarTypes, type Prova } from './types';

export const errStore = writable(
    {
        error_title: "",
        error_message: "",
        error_redirect: "",
    }
);
export const loading = writable(false);

export const novaProvaStore = writable(
    {
        name: "",
        students: [],
        date: "",
        starttime: "",
        endtime: "",
        salas: []
    }
);

export const ProvaStore = writable(
    {
        id: -1,
        name: "",
        students: [],
        date: "",
        starttime: "",
        endtime: "",
        salas: [],
        versoes: [],
        randomize: false,
        blockReturn: false,
        status: 0
    } as Prova
);

export const SidebarState = writable(SidebarTypes.NORMAL as SidebarTypes)