import React from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMapMarkedAlt, faFlag, faToolbox, faSearch, faSignInAlt, faTools,faTimes} from "@fortawesome/free-solid-svg-icons"
import { useState } from "react/cjs/react.development";
import {Link} from "react-router-dom"
import axios from "axios";
const API_URL = "http://localhost:8080"

export default function ClientSpace({submit,partilhaLocalizacao,locaisAvaliacao,locaisProximidade,updateStateSide}){
    const [isSearch,setIsSearch] = useState(false)
    const [pesquisa,setPesquisa] = useState("")
    const [percursoProximidade,setPercursoProximidade] = useState(false)
    const [percursoClassificacao,setPercursoClassificacao] = useState(false)
    const [numeroLocais,setNumeroLocais] = useState(0)
    const [numeroLocaisProximidade,setNumeroLocaisProximidade] = useState(0)
    const [isSharing,setIsSharing] = useState(false)
    const [userPos,setUserPos] = useState({
        'latitude':0,
        'longitude':0
    })

    const onChangePesquisa = event => {
        setPesquisa(event.target.value)
    }

    const onPesquisarClick = (e) => {
        e.preventDefault()
        if(submit){
            submit(pesquisa)
        }
        setPesquisa("")
    }

    const onPartilharLocalizacao = () => {
        if(partilhaLocalizacao){
            navigator.geolocation.getCurrentPosition((pos) => {
                partilhaLocalizacao({
                    'latitude' : pos.coords.latitude,
                    'longitude' : pos.coords.longitude
                })
                setIsSharing(true)
                setUserPos({
                    'latitude' : pos.coords.latitude,
                    'longitude' : pos.coords.longitude
                })
            },
            ()=>{}
            )
        }
    }

    const submitPorAvaliacao = async (e) => {
        e.preventDefault()
        const res = await axios.get(`${API_URL}/locais/percurso/avaliacao`, { params: { 'nLocais': numeroLocais } })
        if(locaisAvaliacao){
            locaisAvaliacao(res)
        }
    }

    const submitPorProximidade = async (e) => {
        e.preventDefault()
        if(isSharing){
            const res = await axios.get(`${API_URL}/locais/percurso/proximidade`, { params: { 
                'nLocais': numeroLocaisProximidade,
                'latitude': userPos.latitude,
                'longitude': userPos.longitude
             } })
            if(locaisProximidade){
                locaisProximidade(res)
            }
        }
    }

    const onChangeNumeroLocais = (e) => {
        setNumeroLocais(e.target.value)
    }

    const onChangeNumeroLocaisProximidade = (e) => {
        setNumeroLocaisProximidade(e.target.value)
    }

    const updateState = () => {
        if(updateStateSide){
            updateStateSide()
        }
    }

    return (
        <>
            <div className="flex flex-col p-10 w-full mr-6">
                <div className="my-4">
                    <FontAwesomeIcon className="mr-4" icon={faMapMarkedAlt}></FontAwesomeIcon>
                        <button onClick={onPartilharLocalizacao} className="hover:font-bold">Partilhar Localização</button>
                </div>
                <div className="my-4">
                    <FontAwesomeIcon className="mr-4" icon={faFlag}></FontAwesomeIcon>
                    <button onClick={() => setPercursoClassificacao(!percursoClassificacao)} className="hover:font-bold">Percurso Por Classificação</button>
                    {percursoClassificacao ? 
                    <div>
                        <form onSubmit={submitPorAvaliacao} className="flex flex-col">
                            <div className="flex justify-between px-1 my-1">
                                <label htmlFor="nLocais">Número de Locais</label>
                                <input  onChange={onChangeNumeroLocais} className="w-16 border-2 border-black rounded" id="nLocais" type="number"/>
                            </div>
                            
                            <button className="ml-2 px-1 bg-blue-300 border rounded hover:ring" type="submit">Obter Locais</button>
                        </form>
                    </div>
                    :
                    null
                    }
                </div>
                <div className="my-4">
                    <FontAwesomeIcon className="mr-4" icon={faFlag}></FontAwesomeIcon>
                    <button onClick={() => setPercursoProximidade(!percursoProximidade)} className="hover:font-bold">Percurso Por Proximidade</button>
                    {percursoProximidade ? 
                    <div>
                        <form onSubmit={submitPorProximidade} className="flex flex-col">
                            <div className="flex justify-between px-1 my-1">
                                <label htmlFor="nLocais">Número de Locais</label>
                                <input onChange={onChangeNumeroLocaisProximidade} className="w-16 border-2 border-black rounded" id="nLocais" type="number"/>
                            </div>
                            
                            <button className="ml-2 px-1 bg-blue-300 border rounded hover:ring" type="submit">Obter Locais</button>
                        </form>
                    </div>
                    :
                    null
                    }
                <div className="mt-5">
                    <FontAwesomeIcon className="mr-4" icon={faTimes}></FontAwesomeIcon>
                    <button onClick={updateState} className="hover:font-bold">Cancelar Pedido de Percurso</button>
                </div>
                </div>
                <div className="my-4 flex flex-col">
                    <div className="flex flex-row">
                        <FontAwesomeIcon className="mr-4" icon={faSearch}></FontAwesomeIcon>
                        <form className="flex" onSubmit={onPesquisarClick}>
                            <input className="border-2 border-black rounded" onChange={onChangePesquisa} type="text" placeholder="Pesquisa"/>
                            <button type="submit" className="ml-2 px-1 bg-blue-300 border rounded hover:ring">Pesquisar</button>
                        </form>
                    </div>
                      
                </div> 
                <div className="my-4">
                    <FontAwesomeIcon className="mr-4" icon={faToolbox}></FontAwesomeIcon>
                        <Link to="/manager">
                            <button className="hover:font-bold">Gerir Local</button>
                        </Link>
                </div>
            </div>
        </>
    )
}