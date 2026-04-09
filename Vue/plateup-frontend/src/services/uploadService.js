import api from './api'

export default {
  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('file', file)

    return api.post('/uploads/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },

  uploadRecipeImage(file) {
    const formData = new FormData()
    formData.append('file', file)

    return api.post('/uploads/recipe', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
  },
}