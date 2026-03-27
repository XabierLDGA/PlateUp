import { defineStore } from 'pinia'
import userService from '../services/userService'
import { getUserAvatar } from '../utils/userAvatar'

const STORAGE_KEY = 'plateup_current_user'

function normalizeUser(user) {
  if (!user) return null

  return {
    ...user,
    avatarUrl: getUserAvatar(user),
  }
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    currentUser: null,
    isAuthenticated: false,
    loading: false,
    initialized: false,
  }),

  getters: {
    userInitials: (state) => {
      if (!state.currentUser?.displayName) return 'PU'
      return state.currentUser.displayName
        .split(' ')
        .map((part) => part[0])
        .join('')
        .slice(0, 2)
        .toUpperCase()
    },
  },

  actions: {
    setSessionUser(user) {
      const normalizedUser = normalizeUser(user)
      this.currentUser = normalizedUser
      this.isAuthenticated = Boolean(normalizedUser?.id)
      this.initialized = true

      if (normalizedUser) {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(normalizedUser))
      } else {
        localStorage.removeItem(STORAGE_KEY)
      }
    },

    async initialize() {
      if (this.initialized) return
      await this.loadFromStorage()
    },

    async loadFromStorage() {
      const rawUser = localStorage.getItem(STORAGE_KEY)

      if (!rawUser) {
        this.currentUser = null
        this.isAuthenticated = false
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

        const response = await userService.getById(parsedUser.id)
        const freshUser = response.data || null

        if (!freshUser?.id) {
          this.logout()
          this.initialized = true
          return
        }

        this.setSessionUser(freshUser)
      } catch (error) {
        console.error('Error restoring user session:', error)
        this.logout()
        this.initialized = true
      }
    },

    async login(credentials) {
      this.loading = true

      try {
        const response = await userService.login(credentials)
        this.setSessionUser(response.data)
        return response.data
      } finally {
        this.loading = false
      }
    },

    async register(payload) {
      this.loading = true

      try {
        const response = await userService.register(payload)
        this.setSessionUser(response.data)
        return response.data
      } finally {
        this.loading = false
      }
    },

    logout() {
      this.currentUser = null
      this.isAuthenticated = false
      this.initialized = true
      localStorage.removeItem(STORAGE_KEY)
    },
  },
})