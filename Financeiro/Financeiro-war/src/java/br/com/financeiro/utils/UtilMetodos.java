/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.financeiro.utils;

import br.com.financeiro.entidades.enums.StatusPagamento;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;

/**
 *Classe utilizada para conter métodos de utilidade publica.
 * @author Guilherme
 */
public class UtilMetodos {

    /**
     * Retira qualquer tipo de aspas, evitando problemas na persistencia no BD
     * @param String
     * @return Retorna String sem aspas
     */
    public static String limpaString(String string) {
        if (string != null) {
            String assuntotemp = string.replace("\"", "").replace("'", "").replace("“", "").replace("‘", "");
            assuntotemp = assuntotemp.replace((char) 147, ' ').replace((char) 148, ' ').replace((char) 150, ' ');
            return assuntotemp;
        } else {
            return null;
        }
    }

    public static String limpaStringCSV(String string) {
        return string.replace("\n", " ").replace("\r", " ").replace(";", ",").replace("\t", " ");
    }

    /**
     * Adicione uma mensagem para ser exibida na tag messagens da JSF de sua página
     * A String msg deve vir já pronta, o método não busca no resource bundle
     * @param String msg
     * @param FacesContext currentInstance
     */
    public static void addMessage(String msg, FacesContext currentInstance) {
        currentInstance.addMessage(msg, new FacesMessage(msg));
    }

    /**
     * Cria  FacesMessage para ser envida ao usuario atraves do resource bundle
     * @param msg
     * @param detalhe
     * @param currentInstance
     * @return
     */
    public static FacesMessage messageFactoring(String msg, FacesMessage.Severity severity, FacesContext currentInstance) {
        FacesMessage message = new FacesMessage();
        message.setSummary(ResourceBundle.getBundle(currentInstance.getApplication().
                getMessageBundle()).getString(msg));
        if (severity != null) {
            message.setSeverity(severity);
        }
        return message;
    }

    /**
     * Cria  FacesMessage para ser envida ao usuario atraves do resource bundle
     * @param msg
     * @param detalhe
     * @param currentInstance
     * @return
     */
    public static void messageFactoringFull(String msg, FacesMessage.Severity severity, FacesContext currentInstance) {
        FacesMessage message = new FacesMessage();
        message.setSummary(ResourceBundle.getBundle(currentInstance.getApplication().
                getMessageBundle()).getString(msg));
        if (severity != null) {
            message.setSeverity(severity);
        }
        currentInstance.addMessage(null, message);
    }

    /**
     * Código Java de uma classe com os métodos de validação de CNPJ de acordo com as regras da Receita Federal.
     * @param str_cnpj
     * @return retorna verdadeiro (true) para CNPJ válido e falso (false) para CNPJ inválido
     */
    static public boolean CNPJ(String str_cnpj) {
        if (str_cnpj == null) {
            return false;
        }
        if (str_cnpj.length() != 14) {
            return false;
        }
        int soma = 0, aux, dig;
        String cnpj_calc = str_cnpj.substring(0, 12);
        char[] chr_cnpj = str_cnpj.toCharArray();

        /* Primeira parte */
        for (int i = 0; i < 4; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
                soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);

        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        /* Segunda parte */
        soma = 0;
        for (int i = 0; i < 5; i++) {
            if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
                soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
            }
        }
        for (int i = 0; i < 8; i++) {
            if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
                soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
            }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

