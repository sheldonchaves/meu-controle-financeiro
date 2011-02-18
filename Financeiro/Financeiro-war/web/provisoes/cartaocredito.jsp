<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.provisoesCartaoCredito}">
    <h:panelGroup id="allpage">

        <h:panelGroup id="tableContasUser">
            <h:form id="contasTable">


                <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />
                <rich:spacer width="100%" height="15"/>

                <h:panelGrid columns="3">
                    <h:outputLabel value="#{texto.intervalo}:" id="intervalolable" for="intervalo"/>
                    <rich:inputNumberSpinner value="#{cartaoCreditoFaces1.meses}" minValue="1" maxValue="3" label="intervalolable"
                                             id="intervalo" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                    <rich:message for="intervalo" styleClass="red"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.atualizar}" reRender="tableContasUser" id="buttonatualizar" actionListener="#{cartaoCreditoFaces1.atualizaValoresCartao}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                </h:panelGrid>
                <rich:spacer height="15" width="100%"/>
                <h:panelGrid columns="1">
                    <a4j:include ajaxRendered="true" viewId="/auxiliar/cartaointerface.jsp" >
                    </a4j:include>
                    <rich:spacer height="15" width="100%"/>
                </h:panelGrid>
            </h:form>
        </h:panelGroup>

        <rich:spacer width="100%" height="20"/>

        <h:panelGroup id="historicoPagamentos">
            <h:form id="buscarContasCC">
                <h:panelGrid columns="3">
                    <h:outputLabel value="#{texto.cartaoCredito}:" id="cartaoLabel" for="cartaoCredito"/>
                    <h:selectOneMenu value="#{cartaoCreditoFaces1.cartaoCreditoUnico}" id="cartaoCredito"
                                     required="true" requiredMessage="#{texto.campoObrigatorio}">
                        <f:converter converterId="CartaoCreditoUnicoConverter"/>
                        <f:validator validatorId="CartaoCreditoUnicoValidador"/>
                        <f:selectItems value="#{cartaoCreditoFaces1.cartoesCredito}"/>
                    </h:selectOneMenu>
                    <rich:message for="cartaoCredito" styleClass="red"/>

                    <h:outputLabel value="#{texto.referenciaFatura}:" id="vencimentoMesLabel" for="vencimentoMes" title="#{texto.titlevencimentoMes}"/>
                    <rich:calendar value="#{cartaoCreditoFaces1.mesCartao}" datePattern="MMMM/yyyy" locale="#{cartaoCreditoFaces1.locale}"
                                   enableManualInput="true" id="vencimentoMes" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                   showWeeksBar="false">
                    </rich:calendar>
                    <rich:message for="vencimentoMes" styleClass="red"/>

                    <h:outputText value=""/>
                    <a4j:commandButton value="#{texto.buscar}" id="buttonBuscarContasCartao" reRender="historicoPagamentos" status="status"
                                       actionListener="#{cartaoCreditoFaces1.atualizarListaContasCartaoCredito}">
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandButton>
                </h:panelGrid>
            </h:form>

            <rich:spacer width="100%" height="15"/>
            <h:panelGroup rendered="#{cartaoCreditoFaces1.dataModelContas != null}">
                <h:graphicImage value="#{cartaoCreditoFaces1.pareto}" height="320" width="1050" styleClass="center" rendered="#{cartaoCreditoFaces1.dataModelContas != null}"/>
                <rich:spacer width="100%" height="10"/>
                <h:form id="tableFormContas">
                    <rich:dataTable value="#{cartaoCreditoFaces1.dataModelContas}" var="conta" id="tablecontas" rowClasses="linha1,linha2" align="center" width="950">
                        <f:facet name="header">
                            <h:panelGroup>
                                <h:outputText value="#{texto.contasCartaoCreditoTable}: "/>
                                <h:outputText value="#{cartaoCreditoFaces1.cartaoCreditoUnico.empresaCartao}"/>
                            </h:panelGroup>
                        </f:facet>
                        <rich:column id="dataVencimento" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.vencimento}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaDataConta}">
                                <f:convertDateTime pattern="dd/MM/yyyy"/>
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{texto.totalFatura}:" styleClass="totais"/>
                            </f:facet>
                        </rich:column>

                        <rich:column id="contaValor" styleClass="center" sortable="true" sortBy="#{conta.contaValor}">
                            <f:facet name="header">
                                <h:outputText value="#{texto.valor}">
                                </h:outputText>
                            </f:facet>
                            <h:outputText value="#{conta.contaValor}" id="valorinp">
                                <f:convertNumber type="currency" locale="#{cartaoCreditoFaces1.locale}"/>
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{cartaoCreditoFaces1.totalFatura}" styleClass="totais">
                                    <f:convertNumber type="currency" locale="#{cartaoCreditoFaces1.locale}"/>
                                </h:outputText>
                            </f:facet>
                        </rich:column>

                        <rich:column id="contaParcelaAtual" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.parcelaAtual}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaParcelaAtual}">
                            </h:outputText>
                        </rich:column>

                        <rich:column id="contaParcelaTotal" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.parcelaTotal}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaParcelaTotal}">
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{texto.totalPagar}:" styleClass="totais"/>
                            </f:facet>
                        </rich:column>

                        <rich:column id="contaStatus" styleClass="center" sortable="true" sortBy="#{conta.contaStatus}">
                            <f:facet name="header">
                                <h:outputText value="#{texto.status}"/>
                            </f:facet>
                            <h:outputText value="#{conta.contaStatus}">
                            </h:outputText>
                            <f:facet name="footer">
                                <h:outputText value="#{cartaoCreditoFaces1.totalPagar}" styleClass="totais">
                                    <f:convertNumber type="currency" locale="#{cartaoCreditoFaces1.locale}"/>
                                </h:outputText>
                            </f:facet>
                        </rich:column>

                        <rich:column id="grupoGasto" styleClass="center" sortable="true" sortBy="#{conta.grupoGasto}">
                            <f:facet name="header">
                                <h:outputText value="#{texto.grupo}"/>
                            </f:facet>
                            <h:outputText value="#{conta.grupoGasto}">
                            </h:outputText>
                        </rich:column>

                        <rich:column id="contaObservacao" styleClass="center">
                            <f:facet name="header">
                                <h:outputText value="#{texto.observacao}"/>
                            </f:facet>
                            <h:outputText value="#{conta.observacao}">
                            </h:outputText>
                        </rich:column>
                    </rich:dataTable>
                </h:form>
            </h:panelGroup>
        </h:panelGroup>
    </h:panelGroup>
</f:subview>