package es.unirioja.paw.web;

import es.unirioja.paw.jpa.AlmacenEntity;
import es.unirioja.paw.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/info")
public class DondeEstamosController {

    @Autowired
    private AlmacenRepository almacenRepository;

    @GetMapping("/donde-estamos")
    public String showMapaAlmacenes(Model model) {
        List<AlmacenEntity> almacenes = almacenRepository.findAll();

       
        model.addAttribute("almacenes", almacenes);
       
        return "almacenes-map";

    }
}
