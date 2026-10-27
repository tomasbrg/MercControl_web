const el=id=>document.getElementById(id);
const val=id=>el(id)?.value.trim()||'';
const moeda=n=>Number(n).toLocaleString('pt-BR',{style:'currency',currency:'BRL'});
const msg=(texto,erro=false)=>{if(el('aviso')){el('aviso').textContent=texto;el('aviso').style.color=erro?'#b32626':'#14744d'}};
const estado={produtos:[],clientes:[],vendas:[]};
async function api(rota,metodo='GET',dados){const res=await fetch('/api/'+rota,{method:metodo,headers:{'Content-Type':'application/json'},body:dados===undefined?undefined:JSON.stringify(dados)});if(!res.ok){let erro={};try{erro=await res.json()}catch{}throw Error(erro.erro||'Falha na comunicação com o servidor ('+res.status+').')}if(res.status===204)return null;const texto=await res.text();return texto?JSON.parse(texto):null}
async function carregar(){try{const [produtos,clientes,vendas]=await Promise.all([api('produtos'),api('clientes'),api('vendas')]);Object.assign(estado,{produtos,clientes,vendas});mostrar()}catch(e){msg('Não foi possível conectar ao servidor: '+e.message,true)}}
function cel(tr,valor){const td=document.createElement('td');td.textContent=valor;tr.appendChild(td)}
function tabela(id,dados,colunas){const corpo=el(id);if(!corpo)return;corpo.replaceChildren();if(!dados.length){const tr=document.createElement('tr'),td=document.createElement('td');td.colSpan=colunas.length;td.className='vazio';td.textContent='Nenhum registro cadastrado.';tr.appendChild(td);corpo.appendChild(tr);return}dados.forEach(item=>{const tr=document.createElement('tr');tr.dataset.id=item.id;tr.style.cursor='pointer';colunas.forEach(fn=>cel(tr,fn(item)));corpo.appendChild(tr)})}
function preencher(id,dados,label){const sel=el(id);if(!sel)return;const anterior=sel.value;sel.replaceChildren();const o=document.createElement('option');o.value='';o.textContent=label;sel.appendChild(o);dados.forEach(item=>{const opt=document.createElement('option');opt.value=item.id;opt.textContent=item.nome+(id==='vProduto'?` (estoque: ${item.qtd})`:'');sel.appendChild(opt)});if([...sel.options].some(x=>x.value===anterior))sel.value=anterior}
function mostrar(){tabela('tabProdutos',estado.produtos,[x=>x.id,x=>x.nome,x=>moeda(x.preco),x=>x.qtd]);tabela('tabClientes',estado.clientes,[x=>x.id,x=>x.nome,x=>x.cpf,x=>x.tel||'']);tabela('tabEstoque',estado.produtos,[x=>x.id,x=>x.nome,x=>moeda(x.preco),x=>x.qtd]);preencher('vCliente',estado.clientes,'Consumidor não identificado');preencher('vProduto',estado.produtos,'Selecione um produto');relatorio()}
function limpar(prefixo){['Codigo','Nome','Preco','Qtd','Cpf','Tel'].forEach(s=>{if(el(prefixo+s))el(prefixo+s).value=''})}
async function acao(fn){try{await fn();await carregar()}catch(e){msg(e.message,true)}}
function dadosProduto(){const nome=val('pNome'),preco=Number(val('pPreco')),qtd=Number(val('pQtd'));if(!nome||!val('pPreco')||!Number.isFinite(preco)||preco<=0||!val('pQtd')||!Number.isInteger(qtd)||qtd<0)throw Error('Informe nome, preço positivo e quantidade inteira não negativa.');return {nome,preco,qtd}}
function cpfValido(c){c=c.replace(/\D/g,'');if(!/^\d{11}$/.test(c)||/^(\d)\1{10}$/.test(c))return false;for(let n=9;n<11;n++){let soma=0;for(let i=0;i<n;i++)soma+=Number(c[i])*(n+1-i);let d=(soma*10)%11;if(d===10)d=0;if(d!==Number(c[n]))return false}return true}
function dadosCliente(){const nome=val('cNome'),cpf=val('cCpf').replace(/\D/g,''),tel=val('cTel');if(!nome||!cpfValido(cpf))throw Error('Informe nome e CPF válido.');return {nome,cpf,tel}}
if(el('pSalvar')){
 el('pNovo').onclick=()=>{limpar('p');msg('Novo cadastro.')};
 el('pSalvar').onclick=()=>acao(async()=>{await api('produtos','POST',dadosProduto());limpar('p');msg('Produto salvo!')});
 el('pAlterar').onclick=()=>acao(async()=>{const id=val('pCodigo');if(!id)throw Error('Selecione um produto.');await api('produtos/'+id,'PUT',dadosProduto());limpar('p');msg('Produto alterado!')});
 el('pExcluir').onclick=()=>acao(async()=>{const id=val('pCodigo');if(!id)throw Error('Selecione um produto.');if(!confirm('Excluir este produto?'))return;await api('produtos/'+id,'DELETE');limpar('p');msg('Produto excluído!')});
 el('tabProdutos').onclick=e=>{const tr=e.target.closest('tr[data-id]');if(!tr)return;const x=estado.produtos.find(x=>String(x.id)===tr.dataset.id);if(x){el('pCodigo').value=x.id;el('pNome').value=x.nome;el('pPreco').value=x.preco;el('pQtd').value=x.qtd}}
}
if(el('cSalvar')){
 el('cNovo').onclick=()=>{limpar('c');msg('Novo cadastro.')};
 el('cSalvar').onclick=()=>acao(async()=>{await api('clientes','POST',dadosCliente());limpar('c');msg('Cliente salvo!')});
 el('cAlterar').onclick=()=>acao(async()=>{const id=val('cCodigo');if(!id)throw Error('Selecione um cliente.');await api('clientes/'+id,'PUT',dadosCliente());limpar('c');msg('Cliente alterado!')});
 el('cExcluir').onclick=()=>acao(async()=>{const id=val('cCodigo');if(!id)throw Error('Selecione um cliente.');if(!confirm('Excluir este cliente?'))return;await api('clientes/'+id,'DELETE');limpar('c');msg('Cliente excluído!')});
 el('tabClientes').onclick=e=>{const tr=e.target.closest('tr[data-id]');if(!tr)return;const x=estado.clientes.find(x=>String(x.id)===tr.dataset.id);if(x){el('cCodigo').value=x.id;el('cNome').value=x.nome;el('cCpf').value=x.cpf;el('cTel').value=x.tel||''}}
}
let carrinho=[];
function total(){return carrinho.reduce((s,x)=>s+x.preco*x.qtd,0)}
function troco(){if(!el('vTroco'))return;const dinheiro=val('vPagamento')==='Dinheiro';el('vRecebido').disabled=!dinheiro;el('vTroco').textContent=moeda(dinheiro?Math.max(0,(Number(val('vRecebido'))||0)-total()):0)}
function desenharCarrinho(){const corpo=el('tabCarrinho');if(!corpo)return;corpo.replaceChildren();if(!carrinho.length){const tr=document.createElement('tr'),td=document.createElement('td');td.colSpan=5;td.className='vazio';td.textContent='Nenhum item adicionado.';tr.appendChild(td);corpo.appendChild(tr)}carrinho.forEach((x,i)=>{const tr=document.createElement('tr');cel(tr,x.nome);cel(tr,moeda(x.preco));cel(tr,x.qtd);cel(tr,moeda(x.preco*x.qtd));const td=document.createElement('td'),b=document.createElement('button');b.textContent='Remover';b.onclick=()=>{carrinho.splice(i,1);desenharCarrinho()};td.appendChild(b);tr.appendChild(td);corpo.appendChild(tr)});el('totalCarrinho').textContent=moeda(total());troco()}
if(el('vAdicionar')){
 el('vAdicionar').onclick=()=>{const p=estado.produtos.find(x=>String(x.id)===val('vProduto')),qtd=Number(val('vQtd'));if(!p||!Number.isInteger(qtd)||qtd<=0){msg('Selecione produto e quantidade válida.',true);return}const item=carrinho.find(x=>x.id===p.id),atual=item?.qtd||0;if(atual+qtd>p.qtd){msg('Estoque insuficiente.',true);return}if(item)item.qtd+=qtd;else carrinho.push({id:p.id,nome:p.nome,preco:Number(p.preco),qtd});el('vProduto').value='';el('vQtd').value='1';desenharCarrinho();msg('Produto adicionado.')};
 el('vLimpar').onclick=()=>{carrinho=[];desenharCarrinho();msg('Compra cancelada.')};
 el('vPagamento').onchange=troco;el('vRecebido').oninput=troco;
 el('vFinalizar').onclick=()=>acao(async()=>{if(!carrinho.length)throw Error('Adicione produtos à compra.');const pagamento=val('vPagamento'),recebido=val('vRecebido');const pedido={clienteId:val('vCliente')?Number(val('vCliente')):null,pagamento,itens:carrinho.map(x=>({id:x.id,qtd:x.qtd})),recebido:pagamento==='Dinheiro'&&recebido!==''?Number(recebido):null};await api('vendas','POST',pedido);carrinho=[];desenharCarrinho();el('vRecebido').value='';msg('Compra finalizada! Veja o relatório.')});
 desenharCarrinho()
}
function relatorio(){if(!el('tabRelatorio'))return;const vendas=estado.vendas;el('rQuantidade').textContent=vendas.length;el('rTotal').textContent=moeda(vendas.reduce((s,v)=>s+Number(v.total),0));tabela('tabRelatorio',vendas,[x=>x.id,x=>x.data,x=>x.cliente,x=>x.itens.map(i=>i.qtd+'x '+i.nome).join(', '),x=>x.pagamento,x=>moeda(x.total)])}
carregar();
