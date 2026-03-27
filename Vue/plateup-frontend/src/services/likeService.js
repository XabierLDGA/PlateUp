import api from './api'

export default {
  getAll() {
    return api.get('/likes')
  },

  getById(userId, recipeId) {
    return api.get(`/likes/${userId}/${recipeId}`)
  },

  create(payload) {
    return api.post('/likes', payload)
  },

  remove(userId, recipeId) {
    return api.delete(`/likes/${userId}/${recipeId}`)
  },
}