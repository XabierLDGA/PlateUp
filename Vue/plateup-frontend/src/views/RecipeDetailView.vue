<template>
  <div class="min-h-screen bg-[#f6f3ee] pb-32">
    <div v-if="detailLoading" class="container-app py-10">
      <div class="rounded-[32px] bg-white p-8 text-center text-gray-500 shadow-sm ring-1 ring-black/5">
        Loading recipe...
      </div>
    </div>

    <div v-else-if="detailError" class="container-app py-10">
      <div class="rounded-[32px] bg-red-50 p-8 text-center text-red-600 shadow-sm ring-1 ring-red-100">
        {{ detailError }}
      </div>
    </div>

    <div v-else-if="recipe" class="container-app space-y-6 py-6">
      <section class="overflow-hidden rounded-[36px] bg-white shadow-sm ring-1 ring-black/5">
        <div class="relative h-80 w-full overflow-hidden">
          <img
            :src="recipeImage"
            :alt="recipe.title"
            class="h-full w-full object-cover"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-black/55 via-transparent to-transparent"></div>

          <button
            type="button"
            class="absolute left-4 top-4 rounded-full bg-white/90 px-4 py-2 text-sm font-semibold text-gray-700 backdrop-blur transition hover:bg-white"
            @click="router.back()"
          >
            Back
          </button>

          <div class="absolute bottom-4 left-4 right-4 flex items-end justify-between gap-4">
            <div class="space-y-3">
              <span class="inline-flex rounded-full bg-white/90 px-4 py-2 text-sm font-semibold text-gray-700">
                {{ recipeCategory }}
              </span>

              <div>
                <h1 class="text-3xl font-bold text-white">
                  {{ recipe.title }}
                </h1>
                <p class="mt-2 max-w-xl text-sm text-white/80">
                  {{ recipe.description }}
                </p>
              </div>
            </div>

            <button
              type="button"
              class="flex items-center gap-2 rounded-full px-4 py-3 text-sm font-semibold transition"
              :class="
                isLikedByCurrentUser
                  ? 'bg-[#f45b3f] text-white shadow-lg'
                  : 'bg-white/90 text-gray-700 hover:bg-white'
              "
              :disabled="likeLoading"
              @click="toggleRecipeLike"
            >
              <Heart class="h-5 w-5" :class="isLikedByCurrentUser ? 'fill-current' : ''" />
              {{ likesCount }}
            </button>
          </div>
        </div>

        <div class="grid gap-5 p-5 md:grid-cols-[1.2fr,0.8fr]">
          <div class="space-y-4">
            <div class="flex flex-wrap items-center gap-2 text-sm font-medium text-gray-500">
              <span class="rounded-full bg-orange-50 px-4 py-2 text-[#f45b3f]">
                {{ recipe.difficulty || 'easy' }}
              </span>
              <span class="rounded-full bg-gray-100 px-4 py-2">
                {{ recipe.totalMinutes || 0 }} min
              </span>
              <span class="rounded-full bg-gray-100 px-4 py-2">
                {{ recipe.servings || 1 }} servings
              </span>
            </div>

            <button
              type="button"
              class="flex w-full items-center gap-4 rounded-[28px] bg-[#fff7f4] p-4 text-left transition hover:bg-[#ffefe8]"
              @click="goToAuthorProfile"
            >
              <img
                :src="authorAvatar"
                :alt="authorName"
                class="h-14 w-14 rounded-full object-cover ring-2 ring-orange-100"
              />

              <div class="min-w-0 flex-1">
                <p class="text-sm text-gray-500">Recipe by</p>
                <p class="truncate text-lg font-bold text-gray-900">
                  {{ authorName }}
                </p>
                <p class="truncate text-sm text-gray-500">@{{ authorUsername }}</p>
              </div>

              <span
                v-if="!isOwnRecipe"
                class="rounded-full px-4 py-2 text-sm font-semibold"
                :class="
                  isFollowingAuthor
                    ? 'bg-white text-gray-700 ring-1 ring-black/5'
                    : 'bg-[#f45b3f] text-white'
                "
                @click.stop="toggleFollowAuthor"
              >
                {{ followLoading ? 'Loading...' : isFollowingAuthor ? 'Following' : 'Follow' }}
              </span>
            </button>
          </div>

          <div class="rounded-[28px] bg-[#f8f8f8] p-5">
            <p class="text-sm font-semibold uppercase tracking-[0.2em] text-gray-400">
              About this recipe
            </p>

            <dl class="mt-4 space-y-4 text-sm text-gray-600">
              <div class="flex items-center justify-between gap-4">
                <dt>Category</dt>
                <dd class="font-semibold text-gray-900">{{ recipeCategory }}</dd>
              </div>

              <div class="flex items-center justify-between gap-4">
                <dt>Total time</dt>
                <dd class="font-semibold text-gray-900">{{ recipe.totalMinutes || 0 }} min</dd>
              </div>

              <div class="flex items-center justify-between gap-4">
                <dt>Servings</dt>
                <dd class="font-semibold text-gray-900">{{ recipe.servings || 1 }}</dd>
              </div>

              <div class="flex items-center justify-between gap-4">
                <dt>Difficulty</dt>
                <dd class="font-semibold capitalize text-gray-900">{{ recipe.difficulty || 'easy' }}</dd>
              </div>
            </dl>
          </div>
        </div>
      </section>

      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="mb-4 flex items-center justify-between">
          <h2 class="text-lg font-bold text-gray-900">Ingredients</h2>
          <span class="text-sm text-gray-500">{{ ingredientChecklist.length }} items</span>
        </div>

        <div v-if="ingredientChecklist.length" class="space-y-3">
          <label
            v-for="ingredient in ingredientChecklist"
            :key="ingredient.key"
            class="flex items-start gap-3 rounded-2xl bg-gray-50 px-4 py-3"
          >
            <input
              v-model="checkedIngredients"
              :value="ingredient.key"
              type="checkbox"
              class="mt-1 h-4 w-4 rounded border-gray-300 text-[#f45b3f] focus:ring-[#f45b3f]"
            />

            <div class="min-w-0 flex-1">
              <div class="flex items-center justify-between gap-4">
                <p class="text-sm font-semibold text-gray-800">
                  {{ ingredient.name }}
                </p>
                <span class="text-xs text-gray-500">{{ ingredient.quantityText }}</span>
              </div>

              <p v-if="ingredient.notes" class="mt-1 text-xs text-gray-500">
                {{ ingredient.notes }}
              </p>
            </div>
          </label>
        </div>

        <div
          v-else
          class="rounded-2xl bg-gray-50 px-4 py-4 text-sm text-gray-500"
        >
          No ingredients added for this recipe yet.
        </div>
      </section>

      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="mb-4 flex items-center justify-between">
          <h2 class="text-lg font-bold text-gray-900">Steps</h2>
          <span class="text-sm text-gray-500">{{ orderedRecipeSteps.length }} steps</span>
        </div>

        <div v-if="orderedRecipeSteps.length" class="space-y-3">
          <article
            v-for="step in orderedRecipeSteps"
            :key="step.id || step.orderIndex"
            class="grid grid-cols-[auto,1fr] gap-4 rounded-2xl bg-gray-50 p-4"
          >
            <div class="flex h-10 w-10 items-center justify-center rounded-full bg-[#f45b3f] text-sm font-bold text-white">
              {{ step.orderIndex }}
            </div>

            <div class="space-y-1">
              <p class="text-sm font-semibold text-gray-800">
                Step {{ step.orderIndex }}
              </p>

              <p class="text-sm leading-6 text-gray-600">
                {{ step.text }}
              </p>
            </div>
          </article>
        </div>

        <div
          v-else
          class="rounded-2xl bg-gray-50 px-4 py-4 text-sm text-gray-500"
        >
          No steps available for this recipe yet.
        </div>
      </section>
    </div>

    <!-- ── Start Cooking button ── -->
    <button
  class="fixed bottom-24 left-1/2 z-40 mb-5 flex w-[calc(100%-2rem)] max-w-md -translate-x-1/2 items-center justify-center gap-2 rounded-full bg-[#f45b3f] px-6 py-4 text-base font-bold text-white shadow-xl transition hover:bg-[#e24a2e]"
  @click="startCooking"
