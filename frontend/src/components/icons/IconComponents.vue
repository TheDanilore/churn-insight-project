<script setup>
defineProps({
  name: {
    type: String,
    required: true,
  },
  size: {
    type: Number,
    default: 24,
  },
  color: {
    type: String,
    default: 'currentColor',
  },
})
</script>

<template>
  <svg v-if="name === 'github'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path
      d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22">
    </path>
  </svg>



  <svg v-else-if="name === 'linkedin'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M16 8a6 6 0 0 1 6 6v7h-4v-7a2 2 0 0 0-2-2 2 2 0 0 0-2 2v7h-4v-7a6 6 0 0 1 6-6z"></path>
    <rect x="2" y="9" width="4" height="12"></rect>
    <circle cx="4" cy="4" r="2"></circle>
  </svg>

  <svg v-else-if="name === 'email'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
    <polyline points="22,6 12,13 2,6"></polyline>
  </svg>

  <svg v-else-if="name === 'youtube'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path
      d="M22.54 6.42a2.78 2.78 0 0 0-1.94-2C18.88 4 12 4 12 4s-6.88 0-8.6.46a2.78 2.78 0 0 0-1.94 2A29 29 0 0 0 1 11.75a29 29 0 0 0 .46 5.33A2.78 2.78 0 0 0 3.4 19c1.72.46 8.6.46 8.6.46s6.88 0 8.6-.46a2.78 2.78 0 0 0 1.94-2 29 29 0 0 0 .46-5.25 29 29 0 0 0-.46-5.33z">
    </path>
    <polygon points="9.75 15.02 15.5 11.75 9.75 8.48 9.75 15.02"></polygon>
  </svg>

  <svg v-else-if="name === 'eye'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
    <circle cx="12" cy="12" r="3"></circle>
  </svg>

  <svg v-else-if="name === 'eye-off'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24">
    </path>
    <line x1="1" y1="1" x2="23" y2="23"></line>
  </svg>

  <svg v-else-if="name === 'code'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <polyline points="16 18 22 12 16 6"></polyline>
    <polyline points="8 6 2 12 8 18"></polyline>
  </svg>

  <svg v-else-if="name === 'credit-card'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect>
    <line x1="1" y1="10" x2="23" y2="10"></line>
  </svg>

  <svg v-else-if="name === 'download'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
    <polyline points="7 10 12 15 17 10"></polyline>
    <line x1="12" y1="15" x2="12" y2="3"></line>
  </svg>

  <svg v-else-if="name === 'arrow-up'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="12" y1="19" x2="12" y2="5"></line>
    <polyline points="5 12 12 5 19 12"></polyline>
  </svg>

  <svg v-else-if="name === 'arrow-down'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="12" y1="5" x2="12" y2="19"></line>
    <polyline points="19 12 12 19 5 12"></polyline>
  </svg>

  <svg v-else-if="name === 'arrow-left'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="19" y1="12" x2="5" y2="12"></line>
    <polyline points="12 19 5 12 12 5"></polyline>
  </svg>

  <svg v-else-if="name === 'arrow-right'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="5" y1="12" x2="19" y2="12"></line>
    <polyline points="12 5 19 12 12 19"></polyline>
  </svg>

  <svg v-else-if="name === 'send'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="22" y1="2" x2="11" y2="13"></line>
    <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
  </svg>

  <svg v-else-if="name === 'sun'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <circle cx="12" cy="12" r="5"></circle>
    <line x1="12" y1="1" x2="12" y2="3"></line>
    <line x1="12" y1="21" x2="12" y2="23"></line>
    <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
    <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
    <line x1="1" y1="12" x2="3" y2="12"></line>
    <line x1="21" y1="12" x2="23" y2="12"></line>
    <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
    <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
  </svg>

  <svg v-else-if="name === 'moon'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
  </svg>

  <svg v-else-if="name === 'menu'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="3" y1="12" x2="21" y2="12"></line>
    <line x1="3" y1="6" x2="21" y2="6"></line>
    <line x1="3" y1="18" x2="21" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'close'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <line x1="18" y1="6" x2="6" y2="18"></line>
    <line x1="6" y1="6" x2="18" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'x-exit'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="18" y1="6" x2="6" y2="18"></line>
    <line x1="6" y1="6" x2="18" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'columns'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 3h7a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-7"></path>
    <path d="M12 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h7"></path>
    <line x1="12" y1="3" x2="12" y2="21"></line>
  </svg>

  <svg v-else-if="name === 'x'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="18" y1="6" x2="6" y2="18"></line>
    <line x1="6" y1="6" x2="18" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'server'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <rect x="2" y="2" width="20" height="8" rx="2" ry="2"></rect>
    <rect x="2" y="14" width="20" height="8" rx="2" ry="2"></rect>
    <line x1="6" y1="6" x2="6.01" y2="6"></line>
    <line x1="6" y1="18" x2="6.01" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'smartphone'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <rect x="5" y="2" width="14" height="20" rx="2" ry="2"></rect>
    <line x1="12" y1="18" x2="12.01" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'layout'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
    <line x1="3" y1="9" x2="21" y2="9"></line>
    <line x1="9" y1="21" x2="9" y2="9"></line>
  </svg>

  <svg v-else-if="name === 'database'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <ellipse cx="12" cy="5" rx="9" ry="3"></ellipse>
    <path d="M21 12c0 1.66-4 3-9 3s-9-1.34-9-3"></path>
    <path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"></path>
  </svg>

  <svg v-else-if="name === 'shopping-cart'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <circle cx="9" cy="21" r="1"></circle>
    <circle cx="20" cy="21" r="1"></circle>
    <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
  </svg>

  <svg v-else-if="name === 'users'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
    <circle cx="9" cy="7" r="4"></circle>
    <path d="M23 21v-2a4 4 0 0 0-3-3.87"></path>
    <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
  </svg>

  <svg v-else-if="name === 'message-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2">
    <path
      d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z">
    </path>
  </svg>

  <svg v-else-if="name === 'link'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71"></path>
    <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path>
  </svg>

  <svg v-else-if="name === 'briefcase'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
    <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
  </svg>

  <svg v-else-if="name === 'award'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <circle cx="12" cy="8" r="7"></circle>
    <polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88"></polyline>
  </svg>

  <svg v-else-if="name === 'check-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
    <polyline points="22 4 12 14.01 9 11.01"></polyline>
  </svg>


  <svg v-else-if="name === 'star'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polygon
      points="12 2 15.09 10.26 23.77 11.27 17.88 17.07 19.24 25.77 12 21.77 4.76 25.77 6.12 17.07 0.22 11.27 8.91 10.26 12 2">
    </polygon>
  </svg>

  <svg v-else-if="name === 'coffee'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M18 8h1a4 4 0 0 1 0 8h-1"></path>
    <path d="M2 8h16v9a4 4 0 0 1-4 4H6a4 4 0 0 1-4-4V8z"></path>
    <line x1="6" y1="1" x2="6" y2="4"></line>
    <line x1="10" y1="1" x2="10" y2="4"></line>
    <line x1="14" y1="1" x2="14" y2="4"></line>
  </svg>

  <svg v-else-if="name === 'book-open'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"></path>
    <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"></path>
  </svg>

  <svg v-else-if="name === 'globe'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <circle cx="12" cy="12" r="10"></circle>
    <line x1="2" y1="12" x2="22" y2="12"></line>
    <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"></path>
  </svg>

  <svg v-else-if="name === 'file-text'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
    <polyline points="14 2 14 8 20 8"></polyline>
    <line x1="16" y1="13" x2="8" y2="13"></line>
    <line x1="16" y1="17" x2="8" y2="17"></line>
    <polyline points="10 9 9 9 8 9"></polyline>
  </svg>

  <svg v-else-if="name === 'file'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
    <polyline points="14 2 14 8 20 8"></polyline>
    <line x1="16" y1="13" x2="8" y2="13"></line>
    <line x1="16" y1="17" x2="8" y2="17"></line>
    <polyline points="10 9 9 9 8 9"></polyline>
  </svg>

  <svg v-else-if="name === 'copy'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect>
    <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path>
  </svg>

  <svg v-else-if="name === 'rotate-ccw'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="1 4 1 10 7 10"></polyline>
    <path d="M3.51 15a9 9 0 1 0 2.13-9.36L1 10"></path>
  </svg>

  <svg v-else-if="name === 'save'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"></path>
    <polyline points="17 21 17 13 7 13 7 21"></polyline>
    <polyline points="7 3 7 8 15 8"></polyline>
  </svg>

  <svg v-else-if="name === 'upload'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
    <polyline points="17 8 12 3 7 8"></polyline>
    <line x1="12" y1="3" x2="12" y2="15"></line>
  </svg>

  <svg v-else-if="name === 'trash-2'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="3 6 5 6 21 6"></polyline>
    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
    <line x1="10" y1="11" x2="10" y2="17"></line>
    <line x1="14" y1="11" x2="14" y2="17"></line>
  </svg>

  <svg v-else-if="name === 'trash'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="3 6 5 6 21 6"></polyline>
    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
    <line x1="10" y1="11" x2="10" y2="17"></line>
    <line x1="14" y1="11" x2="14" y2="17"></line>
  </svg>

  <svg v-else-if="name === 'wifi'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M5 12.55a11 11 0 0 1 14.08 0"></path>
    <path d="M1.42 9a16 16 0 0 1 21.16 0"></path>
    <path d="M8.53 16.11a6 6 0 0 1 6.95 0"></path>
    <line x1="12" y1="20" x2="12.01" y2="20"></line>
  </svg>

  <svg v-else-if="name === 'zoom-in'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <circle cx="11" cy="11" r="8"></circle>
    <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
    <line x1="11" y1="8" x2="11" y2="14"></line>
    <line x1="8" y1="11" x2="14" y2="11"></line>
  </svg>

  <svg v-else-if="name === 'zoom-out'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <circle cx="11" cy="11" r="8"></circle>
    <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
    <line x1="8" y1="11" x2="14" y2="11"></line>
  </svg>

  <svg v-else-if="name === 'maximize'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M8 3H5a2 2 0 0 0-2 2v3m18 0V5a2 2 0 0 0-2-2h-3m0 18h3a2 2 0 0 0 2-2v-3M3 16v3a2 2 0 0 0 2 2h3"></path>
  </svg>

  <svg v-else-if="name === 'minimize'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <path d="M8 3v3a2 2 0 0 1-2 2H3m18 0h-3a2 2 0 0 1-2-2V3m0 18v-3a2 2 0 0 1 2-2h3M3 16h3a2 2 0 0 1 2 2v3"></path>
  </svg>

  <svg v-else-if="name === 'refresh-cw'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <polyline points="23 4 23 10 17 10"></polyline>
    <polyline points="1 20 1 14 7 14"></polyline>
    <path d="M3.51 9a9 9 0 0 1 14.85-3.36L23 10M1 14l4.64 4.36A9 9 0 0 0 20.49 15"></path>
  </svg>

  <svg v-else-if="name === 'calendar'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2">
    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
    <line x1="16" y1="2" x2="16" y2="6"></line>
    <line x1="8" y1="2" x2="8" y2="6"></line>
    <line x1="3" y1="10" x2="21" y2="10"></line>
  </svg>

  <svg v-else-if="name === 'gamepad'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="6" y1="12" x2="10" y2="12"></line>
    <line x1="8" y1="10" x2="8" y2="14"></line>
    <line x1="15" y1="13" x2="15.01" y2="13"></line>
    <line x1="18" y1="11" x2="18.01" y2="11"></line>
    <rect x="2" y="6" width="20" height="12" rx="4"></rect>
  </svg>

  <svg v-else-if="name === 'trophy'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6"></path>
    <path d="M18 9h1.5a2.5 2.5 0 0 0 0-5H18"></path>
    <path d="M4 22h16"></path>
    <path d="M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22"></path>
    <path d="M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22"></path>
    <path d="M18 2H6v7a6 6 0 0 0 12 0V2Z"></path>
  </svg>

  <!-- Iconos adicionales para el sistema de inventario -->
  <svg v-else-if="name === 'chevron-right'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="9 18 15 12 9 6"></polyline>
  </svg>

  <svg v-else-if="name === 'chevron-left'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="15 18 9 12 15 6"></polyline>
  </svg>

  <svg v-else-if="name === 'chevron-up'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="18 15 12 9 6 15"></polyline>
  </svg>

  <svg v-else-if="name === 'chevron-down'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="6 9 12 15 18 9"></polyline>
  </svg>

  <svg v-else-if="name === 'search'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="11" cy="11" r="8"></circle>
    <path d="m21 21-4.35-4.35"></path>
  </svg>

  <svg v-else-if="name === 'list'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="8" y1="6" x2="21" y2="6"></line>
    <line x1="8" y1="12" x2="21" y2="12"></line>
    <line x1="8" y1="18" x2="21" y2="18"></line>
    <line x1="3" y1="6" x2="3.01" y2="6"></line>
    <line x1="3" y1="12" x2="3.01" y2="12"></line>
    <line x1="3" y1="18" x2="3.01" y2="18"></line>
  </svg>

  <svg v-else-if="name === 'grid'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="2" y="2" width="8" height="8"></rect>
    <rect x="14" y="2" width="8" height="8"></rect>
    <rect x="14" y="14" width="8" height="8"></rect>
    <rect x="2" y="14" width="8" height="8"></rect>
  </svg>

  <svg v-else-if="name === 'maximize-2'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="15 3 21 3 21 9"></polyline>
    <polyline points="9 21 3 21 3 15"></polyline>
    <line x1="21" y1="3" x2="14" y2="10"></line>
    <line x1="3" y1="21" x2="10" y2="14"></line>
  </svg>

  <svg v-else-if="name === 'tag'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z"></path>
    <line x1="7" y1="7" x2="7.01" y2="7"></line>
  </svg>

  <svg v-else-if="name === 'clipboard'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"></path>
    <rect x="8" y="2" width="8" height="4" rx="1" ry="1"></rect>
  </svg>

  <svg v-else-if="name === 'box'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z">
    </path>
    <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
    <line x1="12" y1="22.08" x2="12" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'activity'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
  </svg>

  <svg v-else-if="name === 'alert-triangle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
    <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
  </svg>

  <svg v-else-if="name === 'user'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>
    <circle cx="12" cy="7" r="4"></circle>
  </svg>

  <svg v-else-if="name === 'alert-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <line x1="12" y1="8" x2="12" y2="12"></line>
    <line x1="12" y1="16" x2="12.01" y2="16"></line>
  </svg>

  <svg v-else-if="name === 'settings'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="3"></circle>
    <path
      d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z">
    </path>
  </svg>

  <svg v-else-if="name === 'log-out'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
    <polyline points="16 17 21 12 16 7"></polyline>
    <line x1="21" y1="12" x2="9" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'trending-up'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"></polyline>
    <polyline points="17 6 23 6 23 12"></polyline>
  </svg>

  <svg v-else-if="name === 'trending-down'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="23 18 13.5 8.5 8.5 13.5 1 6"></polyline>
    <polyline points="17 18 23 18 23 12"></polyline>
  </svg>

  <svg v-else-if="name === 'dollar-sign'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="12" y1="1" x2="12" y2="23"></line>
    <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
  </svg>

  <svg v-else-if="name === 'bar-chart'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="12" y1="20" x2="12" y2="10"></line>
    <line x1="18" y1="20" x2="18" y2="4"></line>
    <line x1="6" y1="20" x2="6" y2="16"></line>
  </svg>

  <svg v-else-if="name === 'pie-chart'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M21.21 15.89A10 10 0 1 1 8 2.83"></path>
    <path d="M22 12A10 10 0 0 0 12 2v10z"></path>
  </svg>

  <svg v-else-if="name === 'mail'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
    <polyline points="22,6 12,13 2,6"></polyline>
  </svg>

  <svg v-else-if="name === 'phone'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z">
    </path>
  </svg>

  <svg v-else-if="name === 'lock'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
  </svg>

  <svg v-else-if="name === 'key'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M21 2l-2 2m-7.61 7.61a5.5 5.5 0 1 1-7.778 7.778 5.5 5.5 0 0 1 7.777-7.777zm0 0L15.5 7.5m0 0l3 3L22 7l-3-3m-3.5 3.5L19 4">
    </path>
  </svg>

  <svg v-else-if="name === 'crystal'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 2L5 7v10l7 5 7-5V7l-7-5z"></path>
    <path d="M12 2v10M5 7h14M12 17v5"></path>
  </svg>

  <svg v-else-if="name === 'home'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"></path>
    <polyline points="9 22 9 12 15 12 15 22"></polyline>
  </svg>

  <svg v-else-if="name === 'layers'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polygon points="12 2 2 7 12 12 22 7 12 2"></polygon>
    <polyline points="2 17 12 22 22 17"></polyline>
    <polyline points="2 12 12 17 22 12"></polyline>
  </svg>

  <svg v-else-if="name === 'edit'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
  </svg>

  <svg v-else-if="name === 'edit-2'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M17 3a2.828 2.828 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5L17 3z"></path>
  </svg>

  <svg v-else-if="name === 'cube'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" stroke="currentColor"
    stroke-width="2">
    <path
      d="M21 16V8a2 2 0 00-1-1.73l-7-4a2 2 0 00-2 0l-7 4A2 2 0 003 8v8a2 2 0 001 1.73l7 4a2 2 0 002 0l7-4A2 2 0 0021 16z" />
  </svg>

  <svg v-else-if="name === 'truck'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="1" y="3" width="15" height="13"></rect>
    <polygon points="16 8 20 8 23 11 23 16 16 16 16 8"></polygon>
    <circle cx="5.5" cy="18.5" r="2.5"></circle>
    <circle cx="18.5" cy="18.5" r="2.5"></circle>
  </svg>

  <svg v-else-if="name === 'target'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <circle cx="12" cy="12" r="6"></circle>
    <circle cx="12" cy="12" r="2"></circle>
  </svg>

  <svg v-else-if="name === 'repeat'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="17 1 21 5 17 9"></polyline>
    <path d="M3 11V9a4 4 0 0 1 4-4h14"></path>
    <polyline points="7 23 3 19 7 15"></polyline>
    <path d="M21 13v2a4 4 0 0 1-4 4H3"></path>
  </svg>

  <svg v-else-if="name === 'clock'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <polyline points="12 6 12 12 16 14"></polyline>
  </svg>

  <svg v-else-if="name === 'percent'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="19" y1="5" x2="5" y2="19"></line>
    <circle cx="6.5" cy="6.5" r="2.5"></circle>
    <circle cx="17.5" cy="17.5" r="2.5"></circle>
  </svg>

  <svg v-else-if="name === 'plus'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="12" y1="5" x2="12" y2="19"></line>
    <line x1="5" y1="12" x2="19" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'ruler'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M21.3 15.3a2.4 2.4 0 0 1 0 3.4l-2.6 2.6a2.4 2.4 0 0 1-3.4 0L2.7 8.7a2.41 2.41 0 0 1 0-3.4l2.6-2.6a2.41 2.41 0 0 1 3.4 0Z">
    </path>
    <path d="m14.5 12.5 2-2"></path>
    <path d="m11.5 9.5 2-2"></path>
    <path d="m8.5 6.5 2-2"></path>
    <path d="m17.5 15.5 2-2"></path>
  </svg>

  <svg v-else-if="name === 'folder'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"></path>
  </svg>

  <svg v-else-if="name === 'archive'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="21 8 21 21 3 21 3 8"></polyline>
    <line x1="1" y1="3" x2="23" y2="3"></line>
    <path d="M10 12h4"></path>
  </svg>

  <svg v-else-if="name === 'calendar-check'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
    <line x1="3" y1="10" x2="21" y2="10"></line>
    <line x1="16" y1="2" x2="16" y2="6"></line>
    <line x1="8" y1="2" x2="8" y2="6"></line>
    <polyline points="9 16 12 19 16 14"></polyline>
  </svg>

  <svg v-else-if="name === 'timer'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <polyline points="12 6 12 12 16 14"></polyline>
  </svg>

  <svg v-else-if="name === 'leaf'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 22a10 10 0 0 0 10-10c0-5.52-4.48-10-10-10S2 6.48 2 12a10 10 0 0 0 10 10z"></path>
    <path d="M12 4a8 8 0 0 0 0 16"></path>
  </svg>

  <svg v-else-if="name === 'N-Symbol'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="5" y1="21" x2="5" y2="3"></line>
    <line x1="5" y1="3" x2="19" y2="21"></line>
    <line x1="19" y1="21" x2="19" y2="3"></line>
  </svg>

  <svg v-else-if="name === 'N'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="5" y1="21" x2="5" y2="3"></line>
    <line x1="5" y1="3" x2="19" y2="21"></line>
    <line x1="19" y1="21" x2="19" y2="3"></line>
  </svg>

  <svg v-else-if="name === 'P-Symbol'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M5 3h10a4 4 0 0 1 0 8H5"></path>
    <line x1="5" y1="21" x2="5" y2="3"></line>
  </svg>

  <svg v-else-if="name === 'P'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M5 3h10a4 4 0 0 1 0 8H5"></path>
    <line x1="5" y1="21" x2="5" y2="3"></line>
  </svg>

  <svg v-else-if="name === 'K-Symbol'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="5" y1="3" x2="5" y2="21"></line>
    <line x1="5" y1="12" x2="20" y2="3"></line>
    <line x1="5" y1="12" x2="20" y2="21"></line>
  </svg>

  <svg v-else-if="name === 'K'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="5" y1="3" x2="5" y2="21"></line>
    <line x1="5" y1="12" x2="20" y2="3"></line>
    <line x1="5" y1="12" x2="20" y2="21"></line>
  </svg>

  <svg v-else-if="name === 'leaf-plus'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 22a10 10 0 0 0 10-10c0-5.52-4.48-10-10-10S2 6.48 2 12a10 10 0 0 0 10 10z"></path>
    <path d="M12 4a8 8 0 0 0 0 16"></path>
    <line x1="12" y1="10" x2="12" y2="14"></line>
    <line x1="10" y1="12" x2="14" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'bug'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="5" y="8" width="14" height="10" rx="3" ry="3"></rect>
    <path d="M12 8V5"></path>
    <path d="M9 5c-1 0-2 .5-2 2"></path>
    <path d="M15 5c1 0 2 .5 2 2"></path>
    <path d="M7 11l-4-4"></path>
    <path d="M17 11l4-4"></path>
    <path d="M7 15l-4 4"></path>
    <path d="M17 15l4 4"></path>
  </svg>

  <svg v-else-if="name === 'grass'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M17 8C17 11.0211 18.068 12.3964 19.3872 13.5682C20.7063 14.74 22 16.5 22 19V22H2V19C2 16.5 3.29368 14.74 4.61284 13.5682C5.93199 12.3964 7 11.0211 7 8C7 4.97886 9.69571 2 12 2C14.3043 2 17 4.97886 17 8Z">
    </path>
  </svg>

  <svg v-else-if="name === 'skull-crossbones'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="10" r="8"></circle>
    <path d="M9 10h.01"></path>
    <path d="M15 10h.01"></path>
    <path d="M9 14h6"></path>
    <line x1="4" y1="20" x2="20" y2="4"></line>
    <line x1="4" y1="4" x2="20" y2="20"></line>
  </svg>

  <svg v-else-if="name === 'spider'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="4"></circle>
    <path d="M12 16v6"></path>
    <path d="M12 2v2"></path>
    <path d="M8 8l-4-4"></path>
    <path d="M16 8l4-4"></path>
    <path d="M8 16l-4 4"></path>
    <path d="M16 16l4 4"></path>
    <path d="M5 12h-3"></path>
    <path d="M22 12h-3"></path>
  </svg>

  <svg v-else-if="name === 'syringe'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M18 10L12 4l-6 6"></path>
    <path d="M12 4v16"></path>
    <path d="M16 16h4"></path>
    <path d="M4 16h4"></path>
  </svg>

  <svg v-else-if="name === 'hand-paper'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M8 10V5a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v5"></path>
    <path d="M17 10v7a2 2 0 0 1-2 2H9a2 2 0 0 1-2-2v-7"></path>
    <path d="M5 15h14"></path>
    <path d="M12 19v3"></path>
  </svg>

  <svg v-else-if="name === 'shield-alt'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
    <line x1="12" y1="18" x2="12" y2="10"></line>
  </svg>

  <svg v-else-if="name === 'shield'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
  </svg>

  <svg v-else-if="name === 'heartbeat'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M22 12h-4l-3 9L9 3l-3 9H2"></path>
  </svg>

  <svg v-else-if="name === 'seedling'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 20c-3.31 0-6-2.69-6-6V4"></path>
    <path d="M12 20c3.31 0 6-2.69 6-6V4"></path>
    <path d="M8 12c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z"></path>
    <path d="M16 12c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2z"></path>
    <path d="M12 22v-2"></path>
  </svg>

  <svg v-else-if="name === 'atom'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="1"></circle>
    <ellipse cx="12" cy="12" rx="9" ry="3"></ellipse>
    <ellipse cx="12" cy="12" rx="3" ry="9" transform="rotate(60 12 12)"></ellipse>
    <ellipse cx="12" cy="12" rx="3" ry="9" transform="rotate(120 12 12)"></ellipse>
  </svg>

  <svg v-else-if="name === 'water'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M12 2.69L12 21.31M12 21.31C8.03 17.34 3 13.29 3 8.5A6 6 0 0 1 12 2.69M12 21.31C15.97 17.34 21 13.29 21 8.5A6 6 0 0 0 12 2.69" />
  </svg>

  <svg v-else-if="name === 'magic'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="21" y1="1" x2="1" y2="21"></line>
    <path d="M10 10l-2 2-2-2-2-2 2-2 2-2 2 2 2 2z"></path>
    <path d="M22 10l-2 2-2-2-2-2 2-2 2-2 2 2 2 2z"></path>
  </svg>

  <svg v-else-if="name === 'tint'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 2.69L12 18.5C12 20.43 10.43 22 8.5 22C6.57 22 5 20.43 5 18.5C5 13.29 10.03 9.24 12 2.69Z"></path>
    <path d="M12 2.69L12 18.5C12 20.43 13.57 22 15.5 22C17.43 22 19 20.43 19 18.5C19 13.29 13.97 9.24 12 2.69Z"></path>
  </svg>

  <svg v-else-if="name === 'Ca-Symbol'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M10 17a5 5 0 0 1-5-5 5 5 0 0 1 5-5"></path>
    <circle cx="17" cy="14" r="3"></circle>
    <line x1="20" y1="17" x2="20" y2="11"></line>
  </svg>

  <svg v-else-if="name === 'Ca'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M10 17a5 5 0 0 1-5-5 5 5 0 0 1 5-5"></path>
    <circle cx="17" cy="14" r="3"></circle>
    <line x1="20" y1="17" x2="20" y2="11"></line>
  </svg>

  <svg v-else-if="name === 'Mg-Symbol'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4 21V3l4 8 4-8v18"></path>
    <circle cx="18" cy="14" r="3"></circle>
    <path d="M21 14v5a3 3 0 0 1-3 3h-1"></path>
  </svg>

  <svg v-else-if="name === 'Mg'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4 21V3l4 8 4-8v18"></path>
    <circle cx="18" cy="14" r="3"></circle>
    <path d="M21 14v5a3 3 0 0 1-3 3h-1"></path>
  </svg>

  <svg v-else-if="name === 'recycle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="7.5 19 3 14.5 7.5 10"></polyline>
    <path d="M3 14.5v-1c0-2.48 2.02-4.5 4.5-4.5h13.5"></path>

    <polyline points="16.5 5 21 9.5 16.5 14"></polyline>
    <path d="M21 9.5v1c0 2.48-2.02 4.5-4.5 4.5H3"></path>

    <polyline points="10 4.5 14.5 0 19 4.5"></polyline>
    <path d="M14.5 19v-1c0-2.48-2.02-4.5-4.5-4.5H3"></path>
  </svg>

  <svg v-else-if="name === 'apple-alt'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M13.5 2.5C12.5 1.5 10 2 10 4A5 5 0 0 0 5 9c0 3 1.5 5 3 6s2.5 3.5 4 4c1.5-.5 3.5-3 5-4.5S19 12 19 9A5 5 0 0 0 13.5 2.5z">
    </path>
    <path d="M17 5c-1 0-2 1-2 2s1 2 2 2 2-1 2-2-1-2-2-2z"></path>
    <path d="M16 2l1.5 1.5"></path>
  </svg>

  <svg v-else-if="name === 'broadcast-tower'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M2 12s2-4 10-4 10 4 10 4"></path>
    <path d="M2 18s2-4 10-4 10 4 10 4"></path>
    <path d="M4 6s2-3 8-3 8 3 8 3"></path>
    <line x1="12" y1="22" x2="12" y2="2"></line>
  </svg>

  <svg v-else-if="name === 'brain'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 21c-4.97 0-9-3.58-9-8s4.03-8 9-8c4.97 0 9 3.58 9 8s-4.03 8-9 8z"></path>
    <path d="M12 21V3"></path>
    <path d="M9 16c0-2-2-3-2-5s2-3 2-5"></path>
    <path d="M15 16c0-2 2-3 2-5s-2-3-2-5"></path>
    <path d="M8 12h8"></path>
  </svg>

  <svg v-else-if="name === 'biohazard'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <circle cx="12" cy="12" r="2"></circle>
    <line x1="12" y1="14" x2="12" y2="22"></line>
    <line x1="5.63" y1="16.37" x2="3.1" y2="20.9"></line>
    <line x1="18.37" y1="16.37" x2="20.9" y2="20.9"></line>
  </svg>

  <svg v-else-if="name === 'asterisk'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="12" y1="2" x2="12" y2="22"></line>
    <line x1="20.39" y1="5.61" x2="3.61" y2="18.39"></line>
    <line x1="3.61" y1="5.61" x2="20.39" y2="18.39"></line>
  </svg>

  <svg v-else-if="name === 'stopwatch'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="13" r="10"></circle>
    <polyline points="12 9 12 13 14 15"></polyline>
    <line x1="8" y1="4" x2="16" y2="4"></line>
    <line x1="12" y1="4" x2="12" y2="2"></line>
  </svg>

  <svg v-else-if="name === 'history'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="12 6 12 12 16 14"></polyline>

    <circle cx="12" cy="12" r="10"></circle>

    <path d="M12 2.05a10 10 0 0 0-10 10c0 4.97 3.58 9.1 8.2 9.85"></path>
    <polyline points="7 9 3.14 12 7 15"></polyline>
  </svg>

  <svg v-else-if="name === 'plant'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 22V10"></path>
    <path d="M10 12c-2.21 0-4-1.79-4-4V2h8v6c0 2.21-1.79 4-4 4z"></path>
    <path d="M12 10l-4 4"></path>
    <path d="M12 10l4 4"></path>
  </svg>

  <svg v-else-if="name === 'mouse'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="5" y="3" width="14" height="18" rx="7" ry="7"></rect>
    <line x1="12" y1="7" x2="12" y2="11"></line>
  </svg>

  <svg v-else-if="name === 'vial'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M14 2h-4a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2z"></path>
    <line x1="10" y1="8" x2="14" y2="8"></line>
    <line x1="10" y1="12" x2="14" y2="12"></line>
    <line x1="10" y1="16" x2="14" y2="16"></line>
  </svg>

  <svg v-else-if="name === 'bacteria'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M11.5 2C8.75 2 6.5 4.25 6.5 7v10c0 2.75 2.25 5 5 5s5-2.25 5-5V7c0-2.75-2.25-5-5-5z"></path>
    <path d="M2 12h3"></path>
    <path d="M19 12h3"></path>
    <path d="M12 2V5"></path>
    <path d="M12 19V22"></path>
  </svg>

  <svg v-else-if="name === 'calendar-alt'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect>
    <line x1="16" y1="2" x2="16" y2="6"></line>
    <line x1="8" y1="2" x2="8" y2="6"></line>
    <line x1="3" y1="10" x2="21" y2="10"></line>
  </svg>

  <svg v-else-if="name === 'plus-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <line x1="12" y1="8" x2="12" y2="16"></line>
    <line x1="8" y1="12" x2="16" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'spa'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4 14v-2c0-1.1.9-2 2-2h4c1.1 0 2 .9 2 2v2"></path>
    <path d="M12 14c0 1.1-.9 2-2 2H6c-1.1 0-2-.9-2-2"></path>
    <circle cx="15" cy="12" r="4"></circle>
    <path d="M15 16v6"></path>
    <path d="M10 20h-4c-1.1 0-2-.9-2-2v-4"></path>
    <path d="M18 20h-4c-1.1 0-2-.9-2-2v-4"></path>
  </svg>

  <svg v-else-if="name === 'project-diagram'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="3" y="3" width="7" height="7"></rect>
    <rect x="14" y="3" width="7" height="7"></rect>
    <rect x="3" y="14" width="7" height="7"></rect>
    <rect x="14" y="14" width="7" height="7"></rect>
    <line x1="6.5" y1="10" x2="6.5" y2="14"></line>
    <line x1="17.5" y1="10" x2="17.5" y2="14"></line>
    <line x1="10" y1="6.5" x2="14" y2="6.5"></line>
  </svg>

  <svg v-else-if="name === 'apple'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M12 2.5C9.5 2.5 7 4.5 7 7c0 2 1 3.5 2.5 5S12 17 12 17c0 0 2.5-2.5 4-4S17 9 17 7c0-2.5-2.5-4.5-5-4.5z">
    </path>
    <path d="M16 5l1.5 1.5"></path>
  </svg>

  <svg v-else-if="name === 'sign-in-alt'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M15 3h6v18h-6"></path>
    <path d="M10 17l5-5-5-5"></path>
    <line x1="15" y1="12" x2="3" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'flask'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M6 14L3 22h18l-3-8"></path>
    <path d="M12 2v12"></path>
    <path d="M8 2h8"></path>
  </svg>

  <svg v-else-if="name === 'shopping-bag'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M6 2L3 7v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V7l-3-5z"></path>
    <line x1="3" y1="7" x2="21" y2="7"></line>
    <path d="M12 21a2 2 0 0 0 2-2V7a2 2 0 0 0-4 0v12a2 2 0 0 0 2 2z"></path>
  </svg>

  <svg v-else-if="name === 'loader'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="12" y1="2" x2="12" y2="6"></line>
    <line x1="12" y1="18" x2="12" y2="22"></line>
    <line x1="4.93" y1="4.93" x2="7.76" y2="7.76"></line>
    <line x1="16.24" y1="16.24" x2="19.07" y2="19.07"></line>
    <line x1="2" y1="12" x2="6" y2="12"></line>
    <line x1="18" y1="12" x2="22" y2="12"></line>
    <line x1="4.93" y1="19.07" x2="7.76" y2="16.24"></line>
    <line x1="16.24" y1="7.76" x2="19.07" y2="4.93"></line>
  </svg>

  <!-- Iconos adicionales para dashboard y componentes -->
  <svg v-else-if="name === 'bar-chart-2'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="18" y1="20" x2="18" y2="10"></line>
    <line x1="12" y1="20" x2="12" y2="4"></line>
    <line x1="6" y1="20" x2="6" y2="14"></line>
  </svg>

  <svg v-else-if="name === 'line-chart'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="3" y1="18" x2="3" y2="3"></line>
    <line x1="3" y1="3" x2="21" y2="3"></line>
    <polyline points="3 18 7 14 11 17 17 8 21 3"></polyline>
  </svg>

  <svg v-else-if="name === 'info'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <line x1="12" y1="16" x2="12" y2="12"></line>
    <line x1="12" y1="8" x2="12.01" y2="8"></line>
  </svg>

  <svg v-else-if="name === 'arrow-up-right'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="7" y1="17" x2="17" y2="7"></line>
    <polyline points="7 7 17 7 17 17"></polyline>
  </svg>

  <svg v-else-if="name === 'arrow-down-left'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="17" y1="7" x2="7" y2="17"></line>
    <polyline points="17 17 7 17 7 7"></polyline>
  </svg>

  <svg v-else-if="name === 'clipboard-list'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"></path>
    <rect x="8" y="2" width="8" height="4" rx="1" ry="1"></rect>
    <line x1="9" y1="11" x2="15" y2="11"></line>
    <line x1="9" y1="15" x2="15" y2="15"></line>
    <line x1="9" y1="19" x2="15" y2="19"></line>
  </svg>

  <svg v-else-if="name === 'user-plus'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
    <circle cx="8.5" cy="7" r="4"></circle>
    <line x1="20" y1="8" x2="20" y2="14"></line>
    <line x1="17" y1="11" x2="23" y2="11"></line>
  </svg>

  <svg v-else-if="name === 'package'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path
      d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z">
    </path>
    <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
    <line x1="12" y1="22.08" x2="12" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'bar-chart-3'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="3" y1="3" x2="3" y2="20"></line>
    <line x1="21" y1="3" x2="21" y2="20"></line>
    <line x1="9" y1="20" x2="9" y2="10"></line>
    <line x1="15" y1="20" x2="15" y2="6"></line>
  </svg>

  <svg v-else-if="name === 'arrow-down-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <polyline points="8 12 12 16 16 12"></polyline>
    <line x1="12" y1="8" x2="12" y2="16"></line>
  </svg>

  <svg v-else-if="name === 'arrow-up-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none"
    :stroke="color" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <polyline points="16 12 12 8 8 12"></polyline>
    <line x1="12" y1="16" x2="12" y2="8"></line>
  </svg>

  <svg v-else-if="name === 'check-square'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="9 11 12 14 22 4"></polyline>
    <path d="M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11"></path>
  </svg>

  <svg v-else-if="name === 'building'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4 21v-7a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v7"></path>
    <path d="M15 5v-1a1 1 0 0 0-1-1h-2a1 1 0 0 0-1 1v1"></path>
    <path d="M4 21h16"></path>
    <rect x="6" y="8" width="2" height="2"></rect>
    <rect x="10" y="8" width="2" height="2"></rect>
    <rect x="14" y="8" width="2" height="2"></rect>
    <rect x="6" y="13" width="2" height="2"></rect>
    <rect x="10" y="13" width="2" height="2"></rect>
    <rect x="14" y="13" width="2" height="2"></rect>
  </svg>

  <svg v-else-if="name === 'sliders'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="4" y1="21" x2="4" y2="14"></line>
    <line x1="4" y1="10" x2="4" y2="3"></line>
    <line x1="12" y1="21" x2="12" y2="12"></line>
    <line x1="12" y1="8" x2="12" y2="3"></line>
    <line x1="20" y1="21" x2="20" y2="16"></line>
    <line x1="20" y1="12" x2="20" y2="3"></line>
    <circle cx="4" cy="14" r="2"></circle>
    <circle cx="12" cy="12" r="2"></circle>
    <circle cx="20" cy="16" r="2"></circle>
  </svg>

  <svg v-else-if="name === 'inbox'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
    <polyline points="22 6 12 14 2 6"></polyline>
  </svg>

  <svg v-else-if="name === 'building-2'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M3 21H21V5C21 3.9 20.1 3 19 3H5C3.9 3 3 3.9 3 5V21Z"></path>
    <rect x="6" y="7" width="3" height="3"></rect>
    <rect x="15" y="7" width="3" height="3"></rect>
    <rect x="6" y="13" width="3" height="3"></rect>
    <rect x="15" y="13" width="3" height="3"></rect>
    <line x1="9" y1="21" x2="9" y2="17"></line>
    <line x1="15" y1="21" x2="15" y2="17"></line>
  </svg>

  <svg v-else-if="name === 'more-vertical'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="1"></circle>
    <circle cx="12" cy="5" r="1"></circle>
    <circle cx="12" cy="19" r="1"></circle>
  </svg>

  <svg v-else-if="name === 'barcode'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M3 5v14"></path>
    <path d="M8 5v14"></path>
    <path d="M12 5v14"></path>
    <path d="M17 5v14"></path>
    <path d="M21 5v14"></path>
    <path d="M6 5v14"></path>
    <path d="M10 5v14"></path>
    <path d="M14 5v14"></path>
    <path d="M18 5v14"></path>
  </svg>

  <svg v-else-if="name === 'filter'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
    stroke-width="2">
    <polygon points="22,3 2,3 10,12.46 10,19 14,21 14,12.46"></polygon>
  </svg>

  <svg v-else-if="name === 'map-pin'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
    <circle cx="12" cy="10" r="3"></circle>
  </svg>

  <svg v-else-if="name === 'empty-set'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <line x1="4.93" y1="19.07" x2="19.07" y2="4.93"></line>
  </svg>

  <svg v-else-if="name === 'empty-box'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="22 12 16 12 14 15 10 15 8 12 2 12"></polyline>
    <path
      d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z">
    </path>
  </svg>

  <svg v-else-if="name === 'inbox-empty'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <polyline points="22 12 16 12 14 15 10 15 8 12 2 12"></polyline>
    <path
      d="M5.45 5.11L2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z">
    </path>
  </svg>

  <svg v-else-if="name === 'zero'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="5" y="4" width="14" height="16" rx="7"></rect>
  </svg>

  <svg v-else-if="name === 'user-check'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M16 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path>
    <circle cx="8.5" cy="7" r="4"></circle>
    <polyline points="17 11 19 13 23 9"></polyline>
  </svg>

  <svg v-else-if="name === 'minus-circle'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <circle cx="12" cy="12" r="10"></circle>
    <line x1="8" y1="12" x2="16" y2="12"></line>
  </svg>

  <svg v-else-if="name === 'square'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
  </svg>

  <svg v-else-if="name === 'monitor'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
    <line x1="8" y1="21" x2="16" y2="21"></line>
    <line x1="12" y1="17" x2="12" y2="21"></line>
  </svg>

  <svg v-else-if="name === 'hard-drive'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <line x1="22" y1="7" x2="2" y2="7"></line>
    <rect x="2" y="9" width="20" height="8" rx="1" ry="1"></rect>
    <line x1="6" y1="14" x2="6" y2="14.01"></line>
    <line x1="10" y1="14" x2="10" y2="14.01"></line>
  </svg>

  <svg v-else-if="name === 'cpu'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <rect x="4" y="4" width="16" height="16" rx="2" ry="2"></rect>
    <rect x="9" y="9" width="6" height="6"></rect>
    <line x1="9" y1="1" x2="9" y2="4"></line>
    <line x1="15" y1="1" x2="15" y2="4"></line>
    <line x1="9" y1="20" x2="9" y2="23"></line>
    <line x1="15" y1="20" x2="15" y2="23"></line>
    <line x1="20" y1="9" x2="23" y2="9"></line>
    <line x1="20" y1="14" x2="23" y2="14"></line>
    <line x1="1" y1="9" x2="4" y2="9"></line>
    <line x1="1" y1="14" x2="4" y2="14"></line>
  </svg>

  <svg v-else-if="name === 'beaker'" :width="size" :height="size" viewBox="0 0 24 24" fill="none" :stroke="color"
    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
    <path d="M4.9 7h14.2M7 7h10v10c0 1.105-1.79 2-4 2s-4-.895-4-2V7z"></path>
    <line x1="9" y1="4" x2="15" y2="4"></line>
  </svg>
</template>
