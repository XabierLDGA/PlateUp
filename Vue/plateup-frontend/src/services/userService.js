import api from './api'

export default {
  // Obtiene la lista completa de usuarios registrados en la plataforma
  getAll() {
    return api.get('/users')
  },

  // Obtiene los datos de un usuario concreto por su identificador
  getById(id) {
    return api.get(`/users/${id}`)
  },

  // Inicia sesión con email y contraseña, devuelve el token JWT y los datos del usuario
  login(payload) {
    return api.post('/users/login', payload)
  },

  // Registra un nuevo usuario en la plataforma
  register(payload) {
    return api.post('/users/register', payload)
  },

  // Crea un usuario directamente (uso administrativo)
  create(payload) {
    return api.post('/users', payload)
  },

  // Actualiza el perfil o los datos de un usuario existente
  update(id, payload) {
    return api.put(`/users/${id}`, payload)
  },

  // Elimina una cuenta de usuario por su identificador
  remove(id) {
    return api.delete(`/users/${id}`)
  },

  // Registra el acceso diario del usuario para otorgar puntos de racha y experiencia
  dailyCheckin(id) {
    return api.post(`/users/${id}/daily-checkin`)
  },
}