>
  <ChefHat class="h-5 w-5" />
  <span>Start Cooking</span>
</button>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Heart } from 'lucide-vue-next'
import { useAuthStore } from '../stores/authStore'
import recipeService from '../services/recipeService'
import userService from '../services/userService'
import likeService from '../services/likeService'
import ingredientService from '../services/ingredientService'
import recipeIngredientService from '../services/recipeIngredientService'
import recipeStepService from '../services/recipeStepService'
import followService from '../services/followService'
import { getRecipeImage } from '../utils/recipeImage'
import { getUserAvatar } from '../utils/userAvatar'
import { normalizeRecipeCategory } from '../utils/recipeCategories'
import { Trash2, ChefHat } from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const recipe = ref(null)
const author = ref(null)
const likes = ref([])
const ingredients = ref([])
const recipeIngredients = ref([])
const recipeSteps = ref([])
const follows = ref([])
const checkedIngredients = ref([])
const followLoading = ref(false)
const likeLoading = ref(false)
const detailLoading = ref(true)
const detailError = ref('')

const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))

const recipeImage = computed(() => getRecipeImage(recipe.value))
const recipeCategory = computed(() => normalizeRecipeCategory(recipe.value?.category))

const likesCount = computed(() => {
  if (!recipe.value?.id) return 0
  return likes.value.filter((like) => Number(like.recipeId) === Number(recipe.value.id)).length
})

