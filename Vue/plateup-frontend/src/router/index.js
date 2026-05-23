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

// Definición de todas las rutas de la aplicación
const routes = [
  {
    // Ruta raíz: redirige al perfil si hay sesión activa, o al login si no
    path: '/',
    name: 'root',
    component: { template: '<div></div>' },
  },
  {
    // Página de inicio de sesión y registro, accesible sin autenticación
    path: '/login',
    name: 'login',
    component: AuthView,
    meta: { public: true },
  },
  {
    // Feed principal con las recetas de la comunidad
    path: '/home',
    name: 'home',
    component: HomeView,
  },
  {
    // Explorador de recetas con búsqueda y filtros
    path: '/explore',
    name: 'explore',
    component: ExploreView,
  },
  {
    // Formulario para crear una nueva receta
    path: '/create',
    name: 'create',
    component: CreateView,
  },
  {
    // Perfil del usuario autenticado
    path: '/profile',
    name: 'profile',
    component: ProfileView,
  },
  {
    // Formulario para editar el perfil del usuario autenticado
    path: '/profile/edit',
    name: 'edit-profile',
    component: EditProfileView,
  },
  {
    // Lista de seguidores y seguidos de un usuario concreto
    path: '/users/:id/connections',
    name: 'connections',
    component: ConnectionsView,
    props: true,
  },
  {
    // Perfil público de cualquier usuario de la plataforma
    path: '/users/:id',
    name: 'user-profile',
    component: UserProfileView,
    props: true,
  },
  {
    // Vista de detalle de una receta con ingredientes, pasos y comentarios
    path: '/recipes/:id',
    name: 'recipe-detail',
    component: RecipeDetailView,
    props: true,
  },
  {
    // Modo de cocinado paso a paso con temporizador
    path: '/recipes/:id/cook',
    name: 'cooking',
    component: CookingView,
    props: true,
  },
  {
    // Panel de administración, solo accesible para usuarios con rol ADMIN
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

// Guardia de navegación global: gestiona el acceso a rutas protegidas
router.beforeEach(async (to) => {
  const authStore = useAuthStore(pinia)
  // Nos aseguramos de que el estado de autenticación esté listo antes de decidir
  await authStore.initialize()

  // La raíz redirige según el estado de sesión
  if (to.name === 'root') {
    return authStore.isAuthenticated ? { name: 'profile' } : { name: 'login' }
  }

  if (to.meta.public) {
    // Si ya está autenticado y va al login, lo mandamos directamente al perfil
    if (to.name === 'login' && authStore.isAuthenticated) {
      return { name: 'profile' }
    }

    return true
  }

  // Si la ruta requiere autenticación y el usuario no ha iniciado sesión, redirigimos al login
  if (!authStore.isAuthenticated) {
    return { name: 'login' }
  }

  // Si la ruta es solo para administradores y el usuario no lo es, lo mandamos al home
  if (to.meta.adminOnly && !authStore.isAdmin) {
    return { name: 'home' }
  }

  return true
})

export default router