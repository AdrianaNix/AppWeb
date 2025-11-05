package es.unirioja.paw.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "ClienteAuthnFilter", urlPatterns = {"/cliente/*", "/clientes/*"})
public class ClienteAuthnFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServletContext context;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);

        // Revisamos si hay cliente autenticado
        Object cliente = (session != null) ? session.getAttribute("cliente") : null;

        if (cliente != null) {
            // Cliente autenticado → continuar la petición
            chain.doFilter(req, resp);
        } else {
            // No autenticado → guardar URL y redirigir
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String destino = (query != null) ? uri + "?" + query : uri;

            logger.info("Acceso no autorizado a {}. Redirigiendo al login.", destino);

            // Asegurar sesión activa
            session = request.getSession(true);
            session.setAttribute("url-retorno", destino);

            response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

    @Override
    public void destroy() {
        // No se necesita limpiar nada
    }
}