const isLikedByCurrentUser = computed(() => {
  if (!currentUserId.value || !recipe.value?.id) return false

  return likes.value.some(
    (like) =>
      Number(like.userId) === currentUserId.value &&
      Number(like.recipeId) === Number(recipe.value.id)
  )
})

const authorId = computed(() => Number(author.value?.id || recipe.value?.userId || 0))
const authorName = computed(() => author.value?.displayName || 'PlateUp Chef')
const authorUsername = computed(() => author.value?.username || 'plateup')
const authorAvatar = computed(() => getUserAvatar(author.value))

const isOwnRecipe = computed(() => {
  return currentUserId.value && authorId.value && currentUserId.value === authorId.value
})

const isFollowingAuthor = computed(() => {
  if (!currentUserId.value || !authorId.value || isOwnRecipe.value) return false

  return follows.value.some(
    (follow) =>
      Number(follow.followerId) === currentUserId.value &&
      Number(follow.followedId) === authorId.value
  )
})

const ingredientChecklist = computed(() => {
  return recipeIngredients.value.map((recipeIngredient) => {
    const ingredient = ingredients.value.find(
      (item) => Number(item.id) === Number(recipeIngredient.ingredientId)
    )

    const quantity = recipeIngredient.quantityDecimal
    const unit = recipeIngredient.unit || ingredient?.unitDefault || ''
    const quantityText = quantity !== null && quantity !== undefined
      ? `${quantity} ${unit}`.trim()
      : unit
        ? `unit: ${unit}`
        : 'No quantity specified'

    return {
      key: `${recipeIngredient.recipeId}-${recipeIngredient.ingredientId}`,
      recipeId: recipeIngredient.recipeId,
      ingredientId: recipeIngredient.ingredientId,
      name: ingredient?.name || 'Unknown ingredient',
      quantityText,
      notes: recipeIngredient.notes || '',
    }
  })
})

const orderedRecipeSteps = computed(() => {
  return [...recipeSteps.value].sort((a, b) => Number(a.orderIndex) - Number(b.orderIndex))
})

// ── Navigation ────────────────────────────────────────────────

