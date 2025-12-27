<template>
  <footer class="footer">
    <div class="footer-content">
      <p class="footer-credit">
        Hecho con <span class="heart">❤️</span> por <strong>Grupo 05 - Hackathon NoCountry</strong><br>
        Diciembre 2025 - Enero 2026
      </p>
      <p class="footer-copyright">&copy; {{ currentYear }} Todos los derechos reservados</p>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped>
/* ========================
   FOOTER
   ======================== */
.footer {
  background: var(--bg-white);
  border-top: 1px solid var(--border-color);
  padding: 24px 20px;
  text-align: center;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-credit {
  margin: 0 0 12px;
  font-size: 0.95rem;
  color: var(--text-secondary);
  line-height: 1.6;
}

.footer-credit strong {
  color: var(--primary-color);
  font-weight: 700;
}

.heart {
  display: inline-block;
  animation: heartBeat 1.5s ease-in-out infinite;
}

@keyframes heartBeat {
  0%, 100% {
    transform: scale(1);
  }
  14% {
    transform: scale(1.15);
  }
  28% {
    transform: scale(1);
  }
}

.footer-copyright {
  margin: 0;
  font-size: 0.85rem;
  color: var(--text-tertiary);
}

/* ========================
   RESPONSIVE
   ======================== */
@media (max-width: 768px) {
  .footer {
    padding: 16px 12px;
  }

  .footer-credit {
    font-size: 0.85rem;
    margin-bottom: 8px;
  }

  .footer-copyright {
    font-size: 0.75rem;
  }
}
</style>
