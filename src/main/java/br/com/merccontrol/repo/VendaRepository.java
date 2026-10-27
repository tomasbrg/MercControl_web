package br.com.merccontrol.repo;
import br.com.merccontrol.model.Venda;import org.springframework.data.jpa.repository.JpaRepository;import java.util.List;
public interface VendaRepository extends JpaRepository<Venda,Long>{List<Venda> findAllByOrderByIdDesc();}
