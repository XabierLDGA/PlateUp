export const RECIPE_CATEGORIES = [
  'Vegan',
  'Vegetarian',
  'Meat',
  'Fish',
  'Pasta',
  'Rice',
  'Soup',
  'Salad',
  'Breakfast',
  'Dessert',
  'Quick',
  'Student',
  'Healthy',
  'Snack',
]

export const DEFAULT_RECIPE_CATEGORY = 'Quick'

const LEGACY_CATEGORY_MAP = {
  Vegana: 'Vegan',
  Vegetariana: 'Vegetarian',
  Carne: 'Meat',
  Pescado: 'Fish',
  Arroz: 'Rice',
  Sopa: 'Soup',
  Ensalada: 'Salad',
  Desayuno: 'Breakfast',
  Postre: 'Dessert',
  Rápido: 'Quick',
  Estudiante: 'Student',
  Saludable: 'Healthy',
}

export function normalizeRecipeCategory(category) {
  const normalizedCategory = LEGACY_CATEGORY_MAP[category] || category

  return RECIPE_CATEGORIES.includes(normalizedCategory)
    ? normalizedCategory
    : DEFAULT_RECIPE_CATEGORY
}