/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.servlet;

/**
 *
 * @author Brian
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@WebServlet("/CrearSession")
public class CrearSession extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Consumes({MediaType.APPLICATION_JSON})
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession misession= request.getSession(true);
        String idCliente = request.getParameter("idCliente");
        String idTipoCliente = request.getParameter("idTipoCliente");
        String du = request.getParameter("du");
        String usuario = request.getParameter("usuario");
        String contrasenia = request.getParameter("contrasenia");
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        
        misession.setAttribute("idCliente",idCliente);
        misession.setAttribute("idTipoCliente",idTipoCliente);
        misession.setAttribute("du",du);
        misession.setAttribute("usuario",usuario);
        misession.setAttribute("contrasenia",contrasenia);
        misession.setAttribute("nombre",nombre);
        misession.setAttribute("apellido",apellido);

    }
}
