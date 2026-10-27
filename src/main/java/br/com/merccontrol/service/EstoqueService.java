package br.com.merccontrol.service;
public class EstoqueService {
 public int calcularRestante(int atual,int vendido){
  if(atual<0||vendido<=0||vendido>atual)throw new IllegalArgumentException("Quantidade inválida ou estoque insuficiente.");
  return atual-vendido;
 }
}
