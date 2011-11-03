<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.inicial}">
    <h:panelGroup id="allpage">
        <h:panelGroup id="tableContasUser">
            <h:form id="contasTable">
                <h:panelGrid columns="3">
                    <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />

                    <rich:spacer width="50" height="50"/>

                    <h:panelGroup id="graff" styleClass="center">
                        <h:graphicImage value="#{contasEmAberto.graficoPizzaNPagoPago}" height="200" width="250" styleClass="center"/>
                    </h:panelGroup>

                </h:panelGrid>
            </h:form>
        </h:panelGroup>

        <rich:spacer width="100%" height="15"/>
        <h:form id="lmitDate">
            <h:panelGrid columns="3">
                <h:outputLabel value="#{texto.dataInicial}:" for="dateIni" id="dateLabel"/>
                <rich:calendar value="#{contasEmAberto.initBusca}" datePattern="dd/MM/yyyy" enableManualInput="true"
                               id="dateIni" label="#{texto.dataInicial}" locale="#{contasEmAberto.locale}"
                               showWeeksBar="false" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                <rich:message for="dateIni" styleClass="red"/>

                <h:outputLabel value="#{texto.limiteContas}:" for="limiteContas" id="limiteContasLabel"/>
                <rich:inputNumberSpinner value="#{contasEmAberto.limiteContas}" id="limiteContas" enableManualInput="true"
                                         maxValue="60" minValue="5" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                         step="5"/>
                <rich:message for="limiteContas" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.atualizar}" actionListener="#{contasEmAberto.atualizaModelContas}" id="limiteContasButton"
                                   reRender="tableDebito">
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
            </h:panelGrid>
        </h:form>
        <h:panelGroup id="tableDebito">
            <h:form>
                <rich:dataTable value="#{contasEmAberto.dataModel}" var="conta" rows="10" align="center" id="tablecontas" rowClasses="linha1,linha2">
                    <f:facet name="header">
                        <h:outputText value="#{texto.contasEmAberto}"/>
                    </f:facet>

                    <f:facet name="footer">
                        <rich:datascroller align="center" for="tablecontas" maxPages="10"
                                           reRender="tableDebito" id="sc1" />
                    </f:facet>

                    <rich:column id="dataVencimento" styleClass="center" sortable="true" sortBy="#{conta.contaDataConta}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.vencimento}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaDataConta}">
                            <f:convertDateTime pattern="dd/MM/yyyy"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaValor" styleClass="center" sortable="true" sortBy="#{conta.contaValor}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.valor}">
                            </h:outputText>
                        </f:facet>
                        <h:inputText value="#{conta.contaValor}" id="valorinp"
                                     required="true" requiredMessage="#{texto.campoObrigatorio}"
                                     label="#{texto.valor}" size="10">
                            <f:converter converterId="MoneyConverter"/>
                        </h:inputText>
                    </rich:column>

                    <rich:column id="contaTipoMovimentacao" styleClass="center" sortable="true" sortBy="#{conta.contaTipoMovimentacao.tipoMovimentacaoString}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.tipo}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaTipoMovimentacao.tipoMovimentacaoString}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaTipo" styleClass="center" sortable="true" sortBy="#{conta.contaTipo}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.grupo}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaTipo}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaStatus" styleClass="center" sortable="true" sortBy="#{conta.contaStatus}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.status}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaStatus}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaParcelaAtual" styleClass="center" sortable="true" sortBy="#{conta.contaParcelaAtual}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.parcelaAtual}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaParcelaAtual}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaParcelaTotal" styleClass="center" sortable="true" sortBy="#{conta.contaParcelaTotal}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.parcelaTotal}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaParcelaTotal}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaForma" styleClass="center" sortable="true" sortBy="#{conta.contaForma}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.formaPagamento}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaForma}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaObservacao" styleClass="center" sortable="true" sortBy="#{conta.contaObservacao}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.observacao}"/>
                        </f:facet>
                        <h:outputText value="#{conta.contaObservacao}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="contaMovimentacaoFinanceira" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.conta}"/>
                        </f:facet>
                        <rich:comboBox value="#{conta.contaMovimentacaoFinanceira.contaBancaria}" id="contaMovimentada" enableManualInput="false"
                                       defaultLabel="#{texto.selecione}" width="150" listWidth="300">
                            <f:converter converterId="ContaBancariaConverter"/>
                            <f:selectItems value="#{contasEmAberto.contaBancariasItens}"/>
                        </rich:comboBox>
                    </rich:column>

                    <rich:column id="atualizar" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.confirmar}"/>
                        </f:facet>
                        <a4j:commandButton id="apagarLinkPagamento" actionListener="#{contasEmAberto.atualizaPagamento}" reRender="allpage"
                                           image="/imagens/gGasto24.png" status="status" rendered="#{conta.contaTipoMovimentacao.tipoMovimentacaoString == 'Pagamento'}">
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                        <a4j:commandButton id="apagarLinkReceita" actionListener="#{contasEmAberto.atualizaPagamento}" reRender="allpage"
                                           image="/imagens/gReceita24.png" status="status" rendered="#{conta.contaTipoMovimentacao.tipoMovimentacaoString == 'Receita'}">
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                    </rich:column>

                </rich:dataTable>
            </h:form>
        </h:panelGroup>
    </h:panelGroup>
</f:subview>