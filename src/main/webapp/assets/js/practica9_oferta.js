// src/main/webapp/static/assets/js/practica9_oferta.js
document.addEventListener('DOMContentLoaded', () => {
  const enlace        = document.getElementById('descubrir-oferta');
  const priceWrapper  = document.getElementById('price-box-wrapper');
  const ofertaFlash   = document.getElementById('oferta-flash');
  const tplOferta     = Handlebars.compile(document.getElementById('ofertaFlashTemplate').innerHTML);
  const tplCuenta     = Handlebars.compile(document.getElementById('countdownTemplate').innerHTML);
  let intervalo       = null;

  enlace.addEventListener('click', e => {
    e.preventDefault();
    if (intervalo) return;

    const codigo = enlace.dataset.id;
    intervalo = setInterval(() => {
      fetch(`/electrosa/api/articulo/${codigo}/oferta`, {
        headers: { 'Accept': 'application/json' }
      })
      .then(response => {
        if (response.status === 204 || response.status === 404 ) {
          throw new Error('Sin oferta');
        }
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        return response.text();               // leer como texto
      })
      .then(text => {
        if (!text) throw new Error('Sin oferta');  // cuerpo vacío = sin oferta
        return JSON.parse(text);                   // parser JSON sólo si había texto
      })
      .then(data => {
        const context = { oferta: data };

        // 1) Precio y tachado
        priceWrapper.innerHTML = tplOferta(context);

        // 2) Contador
        ofertaFlash.innerHTML = tplCuenta(context);

        // 3) Detener si expiró
        const v = data;
        if ([v.vigenciaDias, v.vigenciaHoras, v.vigenciaMinutos, v.vigenciaSegundos].every(x => x === 0)) {
          clearInterval(intervalo);
        }
      })
      .catch(err => {
        console.warn('Error al obtener oferta:', err.message);
        clearInterval(intervalo);

        ofertaFlash.innerHTML = `
          <div class="no-offer">
            <p>No hay oferta disponible para este artículo.</p>
          </div>`;
      });
    }, 1000);
  });
});
