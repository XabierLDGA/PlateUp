import api from './api'

export default {
  // Obtiene el catálogo completo de ingredientes disponibles
  getAll() {
    return api.get('/ingredients')
  },

  // Obtiene los ingredientes asociados a una receta concreta
  getByRecipeId(recipeId) {
    return api.get(`/ingredients/recipe/${recipeId}`)
  },

  // Obtiene un ingrediente por su identificador
  getById(id) {
    return api.get(`/ingredients/${id}`)
  },

  // Crea un nuevo ingrediente en el sistema
  create(payload) {
    return api.post('/ingredients', payload)
  },

  // Actualiza los datos de un ingrediente existente
  update(id, payload) {
    return api.put(`/ingredients/${id}`, payload)
  },

  // Elimina un ingrediente por su identificador
  remove(id) {
    return api.delete(`/ingredients/${id}`)
  },
}