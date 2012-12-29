/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.financeiro.utils;

import java.io.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Classe auxiliar para trabalhar com Arquivos.
 *
 * @since v.1 22/03/2012
 * @author Guilherme Braga
 */
public final class FileUtils {

    private static final Logger logger = Logger.getLogger(FileUtils.class);
    /**
     * Caminho para a pasta de imagens utilizadas pelo sistema
     */
    public static String CAMINHO_IMAGENS_PERMANENTE;
    /**
     * Nome do logo do hiper cash para utilizacao no e-mail.
     */
    public static final String LOGO_MONEI_MAIL = "logo.png";
    /**
     * Arquivo que representa a logo do e-mail.
     */
    public static File LOGO_EMAIL_FILE;

    static {
        try {
            final String so = System.getProperty("os.name");
            if (StringUtils.containsIgnoreCase(so, "win")) {
                CAMINHO_IMAGENS_PERMANENTE = "C:\\glassfish3\\imagens";
                LOGO_EMAIL_FILE = new File(CAMINHO_IMAGENS_PERMANENTE + File.separator + LOGO_MONEI_MAIL);
            } else {
                CAMINHO_IMAGENS_PERMANENTE = "/usr/share/glassfish3/glassfish/domains/domain1/config";
                LOGO_EMAIL_FILE = new File(LOGO_MONEI_MAIL);
            }
        } catch (Exception e) {
            logger.error("Erro ao tentar criar o logo file referenciado em: " + LOGO_EMAIL_FILE.getAbsolutePath());
            logger.error("O DIRETORIO ATUAL: " + new File(".").getAbsolutePath());
            logger.error("Erro: ", e);
            LOGO_EMAIL_FILE = null;
        }
    }

    /**
     * Não pode ser instânciada.
     */
    private FileUtils() {
    }

    /**
     * Zipa os arquivos passados e devolve o mesmo.
     *
     * @param toZip Arquivos a serem compactados.
     * @param dir Local onde será gerado o arquivo zip.
     * @param nomeFile nome do arquivo, não inser .zip, já é inserido
     * automaticamente.
     * @return java.io.File
     * @throws IOException se Algum erro de IO ocorrer.
     */
    public static File ziparArquivo(final File[] toZip, final File dir,
            final String nomeFile) throws IOException {
        return new ZipHelper().zip(toZip, dir, nomeFile);
    }

    /**
     * Zipa o arquivo passado e devolve o mesmo.
     *
     * @param toZip Arquivos a serem compactados.
     * @param dir Local onde será gerado o arquivo zip.
     * @param nomeFile nome do arquivo, não inser .zip, já é inserido
     * automaticamente.
     * @return java.io.File
     * @throws IOException se Algum erro de IO ocorrer.
     */
    public static File ziparArquivo(final File toZip, final File dir,
            final String nomeFile) throws IOException {
        return ziparArquivo(new File[]{toZip}, dir, nomeFile);
    }

    /**
     * Para dar feedback ao usuário é necessário saber a quantidade de
     * linhas do arquivo, este método possibilita esta tarefa.
     *
     * @param File arquivoLeitura !
     * @return Um int que representa a quantidade de linhas do arquivo
     * @throws IOException
     */
    public static int contarLinhasArquivo(File arquivoLeitura)
            throws IOException {
        FileInputStream fs = null;
        DataInputStream in = null;
        LineNumberReader lineRead = null;
        try {
            long tamanhoArquivo = arquivoLeitura.length();
            fs = new FileInputStream(arquivoLeitura);
            in = new DataInputStream(fs);
            lineRead = new LineNumberReader(new InputStreamReader(in));
            lineRead.skip(tamanhoArquivo);
            int numLinhas = lineRead.getLineNumber() + 1;
            logger.info("***   O ARQUIVO CONTEM: " + numLinhas + " LINHAS!!!!!!!");
            return numLinhas;
        } finally {
            if (fs != null) {
                fs.close();
            }
            if (in != null) {
                in.close();
            }
            if (lineRead != null) {
                lineRead.close();
            }
        }
    }

    /**
     * Para dar feedback ao usuário é necessário saber a quantidade de
     * linhas do arquivo, este método possibilita esta tarefa.
     *
     * @param arquivoLeitura Stream que representa o arquivo que terá as
     * linhas contadas.
     * @param loginUser Login do usuário logado, obrigatório.
     * @return Um int que representa a quantidade de linhas do arquivo
     * @throws IOException se algum erro inesperado de IO ocorrer.
     */
    public static int contarLinhasArquivo(final InputStream arquivoLeitura,
            final String loginUser)
            throws IOException {
        return contarLinhasArquivo(converteInputStreamInFile(arquivoLeitura,
                loginUser, "temp__solida.txt"));
    }

