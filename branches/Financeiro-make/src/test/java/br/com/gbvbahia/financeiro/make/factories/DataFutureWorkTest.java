/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make.factories;

import br.com.gbvbahia.maker.types.complex.*;
import br.com.gbvbahia.maker.works.common.ValueSpecializedFactory;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Guilherme
 */
public class DataFutureWorkTest implements ValueSpecializedFactory {

    /**
     * Como o propertie deve estár definido no valor: "inFuture".
     */
    public static final String KEY_PROPERTIE = "inFuture";

    @Override
    public <T> boolean isWorkWith(Field f, T entity) {
        return f.getType().equals(Date.class)
                || f.getType().equals(Calendar.class);
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
        if (f.getType().equals(Date.class)) {
            f.set(entity, MakeDate.getInFuture());
        } else if (f.getType().equals(Calendar.class)) {
            f.set(entity, MakeCalendar.getInFuture());
        }

    }
}
