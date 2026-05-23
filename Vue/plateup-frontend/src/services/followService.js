import api from './api'

// Formatea la fecha actual en el formato que espera el backend (sin milisegundos ni zona horaria)
function formatDateTimeForBackend(date = new Date()) {
  return date.toISOString().slice(0, 19)
}

// Convierte los IDs de seguidor y seguido a número para evitar problemas de tipo
function normalizeIds(followerId, followedId) {
  return {
    followerId: Number(followerId),
    followedId: Number(followedId),
  }
}

export default {
  // Obtiene todas las relaciones de seguimiento de la plataforma
  getAll() {
    return api.get('/follows')
  },

  // Obtiene la lista de usuarios que sigue un usuario concreto
  getFollowing(userId) {
    return api.get(`/follows/following/${userId}`)
  },

  // Obtiene la lista de seguidores de un usuario concreto
  getFollowers(userId) {
    return api.get(`/follows/followers/${userId}`)
  },

  // Comprueba si existe una relación de seguimiento entre dos usuarios
  getById(followerId, followedId) {
    const ids = normalizeIds(followerId, followedId)
    return api.get(`/follows/${ids.followerId}/${ids.followedId}`)
  },

  // Crea una nueva relación de seguimiento entre dos usuarios
  create(payload) {
    const normalizedPayload = {
      followerId: Number(payload.followerId),
      followedId: Number(payload.followedId),
      // Si no se indica fecha, se usa la actual en el formato correcto
      createdAt: payload.createdAt || formatDateTimeForBackend(),
    }

    if (payload.status) {
      normalizedPayload.status = payload.status
    }

    return api.post('/follows', normalizedPayload)
  },

  // Elimina la relación de seguimiento entre dos usuarios (dejar de seguir)
  remove(followerId, followedId) {
    const ids = normalizeIds(followerId, followedId)
    return api.delete(`/follows/${ids.followerId}/${ids.followedId}`)
  },
}