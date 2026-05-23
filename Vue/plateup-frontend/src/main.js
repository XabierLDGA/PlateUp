// Punto de entrada de la aplicación: registra los plugins globales y monta el componente raíz
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { pinia } from './stores'
import './assets/main.css'

const app = createApp(App)

// Pinia para el manejo de estado global (autenticación, datos de usuario, etc.)
app.use(pinia)
// Vue Router para la navegación entre páginas
app.use(router)
// Montamos la app en el elemento #app del index.html
app.mount('#app')