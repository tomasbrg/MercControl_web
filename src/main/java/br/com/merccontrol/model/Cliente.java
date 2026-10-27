package br.com.merccontrol.model;
import jakarta.persistence.*;import jakarta.validation.constraints.*;
@Entity @Table(name="clientes") public class Cliente {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @NotBlank @Size(max=80) @Column(nullable=false) public String nome;
 @NotBlank @Column(nullable=false,unique=true,length=11) public String cpf;
 @Size(max=30) public String tel;
}
