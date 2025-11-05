/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.unirioja.paw.web;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IdiomaController {
     @GetMapping("/lang/prefs")
    public String showLangPrefsForm(Model model, Locale locale) {
        // Inyectamos el idioma actual para poder seleccionarlo en el <select>
        model.addAttribute("currentLang", locale.getLanguage());
        return "langPrefs";
    }

    @PostMapping("/lang/prefs")
    public String changeLang(@RequestParam("lang") String lang) {
        // El LocaleChangeInterceptor interceptará el parámetro 'lang'
        // y cambiará el Locale en la sesión. Aquí redirigimos al catálogo.
        return "redirect:/catalogo?lang=" + lang;
    }
}
