package br.com.merccontrol.repo;
import br.com.merccontrol.model.Produto;import org.springframework.data.jpa.repository.*;import org.springframework.data.repository.query.Param;import jakarta.persistence.LockModeType;import java.util.Optional;
public interface ProdutoRepository extends JpaRepository<Produto,Long>{
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select p from Produto p where p.id=:id") Optional<Produto> bloquear(@Param("id") Long id);
}
