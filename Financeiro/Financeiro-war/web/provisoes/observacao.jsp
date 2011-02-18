<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.provisoesObservacao}">
    <h:panelGroup id="allpage">

        <h:panelGroup id="tableContasUser">
            <h:form id="contasTable">

                <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />
                <rich:spacer width="100%" height="15"/>

                <h:panelGrid columns="3">
                    <h:outputLabel value="#{texto.dataInicial}:" title="#{texto.titleDataInicio}" id="dataLabel" for="dataShoice"/>
                    <rich:calendar id="dataShoice" label="#{texto.dataInicial}" datePattern="dd/MM/yyyy" value="#{observacoesFaces.dataReferencia}"
                                   required="true" requiredMessage="#{texto.campoObrigatorio}" locale="#{observacoesFaces.locale}"
                                   enableManualInput="true" showWeeksBar="false"/>
                    <rich:message for="dataShoice" styleClass="red"/>

                    <h:outputLabel value="#{texto.observacao}:" for="observacaoinp" id="observacaolabel"/>
                    <h:inputText value="#{observacoesFaces.observacao}" id="observacaoinp" label="#{texto.observacao}"
                                 required="true" requiredMessage="#{texto.campoObrigatorio}" size="30">
                        <f:validateLength minimum="3" maximum="255"/>
                    </h:inputText>
                    <rich:message for="observacaoinp" styleClass="red"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.buscar}" reRender="allpage"
                                       actionListener="#{observacoesFaces.buscarContas}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>

                </h:panelGrid>
            </h:form>
        </h:panelGroup>
        <rich:spacer width="100%" height="15"/>
        <h:panelGroup id="tableDebito" rendered="#{observacoesFaces.contasModel != null}">
            <h:form id="formtableDebito">
                <rich:dataTable value="#{observacoesFaces.contasModel}" var="dc" id="tableDC" align="center" rowClasses="linha1,linha2">
                    <f:facet name="header">
                        <h:outputText value="#{texto.obsContaTable}"/>
                    </f:facet>

                    <rich:column id="valor" styleClass="center" sortable="true" sortBy="#{dc.valor}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.valor}"/>
                        </f:facet>
                        <h:outputText value="#{dc.valor}">
                            <f:converter converterId="MoneyConverter"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="dataVencimento" styleClass="center" sortable="true" sortBy="#{dc.dataVencimento}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.dataVencimento}"/>
                        </f:facet>
                        <h:outputText value="#{dc.dataVencimento}">
                            <f:convertDateTime pattern="dd/MM/yyyy"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="parcelaAtual" styleClass="center" sortable="true" sortBy="#{dc.parcelaAtual}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.parcelaAtual}"/>
                        </f:facet>
                        <h:outputText value="#{dc.parcelaAtual}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="parcelaTotal" styleClass="center" sortable="true" sortBy="#{dc.parcelaTotal}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.parcelaTotal}"/>
                        </f:facet>
                        <h:outputText value="#{dc.parcelaTotal}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="formaPagamento" styleClass="center" sortable="true" sortBy="#{dc.formaPagamento}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.formaPagamento}"/>
                        </f:facet>
                        <h:outputText value="#{dc.formaPagamento}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="statusPagamento" styleClass="center" sortable="true" sortBy="#{dc.statusPagamento}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.status}"/>
                        </f:facet>
                        <h:outputText value="#{dc.statusPagamento}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="grupoGasto" styleClass="center" sortable="true" sortBy="#{dc.grupoGasto}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.grupoGasto}"/>
                        </f:facet>
                        <h:outputText value="#{dc.grupoGasto}">
                            <f:converter converterId="GrupoGastoConverter"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="observacao" styleClass="center" sortable="true" sortBy="#{dc.observacao}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.observacao}"/>
                        </f:facet>
                        <h:outputText value="#{dc.observacao}">
                        </h:outputText>
                    </rich:column>

                </rich:dataTable>
            </h:form>
        </h:panelGroup>
        <rich:spacer height="15" width="500"/>
    </h:panelGroup>
</f:subview>