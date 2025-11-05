document.addEventListener("DOMContentLoaded", () => {
    const toggles = document.querySelectorAll(".pwd-toggle");

    toggles.forEach(toggle => {
        toggle.addEventListener("click", function (event) {
            event.preventDefault();

            // Encuentra el input anterior al enlace
            const input = this.previousElementSibling;

            if (input.type === "password") {
                input.type = "text";
                this.innerHTML = 'Ocultar contraseña<ion-icon name="eye-off"></ion-icon>';
            } else {
                input.type = "password";
                this.innerHTML = 'Mostrar contraseña<ion-icon name="eye"></ion-icon>';
            }
        });
    });
});

