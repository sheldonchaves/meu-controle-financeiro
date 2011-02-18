<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.pessoalSenha}">
    <h:panelGroup id="allpage">
        <h:form id="formsenha">
            <h:panelGrid columns="3">
                <h:outputLabel value="#{texto.senhaAtual}:" id="senhaAtualLabel" for="senhaAtual"/>
                <h:inputSecret value="#{senhaFaces.senhaAtual}" id="senhaAtual" label="#{texto.senhaAtual}" required="true"
                             requiredMessage="#{texto.campoObrigatorio}" maxlength="15" size="28">
                    <f:validateLength maximum="15"/>
                </h:inputSecret>
                <rich:message for="senhaAtual" styleClass="red"/>

                <h:outputLabel value="#{texto.novaSenha}:" id="novaSenhaLabel" for="novaSenha"/>
                <h:inputSecret value="#{senhaFaces.novaSenha}" id="novaSenha" label="#{texto.novaSenha}" required="true"
                             requiredMessage="#{texto.campoObrigatorio}" maxlength="15" size="28">
                    <f:validateLength maximum="15"/>
                </h:inputSecret>
                <rich:message for="novaSenha" styleClass="red"/>

                <h:outputLabel value="#{texto.confirmeNovaSenha}:" id="confirmeNovaSenhaLabel" for="confirmeNovaSenha"/>
                <h:inputSecret value="#{senhaFaces.confirmaSenha}" id="confirmeNovaSenha" label="#{texto.confirmeNovaSenha}" required="true"
                             requiredMessage="#{texto.campoObrigatorio}" maxlength="15" size="28">
                    <f:validateLength maximum="15"/>
                </h:inputSecret>
                <rich:message for="confirmeNovaSenha" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.salvar}" actionListener="#{senhaFaces.salvar}" reRender="allpage"
                                   id="savebutton" >
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
            </h:panelGrid>
        </h:form>
    </h:panelGroup>
</f:subview>