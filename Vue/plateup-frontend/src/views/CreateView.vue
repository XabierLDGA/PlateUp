<template>
  <div class="container-app space-y-6 py-6">
    <header class="space-y-2">
      <p class="text-sm font-medium text-gray-500">Share your best dish</p>
      <h1 class="text-3xl font-bold text-gray-900">Create Recipe</h1>
    </header>

    <form @submit.prevent="publishRecipe" class="space-y-5">
      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="space-y-4">
          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Title</label>
            <input
              v-model="form.title"
              type="text"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Creamy truffle pasta"
              required
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Description</label>
            <textarea
              v-model="form.description"
              rows="4"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Tell us about your recipe..."
              required
            ></textarea>
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Image URL</label>
            <input
              v-model="form.imageUrl"
              type="url"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="https://example.com/recipe.jpg"
            />
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="mb-2 block text-sm font-semibold text-gray-700">Time</label>
              <input
                v-model.number="form.totalMinutes"
                type="number"
                class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
                placeholder="30"
                min="1"
                required
              />
            </div>

            <div>
              <label class="mb-2 block text-sm font-semibold text-gray-700">Servings</label>
              <input
                v-model.number="form.servings"
                type="number"
                class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
                placeholder="2"
                min="1"
                required
              />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="mb-2 block text-sm font-semibold text-gray-700">Category</label>
              <select
                v-model="form.category"
                class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
                required
              >
                <option
                  v-for="category in RECIPE_CATEGORIES"
                  :key="category"
                  :value="category"
                >
                  {{ category }}
                </option>
              </select>
            </div>

            <div>
              <label class="mb-2 block text-sm font-semibold text-gray-700">Difficulty</label>
              <select
                v-model="form.difficulty"
                class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              >
                <option value="easy">easy</option>
                <option value="medium">medium</option>
                <option value="hard">hard</option>
              </select>
            </div>
          </div>
        </div>
      </section>

      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="mb-4 flex items-center justify-between">
          <h2 class="text-lg font-bold text-gray-900">Ingredients</h2>
          <button
            type="button"
            @click="addIngredient"
            class="rounded-full bg-orange-50 px-4 py-2 text-sm font-semibold text-[#f45b3f]"
          >
            Add Ingredient
          </button>
        </div>

        <div class="space-y-3">
          <div
            v-for="(ingredient, index) in form.ingredients"
            :key="index"
            class="grid grid-cols-12 gap-2"
          >
            <input
              v-model="ingredient.name"
              type="text"
              placeholder="Ingredient name"
              class="col-span-5 rounded-2xl border border-gray-200 px-4 py-3 text-sm"
            />
            <input
              v-model.number="ingredient.quantity"
              type="number"
              step="0.01"
              placeholder="Qty"
              class="col-span-3 rounded-2xl border border-gray-200 px-4 py-3 text-sm"
            />
            <input
              v-model="ingredient.unit"
              type="text"
              placeholder="Unit"
              class="col-span-3 rounded-2xl border border-gray-200 px-4 py-3 text-sm"
            />
            <button
              type="button"
              @click="removeIngredient(index)"
              class="col-span-1 rounded-2xl bg-gray-100 text-sm font-bold text-gray-500"
            >
              ×
            </button>
          </div>
        </div>
      </section>

      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="mb-4 flex items-center justify-between">
          <h2 class="text-lg font-bold text-gray-900">Steps</h2>
          <button
            type="button"
            @click="addStep"
            class="rounded-full bg-orange-50 px-4 py-2 text-sm font-semibold text-[#f45b3f]"
          >
            Add Step
          </button>
        </div>

        <div class="space-y-3">
          <div
            v-for="(step, index) in form.steps"
            :key="index"
            class="flex gap-3 rounded-2xl bg-gray-50 p-3"
          >
            <div class="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-[#f45b3f] font-bold text-white">
              {{ index + 1 }}
            </div>

            <textarea
              v-model="step.text"
              rows="3"
              placeholder="Describe this step..."
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
            ></textarea>

            <button
              type="button"
              @click="removeStep(index)"
              class="h-10 w-10 rounded-full bg-gray-200 text-lg font-bold text-gray-600"
            >
              ×
            </button>
          </div>
        </div>
      </section>

      <button
        type="submit"
        :disabled="publishing"
        class="w-full rounded-full bg-[#f45b3f] px-6 py-4 text-base font-bold text-white shadow-lg transition hover:bg-[#e24a2e] disabled:cursor-not-allowed disabled:opacity-70"
      >
        {{ publishing ? 'Publishing...' : 'Publish' }}
      </button>

      <p
        v-if="message"
        class="rounded-2xl px-4 py-3 text-sm font-medium"
        :class="messageType === 'success'
          ? 'bg-green-50 text-green-700'
          : 'bg-red-50 text-red-600'"
      >
        {{ message }}
      </p>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '../stores/authStore'
