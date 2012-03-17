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
import br.com.money.utils.UtilBeans;
import br.com.money.vaidators.interfaces.ValidadorInterface;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Guilherme
 */
@Stateless
public class MovimentacaoFinanceiraBean extends
        AbstractFacade<MovimentacaoFinanceira, Long>
        implements MovimentacaoFinanceiraBeanLocal {

    public MovimentacaoFinanceiraBean() {
        super(MovimentacaoFinanceira.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.manager;
    }

    @Override
    protected ValidadorInterface getValidador() {
        return movimentacaoFinanceiraValidador;
    }

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
        ContaBancaria cb = contaBancariaBean.find(contaBancaria.getId());
        ReceitaDivida rd = receitaDividaBean.find(receitaDivida.getId());
        rd.setValor(receitaDivida.getValor());//Caso seja informado um novo valor ele será considerado
        MovimentacaoFinanceira mf = new MovimentacaoFinanceira(cb, rd);
        cb.setSaldo(cb.getSaldo() + rd.getValorParaCalculoDireto());
        rd.setStatusPagamento(StatusPagamento.PAGA);
        this.contaBancariaBean.salvarContaBancaria(cb);
        this.receitaDividaBean.salvarReceitaDivida(rd);
        create(mf);
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
    public List<MovimentacaoFinanceira> 
            buscarMovimentacaoPorUsuarioStatusPaginada(
            final int posicaoInicial, int tamanho, Usuario usuario,
            Long idContaBancaria) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("id", idContaBancaria);
        return listPesqParam("MovimentacaoFinanceiraBean.buscar"
                + "MovimentacaoPorUsuarioStatusPaginada", parans,
                tamanho, posicaoInicial);
    }

   /**
     * LIMITADO A TRAZER SOMENTE MOVIMENTAÇÕES DE PAGAMENTOS E RECEITAS, QUE TENHAM ReceitaDivida NÃO NULO<BR>
     * Retorna a quantidade de registros de movimentação financeira de um determinado usuário.
     * Necessário para controle da paginação
     * @param usuario
     * @return 
     */
    @Override
    public Integer buscarQtdadeMovimentacaoPorUsuarioStatusPaginada(Usuario usuario, Long idContaBancaria) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("id", idContaBancaria);
        return pesqCount("MovimentacaoFinanceiraBean.buscarQtdade"
                + "MovimentacaoPorUsuarioStatusPaginada", parans).intValue();
    }
    
    @Override
    public List<MovimentacaoFinanceira> buscarTodasTransferenciasEntreContasPaginada(int posicaoInicial, int tamanho, Usuario usuario){
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        return listPesqParam("MovimentacaoFinanceiraBean.buscarTodas"
                + "TransferenciasEntreContasPaginada",
                parans, tamanho, posicaoInicial);
    }
    
    @Override
    public Integer buscarQtdadeTodasTransferenciasEntreContasPaginada(Usuario usuario){
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        return pesqCount("MovimentacaoFinanceiraBean.buscarQtdade"
                + "TodasTransferenciasEntreContasPaginada", parans).intValue();
    }
    
    @Override
    public List<MovimentacaoFinanceira> buscarMovimentacaoPorUsuarioContaPaginada(int posicaoInicial, int tamanho, Usuario usuario, TipoConta tipoConta){
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("tipoConta", tipoConta);
        return listPesqParam("MovimentacaoFinanceiraBean."
                + "buscarMovimentacaoPorUsuarioContaPaginada",
                parans, tamanho, posicaoInicial);
    }
    
    @Override
     public Integer buscarQtdadeMovimentacaoPorUsuarioContaPaginada(Usuario usuario, TipoConta tipoConta) {
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("usuario", usuario);
        parans.put("tipoConta", tipoConta);
        return pesqCount("MovimentacaoFinanceiraBean.buscarQtdade"
                + "MovimentacaoPorUsuarioContaPaginada",
                parans).intValue();
     }
    
    /**
     * UTILIAR APENAS PARA DESFAZER AS MOVIMENTAÇÕES QUE ENVOLVAM ReceitaDivida, TRANSFERENCIA ENTRE CONTAS AINDA NÃO EXISTE COMO DESFAZER<BR>
     * Desfaz a movimentação financeira, credita o valor pago a conta, ou desconta o valor recebido, seta a conta como não paga.
     * @param movimentacaoFinanceira
     * @throws ValidacaoException 
     */
    @Override
    public void desfazerMovimentacaoFinanceira(MovimentacaoFinanceira movimentacaoFinanceira) throws ValidacaoException{
        MovimentacaoFinanceira mv = find(movimentacaoFinanceira.getId());
        if(mv.getReceitaDivida() == null){
            throw new MovimentacaoFinanceiraException("movimentacao"
                    + "FinanceiraDesfazerTransferenciaErro",
                    "Receita/Dívida");
        }
        ContaBancaria cb =
                contaBancariaBean.find(mv.getContaBancariaDebitada().getId());
        ReceitaDivida rd =
                receitaDividaBean.find(mv.getReceitaDivida().getId());
        cb.setSaldo(cb.getSaldo() - rd.getValorParaCalculoDireto());
        rd.setStatusPagamento(StatusPagamento.NAO_PAGA);
        contaBancariaBean.salvarContaBancaria(cb);
        receitaDividaBean.salvarReceitaDivida(rd);
        remove(mv);
        manager.flush();
    }
    
    /**
     * UTILIZE PARA TRANSFERÊNCIAS ENTRE CONTAS
     * Cria uma movimentação fianceira que referencia uma
     * transferência entre contas bancárias
     * @param contaDe
     * @param contaPara
     * @param valor
     * @throws ValidacaoException 
     */
    @Override
    public void realizarTransferenciaEntreContas(ContaBancaria contaDe,
    ContaBancaria contaPara, double valor) throws ValidacaoException{
        if(contaDe.equals(contaPara)){
            throw new MovimentacaoFinanceiraException("transferencia"
                    + "ContaIguais", "Conta Origem, Conta Destino");
        }
        ContaBancaria cbDe = contaBancariaBean.find(contaDe.getId());
        ContaBancaria cbPara = contaBancariaBean.find(contaPara.getId());
        MovimentacaoFinanceira mf = new MovimentacaoFinanceira(cbDe, cbPara, valor);
        cbDe.setSaldo(cbDe.getSaldo() - valor);
        cbPara.setSaldo(cbPara.getSaldo() + valor);
        this.contaBancariaBean.salvarContaBancaria(cbDe);
        this.contaBancariaBean.salvarContaBancaria(cbPara);
        create(mf);
        manager.flush();
    }
    
    @Override
    public List<MovimentacaoFinanceira> buscarMovimentacaoFinanceiraPorUsuarioPeriodo(
            Usuario usuario, TipoConta tipoConta, Date ini, Date fim){
        Map<String, Object> parans = AbstractFacade.getMapParans();
        parans.put("tipoConta", tipoConta);
        parans.put("user", usuario);
        parans.put("dataI", UtilBeans.primeiroUltimoHorario(ini)[0]);
        parans.put("dataF", UtilBeans.primeiroUltimoHorario(fim)[1]);
        return listPesqParam("MovimentacaoFinanceiraBean.buscar"
                + "MovimentacaoFinanceiraPorUsuarioPeriodo", parans);
    }
}
