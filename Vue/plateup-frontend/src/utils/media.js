// URL base del backend obtenida de las variables de entorno
const API_URL = import.meta.env.VITE_API_URL || ''

// Extrae la URL base del servidor sin el segmento "/api" para construir rutas de recursos estáticos
function getBackendBaseUrl() {
  if (!API_URL) return ''

  try {
    const url = new URL(API_URL)
    // Quitamos el sufijo "/api" del path para obtener la raíz del servidor
    const pathWithoutApi = url.pathname.replace(/\/api\/?$/, '')
    const normalizedPath = pathWithoutApi === '/' ? '' : pathWithoutApi
    return `${url.origin}${normalizedPath}`
  } catch {
    // Si la URL no es válida, intentamos un reemplazo simple como alternativa
    return API_URL.replace(/\/api\/?$/, '')
  }
}

// Convierte una ruta relativa de un recurso en una URL absoluta apuntando al backend.
// Si ya es una URL completa (http, data, blob) la devuelve tal cual sin modificar.
export function resolveMediaUrl(path) {
  if (!path || path.trim() === '') {
    return ''
  }

  // Si ya es una URL absoluta o un recurso embebido, no hace falta transformarla
  if (/^(https?:|data:|blob:)/i.test(path)) {
    return path
  }

  const backendBaseUrl = getBackendBaseUrl()

  if (path.startsWith('/')) {
    return `${backendBaseUrl}${path}`
  }

  return `${backendBaseUrl}/${path}`
}