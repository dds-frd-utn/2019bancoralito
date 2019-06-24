/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/ 
package utn.frd.grupo_tbt.rest.services;


import java.util.List;
import javax.ejb.EJB;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import utn.frd.grupo_tbt.entity.Cuenta;
import utn.frd.grupo_tbt.sessions.CuentaFacade;

/**
 *
 * @author Brian
*/
@Path("/cuenta")
public class CuentaRest {
    @EJB
    private CuentaFacade ejbCuentaFacade;
    
    //obtener todas las entidades
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cuenta> findAll(){
        return ejbCuentaFacade.findAll();
    }
    
    //crear entidades
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void createCuenta(Cuenta cuenta) throws JSONException{
        
        /*
        JSONObject cuenta = new JSONObject(cuentaString);
        int idCuenta = cuenta.getInt("idCuenta");
        int idCliente = cuenta.getInt("idCliente");
        int idTipoCuenta = cuenta.getInt("idTipoCuenta");
        */
        ejbCuentaFacade.create(cuenta);
        
        
    }
    @GET
    @Path("/{idCuenta}")
    @Produces({MediaType.TEXT_PLAIN})
    public String infoCuenta(@PathParam("idCuenta") int id) throws JSONException{
        try{
            StoredProcedureQuery storedProcedure = ejbCuentaFacade.getEntityManager().createNamedStoredProcedureQuery("Cuenta.saldoCuenta");
            storedProcedure.setParameter("idCuenta", id);

            Cuenta unaCuenta = (Cuenta)storedProcedure.getSingleResult();
            float saldoActual = unaCuenta.getSaldo();

            JSONObject jsonCuenta = new JSONObject()
                    .put("idCuenta", unaCuenta.getIdCuenta())
                    .put("idCliente", unaCuenta.getIdCliente())
                    .put("saldo", saldoActual);
            return jsonCuenta.toString();
/*
            return unaCuenta.toString();
*/
        }catch(Exception e){
            return e.getMessage();
        }
    }

}