    /**
     * Cria um arquivo com o nome especificado dentro do diretorio padrão
     * da aplicação na pasta temp.
     *
     * @param nomeArquivo Nome do arquivo que será criado.
     * @param loginUser Será criada uma pasta para os arquivos do usuário.
     * @return Arquivo dentro de tempDir/silida/nomeArquivo
     */
    public static File diretorioPadrao(final String nomeArquivo,
            final String loginUser) {
        File f2 = diretorioPadrao(loginUser);
        File toReturn = new File(f2, nomeArquivo);
        return toReturn;
    }

    /**
     * Retorna um java.io.File que representa o diretório padrão da
     * aplicação. tempdir/solida.<br> Sendo tempdir uma referência ao
     * diretório temporário da aplicação.
     *
     * @param loginUser Será criada uma pasta para os arquivos do usuário.
     * @return File apontando para tempdir/solida.
     */
    public static File diretorioPadrao(final String loginUser) {
        File f = new File(System.getProperty("java.io.tmpdir"));
        File f2 = new File(f, "solida");
        File toReturn = new File(f2,
                StringUtils.trim(StringUtils.replace(StringUtils.replace(
                loginUser, " ", "_"), "-", "_")));
        toReturn.mkdirs();
        return toReturn;
    }

    /**
     * Converte um InputStream em arquivo, necessário para leitura do
     * mesmo.
     *
     * @param stream
     * @param nomeUser Login do usuário logado, obrigatório.
     * @param nomeArquivo Nome do arquivo a ser gerado.
     * @return java.io.File
     * @throws IOException
     */
    public static File converteInputStreamInFile(InputStream stream,
            String nomeUser, String nomeArquivo) throws IOException {
        File f = diretorioPadrao(nomeArquivo, nomeUser);
        OutputStream out = new FileOutputStream(f);
        byte buf[] = new byte[1024];
        int len;
        while ((len = stream.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
        out.close();
        stream.close();
        return f;
    }

    /**
     * Converte o arquivo passado em stream para ser enviado via rede.
     *
     * @param file Aquivo a ser convertido.
     * @return Stream com dados do arquivo.
     * @throws FileNotFoundException se não achar o mesmo.
     */
    public static InputStream converteFileInStream(File file)
            throws FileNotFoundException {
        InputStream inStream = new FileInputStream(file);
        return inStream;
    }

    /**
     * Apaga o arquivo passado como parâmetro, se for um <strong>diretório
     * apaga todos</strong> os arquivos dentro dele e de suas
     * <strong>subpastas</strong>.
     *
     * @param f Arquivo a ser removido ou diretório a ter arquivos
     * removidos.
     * @param somenteArquivos True para apagar somente arquivos, deixando
     * estrutura de diretórios. False apaga o que vier pela frente.
     */
    public static void remover(final File f, final boolean somenteArquivos) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; ++i) {
                remover(files[i], somenteArquivos);
            }
        }
        if (!f.isFile() && !somenteArquivos) {
            f.delete();
        } else if (somenteArquivos && f.isFile()) {
            f.delete();
        }
    }

    public static byte[] converteFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            ex.getMessage();
        }
        byte[] bytes = bos.toByteArray();
        bos.close();
        fis.close();
        return bytes;
    }

    public static File converteByte(final byte[] bytes, final String nomeArquivo,
            final String loginUser) throws IOException {
        File toReturn = diretorioPadrao(nomeArquivo, loginUser);
        FileOutputStream fos = new FileOutputStream(toReturn);
        fos.write(bytes);
        fos.close();
        return toReturn;
    }

    /**
     *
     * @param stream Do arquivo. Obrigatório
     * @param nomeUser Nome do usuário solicitante. Obrigatório
     * @param nomeArquivo Nome do arquivo a ser convertido. Obrigatório
     * @return arquivo no formato de bytes[]
     * @throws IOException
     */
    public static byte[] converteStreamInByte(InputStream stream,
            String nomeUser, String nomeArquivo) throws IOException {
        File toConvert = converteInputStreamInFile(stream, nomeUser, nomeArquivo);
        return converteFile(toConvert);
    }
}
