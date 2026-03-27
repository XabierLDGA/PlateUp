import api from './api'

export default {
  getAll() {
    return api.get('/achievements')
  },

  getById(id) {
    return api.get(`/achievements/${id}`)
  },

  create(payload) {
    return api.post('/achievements', payload)
  },

  update(id, payload) {
    return api.put(`/achievements/${id}`, payload)
  },

  remove(id) {
    return api.delete(`/achievements/${id}`)
  },
}