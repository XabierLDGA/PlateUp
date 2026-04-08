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