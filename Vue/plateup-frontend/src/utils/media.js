const API_URL = import.meta.env.VITE_API_URL || ''

function getBackendBaseUrl() {
  if (!API_URL) return ''

  try {
    const url = new URL(API_URL)
    const pathWithoutApi = url.pathname.replace(/\/api\/?$/, '')
    const normalizedPath = pathWithoutApi === '/' ? '' : pathWithoutApi
    return `${url.origin}${normalizedPath}`
  } catch {
    return API_URL.replace(/\/api\/?$/, '')
  }
}

export function resolveMediaUrl(path) {
  if (!path || path.trim() === '') {
    return ''
  }

  if (/^(https?:|data:|blob:)/i.test(path)) {
    return path
  }

  const backendBaseUrl = getBackendBaseUrl()

  if (path.startsWith('/')) {
    return `${backendBaseUrl}${path}`
  }

  return `${backendBaseUrl}/${path}`
}