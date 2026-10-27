package br.com.merccontrol.repo;
import br.com.merccontrol.model.Cliente;import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<Cliente,Long>{boolean existsByCpf(String cpf);}
