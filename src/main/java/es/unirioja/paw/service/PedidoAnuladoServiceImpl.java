package es.unirioja.paw.service;

import es.unirioja.paw.jpa.PedidoanuladoEntity;
import es.unirioja.paw.repository.PedidoAnuladoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PedidoAnuladoServiceImpl implements PedidoAnuladoService {

    private final PedidoAnuladoRepository repo;

    @Autowired
    public PedidoAnuladoServiceImpl(PedidoAnuladoRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<PedidoanuladoEntity> findByCliente(String codigoCliente) {
        return repo.findByCodigoCliente(codigoCliente);
    }

    @Override
    public PedidoanuladoEntity findOne(String codigoPedido) {
        Optional<PedidoanuladoEntity> opt = repo.findById(codigoPedido);
        return opt.orElse(null);
    }
}
