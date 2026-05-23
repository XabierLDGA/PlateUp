<template>
  <div class="container-app space-y-6 py-6">
    <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
      <div class="flex items-center gap-3">
        <button
          type="button"
          class="flex h-11 w-11 items-center justify-center rounded-full bg-gray-100 text-lg font-bold text-gray-700 transition hover:bg-gray-200"
          @click="goBack"
        >
          ←
        </button>

        <div class="min-w-0 flex-1">
          <p class="text-sm text-gray-500">Connections</p>
          <h1 class="truncate text-2xl font-bold text-gray-900">
            {{ profileUser?.displayName || 'PlateUp User' }}
          </h1>
          <p class="truncate text-sm text-gray-500">
            @{{ profileUser?.username || 'plateup' }}
          </p>
        </div>
      </div>

      <div class="mt-5 grid grid-cols-2 gap-3">
        <button
          type="button"
          class="rounded-3xl px-4 py-4 text-center transition"
          :class="
            activeTab === 'followers'
              ? 'bg-[#f45b3f] text-white'
              : 'bg-gray-50 text-gray-700'
          "
          @click="setActiveTab('followers')"
        >
          <p class="text-xs" :class="activeTab === 'followers' ? 'text-orange-100' : 'text-gray-500'">
            Followers
          </p>
          <p class="mt-1 text-2xl font-bold">{{ followersCount }}</p>
        </button>

        <button
          type="button"
          class="rounded-3xl px-4 py-4 text-center transition"
          :class="
            activeTab === 'following'
              ? 'bg-[#f45b3f] text-white'
              : 'bg-gray-50 text-gray-700'
          "
          @click="setActiveTab('following')"
        >
          <p class="text-xs" :class="activeTab === 'following' ? 'text-orange-100' : 'text-gray-500'">
            Following
          </p>
          <p class="mt-1 text-2xl font-bold">{{ followingCount }}</p>
        </button>
      </div>
    </section>

    <section
      v-if="loading"
      class="rounded-[32px] bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
    >
      Loading connections...
    </section>

    <section
      v-else-if="errorMessage"
      class="rounded-[32px] bg-red-50 p-6 text-center text-red-600 shadow-sm ring-1 ring-red-100"
    >
      {{ errorMessage }}
    </section>

    <section
      v-else-if="visibleUsers.length === 0"
      class="rounded-[32px] bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
    >
      {{ emptyMessage }}
    </section>

    <section v-else class="space-y-3">
      <article
        v-for="user in visibleUsers"
        :key="user.id"
        class="flex items-center gap-3 rounded-[28px] bg-white p-4 shadow-sm ring-1 ring-black/5"
      >
        <button
          type="button"
          class="shrink-0 rounded-full transition hover:scale-105"
          @click="goToUserProfile(user.id)"
        >
          <img
            :src="getAvatar(user)"
            :alt="user.displayName"
            class="h-14 w-14 rounded-full object-cover ring-2 ring-orange-50"
          />
        </button>

        <button
          type="button"
          class="min-w-0 flex-1 text-left"
          @click="goToUserProfile(user.id)"
        >
          <p class="truncate text-sm font-bold text-gray-900">{{ user.displayName }}</p>
          <p class="truncate text-sm text-gray-500">@{{ user.username }}</p>
          <p class="mt-1 line-clamp-1 text-xs text-gray-400">
            {{ user.bio || 'No bio available yet.' }}
          </p>
        </button>

        <div class="flex shrink-0 items-center gap-2">
          <button
            v-if="!isCurrentUser(user.id)"
            type="button"
            class="rounded-full px-4 py-2 text-sm font-semibold transition"
            :class="
              isFollowingUser(user.id)
                ? 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                : 'bg-[#f45b3f] text-white hover:bg-[#e24a2e]'
            "
            :disabled="togglingUserId === Number(user.id) || removingFollowerId === Number(user.id)"
            @click="toggleFollow(user.id)"
          >
            {{
              togglingUserId === Number(user.id)
                ? 'Loading...'
                : isFollowingUser(user.id)
                  ? 'Following'
                  : 'Follow'
            }}
          </button>

          <span
            v-else
            class="rounded-full bg-orange-50 px-4 py-2 text-sm font-semibold text-[#f45b3f]"
          >
            You
          </span>

          <button
            v-if="showRemoveFollowerButton(user.id)"
            type="button"
            class="flex h-10 w-10 items-center justify-center rounded-full bg-red-50 text-lg font-bold text-red-500 transition hover:bg-red-100"
            :disabled="removingFollowerId === Number(user.id)"
            @click="removeFollower(user.id)"
          >
            {{ removingFollowerId === Number(user.id) ? '...' : '×' }}
          </button>
        </div>
      </article>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import userService from '../services/userService'
