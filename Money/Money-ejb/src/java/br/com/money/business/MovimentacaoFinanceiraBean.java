/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.money.business;

import br.com.money.business.interfaces.ContaBancariaBeanLocal;
import br.com.money.business.interfaces.MovimentacaoFinanceiraBeanLocal;
import br.com.money.business.interfaces.ReceitaDividaBeanLocal;
import br.com.money.enums.StatusPagamento;
import br.com.money.enums.TipoConta;
import br.com.money.exceptions.MovimentacaoFinanceiraException;
import br.com.money.exceptions.ValidacaoException;
import br.com.money.modelos.ContaBancaria;
import br.com.money.modelos.MovimentacaoFinanceira;
import br.com.money.modelos.ReceitaDivida;
import br.com.money.modelos.Usuario;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Guilherme
 */
@Stateless
public class MovimentacaoFinanceiraBean implements MovimentacaoFinanceiraBeanLocal {

    @EJB
    private ReceitaDividaBeanLocal receitaDividaBean;
    @EJB
    private ContaBancariaBeanLocal contaBancariaBean;
    @EJB(beanName = "movimentacaoFinanceiraValidador")
    private ValidadorInterface movimentacaoFinanceiraValidador;
    @PersistenceContext(name = "jdbc/money")
    private EntityManager manager;
    
   /**
     * Cria a movimentação financeira para a conta e a receita passada.
     * @param contaBancaria
     * @param receitaDivida 
     */
    @Override
    public void salvarMovimentacaoFinanceira(ContaBancaria contaBancaria, ReceitaDivida receitaDivida) throws ValidacaoException{
        ContaBancaria cb = manager.find(ContaBancaria.class, contaBancaria.getId());
        ReceitaDivida rd = manager.find(ReceitaDivida.class, receitaDivida.getId());
        rd.setValor(receitaDivida.getValor());//Caso seja informado um novo valor ele será considerado
        MovimentacaoFinanceira mf = new MovimentacaoFinanceira(cb, rd);
        cb.setSaldo(cb.getSaldo() + rd.getValorParaCalculoDireto());
        rd.setStatusPagamento(StatusPagamento.PAGA);
        this.movimentacaoFinanceiraValidador.validar(mf, this, null);
        this.contaBancariaBean.salvarContaBancaria(cb);
        this.receitaDividaBean.salvarReceitaDivida(rd);
        manager.persist(mf);
        manager.flush();
    }

    
    /**
     * LIMITADO A TRAZER SOMENTE MOVIMENTAÇÕES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO<BR>
     * Retornar as movimentações financeiras de um usuário ou conjuge do mesmo.
     * Páginada, por ser um grande volume de dados
     * @param posicaoInicial
     * @param tamanho Quantidade máxima de registros que serão retornados.
     * @param usuario
     * @return 
     */
    @Override
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioStatusPaginada(int posicaoInicial, int tamanho, Usuario usuario) {
        Query q = manager.createNamedQuery("MovimentacaoFinanceiraBean.buscarMovimentacaoPorUsuarioStatusPaginada");
        q.setMaxResults(tamanho);
        q.setFirstResult(posicaoInicial);
        q.setParameter("usuario", usuario);
        return q.getResultList();
    }

   /**
     * LIMITADO A TRAZER SOMENTE MOVIMENTAÇÕES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO<BR>
     * Retorna a quantidade de registros de movimentação financeira de um determinado usuário.
     * Necessário para controle da paginação
     * @param usuario
     * @return 
     */
    @Override
    public Integer buscarQtdadeMovimentacaoPorUsuarioStatusPaginada(Usuario usuario) {
        Query q = manager.createNamedQuery("MovimentacaoFinanceiraBean.buscarQtdadeMovimentacaoPorUsuarioStatusPaginada");
        q.setParameter("usuario", usuario);
        Long toReturn = (Long) q.getSingleResult();
        return toReturn.intValue();
    }
    
    @Override
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioContaPaginada(int posicaoInicial, int tamanho, Usuario usuario, TipoConta tipoConta){
        Query q = manager.createNamedQuery("MovimentacaoFinanceiraBean.buscarMovimentacaoPorUsuarioContaPaginada");
        q.setMaxResults(tamanho);
        q.setFirstResult(posicaoInicial);
        q.setParameter("usuario", usuario);
        q.setParameter("tipoConta", tipoConta);
        return q.getResultList();
    }
    
    @Override
     public Integer buscarQtdadeMovimentacaoPorUsuarioContaPaginada(Usuario usuario, TipoConta tipoConta) {
        Query q = manager.createNamedQuery("MovimentacaoFinanceiraBean.buscarQtdadeMovimentacaoPorUsuarioContaPaginada");
        q.setParameter("usuario", usuario);
        q.setParameter("tipoConta", tipoConta);
        Long toReturn = (Long) q.getSingleResult();
        return toReturn.intValue(); 
     }
    
    /**
     * UTILIAR APENAS PARA DESFAZER AS MOVIMENTAÇÕES QUE ENVOLVAM ReceitaDivida, TRANSFERENCIA ENTRE CONTAS AINDA NÃO EXISTE COMO DESFAZER<BR>
     * Desfaz a movimentação financeira, credita o valor pago a conta, ou desconta o valor recebido, seta a conta como não paga.
     * @param movimentacaoFinanceira
     * @throws ValidacaoException 
     */
    @Override
    public void desfazerMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) throws ValidacaoException{
        MovimentacaoFinanceira mv = manager.find(MovimentacaoFinanceira.class, movimentacaoFinanceira.getId());
        if(mv.getReceitaDivida() == null){
            throw new MovimentacaoFinanceiraException("movimentacaoFinanceiraDesfazerTransferenciaErro", "Receita/Dívida");
        }
        ContaBancaria cb = manager.find(ContaBancaria.class, mv.getContaBancariaDebitada().getId());
        ReceitaDivida rd = manager.find(ReceitaDivida.class, mv.getReceitaDivida().getId());
        cb.setSaldo(cb.getSaldo() - rd.getValorParaCalculoDireto());
        rd.setStatusPagamento(StatusPagamento.NAO_PAGA);
        contaBancariaBean.salvarContaBancaria(cb);
        receitaDividaBean.salvarReceitaDivida(rd);
        manager.remove(mv);
        manager.flush();
    }
    
    /**
     * UTILIZE PARA TRANSFERÊNCIAS ENTRE CONTAS
     * Cria uma movimentação fianceira que referencia uma transferência entre contas bancárias
     * @param contaDe
     * @param contaPara
     * @param valor
     * @throws ValidacaoException 
     */
    @Override
    public void realizarTransferenciaEntreContas(ContaBancaria contaDe, ContaBancaria contaPara, double valor) throws ValidacaoException{
        if(contaDe.equals(contaPara)){
            throw new MovimentacaoFinanceiraException("transferenciaContaIguais", "Conta Origem, Conta Destino");
        }
        ContaBancaria cbDe = manager.find(ContaBancaria.class, contaDe.getId());
        ContaBancaria cbPara = manager.find(ContaBancaria.class, contaPara.getId());
        MovimentacaoFinanceira mf = new MovimentacaoFinanceira(cbDe, cbPara, valor);
        this.movimentacaoFinanceiraValidador.validar(mf, this, null);
        cbDe.setSaldo(cbDe.getSaldo() - valor);
        cbPara.setSaldo(cbPara.getSaldo() + valor);
        this.contaBancariaBean.salvarContaBancaria(cbDe);
        this.contaBancariaBean.salvarContaBancaria(cbPara);
        manager.persist(mf);
        manager.flush();
    }
}
