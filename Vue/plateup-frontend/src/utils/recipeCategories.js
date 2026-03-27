export const RECIPE_CATEGORIES = [
  'Vegana',
  'Vegetariana',
  'Carne',
  'Pescado',
  'Pasta',
  'Arroz',
  'Sopa',
  'Ensalada',
  'Desayuno',
  'Postre',
  'Rápido',
  'Estudiante',
  'Saludable',
  'Snack',
]

export const DEFAULT_RECIPE_CATEGORY = 'Rápido'

export function normalizeRecipeCategory(category) {
  return RECIPE_CATEGORIES.includes(category) ? category : DEFAULT_RECIPE_CATEGORY
}