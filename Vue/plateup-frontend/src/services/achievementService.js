import api from './api'

export default {
  // Obtiene la lista completa de logros disponibles en la app
  getAll() {
    return api.get('/achievements')
  },

  // Obtiene un logro concreto por su identificador
  getById(id) {
    return api.get(`/achievements/${id}`)
  },

  // Crea un nuevo logro con los datos proporcionados
  create(payload) {
    return api.post('/achievements', payload)
  },

  // Actualiza los datos de un logro existente
  update(id, payload) {
    return api.put(`/achievements/${id}`, payload)
  },

  // Elimina un logro por su identificador
  remove(id) {
    return api.delete(`/achievements/${id}`)
  },
}