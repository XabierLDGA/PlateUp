import api from './api'

export default {
  getAll() {
    return api.get('/recipes')
  },

  getById(id) {
    return api.get(`/recipes/${id}`)
  },

  getByUserId(userId) {
    return api.get(`/recipes/user/${userId}`)
  },

  create(payload) {
    return api.post('/recipes', payload)
  },

  update(id, payload) {
    return api.put(`/recipes/${id}`, payload)
  },

  remove(id) {
    return api.delete(`/recipes/${id}`)
  },

  getFeed(userId, { page = 0, size = 10, category } = {}) {
    const params = { userId, page, size }
    if (category && category !== 'All') params.category = category
    return api.get('/recipes/feed', { params })
  },

  countByUser(userId) {
    return api.get(`/recipes/count/${userId}`)
  },
}