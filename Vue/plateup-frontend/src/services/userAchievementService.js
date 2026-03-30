import api from './api'

export default {
  getAll() {
    return api.get('/userachievements')
  },

  getByUserId(userId) {
    return api.get(`/userachievements/user/${userId}`)
  },

  getById(userId, achievementId) {
    return api.get(`/userachievements/${userId}/${achievementId}`)
  },

  create(payload) {
    return api.post('/userachievements', payload)
  },

  update(userId, achievementId, payload) {
    return api.put(`/userachievements/${userId}/${achievementId}`, payload)
  },

  remove(userId, achievementId) {
    return api.delete(`/userachievements/${userId}/${achievementId}`)
  },
}