<script setup>
// Importamos el componente
import ChurnForm from '@/components/ChurnForm.vue';
</script>

<template>
  <main>
    <ChurnForm />
  </main>
</template>
<style scoped>
  /* Reset básico para que ocupe toda la pantalla y tenga fondo gris */
body {
  margin: 0;
  background-color: #f7fafc; 
  min-height: 100vh;
}
</style>
