import { resolveMediaUrl } from './media'

function buildRecipeFallback(recipe) {
  const title = recipe?.title?.trim() || 'PlateUp Recipe'

  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="1200" height="800" viewBox="0 0 1200 800">
      <rect width="1200" height="800" fill="#fff4ef" />
      <circle cx="600" cy="310" r="120" fill="#f45b3f" opacity="0.15" />
      <text
        x="50%"
        y="47%"
        dominant-baseline="middle"
        text-anchor="middle"
        font-family="Arial, sans-serif"
        font-size="72"
        font-weight="700"
        fill="#f45b3f"
      >
        PlateUp
      </text>
      <text
        x="50%"
        y="60%"
        dominant-baseline="middle"
        text-anchor="middle"
        font-family="Arial, sans-serif"
        font-size="38"
        fill="#374151"
      >
        ${title.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')}
      </text>
    </svg>
  `.trim()

  // Codificamos el SVG como Data URL para poder usarlo directamente en un atributo src
  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`
}

// Si no hay imagen propia, genera un SVG de sustitución con el título de la receta
export function getRecipeImage(recipe) {
  if (recipe?.imageUrl && recipe.imageUrl.trim() !== '') {
    return resolveMediaUrl(recipe.imageUrl)
  }

  return buildRecipeFallback(recipe)
}