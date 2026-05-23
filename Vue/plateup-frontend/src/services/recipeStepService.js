import api from './api'

export default {
  // Obtiene todos los pasos de elaboración de una receta, ordenados por su número de paso
  getByRecipeId(recipeId) {
    return api.get(`/recipesteps/recipe/${recipeId}`)
  },

  // Añade un nuevo paso a una receta
  create(payload) {
    return api.post('/recipesteps', payload)
  },

  // Actualiza el contenido de un paso de receta existente
  update(id, payload) {
    return api.put(`/recipesteps/${id}`, payload)
  },

  // Elimina un paso de receta por su identificador
  remove(id) {
    return api.delete(`/recipesteps/${id}`)
  },
}