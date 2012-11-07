/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make.factories;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.constantes.StatusPagamento;
import br.com.gbvbahia.financeiro.modelos.CartaoCredito;
import br.com.gbvbahia.financeiro.modelos.DespesaParceladaProcedimento;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
import br.com.gbvbahia.maker.types.complex.MakeBigDecimal;
import br.com.gbvbahia.maker.types.complex.MakeDate;
import br.com.gbvbahia.maker.types.complex.MakeString;
import br.com.gbvbahia.maker.types.primitives.numbers.MakeInteger;
import br.com.gbvbahia.maker.works.common.ValueSpecializedFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Usuário do Windows
 */
public class PrepareDespesaProcedimentoWorkTest implements ValueSpecializedFactory {

    public static final String KEY_PROPERTIE = "isProcedimentoDespesaPrepare";
    public static EntityManager manager;

    @Override
    public <T> boolean isWorkWith(Field f, T entity) {
        return f.getType().equals(CartaoCredito.class);
    }

    @Override
    public boolean workValue(String value) {
        if (KEY_PROPERTIE.equals(StringUtils.trim(value))) {
            return true;
        }
        return false;
    }
     @Override
    public <T> void makeValue(final String testName, final Field f,
            final T entity, final boolean makeRelationships)
            throws IllegalAccessException, IllegalArgumentException {
        try {
            DespesaParceladaProcedimento procedimento = (DespesaParceladaProcedimento) entity;
            //De procedimento
            procedimento.setDataVencimento(MakeDate.getInFuture());
            procedimento.setValorEstimado(new BigDecimal(MakeBigDecimal.getIntervalo(100d, 1000d).intValue()));
            procedimento.setValorReal(procedimento.getValorEstimado());
            procedimento.setObservacao(MakeString.getLoren(MakeInteger.getIntervalo(5, 150)));
            procedimento.setStatusPagamento(StatusPagamento.PAGA);
            Usuario usr;
            if (TestesMake.getUsuarioFacade().findAll().isEmpty()) {
                usr = MakeEntity.makeEntity("test_1", Usuario.class);
                manager.getTransaction().begin();
                TestesMake.getUsuarioFacade().create(usr);
                manager.getTransaction().commit();
            }else {
                usr = TestesMake.getUsuarioFacade().findAll().get(0);
            }
            procedimento.setUsuario(usr);
            
            DetalheProcedimento detalhe = MakeEntity.makeEntity("test_1", DetalheProcedimento.class);
            detalhe.setAtivo(true);
            detalhe.setTipo(procedimento.getTipoProcedimento());
            detalhe.setUsuario(procedimento.getUsuario());
            manager.getTransaction().begin();
            TestesMake.getDetalheProcedimentoFacade().create(detalhe);
            manager.getTransaction().commit();
            procedimento.setDetalhe(detalhe);
            
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
