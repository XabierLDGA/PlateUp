import api from './api'

export default {
  getAll() {
    return api.get('/ingredients')
  },

  getByRecipeId(recipeId) {
    return api.get(`/ingredients/recipe/${recipeId}`)
  },

  getById(id) {
    return api.get(`/ingredients/${id}`)
  },

  create(payload) {
    return api.post('/ingredients', payload)
  },

  update(id, payload) {
    return api.put(`/ingredients/${id}`, payload)
  },

  remove(id) {
    return api.delete(`/ingredients/${id}`)
  },
}