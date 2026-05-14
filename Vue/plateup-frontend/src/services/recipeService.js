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

  getFeed({ page = 0, size = 10, category } = {}) {
    const params = { page, size }
    if (category && category !== 'All') params.category = category
    return api.get('/recipes/feed', { params })
  },

  getByIds(ids) {
    if (!ids || ids.length === 0) return Promise.resolve({ data: [] })
    return api.get('/recipes/by-ids', { params: { ids } })
  },

  countByUser(userId) {
    return api.get(`/recipes/count/${userId}`)
  },
}