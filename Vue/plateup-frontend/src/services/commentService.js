import api from './api'

export default {
  // Obtiene todos los comentarios de la plataforma
  getAll() {
    return api.get('/comments')
  },

  // Obtiene un comentario concreto por su identificador
  getById(id) {
    return api.get(`/comments/${id}`)
  },

  // Obtiene todos los comentarios asociados a una receta específica
  getByRecipeId(recipeId) {
    return api.get(`/comments/recipe/${recipeId}`)
  },

  // Publica un nuevo comentario en una receta
  create(payload) {
    return api.post('/comments', payload)
  },

  // Edita el contenido de un comentario existente
  update(id, payload) {
    return api.put(`/comments/${id}`, payload)
  },

  // Elimina un comentario por su identificador
  remove(id) {
    return api.delete(`/comments/${id}`)
  },
}