<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.pessoalAviso}">
    <h:panelGroup id="allpage">
        <h:form id="formsenha">
            <h:panelGrid columns="3">
                <h:outputLabel value="#{texto.diasAntecedentes}:" id="senhaAtualLabel" for="antecedencia"/>
                <rich:inputNumberSpinner minValue="0" maxValue="5" id="antecedencia" label="#{texto.antecedencia}"
                                         value="#{lembreteContaFaces.lembreteConta.dias}" required="true"
                                         requiredMessage="#{texto.campoObrigatorio}"/>
                <rich:message for="antecedencia" styleClass="red"/>

                <h:outputLabel value="#{texto.email}:" id="novaSenhaLabel" for="email"/>
                <h:inputText value="#{lembreteContaFaces.lembreteConta.email}" required="true" requiredMessage="#{texto.campoObrigatorio}"
                             id="email" size="35" >
                    <f:validator validatorId="EmailValidador"/>
                </h:inputText>
                <rich:message for="email" styleClass="red"/>

                <h:outputLabel value="#{texto.status}:" id="confirmeNovaSenhaLabel" for="confirmeNovaSenha"/>
                <h:selectBooleanCheckbox value="#{lembreteContaFaces.lembreteConta.status}" id="status"/>
                <rich:message for="status" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.salvar}" actionListener="#{lembreteContaFaces.salvar}" reRender="allpage"
                                   id="savebutton" >
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
            </h:panelGrid>
        </h:form>
    </h:panelGroup>
</f:subview>