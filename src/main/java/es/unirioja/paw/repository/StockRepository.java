package es.unirioja.paw.repository;

import es.unirioja.paw.jpa.StockEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<StockEntity, String> {

    @Query("select SUM(cantidad) from StockEntity s where s.codigoArticulo = ?#{[0]}")
    public Integer findStockByArticulo(@Param("codigoArticulo") String codigoArticulo);

    public List<StockEntity> findByCodigoArticulo(String codigoArticulo);

}
