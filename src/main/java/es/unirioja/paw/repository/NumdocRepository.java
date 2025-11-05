package es.unirioja.paw.repository;

import es.unirioja.paw.jpa.NumdocEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumdocRepository extends JpaRepository<NumdocEntity, String> {

    public NumdocEntity findOneByAnio(int v);

}
