package br.com.merccontrol.service;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class EstoqueServiceTest {
 private final EstoqueService s=new EstoqueService();
 @Test void vendeTresDeDez(){assertEquals(7,s.calcularRestante(10,3));}
 @Test void vendeTudo(){assertEquals(0,s.calcularRestante(10,10));}
 @Test void impedeExcesso(){assertThrows(IllegalArgumentException.class,()->s.calcularRestante(10,11));}
 @Test void impedeZero(){assertThrows(IllegalArgumentException.class,()->s.calcularRestante(10,0));}
 @Test void impedeNegativo(){assertThrows(IllegalArgumentException.class,()->s.calcularRestante(10,-1));}
 @Test void impedeEstoqueNegativo(){assertThrows(IllegalArgumentException.class,()->s.calcularRestante(-1,1));}
}
