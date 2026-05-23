import { resolveMediaUrl } from './media'

// Obtiene las iniciales del usuario a partir de su nombre o, si no tiene, de su username
function getInitials(user) {
  const source =
    user?.displayName?.trim() ||
    user?.username?.trim() ||
    'PlateUp'

  return source
    .split(' ')
    .map((part) => part[0])
    .join('')
    .slice(0, 2)
    .toUpperCase()
}

// Genera un avatar SVG circular con las iniciales del usuario cuando no tiene foto de perfil
function buildAvatarFallback(user) {
  const initials = getInitials(user)

  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" viewBox="0 0 200 200">
      <rect width="200" height="200" rx="100" fill="#f45b3f" />
      <text
        x="50%"
        y="50%"
        dominant-baseline="middle"
        text-anchor="middle"
        font-family="Arial, sans-serif"
        font-size="72"
        font-weight="700"
        fill="#ffffff"
      >
        ${initials}
      </text>
    </svg>
  `.trim()

  // Codificamos el SVG como Data URL para poder usarlo directamente en un atributo src
  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`
}

// Devuelve la URL del avatar de un usuario.
// Si tiene foto de perfil la resuelve; si no, genera un avatar con sus iniciales.
export function getUserAvatar(user) {
  if (user?.avatarUrl && user.avatarUrl.trim() !== '') {
    return resolveMediaUrl(user.avatarUrl)
  }

  return buildAvatarFallback(user)
}