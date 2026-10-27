package br.com.merccontrol.web;
import br.com.merccontrol.model.*;import br.com.merccontrol.repo.*;import br.com.merccontrol.service.*;
import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;import java.util.*;
@RestController @RequestMapping("/api") public class ApiController {
 private final ProdutoRepository produtos;private final ClienteRepository clientes;private final VendaRepository vendas;private final VendaService vendaService;
 public ApiController(ProdutoRepository p,ClienteRepository c,VendaRepository v,VendaService s){produtos=p;clientes=c;vendas=v;vendaService=s;}
 @GetMapping("/produtos") public List<Produto> produtos(){return produtos.findAll();}
 @PostMapping("/produtos") public Produto criarProduto(@Valid @RequestBody Produto p){p.id=null;return produtos.save(p);}
 @PutMapping("/produtos/{id}") public Produto alterarProduto(@PathVariable Long id,@Valid @RequestBody Produto p){Produto antigo=produtos.findById(id).orElseThrow(()->new NoSuchElementException("Produto não encontrado."));antigo.nome=p.nome;antigo.preco=p.preco;antigo.qtd=p.qtd;return produtos.save(antigo);}
 @DeleteMapping("/produtos/{id}") public void excluirProduto(@PathVariable Long id){if(!produtos.existsById(id))throw new NoSuchElementException("Produto não encontrado.");produtos.deleteById(id);}
 @GetMapping("/clientes") public List<Cliente> clientes(){return clientes.findAll();}
 @PostMapping("/clientes") public Cliente criarCliente(@Valid @RequestBody Cliente c){c.id=null;c.cpf=validarCpf(c.cpf);if(clientes.existsByCpf(c.cpf))throw new IllegalArgumentException("CPF já cadastrado.");return clientes.save(c);}
 @PutMapping("/clientes/{id}") public Cliente alterarCliente(@PathVariable Long id,@Valid @RequestBody Cliente c){Cliente antigo=clientes.findById(id).orElseThrow(()->new NoSuchElementException("Cliente não encontrado."));String cpf=validarCpf(c.cpf);if(!cpf.equals(antigo.cpf)&&clientes.existsByCpf(cpf))throw new IllegalArgumentException("CPF já cadastrado.");antigo.nome=c.nome;antigo.cpf=cpf;antigo.tel=c.tel;return clientes.save(antigo);}
 @DeleteMapping("/clientes/{id}") public void excluirCliente(@PathVariable Long id){if(!clientes.existsById(id))throw new NoSuchElementException("Cliente não encontrado.");clientes.deleteById(id);}
 @GetMapping("/vendas") public List<Map<String,Object>> vendas(){return vendas.findAllByOrderByIdDesc().stream().map(this::resumo).toList();}
 @PostMapping("/vendas") public Map<String,Object> vender(@RequestBody VendaService.Pedido pedido){return resumo(vendaService.registrar(pedido));}
 private Map<String,Object> resumo(Venda v){Map<String,Object> m=new LinkedHashMap<>();m.put("id",v.id);m.put("data",v.data.toString().replace('T',' '));m.put("cliente",v.cliente);m.put("pagamento",v.pagamento);m.put("total",v.total);m.put("itens",v.itens.stream().map(i->Map.of("id",i.produtoId,"nome",i.nome,"qtd",i.qtd,"preco",i.preco)).toList());return m;}
 private String validarCpf(String raw){if(raw==null)throw new IllegalArgumentException("CPF obrigatório.");String c=raw.replaceAll("\\D","");if(c.length()!=11||c.chars().distinct().count()==1)throw new IllegalArgumentException("CPF inválido.");for(int n=9;n<11;n++){int soma=0;for(int i=0;i<n;i++)soma+=(c.charAt(i)-'0')*(n+1-i);int d=soma*10%11;if(d==10)d=0;if(d!=c.charAt(n)-'0')throw new IllegalArgumentException("CPF inválido.");}return c;}
}
