/**
 * practica8_wishlist.js
 *
 * Gestiona la lista de artículos favoritos en localStorage.
 * - Lee la lista actual (JSON) de la clave 'wishlist'.
 * - Permite añadir/quitar códigos de artículo al hacer click en botones.
 * - Actualiza el estado visual del icono (outline/filled).
 */

(function() {
  const STORAGE_KEY = 'wishlist';

  // Carga la wishlist de localStorage, devuelve un Set de strings
  function loadWishlist() {
    const raw = localStorage.getItem(STORAGE_KEY);
    try {
      const arr = raw ? JSON.parse(raw) : [];
      return new Set(arr);
    } catch (e) {
      console.error('Error parsing wishlist JSON:', e);
      return new Set();
    }
  }

  // Guarda el Set de wishlist en localStorage como JSON
  function saveWishlist(set) {
    const arr = Array.from(set);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(arr));
  }

  // Actualiza el icono de un botón según esté en favoritos o no
  function updateButtonVisual(button, isFav) {
    const icon = button.querySelector('ion-icon');
    if (!icon) return;
    // Cambiar entre corazón relleno y outline
    icon.setAttribute('name', isFav ? 'heart' : 'heart-outline');
    // Opcional: cambiar clase para aplicar estilos extra
    button.classList.toggle('favorito', isFav);
  }

  // Inicializar: asociar eventos y pintar estado inicial
  function initWishlistButtons() {
      

    const wishlist = loadWishlist();
    // Selecciona todos los botones de wishlist
    const buttons = document.querySelectorAll('.wishlist-add');
    buttons.forEach(button => {
      const code = button.id || button.dataset.codigo;
      const isFav = wishlist.has(code);

      // Estado visual inicial
      updateButtonVisual(button, isFav);

      // Click handler para toggle
      button.addEventListener('click', () => {
        const currentlyFav = wishlist.has(code);
        if (currentlyFav) {
          wishlist.delete(code);
        } else {
          wishlist.add(code);
        }
        saveWishlist(wishlist);

        updateButtonVisual(button, !currentlyFav);
      });
    });
  }

  // Esperar a que cargue el DOM
  document.addEventListener('DOMContentLoaded', initWishlistButtons);
})();
