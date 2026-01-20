<template>
    <div class="filters-bar">
        <div class="search-input-wrapper">
            <IconComponents name="search" :size="16" class="search-icon" />
            <input type="text" :value="filters.search" @input="$emit('update:search', $event.target.value)"
                @keyup.enter="$emit('filter')" placeholder="Buscar por nombre, email o teléfono..." class="form-input">
        </div>

        <select :value="filters.alerta" @change="$emit('update:alerta', $event.target.value); $emit('filter')"
            class="form-select">
            <option value="">Todos los Riesgos</option>
            <option value="ALTA">🔴 Alta Probabilidad</option>
            <option value="MEDIA">🟡 Media Probabilidad</option>
            <option value="BAJA">🟢 Baja Probabilidad</option>
        </select>

        <button @click="$emit('filter')" class="btn-filter">
            <IconComponents name="filter" :size="16" />
            Filtrar
        </button>
    </div>
</template>

<script setup>
import IconComponents from '@/components/icons/IconComponents.vue'
defineProps(['filters'])
defineEmits(['update:search', 'update:alerta', 'filter'])
</script>

<style scoped>
.filters-bar {
    padding: 16px 24px;
    display: flex;
    gap: 12px;
    background: var(--bg-lighter);
    border-bottom: 1px solid var(--border-color);
    flex-wrap: wrap;
}

.search-input-wrapper {
    flex-grow: 1;
    position: relative;
    min-width: 240px;
}

.search-icon {
    position: absolute;
    left: 12px;
    top: 50%;
    transform: translateY(-50%);
    color: var(--text-secondary);
}

.form-input {
    width: 100%;
    padding: 10px 12px 10px 36px;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    font-size: 0.9rem;
    background: var(--bg-white);
    color: var(--text-primary);
    transition: all 0.2s;
}

.form-input:focus,
.form-select:focus {
    border-color: var(--primary-color);
    outline: none;
    box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);
}

.form-select {
    padding: 10px 12px;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    font-size: 0.9rem;
    background: var(--bg-white);
    color: var(--text-primary);
    cursor: pointer;
    min-width: 160px;
}

.btn-filter {
    background: var(--primary-color);
    color: white;
    border: none;
    padding: 0 20px;
    border-radius: 8px;
    font-weight: 600;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 8px;
    transition: background 0.2s;
}

.btn-filter:hover {
    background: #4f46e5;
}

[data-theme='dark']  .filters-bar {
    background: var(--bg-card);
}

[data-theme='dark']  .form-input,
[data-theme='dark']  .form-select {
    background: var(--bg-lighter);
    border-color: var(--border-color);
    color: var(--text-primary);
}
</style>