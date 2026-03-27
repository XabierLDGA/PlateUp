import api from './api'

export default {
  getAll() {
    return api.get('/users')
  },

  getById(id) {
    return api.get(`/users/${id}`)
  },

  login(payload) {
    return api.post('/users/login', payload)
  },

  register(payload) {
    return api.post('/users/register', payload)
  },

  create(payload) {
    return api.post('/users', payload)
  },

  update(id, payload) {
    return api.put(`/users/${id}`, payload)
  },

  remove(id) {
    return api.delete(`/users/${id}`)
  },
}