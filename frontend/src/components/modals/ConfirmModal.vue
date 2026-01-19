<template>
  <Teleport to="body">
    <Transition name="modal-fade">
      <div v-if="isVisible" class="modal-overlay" @click.self="cancel">
        <div class="modal-container modal-sm" role="dialog" aria-modal="true">
          <div class="modal-header">
            <div class="header-icon" :class="iconColorClass">
              <IconComponent :name="computedIcon" :size="24" />
            </div>
            <div>
              <h3 class="modal-title">{{ title }}</h3>
              <p v-if="subtitle" class="modal-subtitle">{{ subtitle }}</p>
            </div>
          </div>

          <div class="modal-body">
            <p v-html="formattedMessage"></p>
          </div>

          <div class="modal-footer">
            <div class="footer-actions">
              <button class="btn btn-secondary" @click="cancel" :disabled="loading">
                {{ cancelText }}
              </button>
              <button class="btn" :class="confirmButtonClass" @click="confirm" :disabled="loading">
                <IconComponent v-if="loading" name="loader" :size="18" class="spinner" />
                <span>{{ confirmText }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, computed } from 'vue'
import IconComponent from '@/components/icons/IconComponents.vue'

const props = defineProps({
  title: {
    type: String,
    default: 'Confirmar acción',
  },
  subtitle: {
    type: String,
    default: '',
  },
  message: {
    type: String,
    default: '¿Estás seguro de realizar esta acción?',
  },
  confirmText: {
    type: String,
    default: 'Confirmar',
  },
  cancelText: {
    type: String,
    default: 'Cancelar',
  },
  type: {
    type: String,
    default: 'warning', // 'warning', 'danger', 'info', 'success'
    validator: (value) => ['warning', 'danger', 'info', 'success'].includes(value),
  },
  icon: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['confirm', 'cancel'])

const isVisible = ref(false)
const loading = ref(false)

const computedIcon = computed(() => {
  if (props.icon) return props.icon

  const defaultIcons = {
    warning: 'alert-triangle',
    danger: 'alert-circle',
    info: 'info',
    success: 'check-circle',
  }
  return defaultIcons[props.type] || 'alert-triangle'
})

const iconColorClass = computed(() => {
  const classes = {
    warning: 'icon-warning',
    danger: 'icon-danger',
    info: 'icon-info',
    success: 'icon-success',
  }
  return classes[props.type] || classes.warning
})

const confirmButtonClass = computed(() => {
  const classes = {
    warning: 'btn-warning',
    danger: 'btn-danger',
    info: 'btn-primary',
    success: 'btn-success',
  }
  return classes[props.type] || classes.warning
})

// Formatear mensaje: convertir \n en <br>
const formattedMessage = computed(() => {
  if (!props.message) return ''
  return props.message.replace(/\n/g, '<br>')
})

const show = () => {
  isVisible.value = true
  loading.value = false
}

const hide = () => {
  isVisible.value = false
  loading.value = false
}

const confirm = () => {
  loading.value = true
  emit('confirm')
}

const cancel = () => {
  if (!loading.value) {
    emit('cancel')
    hide()
  }
}

// Exponer métodos para uso externo (vía template ref)
defineExpose({
  show,
  hide,
  isVisible,
})
</script>

<style scoped>
/* Header */
.modal-header {
  padding: 2rem 2rem 1.5rem;
  display: flex;
  align-items: center;
  gap: 1.25rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
  justify-content: flex-start;
}

.header-icon {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: 50%;
}

.icon-warning {
  background: rgba(255, 193, 7, 0.15);
  color: #ffc107;
}

.icon-danger {
  background: rgba(220, 53, 69, 0.15);
  color: #dc3545;
}

.icon-info {
  background: rgba(59, 125, 221, 0.15);
  color: #3b7ddd;
}

.icon-success {
  background: rgba(40, 167, 69, 0.15);
  color: #28a745;
}

.modal-title {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--emphasis-color);
  line-height: 1.3;
}

.modal-subtitle {
  margin: 0.25rem 0 0 0;
  font-size: 0.875rem;
  color: var(--secondary-color);
  font-weight: 400;
}

/* Body */
.modal-body {
  padding: 1.5rem 2rem;
}

.modal-body p {
  margin: 0;
  color: var(--black);
  font-size: 0.9375rem;
  line-height: 1.6;
  white-space: pre-line; /* Respeta saltos de línea */
}

</style>
