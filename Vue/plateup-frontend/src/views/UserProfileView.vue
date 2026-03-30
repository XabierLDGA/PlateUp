<template>
  <div class="container-app space-y-6 py-6">
    <section
      v-if="loading"
      class="rounded-[32px] bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
    >
      Loading user profile...
    </section>

    <section
      v-else-if="errorMessage"
      class="rounded-[32px] bg-red-50 p-6 text-center text-red-600 shadow-sm ring-1 ring-red-100"
    >
      {{ errorMessage }}
    </section>

    <template v-else>
      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="flex items-start gap-4">
          <button
            type="button"
            class="rounded-full transition hover:scale-105"
            @click="goToVisitedUserProfile"
          >
            <img
              :src="userAvatar"
              :alt="visitedUser?.displayName"
              class="h-20 w-20 rounded-full object-cover ring-4 ring-orange-50"
            />
          </button>

          <div class="flex-1">
            <h1 class="text-2xl font-bold text-gray-900">
              {{ visitedUser?.displayName || 'PlateUp User' }}
            </h1>

            <p class="text-sm text-gray-500">@{{ visitedUser?.username || 'plateup' }}</p>

            <p class="mt-2 text-sm leading-6 text-gray-600">
              {{ visitedUser?.bio || 'No bio available yet.' }}
            </p>
          </div>
        </div>

        <div class="mt-5 grid grid-cols-3 gap-3">
          <div class="rounded-3xl bg-orange-50 p-4 text-center">
            <p class="text-xs text-gray-500">Recipes</p>
            <p class="mt-1 text-2xl font-bold text-[#f45b3f]">{{ userRecipes.length }}</p>
          </div>

          <button
            type="button"
            class="rounded-3xl bg-gray-50 p-4 text-center transition hover:bg-gray-100"
            @click="openConnections('followers')"
          >
            <p class="text-xs text-gray-500">Followers</p>
            <p class="mt-1 text-2xl font-bold text-gray-800">{{ followersCount }}</p>
          </button>

          <button
            type="button"
            class="rounded-3xl bg-gray-50 p-4 text-center transition hover:bg-gray-100"
            @click="openConnections('following')"
          >
            <p class="text-xs text-gray-500">Following</p>
            <p class="mt-1 text-2xl font-bold text-gray-800">{{ followingCount }}</p>
          </button>
        </div>

        <div v-if="!isOwnProfile" class="mt-5">
          <button
            class="w-full rounded-full px-4 py-3 text-sm font-semibold transition"
            :class="
              isFollowingVisitedUser
                ? 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                : 'bg-[#f45b3f] text-white hover:bg-[#e24a2e]'
            "
            :disabled="followLoading"
            @click="toggleFollow"
          >
            {{
              followLoading
                ? 'Loading...'
                : isFollowingVisitedUser
                  ? 'Following'
                  : 'Follow'
            }}
          </button>
        </div>
      </section>

      <section class="flex gap-3">
        <button
          @click="activeTab = 'recipes'"
          class="flex-1 rounded-full px-4 py-3 text-sm font-semibold transition"
          :class="activeTab === 'recipes' ? 'bg-[#f45b3f] text-white' : 'bg-white text-gray-600 ring-1 ring-black/5'"
        >
          My Recipe Book
        </button>

        <button
          @click="activeTab = 'achievements'"
          class="flex-1 rounded-full px-4 py-3 text-sm font-semibold transition"
          :class="activeTab === 'achievements' ? 'bg-[#f45b3f] text-white' : 'bg-white text-gray-600 ring-1 ring-black/5'"
        >
          Achievements
        </button>
      </section>

      <section v-if="activeTab === 'recipes'" class="space-y-4">
        <h2 class="text-xl font-bold text-gray-900">My Recipe Book</h2>

        <div
          v-if="canViewRecipeBook && userRecipes.length === 0"
          class="rounded-[28px] bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
        >
          This user has no recipes yet.
        </div>

        <div
          v-else-if="!canViewRecipeBook"
          class="rounded-[28px] bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
        >
          Follow this user to unlock their recipe book.
        </div>

        <div v-else class="grid grid-cols-2 gap-4">
          <RouterLink
            v-for="recipe in userRecipes"
            :key="recipe.id"
            :to="`/recipes/${recipe.id}`"
            class="overflow-hidden rounded-[28px] bg-white shadow-sm ring-1 ring-black/5"
          >
            <img
              :src="resolveRecipeImage(recipe)"
              :alt="recipe.title"
              class="h-36 w-full object-cover"
            />

            <div class="space-y-1 p-3">
              <h3 class="line-clamp-1 text-sm font-bold text-gray-900">{{ recipe.title }}</h3>
              <p class="text-xs text-gray-500">
                {{ recipe.totalMinutes || 0 }} min · {{ recipe.difficulty || 'Easy' }}
              </p>
            </div>
          </RouterLink>
        </div>
      </section>

      <section v-else class="space-y-4">
        <h2 class="text-xl font-bold text-gray-900">Achievements</h2>

        <div
          v-if="earnedAchievements.length === 0"
          class="rounded-[28px] bg-white p-6 text-center text-gray-500 shadow-sm ring-1 ring-black/5"
        >
          This user has no achievements yet.
        </div>

        <div v-else class="space-y-3">
          <article
            v-for="achievement in earnedAchievements"
            :key="achievement.id"
            class="rounded-[28px] bg-white p-4 shadow-sm ring-1 ring-black/5"
          >
            <div class="flex items-start gap-4">
              <div class="flex h-14 w-14 items-center justify-center rounded-2xl bg-orange-50 text-2xl">
                🏅
              </div>

              <div class="flex-1">
                <div class="flex items-center justify-between gap-3">
                  <h3 class="text-base font-bold text-gray-900">{{ achievement.name }}</h3>
                  <span class="rounded-full bg-orange-50 px-3 py-1 text-xs font-semibold text-[#f45b3f]">
                    {{ achievement.points }} pts
                  </span>
                </div>

                <p class="mt-1 text-sm text-gray-600">{{ achievement.description }}</p>
                <p class="mt-2 text-xs text-gray-400">{{ achievement.category }}</p>
              </div>
            </div>
          </article>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import recipeService from '../services/recipeService'
