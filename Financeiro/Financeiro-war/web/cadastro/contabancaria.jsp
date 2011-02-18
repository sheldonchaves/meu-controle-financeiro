<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroContaBancaria}">
    <h:panelGroup id="allpage">
        <h:form>
            <h:panelGrid columns="3" id="cadContaGrid" styleClass="espacoEsquerda">
                <f:facet name="header">
                    <h:outputText value="#{texto.facetContaBancaria}"/>
                </f:facet>

                <h:outputLabel value="#{texto.banco}" for="bancoinp" id="bancolabel"/>
                <h:inputText value="#{ContaBancariaFaces.contaBancaria.nomeBanco}" id="bancoinp"
                             maxlength="100" size="25" required="true" requiredMessage="#{texto.campoObrigatorio}">
                    <f:validateLength maximum="100" minimum="5"/>
                </h:inputText>
                <rich:message for="bancoinp" styleClass="red"/>

                <h:outputLabel value="#{texto.tipo}" for="tipoinp" id="tipolabel"/>
                <rich:comboBox id="tipoinp" value="#{ContaBancariaFaces.contaBancaria.tipoConta}" label="tipolabel"
                               required="true" requiredMessage="#{texto.campoObrigatorio}" enableManualInput="true"
                               defaultLabel="#{texto.selecione}" width="153">
                    <f:converter converterId="EnumTipoContaConverter"/>
                    <f:selectItems value="#{ContaBancariaFaces.enunsTipoConta}"/>
                </rich:comboBox>
                <rich:message for="tipoinp" styleClass="red"/>

                <h:outputLabel value="#{texto.agencia}" for="agenciainp" id="agencialabel"/>
                <h:inputText value="#{ContaBancariaFaces.contaBancaria.agencia}" id="agenciainp"
                             maxlength="100" size="25" required="true" requiredMessage="#{texto.campoObrigatorio}">
                    <f:validateLength maximum="255" minimum="5"/>
                    <f:converter converterId="ContaBancariaConversor"/>
                </h:inputText>
                <rich:message for="agenciainp" styleClass="red"/>

                <h:outputLabel value="#{texto.conta}" for="containp" id="contalabel"/>
                <h:inputText value="#{ContaBancariaFaces.contaBancaria.numeroConta}" id="containp"
                             maxlength="100" size="25" required="true" requiredMessage="#{texto.campoObrigatorio}">
                    <f:validateLength maximum="255" minimum="5"/>
                    <f:converter converterId="ContaBancariaConversor"/>
                </h:inputText>
                <rich:message for="containp" styleClass="red"/>

                <h:outputLabel value="#{texto.saldoInicial}" for="saldoInicialinp" id="saldoIniciallabel"/>
                <h:inputText value="#{ContaBancariaFaces.contaBancaria.saldo}" id="saldoInicialinp"
                             maxlength="100" size="25" required="true" requiredMessage="#{texto.campoObrigatorio}">
                    <f:validateLength maximum="255"/>
                    <f:converter converterId="MoneyConverter"/>
                </h:inputText>
                <rich:message for="saldoInicialinp" styleClass="red"/>

                <h:outputLabel value="#{texto.status}" for="statusinp" id="statuslabel"/>
                <h:selectBooleanCheckbox value="#{ContaBancariaFaces.contaBancaria.status}" id="statusinp"/>
                <rich:message for="statusinp" styleClass="red"/>

                <h:outputLabel value="#{texto.observacao}" for="observacaoinp" id="observacaolabel"/>
                <h:inputTextarea value="#{ContaBancariaFaces.contaBancaria.observacao}" id="observacaoinp"
                                 cols="30" rows="3">
                    <f:validateLength maximum="255"/>
                </h:inputTextarea>
                <rich:message for="observacaoinp" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.salvar}" actionListener="#{ContaBancariaFaces.salvarContaBancaria}" reRender="allpage"
                                   id="buttonsave" status="status"/>
            </h:panelGrid>
        </h:form>

        <rich:spacer height="15" width="100%"/>

        <h:panelGroup id="tableContasGroup">
            <h:form id="tableContasForm">
                <rich:dataTable value="#{ContaBancariaFaces.dataModel}" id="contasTable" var="conta" align="center">
                    <f:facet name="header">
                        <h:outputText value="#{texto.contasTable}"/>
                    </f:facet>
                    
                    <rich:column id="nomeBanco" styleClass="center" sortable="true" sortBy="#{conta.nomeBanco}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.banco}"/>
                        </f:facet>
                        <h:outputText value="#{conta.nomeBanco}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="ag" styleClass="center" sortable="true" sortBy="#{conta.agencia}" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.agencia}"/>
                        </f:facet>
                        <h:outputText value="#{conta.agencia}">
                            <f:converter converterId="ContaBancariaConversor"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="conta" styleClass="center" sortable="true" sortBy="#{conta.numeroConta}" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.conta}"/>
                        </f:facet>
                        <h:outputText value="#{conta.numeroConta}">
                            <f:converter converterId="ContaBancariaConversor"/>
                        </h:outputText>
                    </rich:column>

                     <rich:column id="tipoConta" styleClass="center" sortable="true" sortBy="#{conta.tipoConta}" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.tipo}"/>
                        </f:facet>
                        <h:outputText value="#{conta.tipoConta}">
                        </h:outputText>
                    </rich:column>

                     <rich:column id="observacao" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.observacao}"/>
                        </f:facet>
                        <h:outputText value="#{conta.observacao}">
                        </h:outputText>
                    </rich:column>

                     <rich:column id="saldo" styleClass="center" sortable="true" sortBy="#{conta.saldo}" >
                        <f:facet name="header">
                            <h:outputText value="#{texto.saldo}"/>
                        </f:facet>
                        <h:outputText value="#{conta.saldo}">
                            <f:converter converterId="MoneyConverter"/>
                        </h:outputText>
                    </rich:column>

                     <rich:column id="apagar" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.apagar}"/>
                        </f:facet>
                         <a4j:commandButton id="apagarLink" actionListener="#{ContaBancariaFaces.deletarConta}" reRender="tableContasGroup"
                                           image="/imagens/x24.PNG" status="status">
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                    </rich:column>

                </rich:dataTable>
            </h:form>
        </h:panelGroup>
    </h:panelGroup>
</f:subview>