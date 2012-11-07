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

function getInternetExplorer()  
{  
    if (navigator.appName == 'Microsoft Internet Explorer')  
    {  
        alert('Sorry, this site is not compatible with Internet Explorer.\nPlease use Google Chrome or Mozilla FireFox.');
        return false;  
    }  
    return true;  
}  

function infIE(idmsg){
    if(navigator.appName == 'Microsoft Internet Explorer'){
        document.getElementById(idmsg).innerText = "Desculpe, este site não suporta o Internet Explorer.\nPor favor, use o Google Chrome ou Mozilla FireFox";
    }
}

function confirme(msg){
    apagar = confirm(msg)//a variavel apagar aguarda um comando ok ou cancelar retornando assim false ou true
    return apagar;
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

function esconder(id){
    document.getElementById(id).style.visibility="hidden";    
    return true;
}

function mostrar(id){
    document.getElementById(id).style.visibility="visible";    
    return true;
}

function preencheZeros(param,tamanho)  
{  
    var contador = param.value.length;  
    if (param.value.length != tamanho)  
    {  
        do  
        {  
            param.value = "0" + param.value;  
            contador += 1;  
           
        }while (contador <tamanho)  
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
function mudaCor(element, cor){
    element.style.backgroundColor = cor;
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
PrimeFaces.locales['es'] = {
    closeText: 'Cerrar',
    prevText: 'Anterior',
    nextText: 'Siguiente',
    currentText: 'Inicio',
    monthNames: ['Enero','Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun','Jul','Ago','Sep','Oct','Nov','Dic'],
    dayNames: ['Domingo','Lunes','Martes','Miércoles','Jueves','Viernes','Sábado'],
    dayNamesShort: ['Dom','Lun', 'Mar', 'Mie', 'Jue', 'Vie', 'Sab'],
    dayNamesMin: ['D','L','M','X','J','V','S'],
    weekHeader: 'Semana',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Sólo hora',
    timeText: 'Tempo',
    hourText: 'Hora',
    minuteText: 'Minuto',
    secondText: 'Segundo',
    currentText: 'Fecha actual',
    ampm: false,
    month: 'Mes',
    week: 'Semana',
    day: 'Día',
    allDayText : 'Todo el día'
};
PrimeFaces.locales['de'] = {
    closeText: 'Schließen',
    prevText: 'Zurück',
    nextText: 'Weiter',
    currentText: 'Start',
    monthNames: ['Januar', 'Februar', 'MÃ¤rz', 'April', 'Mai', 'Juni', 'Juli', 'August', 'September', 'Oktober', 'November', 'Dezember'],
    monthNamesShort: ['Jan', 'Feb', 'MÃ¤r', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
    dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
    dayNamesShort: ['Son', 'Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam'],
    dayNamesMin: ['S', 'M', 'D', 'M ', 'D', 'F ', 'S'],
    weekHeader: 'Woche',
    FirstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: '',
    timeOnlyTitle: 'Nur Zeit',
    timeText: 'Zeit',
    hourText: 'Stunde',
    minuteText: 'Minute',
    secondText: 'Sekunde',
    currentText: 'Aktuelles Datum',
    ampm: false,
    month: 'Monat',
    week: 'Woche',
    day: 'Tag',
    allDayText: 'Ganzer Tag'
};

