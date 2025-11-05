var map;
let miUbicacion = null;

function crearMapa() {
    var cnf = {
        center: [40.4169473, -3.7035285],
        zoom: 6
    };
    map = L.map('map', cnf);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);
}

function centrarMapaPorZona() {
    const zonaCentro = almacenes.find(a => a.zona === 1); // ¿Este valor está bien?
    if (zonaCentro) {
        map.setView([zonaCentro.coordX, zonaCentro.coordY], 6);
    }
}

function añadirMarcadores() {
    almacenes.forEach(a => {
        const tooltip = `${a.calle}. ${a.cp} - ${a.ciudad}. ${a.provincia}`;
        L.marker([a.coordX, a.coordY])
            .addTo(map)
            .bindPopup(tooltip);
    });
}

function activarGeolocalizacion() {
    document.getElementById('btn-geoloc').addEventListener('click', function () {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;
                alert('Estás en ' + lat + ', ' + lon);

                if (miUbicacion) {
                    map.removeLayer(miUbicacion);
                }

                miUbicacion = L.circle([lat, lon], {
                    color: '#5C7E74',
                    fillColor: '#5C7E74',
                    fillOpacity: 0.5,
                    radius: 20000
                }).addTo(map).bindPopup("Mi ubicación actual");

                map.setView([lat, lon], 6);
            });
        } else {
            alert("Su navegador no soporta la API de geolocalización");
        }
    });
}

function inicializa() {
    crearMapa();
    centrarMapaPorZona();
    añadirMarcadores();
    activarGeolocalizacion();
}

window.addEventListener("load", inicializa);
