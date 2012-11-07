package br.com.gbvbahia.projeto.web.jsfutil;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Responsável por receber exceções JSF, configurado no faces-config.xml, e
 * criar o objeto que irá tratar, se necessário, a exceção.
 *
 * @author Guilherme Braga
 * @since v.1 21/05/2012
 */
public class ViewExpiredExceptionExceptionHandlerFactory extends
        ExceptionHandlerFactory {

    private ExceptionHandlerFactory parent;

    /**
     * Consutrtor utilizado pelo Faces-Config.
     *
     * @param parent Objeto que contém informações do contexto, inclusive a
     * exceção gerada.
     */
    public ViewExpiredExceptionExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new ViewExpiredExceptionExceptionHandler(parent.
                getExceptionHandler());
    }
}