import recipeService from '../services/recipeService'
import ingredientService from '../services/ingredientService'
import recipeIngredientService from '../services/recipeIngredientService'
import recipeStepService from '../services/recipeStepService'
import {
  DEFAULT_RECIPE_CATEGORY,
  RECIPE_CATEGORIES,
  normalizeRecipeCategory,
} from '../utils/recipeCategories'

const authStore = useAuthStore()

const message = ref('')
const messageType = ref('success')
const publishing = ref(false)

const form = ref({
  title: '',
  description: '',
  imageUrl: '',
  totalMinutes: 30,
  servings: 2,
  category: DEFAULT_RECIPE_CATEGORY,
  difficulty: 'easy',
  ingredients: [{ name: '', quantity: null, unit: '' }],
  steps: [{ text: '' }],
})

function addIngredient() {
  form.value.ingredients.push({ name: '', quantity: null, unit: '' })
}

function removeIngredient(index) {
  form.value.ingredients.splice(index, 1)

  if (form.value.ingredients.length === 0) {
    form.value.ingredients.push({ name: '', quantity: null, unit: '' })
  }
}

function addStep() {
  form.value.steps.push({ text: '' })
}

function removeStep(index) {
  form.value.steps.splice(index, 1)

  if (form.value.steps.length === 0) {
    form.value.steps.push({ text: '' })
  }
}

function resetForm() {
  form.value = {
    title: '',
    description: '',
    imageUrl: '',
    totalMinutes: 30,
    servings: 2,
    category: DEFAULT_RECIPE_CATEGORY,
    difficulty: 'easy',
    ingredients: [{ name: '', quantity: null, unit: '' }],
    steps: [{ text: '' }],
  }
}

async function findOrCreateIngredient(rawIngredient, existingIngredients) {
  const normalizedName = rawIngredient.name.trim().toLowerCase()

  let existing = existingIngredients.find(
    (item) => item.name?.trim().toLowerCase() === normalizedName
  )

  if (existing) {
    return existing
  }

  const response = await ingredientService.create({
    name: rawIngredient.name.trim(),
    unitDefault: rawIngredient.unit?.trim() || '',
  })

  existing = response.data
  existingIngredients.push(existing)

  return existing
}

async function publishRecipe() {
  if (publishing.value) return

  publishing.value = true
  message.value = ''

  try {
    await authStore.initialize()

    const validIngredients = form.value.ingredients.filter(
      (ingredient) => ingredient.name.trim() !== ''
    )

    const validSteps = form.value.steps.filter(
      (step) => step.text.trim() !== ''
    )

    const recipePayload = {
      userId: authStore.currentUser.id,
      title: form.value.title,
      description: form.value.description,
      category: normalizeRecipeCategory(form.value.category),
      imageUrl: form.value.imageUrl,
      servings: form.value.servings,
      totalMinutes: form.value.totalMinutes,
      difficulty: form.value.difficulty,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }

    const recipeResponse = await recipeService.create(recipePayload)
    const createdRecipe = recipeResponse.data

    const ingredientsResponse = await ingredientService.getAll()
    const existingIngredients = [...(ingredientsResponse.data || [])]

    for (const ingredient of validIngredients) {
      const ingredientRecord = await findOrCreateIngredient(ingredient, existingIngredients)

      await recipeIngredientService.create({
        recipeId: createdRecipe.id,
        ingredientId: ingredientRecord.id,
        quantityDecimal:
          ingredient.quantity === '' || ingredient.quantity === null || Number.isNaN(Number(ingredient.quantity))
            ? null
            : Number(ingredient.quantity),
        unit: ingredient.unit?.trim() || ingredientRecord.unitDefault || '',
        notes: null,
      })
    }

    for (let index = 0; index < validSteps.length; index++) {
      const step = validSteps[index]

      await recipeStepService.create({
        recipeId: createdRecipe.id,
        orderIndex: index + 1,
        text: step.text.trim(),
        timerSeconds: null,
        mediaUrl: null,
      })
    }

    messageType.value = 'success'
    message.value = 'Recipe published successfully.'
    resetForm()
  } catch (error) {
    console.error('Error publishing recipe:', error)
    messageType.value = 'error'
    message.value = 'The recipe could not be published correctly.'
  } finally {
    publishing.value = false
  }
}
</script>