import api from './api'

export default {
  // Obtiene todos los me gustas registrados en la plataforma
  getAll() {
    return api.get('/likes')
  },

  // Obtiene todos los me gustas de una receta específica
  getByRecipeId(recipeId) {
    return api.get(`/likes/recipe/${recipeId}`)
  },

  // Obtiene todas las recetas que le han gustado a un usuario
  getByUserId(userId) {
    return api.get(`/likes/user/${userId}`)
  },

  // Obtiene el número de me gustas para un conjunto de recetas de una sola vez
  getCounts(recipeIds) {
    // Si no se proporcionan IDs, devolvemos un objeto vacío sin hacer petición
    if (!recipeIds || recipeIds.length === 0) return Promise.resolve({ data: {} })
    return api.get('/likes/counts', { params: { recipeIds } })
  },

  // Comprueba si un usuario concreto ha dado me gusta a una receta
  getById(userId, recipeId) {
    return api.get(`/likes/${userId}/${recipeId}`)
  },

  // Registra un me gusta de un usuario en una receta
  create(payload) {
    return api.post('/likes', payload)
  },

  // Elimina el me gusta de un usuario en una receta (quitar el like)
  remove(userId, recipeId) {
    return api.delete(`/likes/${userId}/${recipeId}`)
  },
}