        return str_cnpj.equals(cnpj_calc);
    }

    /**
     * Pega uma String do resource bundle com base em uma chave informada.
     * @param String msg
     * @param FacesContext currentInstance
     * @return Retorna um texto do resource bundle em formato de string
     */
    public static String getResourceBundle(String msg, FacesContext currentInstance) {
        return ResourceBundle.getBundle(currentInstance.getApplication().
                getMessageBundle()).getString(msg);
    }

    /**
     * Verifica se a String possui o minimo necessário de letras, a quantidade é definida pelo segundo parametro
     * numeros, espaços e caracteres especiais são ignorados.
     * @param String nome
     * @param int letras
     * @return retorna true se o nome estiver dentro do padrão especificado pelo método.
     */
    public static boolean nomeValido(String nome, int letras) {
        if (nome == null || nome.length() < letras) {
            return false;
        }
        int r = 0;
        for (int i = 0; i < nome.length(); i++) {
            Character c = nome.charAt(i);
            if (Character.isLetter(c)) {
                r++;
            }
        }
        if (r < letras) {
            return false;
        }
        return true;
    }

    /**
     * Passe um nome completo e ele retorna apenas os dois primeiros nomes, caso o nome tenha um segundo nome muito curto, até 3 caracteres,
     * ele retorna o 1º, 2º e 3º nome. Ex: Maria Dalva Pereira Retorna: Maria Dalva, José dos Dias Silveira Retorna: José dos Dias
     * @param String nomeCompleto
     * @return String com os dois primeiros nomes
     */
    public static String doisNomes(String nomeCompleto) {
        int i = 0;
        int b = 0;
        int a = 0;
        String nome = null;
        for (int x = 0; x < nomeCompleto.length(); x++) {
            Character z = nomeCompleto.charAt(x);
            if (Character.isSpaceChar(z)) {
                if (a == 0) {
                    a = x;
                    i++;
                } else {
                    b = x;
                    i++;
                }
                //Caso o 2º Nome seja muito curto
                if (b != 0 && (b - a) <= 4) {
                    for (int j = b + 1; j < nomeCompleto.length(); j++) {
                        Character zz = nomeCompleto.charAt(j);
                        if (Character.isSpaceChar(zz)) {
                            x = j;
                            break;
                        } else if (j == nomeCompleto.length() - 1) {
                            x = j + 1;
                            break;
                        }
                    }
                }
            }
            if (i == 2) {
                nome = nomeCompleto.substring(0, x);
                return nome;
            }
        }
        return nomeCompleto;
    }

    /**
     * Metodo validacaoCPF - Responsavel em validar o CPF
     *
     * @return Boolean
     * @since 29/12/2006
     */
    public static boolean validacaoCPF(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "").replace(" ", "");
        if (cpf.length() != 11) {
            return false;
        }
        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;
        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;
        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(cpf.substring(nCount - 1, nCount)).intValue();
            //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;
            //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        //Primeiro resto da divisão por 11.
        resto = (d1 % 11);
        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito1 = 0;
        } else {
            digito1 = 11 - resto;
        }
        d2 += 2 * digito1;
        //Segundo resto da divisão por 11.
        resto = (d2 % 11);
        //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
        if (resto < 2) {
            digito2 = 0;
        } else {
            digito2 = 11 - resto;
        }
        //Digito verificador do CPF que está sendo validado.
        String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());
        //Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
        //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
        return nDigVerific.equals(nDigResult);
    }

    /**
     * Retona a data passada em um string com dd/mm/aaaa
     * @param date
     * @return String dd/MM/aaaa
     */
    public static String getDataString(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer dia = c.get(Calendar.DAY_OF_MONTH);
        Integer mes = c.get(Calendar.MONTH) + 1;
        Integer ano = c.get(Calendar.YEAR);
        return dia.toString() + "/" + mes.toString() + "/" + ano.toString();
    }

    public static String getDataStringMesAno(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer mes = c.get(Calendar.MONTH) + 1;
        Integer ano = c.get(Calendar.YEAR);
        return mes.toString() + "/" + ano.toString();
    }

    public static Object getBean(String name, FacesContext fc) {
        Map sessionMap = fc.getExternalContext().getSessionMap();
        return sessionMap.get(name);
    }

    public static String currencyFormat(Double n) {
        if (n == null) {
            n = 0.00;
        }
        Locale locale = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(n);
    }

    public static String currencyFormat(Long n) {
        if (n == null) {
            n = 0l;
        }
        Locale locale = new Locale("pt", "BR");
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(n);
    }

    /**
     * Para utilizar este método o Servlet SFile deve ser configurado no web.xml
     * @param file
     * @param facesContext
     */
    public static void downloadFile(File file, FacesContext facesContext) {
        String caminhoArquivo = file.getAbsolutePath();
        String nomeArquivo = file.getName();
        HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            resp.sendRedirect(facesContext.getExternalContext().getRequestContextPath()
                    + "/SFile?caminhoArquivo=" + caminhoArquivo + "&nomeArquivo=" + nomeArquivo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        facesContext.responseComplete(); // essa linha NÃƒO PODE FALTAR
    }

    public static void copiarArquivo(File file, String destino, String nomeArquivo) {
        try {
            File delete = new File(destino + File.separator + nomeArquivo);
            if (delete.exists()) {
                delete.delete();
            }
            FileInputStream fileIn = new FileInputStream(file);//file e passado usando JFileChooser
            FileOutputStream fileOut = new FileOutputStream(destino + File.separator + nomeArquivo);
            BufferedInputStream in = new BufferedInputStream(fileIn);
            BufferedOutputStream out = new BufferedOutputStream(fileOut);
            byte[] buffer = new byte[10240];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static List<SelectItem> getStatusPgItens() {
        List<SelectItem> toReturn = new ArrayList<SelectItem>();
        for (StatusPagamento sp : StatusPagamento.values()) {
            toReturn.add(new SelectItem(sp, sp.getStatusString()));
        }
        return toReturn;
    }

    public static Date aumentaMesDate(Date dataVencimento, int qtdade) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataVencimento);
        c.add(Calendar.MONTH, qtdade);
        return c.getTime();
    }

    public static Date aumentaDiaDate(Date dataVencimento, int qtdade) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataVencimento);
        c.add(Calendar.DAY_OF_MONTH, qtdade);
        return c.getTime();
    }

    /**
     * Retorna um array de java.util.Date com o primeiro dia do mês
     * e o ultimo dia do mês, com base na data de referência passada
     * @param referencia
     * @return
     */
    public static Date[] primeiroUltimoDia(Date referencia){
        Date[] toReturn = new Date[2];
        Calendar c1 = Calendar.getInstance();
        c1.setTime(referencia);
        c1.set(Calendar.DAY_OF_MONTH, 1);
        toReturn[0] = c1.getTime();

        Calendar c2 = Calendar.getInstance();
        c2.setTime(referencia);
        c2.set(Calendar.DAY_OF_MONTH, c2.getActualMaximum(Calendar.DAY_OF_MONTH));
        toReturn[1] = c2.getTime();
        return toReturn;
    }

    public static String getURLFinanceiro(HttpSession session, FacesContext fc) {
        String toReturn = UtilMetodos.getResourceBundle("paginaFinanceiro", fc);
        toReturn += session.getServletContext().getContextPath();
        return toReturn;
    }

    public synchronized static String getIdentificadorUnico(int idUser, Date dataVencimento) {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        UUID uuid = new UUID(dataVencimento.getTime(), new Date().getTime());
        String toReturn = idUser + "-" + uuid.toString();
        toReturn = StringUtils.substring(toReturn, 0, 50);
        return toReturn;
    }
}
