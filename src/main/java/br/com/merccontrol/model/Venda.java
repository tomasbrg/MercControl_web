package br.com.merccontrol.model;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDateTime;import java.util.*;
@Entity @Table(name="vendas") public class Venda {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public LocalDateTime data;
 public String cliente;
 @Column(nullable=false) public String pagamento;
 @Column(nullable=false,precision=14,scale=2) public BigDecimal total;
 @OneToMany(mappedBy="venda",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER) public List<ItemVenda> itens=new ArrayList<>();
}
