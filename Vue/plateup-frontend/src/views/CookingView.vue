<template>
    <div class="min-h-screen bg-[#f6f3ee] pb-10">

        <!-- ── Loading ─────────────────────────────────────────── -->
        <div v-if="loading" class="container-app py-10">
            <div class="rounded-[32px] bg-white p-8 text-center text-gray-500 shadow-sm ring-1 ring-black/5">
                Loading recipe…
            </div>
        </div>

        <!-- ── Error ────────────────────────────────────────────── -->
        <div v-else-if="error" class="container-app py-10">
            <div class="rounded-[32px] bg-red-50 p-8 text-center text-red-600 shadow-sm ring-1 ring-red-100">
                {{ error }}
            </div>
        </div>

        <!-- ── Main content ─────────────────────────────────────── -->
        <div v-else-if="recipe" class="container-app space-y-5 py-6">

            <!-- Hero image + title -->
            <section class="overflow-hidden rounded-[36px] bg-white shadow-sm ring-1 ring-black/5">
                <div class="relative h-56 w-full overflow-hidden">
                    <img :src="recipeImage" :alt="recipe.title" class="h-full w-full object-cover" />
                    <div class="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent" />

                    <!-- Back button -->
                    <button type="button"
                        class="absolute left-4 top-4 rounded-full bg-white/90 px-4 py-2 text-sm font-semibold text-gray-700 backdrop-blur transition hover:bg-white"
                        @click="router.back()">
                        ← Back
                    </button>

                    <!-- Title overlay -->
                    <div class="absolute bottom-4 left-4 right-4">
                        <h1 class="text-2xl font-bold text-white drop-shadow">{{ recipe.title }}</h1>
                        <p class="mt-1 text-sm text-white/80">
                            {{ recipe.servings }} servings · {{ recipe.difficulty }}
                        </p>
                    </div>
                </div>

                <!-- Timer bar -->
                <div class="flex items-center justify-between gap-4 px-5 py-4">
                    <div class="flex items-center gap-3">
                        <div class="flex h-12 w-12 items-center justify-center rounded-full"
                            :class="timerRunning ? 'bg-[#f45b3f]' : 'bg-gray-100'">
                            <Timer class="h-5 w-5" :class="timerRunning ? 'text-white' : 'text-gray-500'" />
                        </div>
                        <div>
                            <p class="text-xs font-medium text-gray-400">Elapsed time</p>
                            <p class="text-2xl font-bold tabular-nums text-gray-900">{{ formattedTime }}</p>
                        </div>
                    </div>

                    <div class="flex gap-2">
                        <button type="button" class="rounded-full px-4 py-2 text-sm font-semibold transition" :class="timerRunning
                            ? 'bg-orange-50 text-[#f45b3f] hover:bg-orange-100'
                            : 'bg-[#f45b3f] text-white hover:bg-[#e24a2e]'" @click="toggleTimer">
                            {{ timerRunning ? 'Pause' : 'Resume' }}
                        </button>
                    </div>
                </div>
            </section>

            <!-- Progress bar -->
            <section class="rounded-[28px] bg-white px-5 py-4 shadow-sm ring-1 ring-black/5">
                <div class="mb-2 flex items-center justify-between text-sm font-medium text-gray-500">
                    <span>Progress</span>
                    <span class="text-[#f45b3f]">{{ checkedSteps.length }} / {{ orderedSteps.length }} steps</span>
                </div>
                <div class="h-2.5 w-full overflow-hidden rounded-full bg-gray-100">
                    <div class="h-full rounded-full bg-[#f45b3f] transition-all duration-500"
                        :style="{ width: progressPercent + '%' }" />
                </div>
            </section>

            <!-- Ingredients checklist -->
            <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
                <div class="mb-4 flex items-center justify-between">
                    <h2 class="text-lg font-bold text-gray-900">Ingredients</h2>
                    <span class="text-sm text-gray-400">{{ ingredientChecklist.length }} items</span>
                </div>

                <div v-if="ingredientChecklist.length" class="space-y-2">
                    <label v-for="ing in ingredientChecklist" :key="ing.key"
                        class="flex cursor-pointer items-start gap-3 rounded-2xl px-4 py-3 transition"
                        :class="checkedIngredients.includes(ing.key) ? 'bg-orange-50' : 'bg-gray-50'">
                        <input v-model="checkedIngredients" :value="ing.key" type="checkbox"
                            class="mt-0.5 h-4 w-4 rounded border-gray-300 text-[#f45b3f] focus:ring-[#f45b3f]" />
                        <div class="min-w-0 flex-1">
                            <div class="flex items-center justify-between gap-3">
                                <p class="text-sm font-semibold transition"
                                    :class="checkedIngredients.includes(ing.key) ? 'text-gray-400 line-through' : 'text-gray-800'">
                                    {{ ing.name }}
                                </p>
                                <span class="shrink-0 text-xs text-gray-500">{{ ing.quantityText }}</span>
                            </div>
                            <p v-if="ing.notes" class="mt-0.5 text-xs text-gray-400">{{ ing.notes }}</p>
                        </div>
                    </label>
                </div>

                <p v-else class="rounded-2xl bg-gray-50 px-4 py-3 text-sm text-gray-500">
                    No ingredients listed.
                </p>
            </section>

            <!-- Steps checklist -->
            <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
                <div class="mb-4 flex items-center justify-between">
                    <h2 class="text-lg font-bold text-gray-900">Steps</h2>
                    <span class="text-sm text-gray-400">{{ orderedSteps.length }} steps</span>
                </div>

                <div v-if="orderedSteps.length" class="space-y-3">
                    <label v-for="step in orderedSteps" :key="step.id || step.orderIndex"
                        class="flex cursor-pointer items-start gap-4 rounded-2xl p-4 transition"
                        :class="checkedSteps.includes(stepKey(step)) ? 'bg-green-50 ring-1 ring-green-100' : 'bg-gray-50'">
                        <!-- Step number / check indicator -->
                        <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-full text-sm font-bold transition"
                            :class="checkedSteps.includes(stepKey(step))
                                ? 'bg-green-500 text-white'
                                : 'bg-[#f45b3f] text-white'">
                            <Check v-if="checkedSteps.includes(stepKey(step))" class="h-5 w-5" />
                            <span v-else>{{ step.orderIndex }}</span>
                        </div>

                        <div class="flex-1 space-y-1">
                            <p class="text-sm font-semibold transition"
                                :class="checkedSteps.includes(stepKey(step)) ? 'text-gray-400 line-through' : 'text-gray-800'">
                                Step {{ step.orderIndex }}
                            </p>
                            <p class="text-sm leading-6 transition"
                                :class="checkedSteps.includes(stepKey(step)) ? 'text-gray-400' : 'text-gray-600'">
                                {{ step.text }}
                            </p>
                        </div>

                        <input v-model="checkedSteps" :value="stepKey(step)" type="checkbox"
                            class="mt-1 h-4 w-4 rounded border-gray-300 text-green-500 focus:ring-green-500" />
                    </label>
                </div>

                <p v-else class="rounded-2xl bg-gray-50 px-4 py-3 text-sm text-gray-500">
                    No steps available.
                </p>
            </section>

            <!-- Finish button -->
            <button type="button"
                class="w-full rounded-full px-6 py-4 text-base font-bold text-white shadow-xl transition" :class="allStepsDone
                    ? 'bg-green-500 hover:bg-green-600'
                    : 'bg-[#f45b3f] hover:bg-[#e24a2e]'" :disabled="saving" @click="finishCooking">
                <span v-if="saving">Saving…</span>
                <span v-else-if="allStepsDone" class="inline-flex items-center gap-2">
                    <CheckCircle2 class="h-5 w-5" />
                    <span>Finish &amp; Save to Cooked Recipes</span>
                </span> <span v-else>Finish Cooking ({{ checkedSteps.length }}/{{ orderedSteps.length }} done)</span>
            </button>

        </div>

        <!-- ── Success overlay ───────────────────────────────────── -->
        <Transition name="fade">
            <div v-if="showSuccess"
                class="fixed inset-0 z-[300] flex flex-col items-center justify-center gap-6 bg-white px-8 text-center">
                <div class="flex h-24 w-24 items-center justify-center rounded-full bg-green-100 text-5xl">
                    <ChefHat class="h-15 w-15" />
                </div>
                <h2 class="text-2xl font-bold text-gray-900">Recipe saved!</h2>
                <p class="text-sm text-gray-500 leading-6">
                    <strong>{{ recipe?.title }}</strong> has been added to your Profile.
                    Check it on your <span class="font-semibold text-[#f45b3f]">Cooked Recipes</span> section.
                </p>
                <p class="text-sm text-gray-400">Time: {{ formattedTime }}</p>
                <button type="button"
                    class="rounded-full bg-[#f45b3f] px-8 py-3 text-sm font-bold text-white shadow transition hover:bg-[#e24a2e]"
                    @click="goToProfile">
                    Go to My Recipe Book
                </button>
            </div>
        </Transition>

    </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Check, Timer } from 'lucide-vue-next'
