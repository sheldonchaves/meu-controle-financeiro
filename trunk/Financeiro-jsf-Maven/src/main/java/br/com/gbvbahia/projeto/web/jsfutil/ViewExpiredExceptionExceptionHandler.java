package br.com.gbvbahia.projeto.web.jsfutil;

import br.com.gbvbahia.utils.MensagemUtils;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 * Redireciona o usuário para página de login quando a sessão expira.
 * @since v.1 21/05/2012
 * @author Guilherme Braga
 */
public class ViewExpiredExceptionExceptionHandler extends
        ExceptionHandlerWrapper {
    private ExceptionHandler wrapped;

    /**
     * Construtor utilizado pelo Factory para redirecionamento para pagina de
     * login. Se for uma exceção de sessao expirada.
     * @param wrapped Objeto que contém a exceção.
     */
    public ViewExpiredExceptionExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        for (Iterator<ExceptionQueuedEvent> i =
                getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context =
                    (ExceptionQueuedEventContext) event.getSource();

            Throwable t = context.getException();
            if (t instanceof ViewExpiredException) {
                ViewExpiredException vee = (ViewExpiredException) t;
                FacesContext facesContext = FacesContext.getCurrentInstance();
                Map<String, Object> requestMap =
                        facesContext.getExternalContext().getRequestMap();
                NavigationHandler navigationHandler =
                        facesContext.getApplication().getNavigationHandler();
                try {
                    // Push some useful stuff to the request scope for use in the page
                    requestMap.put("currentViewId", vee.getViewId());
                    navigationHandler.handleNavigation(facesContext, null,
                            "/login");
                    MensagemUtils.messageFactoringFull("sessaoExpirada",
                            null, FacesMessage.SEVERITY_WARN,
                            facesContext);
                    facesContext.renderResponse();
                } finally {
                    i.remove();
                }
            }
        }
        // At this point, the queue will not contain any ViewExpiredEvents.
        //Therefore, let the parent handle them.
        getWrapped().handle();
    }
}
