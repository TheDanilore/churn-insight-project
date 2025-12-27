<template>
  <Teleport to="body">
    <div class="toast-container">
      <TransitionGroup name="toast">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          v-show="toast.visible"
          class="toast"
          :class="[`toast-${toast.type}`]"
          @click="remove(toast.id)"
        >
          <div class="toast-icon">
            <IconComponent v-if="toast.type === 'success'" name="check-circle" :size="20" />
            <IconComponent v-else-if="toast.type === 'error'" name="x-circle" :size="20" />
            <IconComponent v-else-if="toast.type === 'warning'" name="alert-triangle" :size="20" />
            <IconComponent v-else name="info" :size="20" />
          </div>
          <div class="toast-message">{{ toast.message }}</div>
          <button class="toast-close" @click.stop="remove(toast.id)">
            <IconComponent name="x" :size="16" />
          </button>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup>
import { useToast } from '@/composables/useToast'
import IconComponent from '@/components/icons/IconComponents.vue'

const { toasts, remove } = useToast()
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 1rem;
  right: 1rem;
  z-index: 10000;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  pointer-events: none;
}

.toast {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 1.25rem;
  background: var(--white);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  min-width: 320px;
  max-width: 500px;
  pointer-events: all;
  cursor: pointer;
  border-left: 4px solid;
  transition: all 0.3s ease;
}

.toast:hover {
  transform: translateX(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.toast-success {
  border-left-color: #28a745;
}

.toast-success .toast-icon {
  color: #28a745;
}

.toast-error {
  border-left-color: #dc3545;
}

.toast-error .toast-icon {
  color: #dc3545;
}

.toast-warning {
  border-left-color: #ffc107;
}

.toast-warning .toast-icon {
  color: #ffc107;
}

.toast-info {
  border-left-color: #17a2b8;
}

.toast-info .toast-icon {
  color: #17a2b8;
}

.toast-icon {
  flex-shrink: 0;
}

.toast-message {
  flex: 1;
  font-size: 0.875rem;
  color: var(--emphasis-color);
  line-height: 1.5;
}

.toast-close {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  border: none;
  background: none;
  color: var(--secondary-color);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.toast-close:hover {
  background: var(--secondary-bg);
  color: var(--emphasis-color);
}

/* Animations */
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}

.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}

.toast-leave-to {
  opacity: 0;
  transform: translateX(100%) scale(0.8);
}

.toast-move {
  transition: transform 0.3s ease;
}

/* Dark mode */
[data-theme='dark'] .toast {
  background: #2d3748;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

[data-theme='dark'] .toast-message {
  color: #e2e8f0;
}

/* Mobile responsive */
@media (max-width: 576px) {
  .toast-container {
    top: 0.5rem;
    right: 0.5rem;
    left: 0.5rem;
    align-items: stretch;
  }

  .toast {
    min-width: auto;
    max-width: none;
  }
}
</style>
