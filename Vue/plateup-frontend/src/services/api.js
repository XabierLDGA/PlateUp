import axios from 'axios'

const TOKEN_KEY = 'plateup_auth_token'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

export function getStoredToken() {
  return localStorage.getItem(TOKEN_KEY)
}

// Si se pasa null, borra el token y elimina la cabecera de autorización.
export function setAuthToken(token) {
  if (token) {
    localStorage.setItem(TOKEN_KEY, token)
    api.defaults.headers.common.Authorization = `Bearer ${token}`
  } else {
    localStorage.removeItem(TOKEN_KEY)
    delete api.defaults.headers.common.Authorization
  }
}

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
      localStorage.removeItem('plateup_auth_token')
      delete api.defaults.headers.common.Authorization
      window.location.href = '/login'
    }

    return Promise.reject(error)
  }
)

export default api