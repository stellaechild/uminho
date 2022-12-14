import React, { useState } from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import ClientSpace from "./ClientSpace";
import ManagerSpace from "./ManagerSpace";

export default function Sidebar({fun, pLocalizacao,locaisAvaliacaoMap,locaisProximidadeMap,updateStateMap}){
    const [isOpen,setIsOpen] = useState(false);

    const intermediario = texto => {
        if(fun){
            fun(texto)
            setIsOpen(false)
        }
    }

    const partilhaLocalizacao = localizcao => {
        if(pLocalizacao){
            pLocalizacao(localizcao)
        }
    }

    const locaisAvaliacao = locais => {
        if(locaisAvaliacaoMap){
            locaisAvaliacaoMap(locais)
        }
    }

    const locaisProximidade = locais => {
        if(locaisProximidadeMap){
            locaisProximidadeMap(locais)
        }
    }

    const updateStateSide = () => {
        if(updateStateMap){
            updateStateMap()
        }
    }

    return (
        <>
            {!isOpen ?
                (
                    <div className="bottom-0 right-0 fixed bg-green-400 h-full ">
                        <button onClick={() => setIsOpen(!isOpen)} className="fixed bottom-5 right-5 z-10">
                            <FontAwesomeIcon icon={faUser} size="2x"></FontAwesomeIcon>
                        </button>
                    </div>
                )
            :
                (   
                        <button onClick={() => setIsOpen(!isOpen)} className="fixed bottom-5 right-5 z-10">
                            <FontAwesomeIcon icon={faUser} size="2x"></FontAwesomeIcon>
                        </button>
                )
            }    
            <div className={`bottom-0 right-0 fixed bg-white h-full ${isOpen ?'translate-x-0' :'translate-x-full'} ease-in-out duration-300`}>
                <ClientSpace 
                    submit={intermediario}
                    partilhaLocalizacao={partilhaLocalizacao}
                    locaisAvaliacao={locaisAvaliacao}
                    locaisProximidade={locaisProximidade}
                    updateStateSide={updateStateSide}
                />     
            </div>
        </>
    )
}