import { useAuthStore } from '../stores/authStore'
import recipeService from '../services/recipeService'
import ingredientService from '../services/ingredientService'
import recipeIngredientService from '../services/recipeIngredientService'
import recipeStepService from '../services/recipeStepService'
import cookedRecipeService from '../services/cookedRecipeService'
import { getRecipeImage } from '../utils/recipeImage'
import { ChefHat } from 'lucide-vue-next'
import { CheckCircle2 } from 'lucide-vue-next'

// ── Router / Store ───────────────────────────────────────────
const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

// ── State ────────────────────────────────────────────────────
// Datos de la receta y sus ingredientes y pasos
const recipe = ref(null)
const ingredients = ref([])
const recipeIngredients = ref([])
const recipeSteps = ref([])
const loading = ref(true)
const error = ref('')
const saving = ref(false)
const showSuccess = ref(false)

// Listas de ingredientes y pasos marcados como completados
const checkedIngredients = ref([])
const checkedSteps = ref([])

// ── Timer ────────────────────────────────────────────────────
const elapsed = ref(0)   // seconds
const timerRunning = ref(true)
let timerInterval = null

// Inicia el cronómetro si no está ya corriendo
function startTimer() {
    if (timerInterval) return
    timerInterval = setInterval(() => {
        if (timerRunning.value) elapsed.value++
    }, 1000)
}

