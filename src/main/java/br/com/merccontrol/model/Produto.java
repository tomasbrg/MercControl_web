package br.com.merccontrol.model;
import jakarta.persistence.*;import jakarta.validation.constraints.*;import java.math.BigDecimal;
@Entity @Table(name="produtos") public class Produto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=80) @Column(nullable=false) public String nome;
 @NotNull @DecimalMin("0.01") @Column(nullable=false,precision=12,scale=2) public BigDecimal preco;
 @NotNull @Min(0) @Column(nullable=false) public Integer qtd;
}
