import api from './api'

export default {
  // Obtiene todas las recetas de la plataforma
  getAll() {
    return api.get('/recipes')
  },

  // Obtiene una receta concreta por su identificador
  getById(id) {
    return api.get(`/recipes/${id}`)
  },

  // Obtiene todas las recetas publicadas por un usuario específico
  getByUserId(userId) {
    return api.get(`/recipes/user/${userId}`)
  },

  // Crea una nueva receta con los datos del formulario
  create(payload) {
    return api.post('/recipes', payload)
  },

  // Actualiza los datos de una receta existente
  update(id, payload) {
    return api.put(`/recipes/${id}`, payload)
  },

  // Elimina una receta por su identificador
  remove(id) {
    return api.delete(`/recipes/${id}`)
  },

  // Obtiene el feed de recetas paginado, opcionalmente filtrado por categoría
  getFeed({ page = 0, size = 10, category } = {}) {
    const params = { page, size }
    // Solo se añade la categoría si no es "All" (todas las categorías)
    if (category && category !== 'All') params.category = category
    return api.get('/recipes/feed', { params })
  },

  // Obtiene varias recetas a la vez a partir de sus IDs
  getByIds(ids) {
    // Si la lista está vacía, evitamos hacer una petición innecesaria
    if (!ids || ids.length === 0) return Promise.resolve({ data: [] })
    return api.get('/recipes/by-ids', { params: { ids } })
  },

  // Obtiene el número total de recetas publicadas por un usuario
  countByUser(userId) {
    return api.get(`/recipes/count/${userId}`)
  },
}