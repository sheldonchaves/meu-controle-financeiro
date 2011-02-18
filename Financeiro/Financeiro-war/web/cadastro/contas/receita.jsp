<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroReceita}">
    <h:panelGroup id="allpage">
        <h:panelGroup id="gformdebito">
             <h:form id="novoLink">
                <h:panelGrid columns="2">
                    <h:outputText value=""/>
                    <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />

                    <h:outputText value=""/>
                    <rich:spacer width="100%" height="15"/>

                    <h:graphicImage value="/imagens/gReceita24.png" id="movimentado" title="#{texto.titleCadNovaConta}" rendered="#{!receitaFinanceiraFaces.exibirCadReceita}"/>
                    <a4j:commandLink value="#{texto.cadastrarReceita2}" actionListener="#{receitaFinanceiraFaces.clean}" rendered="#{!receitaFinanceiraFaces.exibirCadReceita}"
                                     reRender="allpage">
                        <f:setPropertyActionListener target="#{receitaFinanceiraFaces.exibirCadReceita}" value="#{true}"/>
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandLink>
                    <h:graphicImage value="/imagens/can.png" id="movimentado2" title="#{texto.titleCadNovaConta}" rendered="#{receitaFinanceiraFaces.exibirCadReceita}"/>
                    <a4j:commandLink value="#{texto.cancelar}" actionListener="#{receitaFinanceiraFaces.clean}" rendered="#{receitaFinanceiraFaces.exibirCadReceita}"
                                     reRender="allpage">
                        <f:setPropertyActionListener target="#{receitaFinanceiraFaces.exibirCadReceita}" value="#{false}"/>
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandLink>
                </h:panelGrid>
            </h:form>
            <h:form id="formdebito">
                <h:panelGrid columns="3" rendered="#{receitaFinanceiraFaces.exibirCadReceita}"  styleClass="fieldset_bvb">
                    <h:outputLabel value="#{texto.formaReceita}:" for="formaPagamentoinp" id="formaPagamentolabel"/>
                    <rich:comboBox value="#{receitaFinanceiraFaces.contaReceber.formaRecebimento}" id="formaPagamentoinp" label="formaPagamentolabel"
                                   enableManualInput="false" width="150" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                   defaultLabel="#{texto.selecione}" >
                        <f:converter converterId="EnumFormaRecebimentoConverter"/>
                        <f:selectItems value="#{receitaFinanceiraFaces.formaReceitaItens}"/>
                    </rich:comboBox>
                    <rich:message for="formaPagamentoinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.valor}:" id="valorlable" for="valorinp"/>
                    <h:inputText value="#{receitaFinanceiraFaces.contaReceber.valor}" id="valorinp"
                                 required="true" requiredMessage="#{texto.campoObrigatorio}"
                                 label="valorlable" size="20">
                        <f:converter converterId="MoneyConverter"/>
                    </h:inputText>
                    <rich:message for="valorinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.dataRecebimento}:" id="dataRecebimentolable" for="dataRecebimentoinp"/>
                    <rich:calendar value="#{receitaFinanceiraFaces.contaReceber.dataPagamento}" datePattern="dd/MM/yyyy"
                                   enableManualInput="true" id="dataRecebimentoinp" label="dataRecebimentolable" locale="#{receitaFinanceiraFaces.locale}"
                                   showWeeksBar="false" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                    <rich:message for="dataRecebimentoinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.parcelaAtual}:" id="parcelaAtuallabel" for="parcelaAtualinp"/>
                    <rich:inputNumberSpinner value="#{receitaFinanceiraFaces.contaReceber.parcelaAtual}" label="parcelaAtuallabel" id="parcelaAtualinp"
                                             required="true" requiredMessage="#{texto.campoObrigatorio}" minValue="1" maxValue="120"/>
                    <rich:message for="parcelaAtualinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.parcelaTotal}:" id="parcelaTotallabel" for="parcelaTotalinp"/>
                    <rich:inputNumberSpinner value="#{receitaFinanceiraFaces.contaReceber.parcelaTotal}" label="parcelaTotallabel" id="parcelaTotalinp"
                                             required="true" requiredMessage="#{texto.campoObrigatorio}" minValue="1" maxValue="120"/>
                    <rich:message for="parcelaTotalinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.salvaParcelas}:" id="salvaParcelaslabel" for="salvaParcelasinp"/>
                    <h:selectBooleanCheckbox value="#{receitaFinanceiraFaces.salvarParcelas}" id="salvaParcelasinp" label="salvaParcelaslabel"
                                             disabled="#{receitaFinanceiraFaces.contaReceber.id != null}"/>
                    <rich:message for="salvaParcelasinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.tipo}:" for="tipoinp" id="tipolabel"/>
                    <rich:comboBox value="#{receitaFinanceiraFaces.contaReceber.grupoReceita}" id="tipoinp" label="tipolabel" enableManualInput="false"
                                   width="150" required="true" requiredMessage="#{texto.campoObrigatorio}" defaultLabel="#{texto.selecione}">
                        <f:converter converterId="GrupoReceitaConverter"/>
                        <f:selectItems value="#{receitaFinanceiraFaces.grupoReceitaItens}"/>
                    </rich:comboBox>
                    <rich:message for="tipoinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.observacao}:" for="observacaoinp" id="observacaolabel"/>
                    <h:inputTextarea value="#{receitaFinanceiraFaces.contaReceber.observacao}" id="observacaoinp" label="observacaolabel"
                                     rows="2" cols="30" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                    <rich:message for="observacaoinp" styleClass="red"/>

                    <h:outputText value=""/>
                    <h:panelGroup id="buttonsformsalvar">
                        <a4j:commandButton value="#{texto.salvar}" actionListener="#{receitaFinanceiraFaces.salvar}" reRender="allpage"
                                           id="savebutton" >
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                        <rich:spacer width="3" height="1"/>
                        <a4j:commandButton value="#{texto.limpar}" actionListener="#{receitaFinanceiraFaces.clean}" reRender="gformdebito"
                                           id="cleanbutton" ajaxSingle="true">
                        </a4j:commandButton>
                    </h:panelGroup>
                </h:panelGrid>
            </h:form>
        </h:panelGroup>

        <rich:spacer width="100%" height="30"/>

        <h:form id="lmitDate">
            <h:panelGrid columns="3">
                <h:outputLabel value="#{texto.dataInicial}:" for="dateIni" id="dateLabel"/>
                <rich:calendar value="#{receitaFinanceiraFaces.initBusca}" datePattern="dd/MM/yyyy" enableManualInput="true"
                               id="dateIni" label="#{texto.dataInicial}" locale="#{receitaFinanceiraFaces.locale}"
                               showWeeksBar="false" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                <rich:message for="dateIni" styleClass="red"/>

                <h:outputLabel value="#{texto.limiteContas}:" for="limiteContas" id="limiteContasLabel"/>
                <rich:inputNumberSpinner value="#{receitaFinanceiraFaces.limiteContas}" id="limiteContas" enableManualInput="true"
                                         maxValue="60" minValue="5" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                         step="5"/>
                <rich:message for="limiteContas" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.atualizar}" actionListener="#{receitaFinanceiraFaces.atualizaModelContas}" id="limiteContasButton"
                                   reRender="tableDebito">
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
            </h:panelGrid>
        </h:form>

        <h:panelGroup id="tableDebito">
            <h:form id="formtableDebito">
                <rich:dataTable value="#{receitaFinanceiraFaces.dataModel}" var="dc" id="tableDC" rows="10" align="center" rowClasses="linha1,linha2">
                    <f:facet name="header">
                        <h:outputText value="#{texto.receitaFinanceiraTable}"/>
                    </f:facet>

                    <f:facet name="footer">
                        <rich:datascroller align="center" for="tableDC" maxPages="10"
                                           reRender="tableDebito" id="sc1" />
                    </f:facet>

                    <rich:column id="valor" styleClass="center" sortable="true" sortBy="#{dc.valor}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.valor}"/>
                        </f:facet>
                        <h:outputText value="#{dc.valor}">
                            <f:converter converterId="MoneyConverter"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="dataVencimento" styleClass="center" sortable="true" sortBy="#{dc.dataPagamento}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.dataRecebimento}"/>
                        </f:facet>
                        <h:outputText value="#{dc.dataPagamento}">
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

                    <rich:column id="statusPagamento" styleClass="center" sortable="true" sortBy="#{dc.statusReceita}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.status}"/>
                        </f:facet>
                        <h:outputText value="#{dc.statusReceita}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="grupoGasto" styleClass="center" sortable="true" sortBy="#{dc.grupoReceita}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.grupoReceita}"/>
                        </f:facet>
                        <h:outputText value="#{dc.grupoReceita}">
                            <f:converter converterId="GrupoReceitaConverter"/>
                        </h:outputText>
                    </rich:column>

                    <rich:column id="formaReceitac" styleClass="center" sortable="true" sortBy="#{dc.formaRecebimento}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.formaReceita}"/>
                        </f:facet>
                        <h:outputText value="#{dc.formaRecebimento}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="observacao" styleClass="center" sortable="true" sortBy="#{dc.observacao}">
                        <f:facet name="header">
                            <h:outputText value="#{texto.observacao}"/>
                        </f:facet>
                        <h:outputText value="#{dc.observacao}">
                        </h:outputText>
                    </rich:column>

                    <rich:column id="editar" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.editar}"/>
                        </f:facet>
                        <a4j:commandButton id="editarLink" actionListener="#{receitaFinanceiraFaces.pegarReceita}" reRender="tableDebito"
                                           image="/imagens/edit.png" status="status" rendered="#{dc.movimentacaoFinanceira == null && dc.movimentacaoFinanceira.id == null}">
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                            <f:setPropertyActionListener target="#{receitaFinanceiraFaces.exibirCadReceita}" value="#{true}"/>
                        </a4j:commandButton>
                        <h:graphicImage value="/imagens/movimentado.png" id="editmovimentado" rendered="#{dc.movimentacaoFinanceira != null || dc.movimentacaoFinanceira.id != null}"
                                        title="#{texto.titleMovimentacaoRealizada}"/>
                    </rich:column>

                    <rich:column id="apagar" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.apagar}"/>
                        </f:facet>
                        <a4j:commandButton id="apagarLink" actionListener="#{receitaFinanceiraFaces.pegarReceita}" reRender="tableDebito" oncomplete="Richfaces.showModalPanel('conf_delete');"
                                           image="/imagens/lixo24.png" status="status" rendered="#{dc.movimentacaoFinanceira == null && dc.movimentacaoFinanceira.id == null}">
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                        <h:graphicImage value="/imagens/movimentado.png" id="movimentado" rendered="#{dc.movimentacaoFinanceira != null || dc.movimentacaoFinanceira.id != null}"
                                        title="#{texto.titleMovimentacaoRealizada2}"/>
                    </rich:column>

                </rich:dataTable>
            </h:form>

            <a4j:form>
                <rich:modalPanel id="conf_delete" minHeight="100" minWidth="300"
                                 height="200" width="300">
                    <f:facet name="header">
                        <h:outputText value="#{texto.topTitleModalPanelExcluir}"/>
                    </f:facet>
                    <f:facet name="controls">
                        <h:panelGroup>
                            <h:graphicImage value="/imagens/lixo18.png" styleClass="hidelink" id="hidelink"/>
                        </h:panelGroup>
                    </f:facet>
                    <h:panelGrid columns="2">
                        <h:outputLabel value="#{texto.conta}:"/>
                        <h:outputText value="#{receitaFinanceiraFaces.contaReceber.smallObs}"/>

                        <h:outputLabel value="#{texto.dataRecebimento}:"/>
                        <h:outputText value="#{receitaFinanceiraFaces.contaReceber.dataPagamento}">
                            <f:convertDateTime pattern="dd/MM/yyyy" locale="#{debitoEmContaFaces.locale}"/>
                        </h:outputText>

                        <h:outputLabel value="#{texto.valor}:"/>
                        <h:outputText value="#{receitaFinanceiraFaces.contaReceber.valor}">
                            <f:convertNumber type="currency" locale="#{debitoEmContaFaces.locale}"/>
                        </h:outputText>

                        <h:outputLabel value="#{texto.parcelaAtual}:"/>
                        <h:outputText value="#{receitaFinanceiraFaces.contaReceber.parcelaAtual}"/>

                        <h:outputLabel value="#{texto.parcelaTotal}:"/>
                        <h:outputText value="#{receitaFinanceiraFaces.contaReceber.parcelaTotal}"/>

                        <h:outputLabel value="#{texto.formaPagamento}:"/>
                        <h:outputText value="#{receitaFinanceiraFaces.contaReceber.formaRecebimento.formaRecebimento}"/>

                        <h:outputText value=""/>
                        <h:panelGroup id="buttonsModelPanel">
                            <a4j:commandButton actionListener="#{receitaFinanceiraFaces.apagar}" id="modelButtonApagar" reRender="allpage" value="#{texto.apagar}">
                                <rich:componentControl for="conf_delete" attachTo="modelButtonApagar" operation="hide" event="onclick"/>
                            </a4j:commandButton>
                            <rich:spacer height="1" width="3"/>
                            <a4j:commandButton actionListener="#{receitaFinanceiraFaces.clean}" id="modelButtonCancelar" reRender="allpage" value="#{texto.cancelar}">
                                <rich:componentControl for="conf_delete" attachTo="modelButtonApagar" operation="hide" event="onclick"/>
                            </a4j:commandButton>
                        </h:panelGroup>
                    </h:panelGrid>
                </rich:modalPanel>
            </a4j:form>
        </h:panelGroup>

    </h:panelGroup>
</f:subview>