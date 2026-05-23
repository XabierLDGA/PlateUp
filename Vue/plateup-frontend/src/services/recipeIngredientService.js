import api from './api'

export default {
  // Obtiene la relación de ingredientes con sus cantidades para una receta concreta
  getByRecipeId(recipeId) {
    return api.get(`/recipeingredients/recipe/${recipeId}`)
  },

  // Añade un ingrediente con su cantidad a una receta
  create(payload) {
    return api.post('/recipeingredients', payload)
  },

  // Elimina un ingrediente de una receta
  remove(recipeId, ingredientId) {
    return api.delete(`/recipeingredients/${recipeId}/${ingredientId}`)
  },
}