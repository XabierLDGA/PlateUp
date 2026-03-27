export function getUserAvatar(user) {
  if (user?.avatarUrl && user.avatarUrl.trim() !== '') {
    return user.avatarUrl
  }

  const name =
    user?.displayName?.trim() ||
    user?.username?.trim() ||
    'PlateUp'

  return `https://ui-avatars.com/api/?name=${encodeURIComponent(name)}&background=f45b3f&color=fff`
}