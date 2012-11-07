package br.com.gbvbahia.projeto.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet para enviar arquivos ao usuário.<br> Seu uso é transparente
 * se utilizar o método JsfUtil.downloadFile.<br>
 *
 * @author Guilherme
 */
@WebServlet(urlPatterns = "/SFile")
public class DownServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File arquivo = new File(request.getParameter("caminhoArquivo")); // caminho completo do arquivo, desde a raiz
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + request.getParameter("nomeArquivo"));
        InputStream in = new FileInputStream(arquivo);
        ServletOutputStream output = response.getOutputStream();
        int bit = 512 * 1;
        try {
            while (bit >= 0) {
                bit = in.read();
                if (bit >= 0) {
                    output.write(bit);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        } finally {
            output.flush();
            output.close();
            in.close();
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "File download Servlet";
    }// </editor-fold>
}
