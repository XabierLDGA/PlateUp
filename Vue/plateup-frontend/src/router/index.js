import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/authStore'
import { pinia } from '../stores'
import AuthView from '../views/AuthView.vue'
import HomeView from '../views/HomeView.vue'
import ExploreView from '../views/ExploreView.vue'
import CreateView from '../views/CreateView.vue'
import ProfileView from '../views/ProfileView.vue'
import EditProfileView from '../views/EditProfileView.vue'
import RecipeDetailView from '../views/RecipeDetailView.vue'
import UserProfileView from '../views/UserProfileView.vue'
import ConnectionsView from '../views/ConnectionsView.vue'
import CookingView from '../views/CookingView.vue'
import AdminDashboardView from '../views/AdminDashboardView.vue'

const routes = [
  {
    path: '/',
    name: 'root',
    component: { template: '<div></div>' },
  },
  {
    path: '/login',
    name: 'login',
    component: AuthView,
    meta: { public: true },
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/explore',
    name: 'explore',
    component: ExploreView,
  },
  {
    path: '/create',
    name: 'create',
    component: CreateView,
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
  },
  {
    path: '/profile/edit',
    name: 'edit-profile',
    component: EditProfileView,
  },
  {
    path: '/users/:id/connections',
    name: 'connections',
    component: ConnectionsView,
    props: true,
  },
  {
    path: '/users/:id',
    name: 'user-profile',
    component: UserProfileView,
    props: true,
  },
  {
    path: '/recipes/:id',
    name: 'recipe-detail',
    component: RecipeDetailView,
    props: true,
  },
  {
    path: '/recipes/:id/cook',
    name: 'cooking',
    component: CookingView,
    props: true,
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminDashboardView,
    meta: { adminOnly: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach(async (to) => {
  const authStore = useAuthStore(pinia)
  await authStore.initialize()

  if (to.name === 'root') {
    return authStore.isAuthenticated ? { name: 'profile' } : { name: 'login' }
  }

  if (to.meta.public) {
    if (to.name === 'login' && authStore.isAuthenticated) {
      return { name: 'profile' }
    }

    return true
  }

  if (!authStore.isAuthenticated) {
    return { name: 'login' }
  }

  if (to.meta.adminOnly && !authStore.isAdmin) {
    return { name: 'home' }
  }

  return true
})

export default router