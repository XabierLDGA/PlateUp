import api from './api'

export default {
  getAll() {
    return api.get('/likes')
  },

  getByRecipeId(recipeId) {
    return api.get(`/likes/recipe/${recipeId}`)
  },

  getByUserId(userId) {
    return api.get(`/likes/user/${userId}`)
  },

  getCounts(recipeIds) {
    if (!recipeIds || recipeIds.length === 0) return Promise.resolve({ data: {} })
    return api.get('/likes/counts', { params: { recipeIds } })
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