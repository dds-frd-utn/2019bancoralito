/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/ 
package utn.frd.grupo_tbt.rest.services;


import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;

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
*/
@Path("/movimiento")
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
    public void create(Movimiento movimiento){
        //Registrar transferencia
        
    }
    //actualizar entidades
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public void edit(@PathParam("id")int id){
        //Para cuando pasemos un movimiento de pendiente a realizado
    }
  
    @Path("/ultimos/{cantidad}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Movimiento> findUltimos(@PathParam("cantidad")int cantidad){
        //la idea es que consulten los ultimos n movimientos
        return ejbMovimientoFacade.findAll(); //en vez de esto, armar una namedquery como en clienteRest y traer los ultimos segun la variable cantidad
    }
}