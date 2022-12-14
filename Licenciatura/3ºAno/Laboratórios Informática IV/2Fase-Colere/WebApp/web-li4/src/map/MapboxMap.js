import React, {useEffect, useState} from "react";
import ReactMapGL, {Marker, Popup} from "react-map-gl"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMapMarkerAlt , faGlobe, faStar, faFileAlt, faClock, faCalendarAlt, faBezierCurve} from "@fortawesome/free-solid-svg-icons"
import '../index.css';
import data from "../data.json"
import _ from "lodash"
import { get_ip, listar_locais } from "../API"
import axios from "axios";
import Sidebar from "../components/Sidebar"
const API_URL = "http://localhost:8080"


export default function MapboxMap (props) { 
    const [locais,setLocais] = useState([])
    const [showPopUp,setShowPopUp] = useState({})
    const [showHover,setShowHover] = useState({})
    const [isEvaluating,setIsEvaluating] = useState(false)
    const [avaliacao,setAvaliacao] = useState(0)
    const [ip,setIP] = useState("")
    const [alteracao, setAlteracao] = useState(false)
    const [pesquisa, setPesquisa] = useState("")
    const [userPos,setUserPos] = useState({
        latitude: 41.55126845878385, 
        longitude: -8.427136315682446
    })
    const [viewport, setViewport] = useState({
        latitude: 41.55126845878385, 
        longitude: -8.427136315682446,
        zoom: 17
      })

      useEffect(() => {
          (async () => {
            const locais_listados = await listar_locais()
            setLocais(locais_listados)
      })()
      },[alteracao])

      useEffect(()=> {
        (async () => {
            const res = await get_ip()
            setIP(res.IPv4)
        })()
      },[])

      useEffect(() => {
        navigator.geolocation.getCurrentPosition((pos) =>{
            if(pos.coords.latitude < 41.57883 && pos.coords.longitude > -8.36834 && pos.coords.latitude > 41.52126 && pos.coords.longitude < -8.46302){
                setViewport({
                'latitude' : pos.coords.latitude,
                'longitude' : pos.coords.longitude,
                'zoom' : 17
            })}else{
                setViewport({
                    'latitude' : 41.55126845878385, 
                    'longitude' : -8.427136315682446,
                    'zoom' : 17
                  })
            }
        },()=>{
            setViewport({
                'latitude' : 41.55126845878385, 
                'longitude' : -8.427136315682446,
                'zoom' : 17
              })
        })
      },[])

      useEffect(() => {
        const res = locais.find(local => local.nome === pesquisa)
        if(res){
            setViewport({
                'latitude' : res.localizacao.latitude, 
                'longitude' : res.localizacao.longitude,
                'zoom' : 18
              })
            setShowPopUp({[pesquisa]:true})
        }
      },[pesquisa])

    const onSubmitClick = async nomeEvento => {
        
       axios.post(`${API_URL}/locais`,{
            'nomeLocal' : nomeEvento,
            'ip' : ip,
            'classificacao' : avaliacao
        })
        setIsEvaluating(false)
        setAlteracao(!alteracao)
        alert('Avaliado com sucesso!')
    }

       

      const onEvaluatingClick = () => {
          setIsEvaluating(!isEvaluating)
      }

      const handleSelectChange = (event) => {
        event.preventDefault()
        setAvaliacao(event.target.value)
      }

      function toChildren(texto){
          setPesquisa(texto)
      }

      function pLocalizacao(localizacao){
          if(localizacao.longitude > -8.462061837223589 
            && localizacao.longitude < -8.36318489278102
            && localizacao.latitude < 41.56987681201655
            && localizacao.latitude > 41.521441332635064){
            setViewport({
                ...localizacao,
                'zoom' : 17
            })
            setUserPos({...localizacao})
        }else{
            setViewport({
                'latitude': 41.55126845878385, 
                'longitude': -8.427136315682446,
                'zoom': 17
            })
        }
      }

      function locaisAvaliacaoMap(locais){
          setLocais(locais.data)
      }

      function locaisProximidadeMap(locais){
          console.log(locais.data)
          setLocais(locais.data)
      }

      function updateStateMap(){
          setAlteracao(!alteracao)
      }

    return (
        <>
        <ReactMapGL 
            {...viewport}
            mapboxApiAccessToken="pk.eyJ1Ijoib21pcGFjaGVjbyIsImEiOiJja3lsbXlseXQwc3FmMm51Zm12cjMyYjQzIn0.85zsdfF-eVpN7_Skx3j_3g"
            mapStyle={"mapbox://styles/mapbox/streets-v11"}
            width="100vw" 
            height="100vh" 
            onViewportChange={setViewport} 
        >
            <Marker
                latitude={userPos.latitude}
                longitude={userPos.longitude}
                offsetLeft={-12}
                offsetTop={-32}
            >
                <FontAwesomeIcon 
                    icon={faMapMarkerAlt} 
                    size="2x"
                    color="red"
                ></FontAwesomeIcon>
                <h3>Você</h3>
            </Marker>
            {
                locais.map(entry => {
                    return(
                    <>
                    <Marker
                        key={entry.nome}
                        latitude={entry.localizacao.latitude}
                        longitude={entry.localizacao.longitude}
                        offsetLeft={-12}
                        offsetTop={-32}
                    >
                        {
                            !_.isEmpty(entry.eventos) ?
                            <div onClick={()=>{
                                setShowPopUp({
                                    [entry.nome]:true,
                                })
                            }} className="hover:cursor-pointer">
                                <FontAwesomeIcon 
                                    icon={faMapMarkerAlt} 
                                    size="2x"
                                    color="green"
                                >
                                </FontAwesomeIcon>
                            </div>
                                    :
                            <div onClick={()=>{
                                setShowPopUp({
                                    [entry.nome]:true,
                                })
                            }}
                            className="hover:cursor-pointer">
                                <FontAwesomeIcon 
                                    icon={faMapMarkerAlt} 
                                    size="2x"
                                    color="black"
                                >
                                </FontAwesomeIcon>
                            </div>
                        }
                        
                    </Marker>
                    {
                        showPopUp[entry.nome] ? (
                            <Popup
                                className="min-w-fit z-10"
                                latitude={entry.localizacao.latitude}
                                longitude={entry.localizacao.longitude}
                                closeButton={true}
                                onClose={()=> setShowPopUp({})}
                                dynamicPosition={true}
                                anchor="top"
            
                            >
                                <div className="p-2">
                                    <h3 className="text-xl font-bold p-2 text-center">{entry.nome}</h3>
                                    <div className="flex p-1">
                                        <FontAwesomeIcon size="lg" icon={faGlobe}/><a className="ml-2 hover:text-lime-500 hover:font-bold text-lime-500" href={entry.website}>Website</a>
                                    </div>
                                    <div className="flex p-1 justify-between">
                                        <div className="flex">
                                            <FontAwesomeIcon size="lg" icon={faStar}/>
                                            <p className="ml-2">{entry.classificacoes}</p>
                                        </div>    
                                            <button className="hover:ring border-2 border-black rounded px-1 hover:bg-lime-200" onClick={onEvaluatingClick}>Avaliar</button>
                                    </div>
                                        {
                                        isEvaluating ? 
                                            <form className="flex flex-col">
                                                <label className="mr-2" htmlFor="classificacao">Classificação</label>
                                                <select id="classificacao" onChange={handleSelectChange}>
                                                    <option value="0">0</option>
                                                    <option value="1">1</option>
                                                    <option value="2">2</option>
                                                    <option value="3">3</option>
                                                    <option value="4">4</option>
                                                    <option value="5">5</option>
                                                </select>
                                                <button onClick={()=>onSubmitClick(entry.nome)} className="mr-0 border-2 border-black mt-1 rounded hover:cursor-pointer">Confirmar</button>
                                            </form>
                                            
                                            :
                                            <div>
                                                
                                            </div>
                                        }
                                    
                                    <div className="flex p-1">
                                        <FontAwesomeIcon size="lg" icon={faMapMarkerAlt}/><p className="ml-3">{entry.localizacao.endereco}</p>
                                    </div>
                                    <div className="flex p-1">
                                        <FontAwesomeIcon size="lg" icon={faFileAlt}/><p className="ml-3">{entry.descricao}</p>
                                    </div>
                                    <div className="flex p-1">
                                        <FontAwesomeIcon size="lg" icon={faClock}/><p className="ml-2">{entry.horaAbertura} - {entry.horaFecho}</p>
                                    </div>
                                    {Object.values(entry.eventos).map(evento =>{
                                        return(
                                            <div 
                                            onMouseOver={()=>setShowHover({
                                                [entry.nome]:true,
                                            })} 
                                            onMouseLeave={()=>setShowHover({})}

                                            className="p-1"
                                            >       
                                                <div className="flex hover:font-bold">                              
                                                <FontAwesomeIcon size="lg" icon={faCalendarAlt}/><p className="ml-2">{evento.nome}</p>
                                                </div>
                                                {
                                                    showHover[entry.nome] ?
                                                        <div>
                                                            <p className="ml-10">{evento.descricao}</p>
                                                            <p className="ml-10">{evento.dataHora}</p>
                                                        </div>
                                                        :
                                                            null
                                                }
                                            </div>
                                        )
                                    })}
                                </div>
            
                            </Popup>
                    ):
                        null
                    }
                    
                    </>
                    )
                })
            }
            

        </ReactMapGL>
        <Sidebar 
            fun={toChildren}
            pLocalizacao={pLocalizacao}
            locaisAvaliacaoMap={locaisAvaliacaoMap}
            locaisProximidadeMap={locaisProximidadeMap}
            updateStateMap={updateStateMap}
            />
        </>
      )
  }

