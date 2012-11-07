/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.make.factories;

import br.com.gbvbahia.maker.works.common.ValueSpecializedFactory;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Usuário do Windows
 */
public class DataPastWorkTest implements ValueSpecializedFactory {

    /**
     * Como o propertie deve estár definido no valor: "inFuture".
     */
    public static final String KEY_PROPERTIE = "inPast";

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
            f.set(entity, getDateOneYearAgo().getTime());
        } else if (f.getType().equals(Calendar.class)) {
            f.set(entity, getDateOneYearAgo());
        }
    }
    
    private Calendar getDateOneYearAgo(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        c.add(Calendar.YEAR, -1);
        return c;
    }
}