// Pausa o reanuda el cronómetro
function toggleTimer() {
    timerRunning.value = !timerRunning.value
}

// Detiene y limpia el intervalo del cronómetro
function stopTimer() {
    clearInterval(timerInterval)
    timerInterval = null
}

// Formatea los segundos del cronómetro en formato mm:ss o hh:mm:ss
const formattedTime = computed(() => {
    const h = Math.floor(elapsed.value / 3600)
    const m = Math.floor((elapsed.value % 3600) / 60)
    const s = elapsed.value % 60
    const pad = (n) => String(n).padStart(2, '0')
    return h > 0 ? `${pad(h)}:${pad(m)}:${pad(s)}` : `${pad(m)}:${pad(s)}`
})

// ── Computed ─────────────────────────────────────────────────
const currentUserId = computed(() => Number(authStore.currentUser?.id || 0))
const recipeImage = computed(() => getRecipeImage(recipe.value))

// Pasos de la receta ordenados por su índice
const orderedSteps = computed(() =>
    [...recipeSteps.value].sort((a, b) => Number(a.orderIndex) - Number(b.orderIndex))
)

// Construye la lista de ingredientes con nombre, cantidad y notas para mostrar en pantalla
const ingredientChecklist = computed(() =>
    recipeIngredients.value.map((ri) => {
        const ing = ingredients.value.find((i) => Number(i.id) === Number(ri.ingredientId))
        const qty = ri.quantityDecimal
        const unit = ri.unit || ing?.unitDefault || ''
        const quantityText = qty != null ? `${qty} ${unit}`.trim() : unit ? `unit: ${unit}` : '—'
        return {
            key: `${ri.recipeId}-${ri.ingredientId}`,
            name: ing?.name || 'Unknown ingredient',
            quantityText,
            notes: ri.notes || '',
        }
    })
)

// Calcula el porcentaje de pasos completados para la barra de progreso
const progressPercent = computed(() => {
    if (!orderedSteps.value.length) return 0
    return Math.round((checkedSteps.value.length / orderedSteps.value.length) * 100)
})

// Indica si todos los pasos están marcados como completados
const allStepsDone = computed(() =>
    orderedSteps.value.length > 0 &&
    checkedSteps.value.length === orderedSteps.value.length
)

// ── Helpers ──────────────────────────────────────────────────
// Genera una clave única para identificar cada paso en la lista de checks
function stepKey(step) {
    return `step-${step.id || step.orderIndex}`
}

// ── Actions ──────────────────────────────────────────────────
// Guarda la receta cocinada con el tiempo transcurrido y muestra la pantalla de éxito
async function finishCooking() {
    if (!currentUserId.value || !recipe.value?.id || saving.value) return

    saving.value = true
    timerRunning.value = false

    try {
        await cookedRecipeService.save({
            userId: currentUserId.value,
            recipeId: Number(recipe.value.id),
            elapsedSeconds: elapsed.value,
        })
        showSuccess.value = true
    } catch (err) {
        console.error('Error saving cooked recipe:', err)
        alert('Could not save the recipe. Please try again.')
    } finally {
        saving.value = false
    }
}

// Redirige al perfil del usuario
function goToProfile() {
    router.push({ name: 'profile' })
}

// ── Load ─────────────────────────────────────────────────────
// Carga la receta, sus ingredientes y pasos, y arranca el cronómetro
async function load() {
    loading.value = true
    error.value = ''

    try {
        await authStore.initialize()

        const recipeId = Number(route.params.id)
        const recipeRes = await recipeService.getById(recipeId)
        recipe.value = recipeRes.data

        if (!recipe.value) throw new Error('Recipe not found.')

        const [ingAll, riRes, stepsRes] = await Promise.all([
            ingredientService.getAll(),
            recipeIngredientService.getByRecipeId(recipeId),
            recipeStepService.getByRecipeId(recipeId),
        ])

        ingredients.value = ingAll.data || []
        recipeIngredients.value = riRes.data || []
        recipeSteps.value = stepsRes.data || []

        // Start timer immediately
        startTimer()
    } catch (err) {
        console.error('Error loading cooking view:', err)
        error.value = 'Could not load the recipe.'
    } finally {
        loading.value = false
    }
}

onMounted(load)
onUnmounted(stopTimer)
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.35s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>