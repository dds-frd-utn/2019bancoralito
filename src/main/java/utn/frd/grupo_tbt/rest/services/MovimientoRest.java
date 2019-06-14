/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
package utn.frd.grupo_tbt.rest.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utn.frd.grupo_tbt.entity.Movimiento;
import utn.frd.grupo_tbt.sessions.MovimientoFacade;

/**
 *
 * @author Brian
 
public class MovimientoRest {
    @EJB
    private MovimientoFacade ejbMovimientoFacade;
    
    //obtener todas las entidades
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Movimiento> findAll(){
    return ejbMovimientoFacade.findAll();
    }
    //crear entidades
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Movimiento movimiento) throws MalformedURLException, IOException{
        
        URL url = new URL("lsi.no-ip.org:8282/esferopolis/api/ciudadano/"+du);
        
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
        }
        in.close();
        //jsonRespuesta = mandarHttpRequest("GET",url);
        String texto = content.toString();
        JsonObject jsonResultado = 
        
        if(jsonRespuesta.estadoCrediticio <3){
            ejbMovimientoFacade.crear(cliente);
        }else{
            devolver "error, el estado crediticio no es apto";
        }
        
        ejbMovimientoFacade.create(cliente);
        //crear cuenta con el idCliente que recien generamos
    }
    //actualizar entidades
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public void edit(@PathParam("id")long id, Cliente cliente){
    ejbMovimientoFacade.edit(cliente);
    }
  
    
}
*/