/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make.factories;

import br.com.gbvbahia.maker.works.common.ValueSpecializedFactory;
import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Usuário do Windows
 */
public class UserIdWorkTest implements ValueSpecializedFactory {

    /**
     * Como o propertie deve estár definido no valor: "isCEP".
     */
    public static final String KEY_PROPERTIE = "isUserId";
    private static int count = 1;

    @Override
    public <T> boolean isWorkWith(Field f, T entity) {
        return f.getType().equals(String.class);
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
        f.set(entity, "user_" + count++);
    }
}
