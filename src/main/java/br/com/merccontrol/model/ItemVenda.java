package br.com.merccontrol.model;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="itens_venda") public class ItemVenda {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="venda_id",nullable=false) public Venda venda;
 @Column(nullable=false) public Long produtoId;
 @Column(nullable=false) public String nome;
 @Column(nullable=false) public Integer qtd;
 @Column(nullable=false,precision=12,scale=2) public BigDecimal preco;
}
