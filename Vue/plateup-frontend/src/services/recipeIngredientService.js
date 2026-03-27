import api from './api'

export default {
  getByRecipeId(recipeId) {
    return api.get(`/recipeingredients/recipe/${recipeId}`)
  },

  create(payload) {
    return api.post('/recipeingredients', payload)
  },

  remove(recipeId, ingredientId) {
    return api.delete(`/recipeingredients/${recipeId}/${ingredientId}`)
  },
}