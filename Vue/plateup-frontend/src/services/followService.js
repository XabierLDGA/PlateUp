import api from './api'

function formatDateTimeForBackend(date = new Date()) {
  return date.toISOString().slice(0, 19)
}

function normalizeIds(followerId, followedId) {
  return {
    followerId: Number(followerId),
    followedId: Number(followedId),
  }
}

export default {
  getAll() {
    return api.get('/follows')
  },

  getFollowing(userId) {
    return api.get(`/follows/following/${userId}`)
  },

  getFollowers(userId) {
    return api.get(`/follows/followers/${userId}`)
  },

  getById(followerId, followedId) {
    const ids = normalizeIds(followerId, followedId)
    return api.get(`/follows/${ids.followerId}/${ids.followedId}`)
  },

  create(payload) {
    const normalizedPayload = {
      followerId: Number(payload.followerId),
      followedId: Number(payload.followedId),
      createdAt: payload.createdAt || formatDateTimeForBackend(),
    }

    if (payload.status) {
      normalizedPayload.status = payload.status
    }

    return api.post('/follows', normalizedPayload)
  },

  remove(followerId, followedId) {
    const ids = normalizeIds(followerId, followedId)
    return api.delete(`/follows/${ids.followerId}/${ids.followedId}`)
  },
}