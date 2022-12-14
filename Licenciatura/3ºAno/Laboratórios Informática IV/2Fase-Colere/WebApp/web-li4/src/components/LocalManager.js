import React, { useEffect } from "react";
import { useState } from "react/cjs/react.development";
import axios from "axios"
import _, { set, update } from "lodash"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faTrash } from "@fortawesome/free-solid-svg-icons"
import logo from "./logoOriginal.png"
import { Link } from "react-router-dom";

const API_URL = "http://localhost:8080"



export default function LocalManager(props){
    const [local, setLocal] = useState([])
    const [descricao,setDescricao] = useState("")
    const [horaAbertura,setHoraAbertura] = useState("")
    const [horaFecho,setHoraFecho] = useState("")
    const [website,setWebsite] = useState("")
    const [evento,setEvento] = useState("")
    const [descricaoEvento,setDescricaoEvento] = useState("")
    const [data,setData] = useState("")
    const [submitState,setSubmitState] = useState(0)
    const [novoEvento,setNovoEvento] = useState("")
    const [alteracao,setAlteracao] = useState(false)

    useEffect(() => {
        (async () => {
            const res = await axios.get(`${API_URL}/menuGestor`, { params: { email: props.email } })
            setLocal(res.data)
            setDescricao(res.data.descricao)
            setHoraAbertura(res.data.horaAbertura)
            setHoraFecho(res.data.horaFecho)
            setWebsite(res.data.website)
      })()
    },[alteracao])

    const onSubmitHandler = event => {
        event.preventDefault()

        axios.post(`${API_URL}/menuGestor`,{
            'comando' : 'editarLocal',
            'email' : props.email,
            'descricao' : descricao,
            'horaAbertura' : horaAbertura,
            'horaFecho' : horaFecho,
            'website' : website
        })
        setAlteracao(!alteracao)
        alert('Informações do local alteradas com sucesso!')
    }

    const onEditClick = (e) => {
        e.preventDefault()
        axios.post(`${API_URL}/menuGestor`,{
            'comando' : 'editarEvento',
            'email' : props.email,
            'descricao' : `${descricaoEvento === "" ? local.eventos[evento].descricao : descricaoEvento}`,
            'nomeEventoAntes' : evento,
            'nomeEventoDepois' : `${novoEvento === "" ? local.eventos[evento].nome : novoEvento}`,
            'data' : `${data === "" ? local.eventos[evento].dataHora : data}`
        })
        setAlteracao(!alteracao)
        alert('Evento alterado com sucesso!')
    }

    const onAddClick = (e) => {
        e.preventDefault()
        axios.post(`${API_URL}/menuGestor`,{
            'comando' : 'adicionarEvento',
            'email' : props.email,
            'descricao' : descricaoEvento,
            'nomeEvento' : novoEvento,
            'data' : data
        })
        setAlteracao(!alteracao)
        alert('Evento adicionado com sucesso!')
    }

    const handleDeleteOnClick = (name) => {
        axios.post(`${API_URL}/menuGestor`,{
            'comando' : 'removerEvento',
            'email' : props.email,
            'nomeEvento' : name
        })
        setAlteracao(!alteracao)
        alert('Evento apagado com sucesso!')
    }

    const onDescricaoChange = event => {
        setDescricao(event.target.value)
    }

    const onAberturaChange = event => {
        setHoraAbertura(event.target.value)
    }
    
    const onFechoChange = event => {
        setHoraFecho(event.target.value)
    }

    const onWebsiteChange = event => {
        setWebsite(event.target.value)
    }

    const onEventoChange = event => {
        setEvento(event.target.value)
    }

    const onDescricaoEventoChange = event => {
        setDescricaoEvento(event.target.value)
    }

    const onDataChange = event => {
        setData(event.target.value)
    }

    const onNovoEventoChange = event => {
        setNovoEvento(event.target.value)
    }

    return (
        <div className="mt-3 flex flex-col justify-center items-center">
            <div className="flex flex-col justify-center align-center">
                <table className="border-2 border-black">
                    <tr>
                        <th className="p-2 border-black border-2">Nome</th>
                        <th className="p-2 border-black border-2">Descrição</th>
                        <th className="p-2 border-black border-2">Hora de Abertura</th>
                        <th className="p-2 border-black border-2">Hora de Fecho</th>
                        <th className="p-2 border-black border-2">Website</th>
                    </tr>
                    <tr>
                        <td className="p-2 border-black border-2">{local.nome}</td>
                        <td className="p-2 border-black border-2">{local.descricao}</td>
                        <td className="p-2 border-black border-2">{local.horaAbertura}</td>
                        <td className="p-2 border-black border-2">{local.horaFecho}</td>
                        <td>{local.website}</td>
                    </tr>
                </table>
                <table className="mt-10">
                    <tr>
                        <th className="p-2 border-black border-2">Nome do Evento</th>
                        <th className="p-2 border-black border-2">Descrição</th>
                        <th className="p-2 border-black border-2">Data</th>
                    </tr>

                        {
                            !_.isEmpty(local.eventos) ? 
                                Object.values(local.eventos).map(evento => {
                                    return (
                                    <tr>
                                        <td className="p-2 border-black border-2">
                                            <div className="flex  justify-between">
                                                {evento.nome}
                                                <button onClick={()=> handleDeleteOnClick(evento.nome)}>
                                                    <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                                                </button>
                                            </div>
                                        </td>
                                        <td className="p-2 border-black border-2">{evento.descricao}</td>
                                        <td className="p-2 border-black border-2">{evento.dataHora}</td>
                                    </tr>
                                    )
                                })
                                :
                            <tr>
                                <td className="p-2 border-black border-2">n/d</td>
                                <td className="p-2 border-black border-2">n/d</td>
                                <td className="p-2 border-black border-2">n/d</td>
                            </tr>
                        }
                </table>
            </div>
            <div className="flex">
                <div className="w-96 mt-10 p-3 border-2 rounded border-black mr-2">
                    <h3 className="text-center text-lg font-bold mb-2">Editar Informação do Local</h3>
                    <form onSubmit={onSubmitHandler} className="flex flex-col">
                        <label htmlFor="descricao">Descrição</label>
                        <input placeholder={local.descricao} onChange={onDescricaoChange} id="descricao" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <label htmlFor="hAbertura">Hora de Abertura</label>
                        <input placeholder={local.horaAbertura} onChange={onAberturaChange} id="hAbertura" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <label htmlFor="hFecho">Hora de Fecho</label>
                        <input placeholder={local.horaFecho} onChange={onFechoChange} id="hFecho" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <label htmlFor="website">Website</label>
                        <input placeholder={local.website} onChange={onWebsiteChange} id="website" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <div>
                            <button type="submit" className="mt-2 bg-blue-300 p-1 rounded px-2 w-full hover:ring">Confirmar Alterações</button>
                        </div>
                    </form>
                </div>

                <div className="w-96 mt-10 p-3 border-2 rounded border-black ml-2">
                    <h3 className="text-center text-lg font-bold mb-2">Editar/Adicionar Eventos</h3>
                    <form className="flex flex-col">
                        <label htmlFor="evento">Evento</label>
                        <input placeholder="Ex:. Visita Guiada" onChange={onEventoChange} id="evento" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <label htmlFor="novoevento">Novo Nome de Evento</label>
                        <input placeholder="Ex:. Visita Guiada" onChange={onNovoEventoChange} id="novoevento" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <label htmlFor="descricaoEvento">Descricao</label>
                        <input placeholder="Percurso pelo Museu" onChange={onDescricaoEventoChange} id="descricaoEvento" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <label htmlFor="data">Data</label>
                        <input placeholder="Ex:. 2022-01-28 10:00:00" onChange={onDataChange} id="data" type="text" className="border-2 border-blue-200 w-full px-1 rounded"/>
                        <div>
                            <button onClick={onEditClick} className="mt-2 bg-blue-300 p-1 rounded px-2 w-full hover:ring">Confirmar Alterações</button>
                            <button onClick={onAddClick} className="mt-2 bg-blue-300 p-1 rounded px-2 w-full hover:ring">Adicionar Evento</button>
                        </div>
                    </form>
                </div>
            </div>
                        <Link to="/">
                            <h3 className="mt-8 text-center">Voltar</h3>
                            <img src={logo} alt="Voltar à aplicação!" width={100}/>
                        </Link>
        </div>
    )
}