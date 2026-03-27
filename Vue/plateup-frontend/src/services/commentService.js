import api from './api'

export default {
  getAll() {
    return api.get('/comments')
  },

  getById(id) {
    return api.get(`/comments/${id}`)
  },

  getByRecipeId(recipeId) {
    return api.get(`/comments/recipe/${recipeId}`)
  },

  create(payload) {
    return api.post('/comments', payload)
  },

  update(id, payload) {
    return api.put(`/comments/${id}`, payload)
  },

  remove(id) {
    return api.delete(`/comments/${id}`)
  },
}