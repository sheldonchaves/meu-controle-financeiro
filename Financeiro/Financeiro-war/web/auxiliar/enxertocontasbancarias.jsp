<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%-- PARAMETROS
enxerto de informação de contas bancárias
--%>
<f:subview id="inicialView" >
    <h:panelGroup>
            <rich:dataTable value="#{enxertoContasGerencial.contasDataModel}" var="conta" align="left" id="tablecontasbancarias">
                <rich:column id="bancoNome">
                    <f:facet name="header">
                        <h:outputText value="#{texto.banco}"/>
                    </f:facet>
                    <h:outputText value="#{conta.nomeBanco}"/>
                </rich:column>

                <rich:column id="agencia">
                    <f:facet name="header">
                        <h:outputText value="#{texto.agencia}"/>
                    </f:facet>
                    <h:outputText value="#{conta.agencia}">
                        <f:converter converterId="ContaBancariaConversor"/>
                    </h:outputText>
                </rich:column>

                <rich:column id="conta">
                    <f:facet name="header">
                        <h:outputText value="#{texto.conta}"/>
                    </f:facet>
                    <h:outputText value="#{conta.numeroConta}">
                        <f:converter converterId="ContaBancariaConversor"/>
                    </h:outputText>
                </rich:column>

                <rich:column id="saldo">
                    <f:facet name="header">
                        <h:outputText value="#{texto.saldo}"/>
                    </f:facet>
                    <h:outputText value="#{conta.saldo}">
                        <f:convertNumber type="currency" locale="#{enxertoContasGerencial.locale}"/>
                    </h:outputText>
                </rich:column>
            </rich:dataTable>
    </h:panelGroup>
</f:subview>