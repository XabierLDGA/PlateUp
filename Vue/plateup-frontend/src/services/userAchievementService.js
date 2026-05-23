import api from './api'

export default {
  // Obtiene todos los logros desbloqueados por todos los usuarios
  getAll() {
    return api.get('/userachievements')
  },

  // Obtiene los logros que ha desbloqueado un usuario específico
  getByUserId(userId) {
    return api.get(`/userachievements/user/${userId}`)
  },

  // Comprueba si un usuario concreto tiene desbloqueado un logro específico
  getById(userId, achievementId) {
    return api.get(`/userachievements/${userId}/${achievementId}`)
  },

  // Registra el desbloqueo de un logro para un usuario
  create(payload) {
    return api.post('/userachievements', payload)
  },

  // Actualiza el estado de un logro desbloqueado por un usuario (por ejemplo, para marcarlo como visto)
  update(userId, achievementId, payload) {
    return api.put(`/userachievements/${userId}/${achievementId}`, payload)
  },

  // Elimina la asociación entre un usuario y un logro
  remove(userId, achievementId) {
    return api.delete(`/userachievements/${userId}/${achievementId}`)
  },
}