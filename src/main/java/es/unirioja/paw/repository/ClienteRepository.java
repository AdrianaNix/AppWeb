package es.unirioja.paw.repository;

import es.unirioja.paw.jpa.ClienteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, String> {

    public void save(Optional<ClienteEntity> entity);
    
}
