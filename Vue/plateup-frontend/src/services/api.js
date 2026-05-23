import axios from 'axios'

// Clave usada para guardar el token JWT en el localStorage del navegador
const TOKEN_KEY = 'plateup_auth_token'

// Instancia de axios preconfigurada con la URL base del backend y cabeceras por defecto
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Recupera el token JWT guardado en el localStorage, si existe
export function getStoredToken() {
  return localStorage.getItem(TOKEN_KEY)
}

// Guarda el token en el localStorage y lo añade a las cabeceras de todas las peticiones.
// Si se pasa null o undefined, borra el token y elimina la cabecera de autorización.
export function setAuthToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
    api.defaults.headers.common.Authorization = `Bearer ${token}`
  } else {
    localStorage.removeItem(TOKEN_KEY)
    delete api.defaults.headers.common.Authorization
  }
}

// Si ya había un token guardado de sesiones anteriores, lo restauramos al arrancar
const existingToken = getStoredToken()
if (existingToken) {
  setAuthToken(existingToken)
}

// Interceptor de respuestas: gestiona errores de autenticación de forma global
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status

    if (status === 401 || status === 403) {
      console.warn('Token inválido o expirado')

      // eliminar token
      localStorage.removeItem('plateup_auth_token')

      // quitar header
      delete api.defaults.headers.common.Authorization

      // redirigir a login
      window.location.href = '/login'
    }

    return Promise.reject(error)
  }
)

export default api