import { defineStore } from 'pinia'
import userService from '../services/userService'
import { getStoredToken, setAuthToken } from '../services/api'

const STORAGE_KEY = 'plateup_current_user'

function normalizeUser(user) {
  if (!user) return null

  return {
    ...user,
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    currentUser: null,
    authToken: null,
    isAuthenticated: false,
    loading: false,
    initialized: false,
  }),

  getters: {
    // Extrae las iniciales del nombre del usuario para mostrarlas en el avatar
    userInitials: (state) => {
      if (!state.currentUser?.displayName) return 'PU'
      return state.currentUser.displayName
        .split(' ')
        .map((part) => part[0])
        .join('')
        .slice(0, 2)
        .toUpperCase()
    },
    // Comprueba si el usuario actual tiene rol de administrador
    isAdmin: (state) => state.currentUser?.role === 'ADMIN',
  },

  actions: {
    setSessionUser(user) {
      const normalizedUser = normalizeUser(user)
      this.currentUser = normalizedUser
      this.isAuthenticated = Boolean(normalizedUser?.id && this.authToken)
      this.initialized = true

      if (normalizedUser) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(normalizedUser))
      } else {
        localStorage.removeItem(STORAGE_KEY)
      }
    },

    setSessionToken(token) {
      this.authToken = token || null
      setAuthToken(this.authToken)
      this.isAuthenticated = Boolean(this.currentUser?.id && this.authToken)
    },

    async initialize() {
      if (this.initialized) return
      await this.loadFromStorage()
    },

    async loadFromStorage() {
      const rawUser = localStorage.getItem(STORAGE_KEY)
      const storedToken = getStoredToken()

      // Si falta el usuario o el token guardados, cerramos la sesión por seguridad
      if (!rawUser || !storedToken) {
        this.logout()
        this.initialized = true
        return
      }

      try {
        const parsedUser = JSON.parse(rawUser)

        if (!parsedUser?.id) {
          this.logout()
          this.initialized = true
          return
        }

        this.setSessionToken(storedToken)

        // Verificamos que el usuario sigue existiendo en el servidor antes de restaurar la sesión
        const response = await userService.getById(parsedUser.id)
        const freshUser = response.data || null

        if (!freshUser?.id) {
          this.logout()
          this.initialized = true
          return
        }

        // Aprovechamos la restauración de sesión para registrar el acceso diario
        const checkinResponse = await userService.dailyCheckin(freshUser.id)
        this.setSessionUser(checkinResponse.data)
      } catch (error) {
        console.error('Error restoring user session:', error)
        this.logout()
        this.initialized = true
      }
    },

    // Inicia sesión con las credenciales del usuario, guarda el token y registra el acceso diario
    async login(credentials) {
      this.loading = true

      try {
        const response = await userService.login(credentials)
        const token = response.data?.token || null
        const user = response.data?.user || null

        this.setSessionToken(token)
        this.setSessionUser(user)

        if (user?.id) {
          const checkinResponse = await userService.dailyCheckin(user.id)
          this.setSessionUser(checkinResponse.data)
          return checkinResponse.data
        }

        return user
      } finally {
        this.loading = false
      }
    },

    // Registra un nuevo usuario, inicia su sesión automáticamente y registra el acceso diario
    async register(payload) {
      this.loading = true

      try {
        const response = await userService.register(payload)
        const token = response.data?.token || null
        const user = response.data?.user || null

        this.setSessionToken(token)
        this.setSessionUser(user)

        if (user?.id) {
          const checkinResponse = await userService.dailyCheckin(user.id)
          this.setSessionUser(checkinResponse.data)
          return checkinResponse.data
        }

        return user
      } finally {
        this.loading = false
      }
    },

    async refreshCurrentUser() {
      if (!this.currentUser?.id || !this.authToken) return null

      const response = await userService.getById(this.currentUser.id)
      const freshUser = response.data || null

      if (!freshUser?.id) {
        this.logout()
        return null
      }

      this.setSessionUser(freshUser)
      return freshUser
    },

    logout() {
      this.currentUser = null
      this.authToken = null
      this.isAuthenticated = false
      this.initialized = true
      localStorage.removeItem(STORAGE_KEY)
      setAuthToken(null)
    },
  },
})