<template>
  <div class="container-app space-y-6 py-6">
    <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
      <div class="flex items-center gap-3 rounded-2xl bg-gray-50 px-4 py-3">
        <Search class="h-5 w-5 text-gray-400" />
        <input
          v-model="searchTerm"
          type="text"
          placeholder="Search people or recipes"
          class="w-full bg-transparent text-sm text-gray-700 outline-none placeholder:text-gray-400"
        />
      </div>
    </section>

    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-bold text-gray-900">People to discover</h2>
        <span class="text-sm text-[#f45b3f]">{{ suggestedUsers.length }} users</span>
      </div>

      <div class="flex gap-4 overflow-x-auto pb-2">
        <article
          v-for="user in suggestedUsers"
          :key="user.id"
          class="w-44 shrink-0 rounded-[28px] bg-white p-4 text-center shadow-sm ring-1 ring-black/5"
        >
          <div>
            <button
              type="button"
              class="rounded-full transition hover:scale-105"
              @click="goToUserProfile(user.id)"
            >
              <img
                :src="resolveUserAvatar(user)"
                :alt="user.displayName || user.username"
                class="h-16 w-16 rounded-full object-cover ring-4 ring-orange-50"
              />
            </button>

            <h3 class="mt-3 line-clamp-1 text-sm font-bold text-gray-900">
              {{ user.displayName || user.username }}
            </h3>

            <p class="line-clamp-1 text-xs text-gray-500">@{{ user.username }}</p>

            <button
              class="mt-4 rounded-full px-4 py-2 text-sm font-semibold transition"
              :class="
                isFollowingUser(user.id)
                  ? 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                  : 'bg-[#f45b3f] text-white hover:bg-[#e24a2e]'
              "
              :disabled="processingUserId === user.id"
              @click="toggleFollow(user.id)"
            >
              {{
                processingUserId === user.id
                  ? 'Loading...'
                  : isFollowingUser(user.id)
                    ? 'Following'
                    : 'Follow'
              }}
            </button>
          </div>
        </article>
      </div>
    </section>

    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-xl font-bold text-gray-900">Discover recipes</h2>
        <span class="text-sm text-[#f45b3f]">{{ filteredRecipes.length }} results</span>
      </div>

      <div class="grid grid-cols-2 gap-4">
        <RouterLink
          v-for="recipe in filteredRecipes"
          :key="recipe.id"
          :to="`/recipes/${recipe.id}`"
          class="overflow-hidden rounded-[28px] bg-white shadow-sm ring-1 ring-black/5"
        >
          <div class="relative">
            <img
              :src="resolveRecipeImage(recipe)"
              :alt="recipe.title"
              class="h-44 w-full object-cover"
            />
            <div class="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent"></div>

            <span class="absolute left-3 top-3 rounded-full bg-white/90 px-3 py-1 text-xs font-semibold text-gray-700">
              {{ resolveCategory(recipe) }}
            </span>
          </div>

          <div class="space-y-2 p-3">
            <h3 class="line-clamp-1 text-sm font-bold text-gray-900">{{ recipe.title }}</h3>

            <div class="flex items-center justify-between text-xs text-gray-500">
              <span>{{ recipe.totalMinutes || 0 }} min</span>
              <span>{{ recipe.difficulty || 'Easy' }}</span>
            </div>
          </div>
        </RouterLink>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from 'lucide-vue-next'
import { useAuthStore } from '../stores/authStore'
import userService from '../services/userService'
import recipeService from '../services/recipeService'
import followService from '../services/followService'
import { getRecipeImage } from '../utils/recipeImage'
import { getUserAvatar } from '../utils/userAvatar'
import { normalizeRecipeCategory } from '../utils/recipeCategories'

const router = useRouter()
const authStore = useAuthStore()

const users = ref([])
const recipes = ref([])
const follows = ref([])
const searchTerm = ref('')
const processingUserId = ref(null)

const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))

const suggestedUsers = computed(() => {
  return users.value
    .filter((user) => Number(user.id) !== currentUserId.value)
    .filter((user) => {
      const text = `${user.username || ''} ${user.displayName || ''}`.toLowerCase()
      return text.includes(searchTerm.value.toLowerCase())
    })
    .slice(0, 8)
})

const filteredRecipes = computed(() => {
  return recipes.value.filter((recipe) => {
    const author = users.value.find((user) => Number(user.id) === Number(recipe.userId))
    const text =
      `${recipe.title || ''} ${recipe.description || ''} ${recipe.category || ''} ${author?.username || ''} ${author?.displayName || ''}`.toLowerCase()

    return text.includes(searchTerm.value.toLowerCase())
  })
})

function resolveRecipeImage(recipe) {
  return getRecipeImage(recipe)
}

function resolveUserAvatar(user) {
  return getUserAvatar(user)
}

function resolveCategory(recipe) {
  return normalizeRecipeCategory(recipe?.category)
}

function isFollowingUser(userId) {
  return follows.value.some(
    (follow) =>
      Number(follow.followerId) === currentUserId.value &&
      Number(follow.followedId) === Number(userId)
  )
}

function goToUserProfile(userId) {
  if (!userId) return

  if (Number(userId) === currentUserId.value) {
    router.push({ name: 'profile' })
    return
  }

  router.push({
    name: 'user-profile',
    params: { id: userId },
  })
}

async function toggleFollow(followedId) {
  if (!currentUserId.value) return
  if (currentUserId.value === Number(followedId)) return
  if (processingUserId.value === followedId) return

  processingUserId.value = followedId

  try {
    if (isFollowingUser(followedId)) {
      await followService.remove(currentUserId.value, followedId)

      follows.value = follows.value.filter(
        (follow) =>
          !(
            Number(follow.followerId) === currentUserId.value &&
            Number(follow.followedId) === Number(followedId)
          )
      )
    } else {
      await followService.create({
        followerId: currentUserId.value,
        followedId: Number(followedId),
      })

      follows.value.push({
        followerId: currentUserId.value,
        followedId: Number(followedId),
      })
    }
  } catch (error) {
    console.error('Error toggling follow in ExploreView:', error)
  } finally {
    processingUserId.value = null
  }
}

async function loadData() {
  await authStore.initialize()

  const [usersResponse, recipesResponse, followsResponse] = await Promise.all([
    userService.getAll(),
    recipeService.getAll(),
    followService.getAll(),
  ])

  users.value = usersResponse.data || []
  recipes.value = recipesResponse.data || []
  follows.value = followsResponse.data || []
}

onMounted(loadData)
</script>