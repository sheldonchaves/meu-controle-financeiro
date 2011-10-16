/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//<! [CDATA [

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