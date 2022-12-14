import React, { useEffect } from "react"
import { useState } from "react/cjs/react.development"
import LocalManager from "./LocalManager"
import axios from 'axios'
import logo from "./logoOriginal.png"
import { Link } from "react-router-dom";
const API_URL = "http://localhost:8080"


export default function Manager(){
    const [email,setEmail] = useState("")
    const [password,setPassword] = useState("")
    const [isLogged,setIsLogged] = useState(false)
    const [state,setState] = useState(0)

    useEffect(() => {
        if(state === 200){
            setIsLogged(true)
        }
    },[state])

    const onEmailChangeHandler = (event) => {
        setEmail(event.target.value)
    }

    const onPasswordChangeHandler = (event) => {
        setPassword(event.target.value)
    }

    const onSubmitHandler = (event) => {
        event.preventDefault()
        
        axios.post(`${API_URL}/login`,{
            'email' : email,
            'password' : password
        })
        .then(response => {
            setState(response.status)
        })
        .catch(error => {
            setState(400)
            alert('Credenciais inválidas')
        })
    }

    return(
        <>
        {
        !isLogged ? 
            <div className="flex flex-col justify-center items-center h-screen">
                <img className="mb-10" src={logo} alt="Voltar à aplicação!" width={200}/>       
                <div className="w-64">
                    <form onSubmit={onSubmitHandler}>
                        <label htmlFor="email">Email</label>
                        <input onChange={onEmailChangeHandler} id="email" className="border-2 border-blue-200 w-full px-1 rounded" type="text"/>
                        <label htmlFor="password" className="mt-2">Password</label>
                        <input onChange={onPasswordChangeHandler} id="password" className="border-2 border-blue-200 w-full px-1 rounded" type="password"/>
                        <div className="flex">    
                            <button type="submit" className="mr-1 w-full bg-blue-300 border rounded p-1 mt-3 w-{45} hover:ring">Log in</button>
                            <Link to="/">
                                <h3 type="submit" className="ml-1 w-full bg-blue-300 border rounded p-1 mt-3 w-{45} hover:ring">Voltar</h3>
                            </Link>
                        </div>
                    </form>
                </div>            
            </div>
            :
            <LocalManager email={email}/>
        }
        </>
    )
}