import api from './api'

export default {
  /** Fetch all cooked entries for a user */
  getByUserId(userId) {
    return api.get(`/cooked/user/${userId}`)
  },

  /** Check if a specific recipe has been cooked by a user */
  getEntry(userId, recipeId) {
    return api.get(`/cooked/user/${userId}/recipe/${recipeId}`)
  },

  /** Save (or update) a cooked entry after finishing cooking */
  save(payload) {
    // payload: { userId, recipeId, elapsedSeconds }
    return api.post('/cooked', payload)
  },

  /** Remove a cooked entry */
  remove(userId, recipeId) {
    return api.delete(`/cooked/user/${userId}/recipe/${recipeId}`)
  },
}