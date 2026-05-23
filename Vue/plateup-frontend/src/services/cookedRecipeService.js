import api from './api'

export default {
  // Obtiene todas las recetas que ha cocinado un usuario concreto
  getByUserId(userId) {
    return api.get(`/cooked/user/${userId}`)
  },

  // Comprueba si un usuario ya ha cocinado una receta específica
  getEntry(userId, recipeId) {
    return api.get(`/cooked/user/${userId}/recipe/${recipeId}`)
  },

  // Guarda o actualiza el registro de una receta cocinada, incluyendo el tiempo empleado
  save(payload) {
    // payload: { userId, recipeId, elapsedSeconds }
    return api.post('/cooked', payload)
  },

  // Elimina el registro de una receta cocinada por un usuario
  remove(userId, recipeId) {
    return api.delete(`/cooked/user/${userId}/recipe/${recipeId}`)
  },
}