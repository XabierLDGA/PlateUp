<template>
  <RouterLink
    :to="`/recipes/${recipe.id}`"
    class="block overflow-hidden rounded-[28px] bg-white shadow-sm ring-1 ring-black/5 transition hover:-translate-y-0.5"
  >
    <div class="relative h-52 w-full overflow-hidden">
      <img
        :src="coverImage"
        :alt="recipe.title"
        class="h-full w-full object-cover"
      />
      <div class="absolute inset-0 bg-gradient-to-t from-black/45 via-transparent to-transparent"></div>

      <span
        class="absolute left-3 top-3 rounded-full bg-white/90 px-3 py-1 text-xs font-semibold text-gray-700"
      >
        {{ recipeCategory }}
      </span>

      <button
        type="button"
        class="absolute bottom-3 right-3 flex items-center gap-1 rounded-full px-3 py-1 text-xs font-semibold transition"
        :class="
          isLiked
            ? 'bg-[#f45b3f] text-white shadow'
            : 'bg-white/90 text-gray-700 hover:bg-white'
        "
        :disabled="likeLoading"
        @click.stop.prevent="emitToggleLike"
      >
        <Heart class="h-4 w-4" :class="isLiked ? 'fill-current' : ''" />
        {{ recipeLikes }}
      </button>
    </div>

    <div class="space-y-4 p-4">
      <div class="space-y-1">
        <h3 class="line-clamp-1 text-lg font-bold text-gray-900">
          {{ recipe.title }}
        </h3>
        <p class="line-clamp-2 text-sm text-gray-500">
          {{ recipe.description || 'No description available yet.' }}
        </p>
      </div>

      <div class="flex flex-wrap items-center gap-2 text-xs font-medium text-gray-500">
        <span class="rounded-full bg-orange-50 px-3 py-1 text-[#f45b3f]">
          {{ recipe.difficulty || 'Easy' }}
        </span>
        <span class="rounded-full bg-gray-100 px-3 py-1">
          {{ recipe.totalMinutes || 0 }} min
        </span>
        <span class="rounded-full bg-gray-100 px-3 py-1">
          {{ recipe.servings || 1 }} servings
        </span>
      </div>

      <div class="flex items-center justify-between">
        <div class="flex items-center gap-3">
          <button
            type="button"
            class="rounded-full transition hover:scale-105"
            @click.stop.prevent="goToAuthorProfile"
          >
            <img
              :src="authorAvatar"
              :alt="authorName"
              class="h-10 w-10 rounded-full object-cover ring-2 ring-orange-100"
            />
          </button>

          <div>
            <p class="text-sm font-semibold text-gray-800">{{ authorName }}</p>
            <p class="text-xs text-gray-500">@{{ authorUsername }}</p>
          </div>
        </div>

        <button
          class="rounded-full bg-[#f45b3f] px-4 py-2 text-sm font-semibold text-white transition hover:bg-[#e24a2e]"
          @click.stop.prevent="goToAuthorProfile"
        >
          View
        </button>
      </div>
    </div>
  </RouterLink>
</template>

<script setup>
// Tarjeta de receta: muestra la info resumida de una receta en los listados y el explorador
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Heart } from 'lucide-vue-next'
import { useAuthStore } from '../../stores/authStore'
import { getRecipeImage } from '../../utils/recipeImage'
import { getUserAvatar } from '../../utils/userAvatar'
import { normalizeRecipeCategory } from '../../utils/recipeCategories'

const router = useRouter()
const authStore = useAuthStore()

// Props que recibe la tarjeta desde el componente padre
const props = defineProps({
  recipe: {
    type: Object,
    required: true,
  },
  author: {
    type: Object,
    default: null,
  },
  likesCount: {
    type: Number,
    default: 0,
  },
  isLiked: {
    type: Boolean,
    default: false,
  },
  likeLoading: {
    type: Boolean,
    default: false,
  },
})

const emit = defineEmits(['toggle-like'])

// Datos calculados a partir de las props: imagen, categoría, likes y datos del autor
const coverImage = computed(() => getRecipeImage(props.recipe))
const recipeCategory = computed(() => normalizeRecipeCategory(props.recipe?.category))
const recipeLikes = computed(() => props.likesCount)

// Si no hay autor disponible, mostramos valores por defecto para no dejar campos vacíos
const authorName = computed(() => props.author?.displayName || 'PlateUp Chef')
const authorUsername = computed(() => props.author?.username || 'plateup')
const authorAvatar = computed(() => getUserAvatar(props.author))

// El id del autor puede venir del objeto author o directamente de la receta
const authorId = computed(() => {
  return props.author?.id ?? props.recipe?.userId ?? null
})

// Propagamos el evento de like al componente padre para que gestione la lógica
function emitToggleLike() {
  emit('toggle-like', props.recipe)
}

// Navega al perfil del autor; si es el usuario actual, redirige a su propio perfil
function goToAuthorProfile() {
  if (!authorId.value) return

  if (Number(authStore.currentUser?.id) === Number(authorId.value)) {
    router.push({ name: 'profile' })
    return
  }

  router.push({
    name: 'user-profile',
    params: { id: authorId.value },
  })
}
</script>