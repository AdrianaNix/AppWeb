window.addEventListener("load", function() {
    // Calcular la URL completa para la API de banner
    let url = window.location.origin + "/electrosa/api/banner/header";
    
    // Realizar la solicitud AJAX usando Fetch API
    fetch(url)
        .then(response => response.text())  // Obtener el texto de la respuesta
        .then(response => renderBanner(response))  // Actualizar el contenido del banner
        .catch(err => console.error("Error al cargar el banner:", err));
});

// Función para actualizar el contenido del banner
function renderBanner(bannerMessage) {
    // Localizar el elemento con id 'news-banner' y actualizar su contenido
    const bannerElement = document.getElementById("news-banner");
    if (bannerElement) {
        bannerElement.innerHTML = bannerMessage;
    }
}



