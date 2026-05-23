import api from './api'

export default {
  // Obtiene las estadísticas generales de la plataforma para el panel de administración
  getStats() {
    return api.get('/admin/stats')
  },

  // Obtiene el número de recetas agrupadas por categoría
  getRecipesByCategory() {
    return api.get('/admin/recipes-by-category')
  },

  // Obtiene el ranking de usuarios más activos de la plataforma
  getTopUsers() {
    return api.get('/admin/top-users')
  },

  // Obtiene las recetas más populares o valoradas
  getTopRecipes() {
    return api.get('/admin/top-recipes')
  },

  // Obtiene el número de usuarios registrados agrupados por mes
  getUsersByMonth() {
    return api.get('/admin/users-by-month')
  },

  // Cambia el rol de un usuario (por ejemplo, de usuario normal a administrador)
  updateUserRole(userId, role) {
    return api.put(`/users/${userId}/role`, { role })
  },
}
