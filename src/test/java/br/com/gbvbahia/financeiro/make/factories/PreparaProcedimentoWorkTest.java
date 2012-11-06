/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make.factories;

import br.com.gbvbahia.financeiro.TestesMake;
import br.com.gbvbahia.financeiro.constantes.TipoProcedimento;
import br.com.gbvbahia.financeiro.modelos.DetalheProcedimento;
import br.com.gbvbahia.financeiro.modelos.Procedimento;
import br.com.gbvbahia.financeiro.modelos.Usuario;
import br.com.gbvbahia.maker.MakeEntity;
import br.com.gbvbahia.maker.works.common.ValueSpecializedFactory;
import java.lang.reflect.Field;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guilherme
 */
public class PreparaProcedimentoWorkTest implements ValueSpecializedFactory {

    public static final String KEY_PROPERTIE = "isProcedimentoPrepare";
    public static EntityManager manager;
    
    @Override
    public <T> boolean isWorkWith(Field f, T entity) {
        return f.getType().equals(Enum.class);
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
            f.set(entity, TipoProcedimento.RECEITA_FINANCEIRA);
            Procedimento procedimento = (Procedimento) entity;
            Usuario usr;
            if (TestesMake.getUsuarioFacade().findAll().isEmpty()) {
                usr = MakeEntity.makeEntity("test_1", Usuario.class);
                manager.getTransaction().begin();
                TestesMake.getUsuarioFacade().create(usr);
                manager.getTransaction().commit();
            }else {
                usr = TestesMake.getUsuarioFacade().findAll().get(0);
            }
            DetalheProcedimento detalhe = MakeEntity.makeEntity("test_1", DetalheProcedimento.class);
            detalhe.setAtivo(true);
            detalhe.setTipo(TipoProcedimento.RECEITA_FINANCEIRA);
            detalhe.setUsuario(usr);
            manager.getTransaction().begin();
            TestesMake.getDetalheProcedimentoFacade().create(detalhe);
            manager.getTransaction().commit();
            procedimento.setDetalhe(detalhe);
            procedimento.setUsuario(usr);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
