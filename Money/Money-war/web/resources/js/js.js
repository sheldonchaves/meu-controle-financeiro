/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//<! [CDATA [
//onkeypress="return(MascaraMoeda(this,'.',',',event))"
function MascaraMoeda(objTextBox, SeparadorMilesimo, SeparadorDecimal, e){
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    var aux = aux2 = '';
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if(whichCode == 13 || whichCode == 8 || whichCode == 0) return true;
    key = String.fromCharCode(whichCode); // Valor para o código da Chave
    if (strCheck.indexOf(key) == -1) return false; // Chave inválida
    len = objTextBox.value.length;
    for(i = 0; i < len; i++)
        if ((objTextBox.value.charAt(i) != '0') && (objTextBox.value.charAt(i) != SeparadorDecimal)) break;
    aux = '';
    for(; i < len; i++)
        if (strCheck.indexOf(objTextBox.value.charAt(i))!=-1) aux += objTextBox.value.charAt(i);
    aux += key;
    len = aux.length;
    if (len == 0) objTextBox.value = '';
    if (len == 1) objTextBox.value = '0'+ SeparadorDecimal + '0' + aux;
    if (len == 2) objTextBox.value = '0'+ SeparadorDecimal + aux;
    if (len > 2) {
        aux2 = '';
        for (j = 0, i = len - 3; i >= 0; i--) {
            if (j == 3) {
                aux2 += SeparadorMilesimo;
                j = 0;
            }
            aux2 += aux.charAt(i);
            j++;
        }
        objTextBox.value = '';
        len2 = aux2.length;
        for (i = len2 - 1; i >= 0; i--)
        objTextBox.value += aux2.charAt(i);
        objTextBox.value += SeparadorDecimal + aux.substr(len - 2, len);
    }
    return false;
}

function verificaEmail(msg){
    var i = document.getElementById("acessoEmbutido:cadUserForm:useremailCad").value;
    var ii = document.getElementById("acessoEmbutido:cadUserForm:useremailCad2").value; 
    if( i != ii){
        document.getElementById(msg).innerText = "***";
    }else{
        document.getElementById(msg).innerText = "";
    }
}

function getInternetExplorer()  
{  
   if (navigator.appName == 'Microsoft Internet Explorer')  
   {  
       alert('Sorry, this site is not compatible with Internet Explorer.\nPlease use Google Chrome or Mozilla FireFox.');
     return false;  
   }  
   return true;  
}  
function confirme(msg){
    apagar = confirm(msg)//a variavel apagar aguarda um comando ok ou cancelar retornando assim false ou true
    return apagar;
}

function infIE(idmsg){
    if(navigator.appName == 'Microsoft Internet Explorer'){
        document.getElementById(idmsg).innerText = "Desculpe, este site não suporta o Internet Explorer.\nPor favor, use o Google Chrome ou Mozilla FireFox";
    }
}

function Contar(Campo, idmsg, limite){
    //alert(document.getElementById(idmsg).id);
    var max = parseInt(limite);
    document.getElementById(idmsg).innerText = max - Campo.length
    if((max - Campo.length)<=0){
        document.getElementById(idmsg).style.color = "red";
    }else{
        document.getElementById(idmsg).style.color = "blue";
    }
}
function formatar(src, mask, e){
  var whichCode = (window.Event) ? e.which : e.keyCode;
  if(whichCode == 13 || whichCode == 8 || whichCode == 0) return true;
  var i = src.value.length;
  var saida = mask.substring(0,1);
  var texto = mask.substring(i)
if (texto.substring(0,1) != saida)
  {
    src.value += texto.substring(0,1);
  }
}

//onkeypress='return SomenteNumero(event)'
function SomenteNumero(e){
    var tecla=(window.event)?event.keyCode:e.which;   
    if((tecla>47 && tecla<58)) return true;
    else{
    	if (tecla==8 || tecla==0) return true;
	else  return false;
    }
}

function focusCampo(id){
    var inputField = document.getElementById(id);
    if (inputField != null && inputField.value.length != 0){
        if (inputField.createTextRange){
            var FieldRange = inputField.createTextRange();
            FieldRange.moveStart('character',inputField.value.length);
            FieldRange.collapse();
            FieldRange.select();
        }else if (inputField.selectionStart || inputField.selectionStart == '0') {
            var elemLen = inputField.value.length;
            inputField.selectionStart = elemLen;
            inputField.selectionEnd = elemLen;
            inputField.focus();
        }
    }else{
        inputField.focus();
    }
}
PrimeFaces.locales['pt'] = {
    closeText: 'Fechar',
    prevText: 'Anterior',
    nextText: 'Próximo',
    currentText: 'Começo',
    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'],
    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
    dayNamesMin: ['D','S','T','Q','Q','S','S'],
    weekHeader: 'Semana',
    firstDay: 0,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Só Horas',
    timeText: 'Tempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    ampm: false,
    month: 'Mês',
    week: 'Semana',
    day: 'Dia',
    allDayText : 'Todo o Dia'
};
/**
function getInternetExplorerVersion()
// Returns the version of Windows Internet Explorer or a -1
// (indicating the use of another browser).
{
   var rv = -1; // Return value assumes failure.
   if (navigator.appName == 'Microsoft Internet Explorer')
   {
      var ua = navigator.userAgent;
      var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
      if (re.exec(ua) != null)
         rv = parseFloat( RegExp.$1 );
   }
   return rv;
}
function checkIEVersion()
{
   var ver = getInternetExplorerVersion();
   if ( ver > -1 )
   {
         if(ver == "9"){
             alert('true');
             return true;
         }
         else{
             alert('false');
             return false;
         }
   }
   else{
       return false;
   }
}
*/
//]]>