function startCooking() {
  if (!recipe.value?.id) return
  router.push({ name: 'cooking', params: { id: recipe.value.id } })
}

function goToAuthorProfile() {
  if (!authorId.value) return

  if (Number(authorId.value) === currentUserId.value) {
    router.push({ name: 'profile' })
    return
  }

  router.push({
    name: 'user-profile',
    params: { id: authorId.value },
  })
}

// ── Actions ───────────────────────────────────────────────────

async function toggleRecipeLike() {
  const recipeId = Number(recipe.value?.id)

  if (!currentUserId.value || !recipeId || likeLoading.value) return

  likeLoading.value = true

  try {
    if (isLikedByCurrentUser.value) {
      await likeService.remove(currentUserId.value, recipeId)

      likes.value = likes.value.filter(
        (like) =>
          !(
            Number(like.userId) === currentUserId.value &&
            Number(like.recipeId) === recipeId
          )
      )
    } else {
      const payload = {
        userId: currentUserId.value,
        recipeId,
      }

      const response = await likeService.create(payload)

      likes.value.push(response.data || payload)
    }
  } catch (error) {
    console.error('Error toggling like in RecipeDetailView:', error)
  } finally {
    likeLoading.value = false
  }
}

async function toggleFollowAuthor() {
  if (!currentUserId.value || !authorId.value || isOwnRecipe.value) return

  followLoading.value = true

  try {
    if (isFollowingAuthor.value) {
      await followService.remove(currentUserId.value, authorId.value)

      follows.value = follows.value.filter(
        (follow) =>
          !(
            Number(follow.followerId) === currentUserId.value &&
            Number(follow.followedId) === authorId.value
          )
      )
    } else {
      await followService.create({
        followerId: currentUserId.value,
        followedId: authorId.value,
      })

      follows.value.push({
        followerId: currentUserId.value,
        followedId: authorId.value,
      })
    }
  } catch (error) {
    console.error('Error toggling follow in RecipeDetailView:', error)
  } finally {
    followLoading.value = false
  }
}

// ── Load ──────────────────────────────────────────────────────

async function loadRecipeDetail() {
  detailLoading.value = true
  detailError.value = ''

  try {
    await authStore.initialize()

    const recipeId = Number(route.params.id)
    const recipeResponse = await recipeService.getById(recipeId)
    recipe.value = recipeResponse.data

    if (!recipe.value) {
      throw new Error('Recipe not found.')
    }

    const results = await Promise.allSettled([
      userService.getById(recipe.value.userId),
      likeService.getAll(),
      ingredientService.getAll(),
      recipeIngredientService.getByRecipeId(recipeId),
      recipeStepService.getByRecipeId(recipeId),
      followService.getAll(),
    ])

    const [
      userResponse,
      likesResponse,
      ingredientsResponse,
      recipeIngredientsResponse,
      stepsResponse,
      followsResponse,
    ] = results

    author.value = userResponse.status === 'fulfilled' ? userResponse.value.data : null
    likes.value = likesResponse.status === 'fulfilled' ? likesResponse.value.data || [] : []
    ingredients.value = ingredientsResponse.status === 'fulfilled' ? ingredientsResponse.value.data || [] : []
    recipeIngredients.value = recipeIngredientsResponse.status === 'fulfilled' ? recipeIngredientsResponse.value.data || [] : []
    recipeSteps.value = stepsResponse.status === 'fulfilled' ? stepsResponse.value.data || [] : []
    follows.value = followsResponse.status === 'fulfilled' ? followsResponse.value.data || [] : []
    checkedIngredients.value = []

    results.forEach((result, index) => {
      if (result.status === 'rejected') {
        console.error(`Error loading recipe detail resource ${index}:`, result.reason)
      }
    })
  } catch (error) {
    console.error('Error loading recipe detail:', error)
    detailError.value = 'The recipe could not be loaded correctly.'
  } finally {
    detailLoading.value = false
  }
}

onMounted(loadRecipeDetail)
</script>