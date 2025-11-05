document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.item').forEach(itemEl => {
    const select = itemEl.querySelector('.line-qty');
    const subEl  = itemEl.querySelector('.line-subtotal');
    const url    = itemEl.dataset.updateUrl;
    const lineaId = itemEl.dataset.lineId;

    select.addEventListener('change', () => {
      const nuevaCantidad = parseInt(select.value, 10);

      fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept':       'application/json'
        },
        body: JSON.stringify({ lineaId, cantidad: nuevaCantidad })
      })
      .then(r => {
        if (!r.ok) throw new Error(`HTTP ${r.status}`);
        return r.json();
      })
      .then(data => {
        // 1) Si subtotal === 0, borramos la línea del DOM
        if (data.subtotal === 0) {
          itemEl.remove();
        } else {
          // 2) Actualizamos el subtotal de la línea
          subEl.textContent = data.subtotal.toFixed(2) + ' €';
        }

        // 3) Actualizamos el total de la cesta
        const totalEl = document.getElementById('total-amount');
        if (totalEl && data.total != null) {
          totalEl.textContent = data.total.toFixed(2) + ' €';
        }

        // 4) Si ya no quedan líneas, opcionalmente ocultar total y botón
        if (!document.querySelector('.item')) {
          document.getElementById('total-amount').closest('.total-line').remove();
          document.querySelector('.cart-actions a.banner-btn[th\\:href]').remove();
        }
      })
      .catch(err => {
        console.error('No se pudo actualizar la cantidad:', err);
      });
    });
  });
});
