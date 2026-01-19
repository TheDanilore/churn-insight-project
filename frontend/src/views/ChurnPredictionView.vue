<template>
  <MainLayout>
    <div class="container-fluid">
      <ChurnForm />
    </div>
  </MainLayout>
</template>

<script setup>
import ChurnForm from '@/components/ChurnForm.vue'
import MainLayout from '@/components/layouts/MainLayout.vue';
</script>


<style scoped>
.churn-view {
  padding: 40px 20px;
  max-width: 900px;
  margin: 0 auto;
}


@media (max-width: 768px) {
  .churn-view {
    padding: 20px 16px;
  }
}

.container-fluid {
    display: flex;
    flex-direction: column;
    gap: 2.5rem;
    padding: 2.5rem 2rem;
    background: linear-gradient(135deg,
            var(--tertiary-bg) 0%,
            var(--white) 50%,
            var(--tertiary-bg) 100%);
    min-height: 100vh;
    background-attachment: fixed;
}
</style>
