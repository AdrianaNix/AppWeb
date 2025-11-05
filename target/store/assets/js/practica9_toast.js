// src/main/webapp/static/assets/js/practica9_toast.js
document.addEventListener('DOMContentLoaded', () => {
  const toast      = document.getElementById('notification-toast');
  const titleEl    = document.getElementById('toast-title');
  const closeBtn   = toast.querySelector('[data-toast-close]');
  const url        = toast.dataset.url;
  let intervalId   = null;

  // Cerrar manualmente la toast
  closeBtn.addEventListener('click', () => {
    toast.classList.add('closed');
    clearInterval(intervalId);
  });

  function fetchUltimaCompra() {
    fetch(url, {
      headers: { 'Accept': 'application/json' }
    })
    .then(resp => {
      if (!resp.ok) {
        // 204 o 404: no hay datos → abortamos silencioso
        if (resp.status === 204 || resp.status === 404) return null;
        throw new Error(`HTTP ${resp.status}`);
      }
      return resp.json();
    })
    .then(data => {
      if (!data || !data.nombre) return;  // nada que mostrar
      // Actualizamos el título con el nombre del artículo
      titleEl.textContent = data.nombre;
      // Arrancamos la animación CSS
      toast.style.animationPlayState = 'running';
    })
    .catch(err => {
      console.error('Error al obtener última compra:', err);
    });
  }

  // Primera llamada inmediata
  fetchUltimaCompra();
  // Repetir cada 10 segundos
  intervalId = setInterval(fetchUltimaCompra, 10_000);
});
