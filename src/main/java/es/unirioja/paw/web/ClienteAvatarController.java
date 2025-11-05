package es.unirioja.paw.web;

import es.unirioja.paw.jpa.ClienteEntity;
import es.unirioja.paw.service.AvatarClienteChangeUseCase;
import es.unirioja.paw.service.data.AvatarClienteChangeRequest;
import es.unirioja.paw.service.data.AvatarClienteChangeResponse;
import es.unirioja.paw.web.data.ClienteAvatarInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/cliente")
public class ClienteAvatarController {

    @Autowired
    private AvatarClienteChangeUseCase avatarUseCase;

    /** GET muestra el formulario */
    @GetMapping("/avatar")
    public String showAvatarForm(HttpSession session, Model model) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }
        // Pasar a la vista el avatar actual (si ya existe)
        ClienteAvatarInfo info = (ClienteAvatarInfo) session.getAttribute("clienteAvatar");
        if (info != null) {
            model.addAttribute("avatarInfo", info);
        }
        return "avatar";
    }

    /** POST procesa la subida del archivo */
    @PostMapping("/avatar")
    public String uploadAvatarImage(
            HttpSession session,
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file) {

        ClienteEntity cliente = (ClienteEntity) session.getAttribute("cliente");
        if (cliente == null) {
            return "redirect:/login";
        }

        String codigo = cliente.getCodigo();
        // Carpeta donde servimos /static/assets/avatar/**
        String realPath = request.getServletContext().getRealPath("/assets/avatar");

        Optional<AvatarClienteChangeResponse> resp = avatarUseCase.execute(
            new AvatarClienteChangeRequest(codigo, file, realPath)
        );

        if (resp.isPresent()) {
            // Actualizamos sesión con la nueva info
            session.setAttribute("clienteAvatar",
                new ClienteAvatarInfo(resp.get().cliente));
        }

        return "redirect:/cliente/cuenta";
    }
}
