import api from './api'

export default {
  // Sube una imagen de perfil (avatar) al servidor y devuelve la URL resultante
  uploadAvatar(file) {
    // Se usa FormData para enviar el fichero como multipart
    const formData = new FormData()
    formData.append('file', file)

    return api.post('/uploads/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  // Sube la imagen principal de una receta al servidor y devuelve la URL resultante
  uploadRecipeImage(file) {
    // Se usa FormData para enviar el fichero como multipart
    const formData = new FormData()
    formData.append('file', file)

    return api.post('/uploads/recipe', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },
}