
window.addEventListener("load", init)
// o window.addEventListener("DOMContentLoaded",init)
function init() {
    console.log("se inicia la función")
    let languageSelector = document.getElementById('language-selector');
    languageSelector.addEventListener('change', function (e) {
        this.form.submit();
    });
}

