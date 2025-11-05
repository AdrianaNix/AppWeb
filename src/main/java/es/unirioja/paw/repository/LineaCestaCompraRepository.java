package es.unirioja.paw.repository;

import es.unirioja.paw.jpa.LineaCestaCompraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaCestaCompraRepository
        extends JpaRepository<LineaCestaCompraEntity, String> {
}