import userService from '../services/userService'
import followService from '../services/followService'
import achievementService from '../services/achievementService'
import userAchievementService from '../services/userAchievementService'
import { getRecipeImage } from '../utils/recipeImage'
import { getUserAvatar } from '../utils/userAvatar'

const props = defineProps({
  id: {
    type: [String, Number],
    required: true,
  },
})

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(true)
const errorMessage = ref('')
const followLoading = ref(false)
const activeTab = ref('recipes')

const visitedUser = ref(null)
const userRecipes = ref([])
const follows = ref([])
const achievements = ref([])
const userAchievements = ref([])

const numericUserId = computed(() => Number(props.id))
const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))

const userAvatar = computed(() => getUserAvatar(visitedUser.value))

const isOwnProfile = computed(() => {
  return currentUserId.value && numericUserId.value === currentUserId.value
})

const followersCount = computed(() => {
  if (!numericUserId.value) return 0
  return follows.value.filter((item) => Number(item.followedId) === numericUserId.value && item.status === 'accepted').length
})

const followingCount = computed(() => {
  if (!numericUserId.value) return 0
  return follows.value.filter((item) => Number(item.followerId) === numericUserId.value && item.status === 'accepted').length
})

const isFollowingVisitedUser = computed(() => {
  if (!currentUserId.value || !numericUserId.value || isOwnProfile.value) return false

  return follows.value.some(
    (item) =>
      Number(item.followerId) === currentUserId.value &&
      Number(item.followedId) === numericUserId.value &&
      item.status === 'accepted'
  )
})

const canViewRecipeBook = computed(() => {
  return isOwnProfile.value || isFollowingVisitedUser.value
})

const earnedAchievements = computed(() => {
  if (!numericUserId.value) return []

  const userAchievementRows = userAchievements.value.filter(
    (item) => Number(item.user_id) === numericUserId.value
  )

  return userAchievementRows
    .map((userAchievement) =>
      achievements.value.find(
        (achievement) => Number(achievement.id) === Number(userAchievement.achievement_id)
      )
    )
    .filter(Boolean)
})

function resolveRecipeImage(recipe) {
  return getRecipeImage(recipe)
}

function goToVisitedUserProfile() {
  if (!numericUserId.value) return

  if (numericUserId.value === currentUserId.value) {
    router.push({ name: 'profile' })
    return
  }

  router.push({
    name: 'user-profile',
    params: { id: numericUserId.value },
  })
}

function openConnections(tab) {
  if (!numericUserId.value) return

  router.push({
    name: 'connections',
    params: { id: numericUserId.value },
    query: { tab },
  })
}

async function toggleFollow() {
  if (!currentUserId.value || !numericUserId.value || isOwnProfile.value) return

  followLoading.value = true

  try {
    if (isFollowingVisitedUser.value) {
      await followService.remove(currentUserId.value, numericUserId.value)

      follows.value = follows.value.filter(
        (item) =>
          !(
            Number(item.followerId) === currentUserId.value &&
            Number(item.followedId) === numericUserId.value
          )
      )
    } else {
      await followService.create({
        followerId: currentUserId.value,
        followedId: numericUserId.value,
      })

      follows.value.push({
        followerId: currentUserId.value,
        followedId: numericUserId.value,
        status: 'accepted',
      })
    }
  } catch (error) {
    console.error('Error toggling follow in UserProfileView:', error)
  } finally {
    followLoading.value = false
  }
}

async function loadUserProfile() {
  loading.value = true
  errorMessage.value = ''
  activeTab.value = 'recipes'

  try {
    await authStore.initialize()

    const userId = numericUserId.value

    if (!userId || Number.isNaN(userId)) {
      throw new Error('Invalid user id.')
    }

    const [
      userResponse,
      recipesResponse,
      followsResponse,
      achievementsResponse,
      userAchievementsResponse,
    ] = await Promise.all([
      userService.getById(userId),
      recipeService.getByUserId(userId),
      followService.getAll(),
      achievementService.getAll(),
      userAchievementService.getByUserId(userId),
    ])

    if (!userResponse.data) {
      throw new Error('User not found.')
    }

    visitedUser.value = userResponse.data
    userRecipes.value = recipesResponse.data || []
    follows.value = followsResponse.data || []
    achievements.value = achievementsResponse.data || []
    userAchievements.value = userAchievementsResponse.data || []
  } catch (error) {
    console.error('Error loading visited user profile:', error)
    visitedUser.value = null
    userRecipes.value = []
    follows.value = []
    achievements.value = []
    userAchievements.value = []
    errorMessage.value = 'The user profile could not be loaded.'
  } finally {
    loading.value = false
  }
}

onMounted(loadUserProfile)

watch(
  () => props.id,
  () => {
    loadUserProfile()
  }
)
</script>