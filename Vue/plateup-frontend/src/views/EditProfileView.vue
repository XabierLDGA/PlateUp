<template>
  <div class="container-app space-y-6 py-6">
    <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
      <div class="flex items-center justify-between gap-3">
        <div>
          <p class="text-sm font-semibold text-[#f45b3f]">Profile settings</p>
          <h1 class="text-2xl font-bold text-gray-900">Edit profile</h1>
        </div>

        <button
          type="button"
          class="rounded-full bg-gray-100 px-4 py-2 text-sm font-semibold text-gray-700 transition hover:bg-gray-200"
          @click="goBack"
        >
          Back
        </button>
      </div>

      <div class="mt-6 flex flex-col items-center gap-4 rounded-[28px] bg-orange-50 px-5 py-6 text-center">
        <img
          :src="avatarPreview"
          :alt="currentUser?.displayName || 'Profile avatar'"
          class="h-28 w-28 rounded-full object-cover ring-4 ring-white"
        />

        <div>
          <h2 class="text-lg font-bold text-gray-900">{{ currentUser?.displayName || 'PlateUp User' }}</h2>
          <p class="text-sm text-gray-500">@{{ currentUser?.username || 'plateup' }}</p>
        </div>

        <input
          ref="fileInputRef"
          type="file"
          accept="image/png,image/jpeg,image/jpg,image/webp"
          class="hidden"
          @change="handleFileChange"
        />

        <div class="flex w-full flex-col gap-3 sm:flex-row sm:justify-center">
          <button
            type="button"
            class="rounded-2xl bg-[#f45b3f] px-5 py-3 text-sm font-semibold text-white transition hover:bg-[#e14f35] disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="savingAvatar"
            @click="openFilePicker"
          >
            {{ savingAvatar ? 'Saving...' : 'Change profile photo' }}
          </button>

          <button
            v-if="previewUrl"
            type="button"
            class="rounded-2xl bg-white px-5 py-3 text-sm font-semibold text-gray-700 ring-1 ring-black/5 transition hover:bg-gray-50"
            :disabled="savingAvatar"
            @click="resetSelectedImage"
          >
            Cancel image
          </button>
        </div>

        <p v-if="avatarSuccessMessage" class="text-sm font-medium text-green-600">
          {{ avatarSuccessMessage }}
        </p>
        <p v-if="avatarErrorMessage" class="text-sm font-medium text-red-600">
          {{ avatarErrorMessage }}
        </p>
      </div>
    </section>

    <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
      <div class="flex items-start gap-3">
        <div class="flex h-12 w-12 items-center justify-center rounded-full bg-red-50">
          <Trash2 class="h-6 w-6 text-red-600" />
        </div>

        <div class="flex-1">
          <h2 class="text-lg font-bold text-gray-900">Delete account</h2>
          <p class="mt-1 text-sm leading-6 text-gray-600">
            This permanently deletes your account, your recipes and your profile activity. This action cannot be undone.
          </p>
        </div>
      </div>

      <button
        type="button"
        class="mt-5 flex w-full items-center justify-center gap-2 rounded-2xl bg-red-50 px-4 py-4 text-sm font-semibold text-red-600 transition hover:bg-red-100"
        @click="showDeleteModal = true"
      >
        <Trash2 class="h-4 w-4" />
        <span>Delete my account</span>
      </button>

      <p v-if="deleteErrorMessage" class="mt-3 text-sm font-medium text-red-600">
        {{ deleteErrorMessage }}
      </p>
    </section>

    <div
      v-if="showDeleteModal"
      class="fixed inset-0 z-[200] flex items-center justify-center bg-black/50 px-4"
    >
      <div class="w-full max-w-sm rounded-[28px] bg-white p-6 shadow-2xl">
        <div class="flex items-start gap-3">
          <div class="flex h-12 w-12 items-center justify-center rounded-full bg-red-50">
            <AlertTriangle class="h-6 w-6 text-red-600" />
          </div>

          <div>
            <h3 class="text-lg font-bold text-gray-900">Delete account?</h3>
            <p class="text-sm text-gray-500">
              Write <strong>DELETE</strong> to confirm. This cannot be undone.
            </p>
          </div>
        </div>

        <input
          v-model="deleteConfirmation"
          type="text"
          placeholder="Type DELETE"
          class="mt-5 w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm outline-none transition focus:border-[#f45b3f]"
        />

        <div class="mt-6 flex gap-3">
          <button
            type="button"
            class="flex-1 rounded-2xl bg-gray-100 px-4 py-3 text-sm font-semibold text-gray-700 transition hover:bg-gray-200"
            :disabled="deletingAccount"
            @click="closeDeleteModal"
          >
            Cancel
          </button>

          <button
            type="button"
            class="flex-1 rounded-2xl bg-red-600 px-4 py-3 text-sm font-semibold text-white transition hover:bg-red-700 disabled:cursor-not-allowed disabled:opacity-60"
            :disabled="deletingAccount || deleteConfirmation.trim().toUpperCase() !== 'DELETE'"
            @click="deleteAccount"
          >
            {{ deletingAccount ? 'Deleting...' : 'Delete account' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onBeforeUnmount, ref } from 'vue'
import { useRouter } from 'vue-router'
import { AlertTriangle, Trash2 } from 'lucide-vue-next'
import { useAuthStore } from '../stores/authStore'
import userService from '../services/userService'
import uploadService from '../services/uploadService'
import { getUserAvatar } from '../utils/userAvatar'

const router = useRouter()
const authStore = useAuthStore()

// Estado del selector de imagen de perfil
const fileInputRef = ref(null)
const selectedFile = ref(null)
const previewUrl = ref('')
const savingAvatar = ref(false)
const avatarErrorMessage = ref('')
const avatarSuccessMessage = ref('')

// Estado del modal de confirmación para eliminar la cuenta
const showDeleteModal = ref(false)
const deleteConfirmation = ref('')
const deletingAccount = ref(false)
const deleteErrorMessage = ref('')

// Usuario actual y previsualización del avatar (usa la URL temporal si hay una pendiente)
const currentUser = computed(() => authStore.currentUser)
const avatarPreview = computed(() => previewUrl.value || getUserAvatar(currentUser.value))

// Navega de vuelta al perfil del usuario
function goBack() {
  router.push({ name: 'profile' })
}

// Abre el selector de archivos para elegir una nueva foto de perfil
function openFilePicker() {
  avatarErrorMessage.value = ''
  avatarSuccessMessage.value = ''
  fileInputRef.value?.click()
}

// Libera la URL temporal de previsualización del avatar
function revokePreviewUrl() {
  if (previewUrl.value && previewUrl.value.startsWith('blob:')) {
    URL.revokeObjectURL(previewUrl.value)
  }
}

// Limpia la imagen seleccionada y resetea los mensajes
function resetSelectedImage() {
  revokePreviewUrl()
  selectedFile.value = null
  previewUrl.value = ''
  avatarErrorMessage.value = ''
  avatarSuccessMessage.value = ''

  if (fileInputRef.value) {
    fileInputRef.value.value = ''
  }
}

// Valida el archivo de imagen elegido y lo sube automáticamente si es válido
async function handleFileChange(event) {
  const file = event.target.files?.[0]
  avatarErrorMessage.value = ''
  avatarSuccessMessage.value = ''

  if (!file) return

  const validTypes = ['image/png', 'image/jpeg', 'image/jpg', 'image/webp']
  const maxSizeInBytes = 5 * 1024 * 1024

  if (!validTypes.includes(file.type)) {
    avatarErrorMessage.value = 'Please choose a PNG, JPG or WEBP image.'
    resetSelectedImage()
    return
  }

  if (file.size > maxSizeInBytes) {
    avatarErrorMessage.value = 'The image must be smaller than 5 MB.'
    resetSelectedImage()
    return
  }

  revokePreviewUrl()
  selectedFile.value = file
  previewUrl.value = URL.createObjectURL(file)

  await saveAvatar()
}

// Sube el avatar al servidor y actualiza el perfil del usuario en el store
async function saveAvatar() {
  if (!currentUser.value?.id || !selectedFile.value) return

  savingAvatar.value = true
  avatarErrorMessage.value = ''
  avatarSuccessMessage.value = ''

  try {
    const uploadResponse = await uploadService.uploadAvatar(selectedFile.value)
    const uploadedPath = uploadResponse.data?.path || ''

    if (!uploadedPath) {
      throw new Error('Upload path not received')
    }

    const response = await userService.update(currentUser.value.id, {
      avatarUrl: uploadedPath,
    })

    authStore.setSessionUser(response.data)
    avatarSuccessMessage.value = 'Profile photo updated successfully.'
    resetSelectedImage()
  } catch (error) {
    console.error('Error updating avatar:', error)
    avatarErrorMessage.value =
      error?.response?.data?.message || 'The profile photo could not be updated.'
  } finally {
    savingAvatar.value = false
  }
}

// Cierra el modal de eliminar cuenta si no hay una eliminación en curso
function closeDeleteModal() {
  if (deletingAccount.value) return

  showDeleteModal.value = false
  deleteConfirmation.value = ''
  deleteErrorMessage.value = ''
}

// Elimina la cuenta del usuario, cierra sesión y redirige al login
async function deleteAccount() {
  if (!currentUser.value?.id || deletingAccount.value) return

  deletingAccount.value = true
  deleteErrorMessage.value = ''

  try {
    await userService.remove(currentUser.value.id)
    authStore.logout()
    closeDeleteModal()
    router.push({ name: 'login' })
  } catch (error) {
    console.error('Error deleting account:', error)
    deleteErrorMessage.value =
      error?.response?.data?.message || 'The account could not be deleted.'
  } finally {
    deletingAccount.value = false
  }
}

// Limpia la URL temporal al desmontar el componente
onBeforeUnmount(() => {
  revokePreviewUrl()
})
</script>