<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<f:subview id="inicialView" rendered="#{FluxoExibicaoFaces.cadastroContas}">
    <h:panelGroup id="allpage">
        <h:panelGroup id="gformdebito">
            <h:form id="novoLink">
                <h:panelGrid columns="2">
                    <h:outputText value=""/>
                    <a4j:include ajaxRendered="true" viewId="/auxiliar/enxertocontasbancarias.jsp" />

                    <h:outputText value=""/>
                    <rich:spacer width="100%" height="15"/>

                    <h:graphicImage value="/imagens/gGasto24.png" id="movimentado" title="#{texto.titleCadNovaConta}" rendered="#{!debitoEmContaFaces.exibirCadConta}"/>
                    <a4j:commandLink value="#{texto.cadastrarConta2}" actionListener="#{debitoEmContaFaces.clean}" rendered="#{!debitoEmContaFaces.exibirCadConta}"
                                     reRender="allpage">
                        <f:setPropertyActionListener target="#{debitoEmContaFaces.exibirCadConta}" value="#{true}"/>
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandLink>
                    <h:graphicImage value="/imagens/gGasto24.png" id="movimentadoDiaria" title="#{texto.titleCadNovaConta}" rendered="#{!debitoEmContaFaces.exibirCadContaDiaria}"/>
                    <a4j:commandLink value="#{texto.cadastrarConta3}" actionListener="#{debitoEmContaFaces.clean}" rendered="#{!debitoEmContaFaces.exibirCadContaDiaria}"
                                     reRender="allpage">
                        <f:setPropertyActionListener target="#{debitoEmContaFaces.exibirCadContaDiaria}" value="#{true}"/>
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandLink>
                    <h:graphicImage value="/imagens/can.png" id="movimentado2" title="#{texto.titleCadNovaConta}" rendered="#{debitoEmContaFaces.exibirCadConta || debitoEmContaFaces.exibirCadContaDiaria}"/>
                    <a4j:commandLink value="#{texto.cancelar}" actionListener="#{debitoEmContaFaces.clean}" rendered="#{debitoEmContaFaces.exibirCadConta || debitoEmContaFaces.exibirCadContaDiaria}"
                                     reRender="allpage">
                        <f:setPropertyActionListener target="#{debitoEmContaFaces.exibirCadConta}" value="#{false}"/>
                        <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                    </a4j:commandLink>
                </h:panelGrid>
            </h:form>
            <h:form id="formdebito">
                <h:panelGrid columns="3" rendered="#{debitoEmContaFaces.exibirCadConta || debitoEmContaFaces.exibirCadContaDiaria}" styleClass="fieldset_bvb">
                    <f:facet name="header">
                        <h:panelGroup>
                            <h:outputText value="#{texto.cadastrarConta2}" rendered="#{debitoEmContaFaces.exibirCadConta}"/>
                            <h:outputText value="#{texto.cadastrarConta3}" rendered="#{debitoEmContaFaces.exibirCadContaDiaria}"/>
                        </h:panelGroup>
                    </f:facet>
                    <h:outputLabel value="#{texto.formaPagamento}:" for="formaPagamentoinp" id="formaPagamentolabel"/>
                    <rich:comboBox value="#{debitoEmContaFaces.contaPagar.formaPagamento}" id="formaPagamentoinp" label="formaPagamentolabel"
                                   enableManualInput="false" width="150" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                   defaultLabel="#{texto.selecione}" valueChangeListener="#{debitoEmContaFaces.formaPagamentoListener}">
                        <f:converter converterId="EnumFormaPagamentoConverter"/>
                        <f:selectItems value="#{debitoEmContaFaces.formaPagamentoItens}"/>
                        <a4j:support event="onchange" ajaxSingle="true"
                                     reRender="cartaoCreditolabel,cartaoCreditoinp,cartaoCreditoinpmsg,contaDestinolabel,cartaoCreditoinp,cartaoCreditoinpmsg"/>
                    </rich:comboBox>
                    <rich:message for="formaPagamentoinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.cartaoCredito}:" for="cartaoCreditoinp" id="cartaoCreditolabel" rendered="#{debitoEmContaFaces.renderedCartaoCredito}"/>
                    <h:selectOneMenu value="#{debitoEmContaFaces.contaPagar.cartaoCreditoUnico}" id="cartaoCreditoinp" label="cartaoCreditolabel"
                                     required="true" requiredMessage="#{texto.campoObrigatorio}" rendered="#{debitoEmContaFaces.renderedCartaoCredito}">
                        <f:converter converterId="CartaoCreditoUnicoConverter"/>
                        <f:validator validatorId="CartaoCreditoUnicoValidador"/>
                        <f:selectItems value="#{debitoEmContaFaces.cartaoCreditoItens}"/>
                        <a4j:support event="onchange" ajaxSingle="true" reRender="formdebito" actionListener="#{debitoEmContaFaces.sugerirDataCC}"/>
                    </h:selectOneMenu>
                    <rich:message for="cartaoCreditoinp" styleClass="red" rendered="#{debitoEmContaFaces.renderedCartaoCredito}" id="cartaoCreditoinpmsg"/>

                    <h:outputLabel value="#{texto.contaDestino}:" for="contaDestinoinp" id="contaDestinolabel" rendered="#{debitoEmContaFaces.renderedContaTransferencia}"/>
                    <rich:comboBox value="#{debitoEmContaFaces.contaPagar.contaPara}" id="contaDestinoinp" label="contaDestinolabel" enableManualInput="false"
                                   rendered="#{debitoEmContaFaces.renderedContaTransferencia}" defaultLabel="#{texto.selecione}" width="150"
                                   listWidth="300">
                        <f:converter converterId="ContaBancariaConverter"/>
                        <f:selectItems value="#{debitoEmContaFaces.contaBancarias}"/>
                    </rich:comboBox>
                    <rich:message for="contaDestinoinp" styleClass="red" rendered="#{debitoEmContaFaces.renderedContaTransferencia}" id="contaDestinomsg"/>


                    <h:outputLabel value="#{texto.valor}:" id="valorlable" for="valorinp"/>
                    <h:inputText value="#{debitoEmContaFaces.contaPagar.valor}" id="valorinp"
                                 required="true" requiredMessage="#{texto.campoObrigatorio}"
                                 label="valorlable" size="20">
                        <f:converter converterId="MoneyConverter"/>
                    </h:inputText>
                    <rich:message for="valorinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.dataVencimento}:" id="dataVencimentolable" for="dataVencimentoinp"/>
                    <rich:calendar value="#{debitoEmContaFaces.contaPagar.dataVencimento}" datePattern="dd/MM/yyyy"
                                   enableManualInput="true" id="dataVencimentoinp" label="dataVencimentolable" locale="#{debitoEmContaFaces.locale}"
                                   showWeeksBar="false" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                    <rich:message for="dataVencimentoinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.parcelaAtual}:" id="parcelaAtuallabel" for="parcelaAtualinp"/>
                    <rich:inputNumberSpinner value="#{debitoEmContaFaces.contaPagar.parcelaAtual}" label="parcelaAtuallabel" id="parcelaAtualinp"
                                             required="true" requiredMessage="#{texto.campoObrigatorio}" minValue="1" maxValue="120"/>
                    <rich:message for="parcelaAtualinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.parcelaTotal}:" id="parcelaTotallabel" for="parcelaTotalinp"/>
                    <rich:inputNumberSpinner value="#{debitoEmContaFaces.contaPagar.parcelaTotal}" label="parcelaTotallabel" id="parcelaTotalinp"
                                             required="true" requiredMessage="#{texto.campoObrigatorio}" minValue="1" maxValue="120"/>
                    <rich:message for="parcelaTotalinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.salvaParcelas}:" id="salvaParcelaslabel" for="salvaParcelasinp"/>
                    <h:selectBooleanCheckbox value="#{debitoEmContaFaces.salvarParcelas}" id="salvaParcelasinp" label="salvaParcelaslabel"
                                             disabled="#{debitoEmContaFaces.contaPagar.id != null}"/>
                    <rich:message for="salvaParcelasinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.intervaloDias}:" id="salvaParcelaslabeldias" for="salvaParcelasdiasinp"
                                   rendered="#{debitoEmContaFaces.exibirCadContaDiaria && debitoEmContaFaces.contaPagar.id == null}"/>
                    <rich:inputNumberSpinner value="#{debitoEmContaFaces.diaria}" id="salvaParcelasdiasinp" label="#{texto.intervaloDias}"
                                             required="true" requiredMessage="#{texto.campoObrigatorio}" minValue="1" maxValue="120"
                                             rendered="#{debitoEmContaFaces.exibirCadContaDiaria && debitoEmContaFaces.contaPagar.id == null}"/>
                    <rich:message for="salvaParcelasdiasinp" styleClass="red"
                                  rendered="#{debitoEmContaFaces.exibirCadContaDiaria && debitoEmContaFaces.contaPagar.id == null}"/>

                    <h:outputLabel value="#{texto.tipo}:" for="tipoinp" id="tipolabel"/>
                    <rich:comboBox value="#{debitoEmContaFaces.contaPagar.grupoGasto}" id="tipoinp" label="tipolabel" enableManualInput="false"
                                   width="150" required="true" requiredMessage="#{texto.campoObrigatorio}" defaultLabel="#{texto.selecione}">
                        <f:converter converterId="GrupoGastoConverter"/>
                        <f:selectItems value="#{debitoEmContaFaces.grupoGastoItens}"/>
                    </rich:comboBox>
                    <rich:message for="tipoinp" styleClass="red"/>

                    <h:outputLabel value="#{texto.observacao}:" for="observacaoinp" id="observacaolabel"/>
                    <h:inputTextarea value="#{debitoEmContaFaces.contaPagar.observacao}" id="observacaoinp" label="observacaolabel"
                                     rows="2" cols="30" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                    <rich:message for="observacaoinp" styleClass="red"/>

                    <h:outputText value=""/>
                    <h:panelGroup id="buttonsformsalvar">
                        <a4j:commandButton value="#{texto.salvar}" actionListener="#{debitoEmContaFaces.salvar}" reRender="allpage"
                                           id="savebutton" >
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                        <rich:spacer width="3" height="1"/>
                        <a4j:commandButton value="#{texto.limpar}" actionListener="#{debitoEmContaFaces.clean}" reRender="gformdebito"
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
                <rich:calendar value="#{debitoEmContaFaces.initBusca}" datePattern="dd/MM/yyyy" enableManualInput="true"
                               id="dateIni" label="#{texto.dataInicial}" locale="#{debitoEmContaFaces.locale}"
                               showWeeksBar="false" required="true" requiredMessage="#{texto.campoObrigatorio}"/>
                <rich:message for="dateIni" styleClass="red"/>

                <h:outputLabel value="#{texto.limiteContas}:" for="limiteContas" id="limiteContasLabel"/>
                <rich:inputNumberSpinner value="#{debitoEmContaFaces.limiteContas}" id="limiteContas" enableManualInput="true"
                                         maxValue="60" minValue="5" required="true" requiredMessage="#{texto.campoObrigatorio}"
                                         step="5"/>
                <rich:message for="limiteContas" styleClass="red"/>

                <h:outputText value=""/>
                <a4j:commandButton value="#{texto.atualizar}" actionListener="#{debitoEmContaFaces.atualizaModelContas}" id="limiteContasButton"
                                   reRender="tableDebito">
                    <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                </a4j:commandButton>
            </h:panelGrid>
        </h:form>
        <h:panelGroup id="tableDebito">
            <h:form id="formtableDebito">
                <rich:dataTable value="#{debitoEmContaFaces.dataModel}" var="dc" id="tableDC" rows="10" align="center" rowClasses="linha1,linha2">
                    <f:facet name="header">
                        <h:outputText value="#{texto.debitoContaTable}"/>
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

                    <rich:column id="editar" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.editar}"/>
                        </f:facet>
                        <a4j:commandButton id="editarLink" actionListener="#{debitoEmContaFaces.pegarConta}" reRender="allpage"
                                           image="/imagens/edit.png" rendered="#{dc.movimentacaoFinanceira == null && dc.movimentacaoFinanceira.id == null}">
                            <f:setPropertyActionListener target="#{debitoEmContaFaces.exibirCadConta}" value="#{true}"/>
                            <a4j:ajaxListener type="org.ajax4jsf.ajax.ForceRender"/>
                        </a4j:commandButton>
                        <h:graphicImage value="/imagens/movimentado.png" id="editmovimentado" rendered="#{dc.movimentacaoFinanceira != null || dc.movimentacaoFinanceira.id != null}"
                                        title="#{texto.titleMovimentacaoRealizada}"/>
                    </rich:column>

                    <rich:column id="apagar" styleClass="center">
                        <f:facet name="header">
                            <h:outputText value="#{texto.apagar}"/>
                        </f:facet>
                        <a4j:commandButton id="apagarLink" actionListener="#{debitoEmContaFaces.pegarConta}" oncomplete="Richfaces.showModalPanel('conf_delete');"
                                           image="/imagens/lixo24.png" rendered="#{dc.movimentacaoFinanceira == null && dc.movimentacaoFinanceira.id == null}">
                        </a4j:commandButton>
                        <h:graphicImage value="/imagens/movimentado.png" id="movimentado" rendered="#{dc.movimentacaoFinanceira != null || dc.movimentacaoFinanceira.id != null}"
                                        title="#{texto.titleMovimentacaoRealizada}"/>
                    </rich:column>

                </rich:dataTable>
            </h:form>


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
                <h:form id="showModelForm">
                    <h:panelGrid columns="2">
                        <h:outputLabel value="#{texto.conta}:"/>
                        <h:outputText value="#{debitoEmContaFaces.contaPagar.smallObs}"/>

                        <h:outputLabel value="#{texto.vencimento}:"/>
                        <h:outputText value="#{debitoEmContaFaces.contaPagar.dataVencimento}">
                            <f:convertDateTime pattern="dd/MM/yyyy" locale="#{debitoEmContaFaces.locale}"/>
                        </h:outputText>

                        <h:outputLabel value="#{texto.valor}:"/>
                        <h:outputText value="#{debitoEmContaFaces.contaPagar.valor}">
                            <f:convertNumber type="currency" locale="#{debitoEmContaFaces.locale}"/>
                        </h:outputText>

                        <h:outputLabel value="#{texto.parcelaAtual}:"/>
                        <h:outputText value="#{debitoEmContaFaces.contaPagar.parcelaAtual}"/>

                        <h:outputLabel value="#{texto.parcelaTotal}:"/>
                        <h:outputText value="#{debitoEmContaFaces.contaPagar.parcelaTotal}"/>

                        <h:outputLabel value="#{texto.formaPagamento}:"/>
                        <h:outputText value="#{debitoEmContaFaces.contaPagar.formaPagamento.formaPagamento}"/>

                        <h:outputText value="#{texto.excluirParcelamentos}" title="#{texto.titleExcluirParcelamentos}"
                                      rendered="#{debitoEmContaFaces.contaPagar.parcelaTotal > 1 && debitoEmContaFaces.contaPagar.identificador != null}"/>
                        <h:selectBooleanCheckbox value="#{debitoEmContaFaces.excluirParcelamento}" title="#{texto.titleExcluirParcelamentos}" id="modelex"
                                                 rendered="#{debitoEmContaFaces.contaPagar.parcelaTotal > 1 && debitoEmContaFaces.contaPagar.identificador != null}">
                        </h:selectBooleanCheckbox>
                        <h:outputText value=""/>
                        <h:panelGroup id="buttonsModelPanel">
                            <a4j:commandButton actionListener="#{debitoEmContaFaces.apagar}" id="modelButtonApagar" reRender="allpage" value="#{texto.apagar}">
                                <rich:componentControl for="conf_delete" attachTo="modelButtonApagar" operation="hide" event="onclick"/>
                            </a4j:commandButton>
                            <rich:spacer height="1" width="3"/>
                            <a4j:commandButton actionListener="#{debitoEmContaFaces.clean}" id="modelButtonCancelar" reRender="allpage" value="#{texto.cancelar}">
                                <rich:componentControl for="conf_delete" attachTo="modelButtonApagar" operation="hide" event="onclick"/>
                            </a4j:commandButton>
                        </h:panelGroup>
                    </h:panelGrid>
                </h:form>
            </rich:modalPanel>

        </h:panelGroup>

    </h:panelGroup>
</f:subview>