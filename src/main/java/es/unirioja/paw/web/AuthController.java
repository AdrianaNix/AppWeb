package es.unirioja.paw.web;

import es.unirioja.paw.exception.RepositoryException;
import es.unirioja.paw.jpa.CestaCompraEntity;
import es.unirioja.paw.jpa.ClienteEntity;
import es.unirioja.paw.service.AuthService;
import es.unirioja.paw.service.RegistroClienteUseCase;
import es.unirioja.paw.repository.CestaCompraRepository;
import es.unirioja.paw.service.data.RegistroClienteRequest;
import es.unirioja.paw.service.data.RegistroClienteResponse;
import es.unirioja.paw.web.data.RegistroClienteValidationResponse;
import es.unirioja.paw.web.data.RegistroPostPayload;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final String USER_REGISTERED_KEY = "userRegistered";
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private RegistroClienteUseCase registroClienteUseCase;

    @Autowired
    private CestaCompraRepository cestaRepo;

    @ModelAttribute("loginForm")
    public LoginForm loginForm() {
        return new LoginForm();
    }

    @ModelAttribute("registroForm")
    public RegistroPostPayload registroForm() {
        return new RegistroPostPayload();
    }

    // ---- LOGIN / LOGOUT ----
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @ModelAttribute("loginForm") @Valid LoginForm form,
            BindingResult br,
            HttpSession session,
            Model model) {

        if (br.hasErrors()) {
            return "login";
        }

        ClienteEntity cliente = authService.authenticate(form.getUsername(), form.getPassword());
        if (cliente != null) {
            logger.info("Login correcto para usuario {}", form.getUsername());
            session.setAttribute(SessionConstants.CLIENTE_KEY, cliente);

            CestaCompraEntity cesta = cestaRepo.findOneByCodigoCliente(cliente.getCodigo());
            if (cesta == null) {
                cesta = new CestaCompraEntity();
                cesta.setCodigoCliente(cliente.getCodigo());
                cesta.setFechaInicio(LocalDateTime.now());
                cesta.setLineas(new ArrayList<>());
                cesta = cestaRepo.save(cesta);
            }
            session.setAttribute(SessionConstants.CESTA_KEY, cesta);

            return "redirect:/cliente/cuenta";
        }

        logger.warn("Login fallido para usuario {}", form.getUsername());
        model.addAttribute("loginError", "Usuario o contraseña incorrectos");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        logger.info("Sesión cerrada");
        return "redirect:/auth/login";
    }

    // ---- REGISTER ----
    @GetMapping("/register")
    public String showRegister() {
        return "registro";
    }

    @PostMapping("/register")
    public String doRegister(
            @ModelAttribute("registroForm") RegistroPostPayload payload,
            BindingResult br,
            HttpSession session,
            Model model) {

        // Validación básica de campos
        RegistroClienteValidationResponse validation = validateRegistroPayload(payload);
        if (!validation.isSuccess()) {
            logger.info("Errores de validación en registro: {}", validation.getMessageCollection());
            model.addAttribute("messages", validation.getMessageCollection());
            model.addAttribute("backing", payload);
            return "registro";
        }

        try {
            // Intento de registro en negocio
            RegistroClienteResponse response = registroClienteUseCase.registrar(new RegistroClienteRequest(payload));
            if (response.isSuccess()) {
                logger.info("Registro exitoso para usuario {}", payload.getUsername());
                session.setAttribute(USER_REGISTERED_KEY, response.cliente);
                return "redirect:/auth/welcome";
            }

            logger.error("Registro fallido en negocio para usuario {}", payload.getUsername());
            model.addAttribute("messages", List.of("No se pudo completar el registro. Comprueba los datos e inténtalo de nuevo."));
            model.addAttribute("backing", payload);
            return "registro";

        } catch (RepositoryException e) {
            // Manejo del caso cuando el usuario ya existe
            logger.error("Error al registrar usuario: {}", e.getMessage());
            model.addAttribute("messages", List.of(e.getMessage())); 
            model.addAttribute("backing", payload);
            return "registro";
        }
    }

    @GetMapping("/welcome")
    public String welcome(Model model, HttpSession session) {
        ClienteEntity cliente = (ClienteEntity) session.getAttribute(USER_REGISTERED_KEY);
        logger.info("En welcome(): session.getAttribute('{}') = {}", USER_REGISTERED_KEY, cliente);
        if (cliente != null) {
            model.addAttribute("clienteRegistrado", cliente);
            session.removeAttribute(USER_REGISTERED_KEY);
            logger.info("Cliente añadido a modelo y eliminado de sesión");
        }
        return "welcome";
    }

    // Validación del registro
    private RegistroClienteValidationResponse validateRegistroPayload(RegistroPostPayload payload) {
        RegistroClienteValidationResponse res = new RegistroClienteValidationResponse();
        res.setSuccess(true);

        if (payload.getUsername() == null || payload.getUsername().isBlank()) {
            res.getMessageCollection().add("Campo 'Usuario': debe proporcionar un valor");
            res.setSuccess(false);
        } else if (payload.getUsername().length() > 50) {
            res.getMessageCollection().add("Campo 'Usuario': longitud máxima 50 caracteres");
            res.setSuccess(false);
        }

        if (payload.getEmail() == null || payload.getEmail().isBlank()) {
            res.getMessageCollection().add("Campo 'Email': debe proporcionar un valor");
            res.setSuccess(false);
        } else if (payload.getEmail().length() > 100) {
            res.getMessageCollection().add("Campo 'Email': longitud máxima 100 caracteres");
            res.setSuccess(false);
        }

        if (!payload.getAcceptConditions()) {
            res.getMessageCollection().add("Debes aceptar la política de privacidad para continuar.");
            res.setSuccess(false);
        }

        String p1 = payload.getPassword1();
        String p2 = payload.getPassword2();
        if (p1 == null || p1.isBlank()) {
            res.getMessageCollection().add("Campo 'Contraseña': debe proporcionar un valor");
            res.setSuccess(false);
        }
        if (p2 == null || p2.isBlank()) {
            res.getMessageCollection().add("Campo 'Repita contraseña': debe proporcionar un valor");
            res.setSuccess(false);
        }
        if (p1 != null && p2 != null && !p1.equals(p2)) {
            res.getMessageCollection().add("Las contraseñas no coinciden");
            res.setSuccess(false);
        }

        return res;
    }
}
