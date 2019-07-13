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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/VerSession")
public class VerSession extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession misession= (HttpSession) request.getSession();

        JSONObject usuario = new JSONObject();
        
        try {
            usuario.put("idCliente", Integer.parseInt((String)misession.getAttribute("idCliente")))
                    .put("idTipoCliente",Integer.parseInt((String)misession.getAttribute("idTipoCliente")))
                    .put("usuario",(String)misession.getAttribute("usuario"))
                    .put("contrasenia",(String)misession.getAttribute("contrasenia"))
                    .put("nombre",(String)misession.getAttribute("nombre"))
                    .put("apellido",(String)misession.getAttribute("apellido"))
                    .put("du",Integer.parseInt((String)misession.getAttribute("du")))
                    ;
        } catch (JSONException ex) {
            Logger.getLogger(VerSession.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PrintWriter pw= response.getWriter();
        pw.println(usuario.toString());
        pw.close();
    }
}