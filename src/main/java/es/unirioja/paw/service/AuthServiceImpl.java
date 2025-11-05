package es.unirioja.paw.service;

import es.unirioja.paw.jpa.ClienteEntity;
import es.unirioja.paw.jpa.UsuarioEntity;
import es.unirioja.paw.repository.ClienteRepository;
import es.unirioja.paw.repository.UsuarioRepository;
import java.security.MessageDigest;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private UsuarioRepository usuarioRepository;

    private ClienteRepository clienteRepository;

    private SimpleEncoder simpleEncoder;

    @Autowired
    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            SimpleEncoder simpleEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.simpleEncoder = simpleEncoder;
    }

    @Override
    public ClienteEntity authenticate(String username, String password) {
        UsuarioEntity usuarioExample = new UsuarioEntity();
        usuarioExample.setUsername(username);
        String encodedPassword = simpleEncoder.sha1(password);
        usuarioExample.setPassword(encodedPassword);
        ClienteEntity clienteExample = new ClienteEntity();
        clienteExample.setUsername(username);

        Optional<UsuarioEntity> usuario = usuarioRepository.findOne(Example.of(usuarioExample));
        Optional<ClienteEntity> cliente = clienteRepository.findOne(Example.of(clienteExample));
        if (usuario.isPresent() && cliente.isPresent()) {
            logger.info("Auth success");
            return cliente.get();
        }

        logger.warn("Auth failed: username={}", username);
        return null;
    }

}
