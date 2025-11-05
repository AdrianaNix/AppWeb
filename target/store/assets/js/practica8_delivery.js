/**
 * practica8_delivery.js
 *
 * Este script:
 * 1. Agrupa los pedidos por región y mes.
 * 2. Calcula el tiempo medio de entrega (transitTimeAsDays).
 * 3. Genera la configuración necesaria para el gráfico de TOAST UI.
 */

(function() {
  // -- 1. Definimos meses para etiquetas --
  const MONTH_LABELS = [
    'Ene','Feb','Mar','Abr','May','Jun',
    'Jul','Ago','Sep','Oct','Nov','Dic'
  ];

  // -- 2. Inicializamos estructura para acumular datos --
  const regions = ['PB','IC','IN'];
  // { 'PB': [ [],[],… 12 arrays … ], 'IC': […], 'IN': […] }
  const buckets = regions.reduce((acc, r) => {
    acc[r] = Array.from({length: 12}, () => []);
    return acc;
  }, {});

  // -- 3. Repartimos cada stat en su cubeta de mes y región --
  pedidoStats.forEach(stat => {
    const monthIdx = stat.shippingMonth - 1;    // de 0 a 11
    const region   = stat.shippingRegion;       // 'PB','IC','IN'
    if (buckets[region] && stat.transitTimeAsDays >= 0) {
      buckets[region][monthIdx].push(stat.transitTimeAsDays);
    }
  });

  // -- 4. Calculamos la media de cada mes y región --
  function mean(arr) {
    if (arr.length === 0) return 0;
    return arr.reduce((sum, v) => sum + v, 0) / arr.length;
  }

  const series = regions.map(region => {
    const data = buckets[region].map(monthArray => Math.round(mean(monthArray)*100)/100);
    return { name: region, data };
  });

  // -- 5. Configuramos y renderizamos el gráfico de líneas --
  toastui.Chart.lineChart({
    el: document.getElementById('chart'),
    data: {
      categories: MONTH_LABELS,
      series: series
    },
    options: {
      chart: { title: 'Tiempo medio de entrega (días)' },
      yAxis: { title: 'Días', min: 0 },
      xAxis: { title: 'Mes' },
      series: {
        showDot: true,     // marcar cada punto
        spline: true       // líneas suaves
      },
      tooltip: { suffix: ' días' }
    }
  });

})();
