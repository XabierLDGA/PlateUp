import api from './api'

export default {
  getStats() {
    return api.get('/admin/stats')
  },
  getRecipesByCategory() {
    return api.get('/admin/recipes-by-category')
  },
  getTopUsers() {
    return api.get('/admin/top-users')
  },
  getTopRecipes() {
    return api.get('/admin/top-recipes')
  },
  getUsersByMonth() {
    return api.get('/admin/users-by-month')
  },
  updateUserRole(userId, role) {
    return api.put(`/users/${userId}/role`, { role })
  },
}
