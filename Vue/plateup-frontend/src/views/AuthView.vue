<template>
  <div class="container-app flex min-h-screen items-center py-8">
    <div class="w-full space-y-6">
      <section class="rounded-[36px] bg-gradient-to-br from-[#f45b3f] to-[#ff8a66] p-6 text-white shadow-xl">
        <p class="text-sm text-white/80">Welcome to</p>
        <h1 class="mt-2 text-4xl font-bold">PlateUp</h1>
        <p class="mt-3 text-sm leading-6 text-white/90">
          Sign in to continue with your recipes, followers and profile.
        </p>
      </section>

      <section class="rounded-[32px] bg-white p-5 shadow-sm ring-1 ring-black/5">
        <div class="grid grid-cols-2 gap-3">
          <button
            type="button"
            class="rounded-full px-4 py-3 text-sm font-semibold transition"
            :class="mode === 'login' ? 'bg-[#f45b3f] text-white' : 'bg-gray-100 text-gray-700'"
            @click="setMode('login')"
          >
            Sign In
          </button>

          <button
            type="button"
            class="rounded-full px-4 py-3 text-sm font-semibold transition"
            :class="mode === 'register' ? 'bg-[#f45b3f] text-white' : 'bg-gray-100 text-gray-700'"
            @click="setMode('register')"
          >
            Create Account
          </button>
        </div>

        <form v-if="mode === 'login'" class="mt-5 space-y-4" @submit.prevent="submitLogin">
          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Username or email</label>
            <input
              v-model="loginForm.identifier"
              type="text"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Enter your username or email"
              required
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Password</label>
            <input
              v-model="loginForm.password"
              type="password"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Enter your password"
              required
            />
          </div>

          <p v-if="errorMessage" class="rounded-2xl bg-red-50 px-4 py-3 text-sm font-medium text-red-600">
            {{ errorMessage }}
          </p>

          <button
            type="submit"
            class="w-full rounded-full bg-[#f45b3f] px-6 py-4 text-base font-bold text-white shadow-lg transition hover:bg-[#e24a2e]"
            :disabled="authStore.loading"
          >
            {{ authStore.loading ? 'Signing in...' : 'Sign In' }}
          </button>
        </form>

        <form v-else class="mt-5 space-y-4" @submit.prevent="submitRegister">
          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Username</label>
            <input
              v-model="registerForm.username"
              type="text"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Choose a username"
              required
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Display name</label>
            <input
              v-model="registerForm.displayName"
              type="text"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="How your name will appear"
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Email</label>
            <input
              v-model="registerForm.email"
              type="email"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Enter your email"
              required
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Password</label>
            <input
              v-model="registerForm.password"
              type="password"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Create a password"
              required
            />
          </div>

          <div>
            <label class="mb-2 block text-sm font-semibold text-gray-700">Bio</label>
            <textarea
              v-model="registerForm.bio"
              rows="3"
              class="w-full rounded-2xl border border-gray-200 px-4 py-3 text-sm"
              placeholder="Tell us something about you"
            ></textarea>
          </div>

          <p class="rounded-2xl bg-orange-50 px-4 py-3 text-sm text-gray-700">
            You can upload your avatar after creating the account from the profile settings screen.
          </p>

          <p v-if="errorMessage" class="rounded-2xl bg-red-50 px-4 py-3 text-sm font-medium text-red-600">
            {{ errorMessage }}
          </p>

          <button
            type="submit"
            class="w-full rounded-full bg-[#f45b3f] px-6 py-4 text-base font-bold text-white shadow-lg transition hover:bg-[#e24a2e]"
            :disabled="authStore.loading"
          >
            {{ authStore.loading ? 'Creating account...' : 'Create Account' }}
          </button>
        </form>
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const mode = ref('login')
const errorMessage = ref('')

const loginForm = ref({
  identifier: '',
  password: '',
})

const registerForm = ref({
  username: '',
  displayName: '',
  email: '',
  password: '',
  bio: '',
  visibilityDefault: 'public',
})

function setMode(nextMode) {
  mode.value = nextMode
  errorMessage.value = ''
}

async function submitLogin() {
  errorMessage.value = ''

  try {
    await authStore.login({
      identifier: loginForm.value.identifier,
      password: loginForm.value.password,
    })

    router.push({ name: 'profile' })
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || 'The login could not be completed.'
  }
}

async function submitRegister() {
  errorMessage.value = ''

  try {
    await authStore.register({
      username: registerForm.value.username,
      displayName: registerForm.value.displayName,
      email: registerForm.value.email,
      password: registerForm.value.password,
      bio: registerForm.value.bio,
      avatarUrl: '',
      visibilityDefault: registerForm.value.visibilityDefault,
    })

    router.push({ name: 'profile' })
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || 'The account could not be created.'
  }
}
</script>