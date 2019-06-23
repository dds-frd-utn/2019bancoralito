<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/ 
package utn.frd.grupo_tbt.rest.services;


import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import utn.frd.grupo_tbt.entity.Cliente;
import utn.frd.grupo_tbt.entity.Cuenta;
import utn.frd.grupo_tbt.entity.Movimiento;
import utn.frd.grupo_tbt.sessions.CuentaFacade;
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

@EJB
    
    private CuentaFacade ejbCuentaFacade;

    
@Path("/movimientos")
@POST
@Produces({MediaType.TEXT_PLAIN})
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public String Transferencias(
	@FormParam("cuenta_origen") String cuenta_origen,
	@FormParam("cuenta_destino") String cuenta_destino,
	@FormParam("monto") String monto,
	@FormParam("tipo_transferencia") String tipo_transferencia) throws JSONException{

	
        
	int valorTransferir = Integer.parseInt(monto);
	int idCuenta = Integer.parseInt(cuenta_origen);
        int saldoDisponible;
        
        
        Date fechaActual = new Date();
        String urlTransferencia = "http://localhost:8080/esferopolis/api/transferencia";
        
        // Averiguo cual es el saldo actual de la cuenta origen.
        Query query = ejbCuentaFacade.getEntityManager().createNamedQuery("Cuenta.saldoCuenta",Cuenta.class);
        query.setParameter("idCuenta", idCuenta); 
        List results = query.getResultList();      
        Object foundEntity = results.get(0);       
        saldoDisponible = (Integer)foundEntity;
        
        
        
        // Verifico si la cuenta de origen tiene el monto suficiente.
        if (saldoDisponible > valorTransferir){
                        
            // si tipo_transferencia == 1 -> es una transferencia. 
            if (tipo_transferencia == "1"){
                                               
            }
            
            // si tipo_transferencia == 2 -> es una venta (hay impuesto). 
            if (tipo_transferencia == "2"){
                
            }    
            
            // envio el json con la transferencia al banco central.
            JSONObject transferencia = new JSONObject();
                transferencia.put("cuentaOrigen", idCuenta);
                transferencia.put("cuentaDestino", cuenta_destino);    
                transferencia.put("importe", valorTransferir);
                transferencia.put("fechaInicio", fechaActual );
                transferencia.put("fechaFin","");
                transferencia.put("estado","");
            
         
            
            
            
                
        }
        return null;
        
        
        
        
        
        
        
        
        
        
        
        

	


}
}

=======
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
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
