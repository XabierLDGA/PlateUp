import { resolveMediaUrl } from './media'

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

  return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`
}

export function getUserAvatar(user) {
  if (user?.avatarUrl && user.avatarUrl.trim() !== '') {
    return resolveMediaUrl(user.avatarUrl)
  }

  return buildAvatarFallback(user)
}