import followService from '../services/followService'
import { getUserAvatar } from '../utils/userAvatar'

const props = defineProps({
  id: {
    type: [String, Number],
    required: true,
  },
})

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

// Estado de carga, errores y datos del perfil visitado
const loading = ref(true)
const errorMessage = ref('')
const profileUser = ref(null)
const users = ref([])
const follows = ref([])

// IDs en proceso de seguimiento o eliminación, para bloquear doble clic
const togglingUserId = ref(null)
const removingFollowerId = ref(null)

// Pestaña activa: 'followers' o 'following'
const activeTab = ref('followers')

// IDs numéricos del usuario visitado y del usuario actual
const numericUserId = computed(() => Number(props.id))
const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))

// Cuenta cuántos seguidores tiene el usuario visitado
const followersCount = computed(() => {
  if (!numericUserId.value) return 0
  return follows.value.filter((item) => Number(item.followedId) === numericUserId.value).length
})

// Cuenta a cuántos usuarios sigue el usuario visitado
const followingCount = computed(() => {
  if (!numericUserId.value) return 0
  return follows.value.filter((item) => Number(item.followerId) === numericUserId.value).length
})

// Indica si el usuario está viendo su propia lista de conexiones
const isOwnConnectionsView = computed(() => {
  return numericUserId.value === currentUserId.value
})

// Lista de usuarios a mostrar según la pestaña activa (seguidores o seguidos)
const visibleUsers = computed(() => {
  if (!numericUserId.value) return []

  const relevantIds =
    activeTab.value === 'followers'
      ? follows.value
          .filter((item) => Number(item.followedId) === numericUserId.value)
          .map((item) => Number(item.followerId))
      : follows.value
          .filter((item) => Number(item.followerId) === numericUserId.value)
          .map((item) => Number(item.followedId))

  const uniqueIds = [...new Set(relevantIds)]

  return uniqueIds
    .map((id) => users.value.find((user) => Number(user.id) === id))
    .filter(Boolean)
})

// Mensaje a mostrar cuando la lista de la pestaña activa está vacía
const emptyMessage = computed(() => {
  if (activeTab.value === 'followers') {
    return 'This user has no followers yet.'
  }

  return 'This user is not following anyone yet.'
})

// Sincroniza la pestaña activa con el parámetro 'tab' de la URL
function syncTabFromQuery() {
  activeTab.value = route.query.tab === 'following' ? 'following' : 'followers'
}

// Cambia la pestaña activa y actualiza la URL sin recargar la página
function setActiveTab(tab) {
  router.replace({
    name: 'connections',
    params: { id: numericUserId.value },
    query: { tab },
  })
}

function getAvatar(user) {
  return getUserAvatar(user)
}

// Comprueba si un usuario de la lista es el usuario logueado
function isCurrentUser(userId) {
  return Number(userId) === currentUserId.value
}

// Comprueba si el usuario logueado sigue a otro usuario
function isFollowingUser(userId) {
  if (!currentUserId.value || !userId) return false

  return follows.value.some(
    (item) =>
      Number(item.followerId) === currentUserId.value &&
      Number(item.followedId) === Number(userId)
  )
}

