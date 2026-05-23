// Lista de todas las categorías de recetas válidas en la aplicación
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

// Categoría que se asigna por defecto si no se especifica ninguna o la indicada no es válida
export const DEFAULT_RECIPE_CATEGORY = 'Quick'

// Mapa de compatibilidad con categorías antiguas en español para no romper recetas ya existentes
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

// Normaliza el nombre de una categoría al valor canónico actual.
// Si la categoría viene del sistema antiguo (en español), la traduce; si no es válida, usa la por defecto.
export function normalizeRecipeCategory(category) {
  const normalizedCategory = LEGACY_CATEGORY_MAP[category] || category

  return RECIPE_CATEGORIES.includes(normalizedCategory)
    ? normalizedCategory
    : DEFAULT_RECIPE_CATEGORY
}