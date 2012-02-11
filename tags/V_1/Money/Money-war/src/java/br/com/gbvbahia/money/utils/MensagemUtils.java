/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.money.utils;

import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.lang.StringUtils;

/**
 * Irá substituir alguns métodos de UtilMetodos
 * @author Guilherme
 */
public class MensagemUtils {
	/**
	 * Cria FacesMessage para ser envida ao usuario atraves do resource bundle.
	 * Menssagem a adicionada dentro deste mÃ©todo.
	 * 
	 * @param msg
	 * @param variantes Um array de objeto ara sobrescrever todos {0}... da msg
	 * @param detalhe
	 * @param currentInstance
	 * @return
	 */
	public static void messageFactoringFull(String msg, Object[] variantes, FacesMessage.Severity severity, FacesContext currentInstance) {
		messageFactoringFull(null, msg, variantes, severity, currentInstance);
	}

	/**
	 * Cria FacesMessage para ser envida ao usuario atraves do resource bundle.
	 * Menssagem a adicionada dentro deste mÃ©todo.
	 * 
	 * @param clientId
	 * @param msg Texto em negrito, antes do detail.
	 * @param variantes Um array de objeto ara sobrescrever todos {0}... da msg
	 * @param severity
	 * @param currentInstance
	 */
	public static void messageFactoringFull(String clientId, String msg, Object[] variantes, FacesMessage.Severity severity,
			FacesContext currentInstance) {
		messageFactoringFull(null, clientId, msg, variantes, severity, currentInstance);
	}

	/**
	 * Cria FacesMessage para ser envida ao usuario atraves do resource bundle.
	 * Menssagem a adicionada dentro deste mÃ©todo.
	 * 
	 * @param detail Texto normal, apos a msg
	 * @param clientId ID no JSF, um input text, slectItem...
	 * @param msg Texto em negrito, antes do detail.
	 * @param variantes Um array de objeto ara sobrescrever todos {0}... da msg
	 * @param severity
	 * @param currentInstance
	 */
	public static void messageFactoringFull(String detail, String clientId, String msg, Object[] variantes, FacesMessage.Severity severity, FacesContext currentInstance) {
		FacesMessage message = new FacesMessage();
		try {
			String msgPropert = getBundleVarArgs(msg, currentInstance, variantes);
			message.setSummary(msgPropert);
		} catch (MissingResourceException e) {
			message.setSummary(msg);
		}
		if (severity != null) {
			message.setSeverity(severity);
		}
		if (detail == null) {
			message.setDetail("");
		} else {
			message.setDetail(": " + detail);
		}
		currentInstance.addMessage(clientId, message);
	}

	public static String getBundleVarArgs(String msg, FacesContext context, Object... var) {
		String toReturn = getResourceBundle(msg, context);
		if (var != null) {
			for (int i = 0; i < var.length; i++) {
				toReturn = StringUtils.replace(toReturn, "{" + i + "}", var[i].toString());
			}
		}
		return toReturn;
	}

	/**
	 * Pega uma String do resource bundle com base em uma chave informada.
	 * 
	 * @param String
	 *            msg
	 * @param FacesContext
	 *            currentInstance
	 * @return Retorna um texto do resource bundle em formato de string
	 */
	public static String getResourceBundle(String msg, FacesContext currentInstance) {
		try{
		return ResourceBundle.getBundle(currentInstance.getApplication().getMessageBundle()).getString(msg);
		}catch (Exception ex) {
			return msg;
		}
	}

    /**
     * Transforma um set de erros em uma Excecao NegocioException para ser lancada na camada de negocio.
     * @param erros
     * @throws NegocioException
     */
    public static void hibernateValidadionToNegocioException(Set<ConstraintViolation<Object>> erros) throws br.com.money.exceptions.ValidacaoException{
    	String erro = "";
    	for(ConstraintViolation cv : erros){
    		erro += cv.getMessage() + " ";
    	}
    	throw new ValidacaoException(erro);
    	
    }
    
    /**
     * Realiza validacao em um objeto com base nas anotacoes da especificacao JSR303, aqui
     * implemenetado pelo HibernateValidator nas entidades
     * @param entidade
     * @throws NegocioException
     */
   public static void validar(Object entidade) throws br.com.money.exceptions.ValidacaoException{
	   ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	   Validator validator = factory.getValidator();
	   Set<ConstraintViolation<Object>> msgs = validator.validate(entidade);
	   if(!msgs.isEmpty()){
		   hibernateValidadionToNegocioException(msgs);
	   }
	   return;
   }
   
   /**
    * Realiza validacao de uma colecao de objetos com base nas anotacoes da especificacao JSR303, aqui
    * implemenetado pelo HibernateValidator nas entidades
    * @param entidade
    * @throws NegocioException
    */
   public static void validar(List entidades) throws br.com.money.exceptions.ValidacaoException{
	   for(Object entidade : entidades){
		   validar(entidade);
	   }
   }
   
}
