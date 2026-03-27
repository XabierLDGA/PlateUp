import api from './api'

export default {
  getByRecipeId(recipeId) {
    return api.get(`/recipesteps/recipe/${recipeId}`)
  },

  create(payload) {
    return api.post('/recipesteps', payload)
  },

  update(id, payload) {
    return api.put(`/recipesteps/${id}`, payload)
  },

  remove(id) {
    return api.delete(`/recipesteps/${id}`)
  },
}