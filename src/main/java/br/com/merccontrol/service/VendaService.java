package br.com.merccontrol.service;
import br.com.merccontrol.model.*;import br.com.merccontrol.repo.*;
import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;import java.time.LocalDateTime;import java.util.*;
@Service public class VendaService {
 private final ProdutoRepository produtos;private final ClienteRepository clientes;private final VendaRepository vendas;
 private final EstoqueService estoque=new EstoqueService();
 public VendaService(ProdutoRepository produtos,ClienteRepository clientes,VendaRepository vendas){this.produtos=produtos;this.clientes=clientes;this.vendas=vendas;}
 public record Linha(Long id,Integer qtd){}
 public record Pedido(Long clienteId,String pagamento,List<Linha> itens,BigDecimal recebido){}
 @Transactional public Venda registrar(Pedido pedido){
  if(pedido==null||pedido.itens()==null||pedido.itens().isEmpty())throw new IllegalArgumentException("Adicione itens à compra.");
  if(!List.of("Dinheiro","Pix","Cartão").contains(pedido.pagamento()))throw new IllegalArgumentException("Pagamento inválido.");
  Map<Long,Integer> quantidades=new TreeMap<>();
  for(Linha l:pedido.itens()){
   if(l==null||l.id()==null||l.qtd()==null||l.qtd()<=0)throw new IllegalArgumentException("Item inválido.");
   quantidades.merge(l.id(),l.qtd(),Math::addExact);
  }
  Venda venda=new Venda();venda.data=LocalDateTime.now();venda.pagamento=pedido.pagamento();venda.total=BigDecimal.ZERO;
  if(pedido.clienteId()!=null){Cliente c=clientes.findById(pedido.clienteId()).orElseThrow(()->new IllegalArgumentException("Cliente não encontrado."));venda.cliente=c.nome;}
  else venda.cliente="Consumidor";
  for(var entrada:quantidades.entrySet()){
   Produto p=produtos.bloquear(entrada.getKey()).orElseThrow(()->new IllegalArgumentException("Produto não encontrado."));
   int qtd=entrada.getValue();p.qtd=estoque.calcularRestante(p.qtd,qtd);
   ItemVenda item=new ItemVenda();item.venda=venda;item.produtoId=p.id;item.nome=p.nome;item.preco=p.preco;item.qtd=qtd;venda.itens.add(item);
   venda.total=venda.total.add(p.preco.multiply(BigDecimal.valueOf(qtd)));
  }
  if("Dinheiro".equals(pedido.pagamento())&&(pedido.recebido()==null||pedido.recebido().compareTo(venda.total)<0))throw new IllegalArgumentException("Valor recebido insuficiente.");
  return vendas.save(venda);
 }
}