// Muestra el botón de eliminar seguidor solo en la pestaña propia de seguidores
function showRemoveFollowerButton(userId) {
  return (
    activeTab.value === 'followers' &&
    isOwnConnectionsView.value &&
    !isCurrentUser(userId)
  )
}

// Navega hacia atrás al perfil correspondiente según si es propio o ajeno
function goBack() {
  if (numericUserId.value === currentUserId.value) {
    router.push({ name: 'profile' })
    return
  }

  router.push({
    name: 'user-profile',
    params: { id: numericUserId.value },
  })
}

// Navega al perfil de un usuario de la lista
function goToUserProfile(userId) {
  if (Number(userId) === currentUserId.value) {
    router.push({ name: 'profile' })
    return
  }

  router.push({
    name: 'user-profile',
    params: { id: Number(userId) },
  })
}

// Alterna el seguimiento de un usuario: si ya lo sigue lo deja de seguir, si no lo sigue
async function toggleFollow(userId) {
  const targetUserId = Number(userId)

  if (!currentUserId.value || !targetUserId || targetUserId === currentUserId.value) {
    return
  }

  togglingUserId.value = targetUserId

  try {
    if (isFollowingUser(targetUserId)) {
      await followService.remove(currentUserId.value, targetUserId)

      follows.value = follows.value.filter(
        (item) =>
          !(
            Number(item.followerId) === currentUserId.value &&
            Number(item.followedId) === targetUserId
          )
      )
    } else {
      await followService.create({
        followerId: currentUserId.value,
        followedId: targetUserId,
      })

      follows.value.push({
        followerId: currentUserId.value,
        followedId: targetUserId,
      })
    }
  } catch (error) {
    console.error('Error toggling follow in ConnectionsView:', error)
  } finally {
    togglingUserId.value = null
  }
}

// Elimina a un seguidor de la propia lista (solo disponible en la vista propia)
async function removeFollower(userId) {
  const followerUserId = Number(userId)

  if (
    !isOwnConnectionsView.value ||
    !currentUserId.value ||
    !followerUserId ||
    followerUserId === currentUserId.value
  ) {
    return
  }

  removingFollowerId.value = followerUserId

  try {
    await followService.remove(followerUserId, currentUserId.value)

    follows.value = follows.value.filter(
      (item) =>
        !(
          Number(item.followerId) === followerUserId &&
          Number(item.followedId) === currentUserId.value
        )
    )
  } catch (error) {
    console.error('Error removing follower in ConnectionsView:', error)
  } finally {
    removingFollowerId.value = null
  }
}

// Carga el perfil del usuario y todas sus conexiones desde la API
async function loadConnections() {
  loading.value = true
  errorMessage.value = ''

  try {
    await authStore.initialize()

    const userId = numericUserId.value

    if (!userId || Number.isNaN(userId)) {
      throw new Error('Invalid user id.')
    }

    const [profileResponse, usersResponse, followsResponse] = await Promise.all([
      userService.getById(userId),
      userService.getAll(),
      followService.getAll(),
    ])

    if (!profileResponse.data) {
      throw new Error('User not found.')
    }

    profileUser.value = profileResponse.data
    users.value = usersResponse.data || []
    follows.value = followsResponse.data || []
  } catch (error) {
    console.error('Error loading connections:', error)
    profileUser.value = null
    users.value = []
    follows.value = []
    errorMessage.value = 'The connections list could not be loaded.'
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  syncTabFromQuery()
  await loadConnections()
})

// Actualiza la pestaña activa cuando cambia el parámetro tab en la URL
watch(
  () => route.query.tab,
  () => {
    syncTabFromQuery()
  }
)

// Recarga las conexiones cuando se navega a otro usuario
watch(
  () => props.id,
  async () => {
    syncTabFromQuery()
    await loadConnections()
  }
)
</script>