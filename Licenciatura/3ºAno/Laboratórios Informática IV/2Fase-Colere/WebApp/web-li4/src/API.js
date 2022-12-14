const API_URL = "http://localhost:8080"

export async function listar_locais(){
    const response = await fetch(`${API_URL}/locais`)
    return response.json()
}

export async function local_gestor(){
    const response = await fetch(`${API_URL}/menuGestor?email=biscainhos@gmail.com`)
    return response.json()
}

export async function get_ip(){
    const response = await fetch('https://geolocation-db.com/json/')
    return response.json()
}


