/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/ 
package utn.frd.grupo_tbt.rest.services;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
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
    public String enviarHttpRequest(String urlParam, String method, JSONObject jsonParam){
        try {
            URL url = new URL(urlParam);

            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            if(jsonParam.length() >0){
                urlConnection.setFixedLengthStreamingMode(jsonParam.toString().getBytes().length);
            }
            urlConnection.setRequestProperty(
                                   "Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            urlConnection.connect();
            if(jsonParam.length() >0){
                OutputStream os;
                os = new BufferedOutputStream(urlConnection.getOutputStream());
                os.write(jsonParam.toString().getBytes());
                os.flush();
            }
            StringBuilder sBuilder;
            InputStream inputStream;
            inputStream= urlConnection.getInputStream();
            
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 5);
            sBuilder = new StringBuilder();
            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            String texto = sBuilder.toString();
            return texto;
        } catch (IOException e) {
           return e.getMessage();
        }
    }
    @EJB
    private MovimientoFacade ejbMovimientoFacade;
    
    //obtener todas las entidades
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Movimiento> findAll(){
        return ejbMovimientoFacade.findAll();
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
    
    @POST
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String Transferencias(
	@FormParam("cuenta_origen") String cuenta_origen,
	@FormParam("cuenta_destino") String cuenta_destino,
	@FormParam("monto") String monto,
	@FormParam("tipo_transferencia") String tipo_transferencia) throws JSONException{

	float valorTransferir = Float.parseFloat(monto);
	int idCuenta = Integer.parseInt(cuenta_origen);
        String respuesta = this.enviarHttpRequest("http://localhost:8080/tp2019/rest/cuenta/"+idCuenta,"GET",new JSONObject());
        
        float saldoDisponible = Float.parseFloat((new JSONObject(respuesta)).getString("saldo"));
        
        
        Date fechaActual = new Date();
        String urlTransferencia = "http://localhost:8080/esferopolis/api/transferencia";
        
        // Averiguo cual es el saldo actual de la cuenta origen.
        
       
        
        // si tipo_transferencia == 2 -> es una transferencia. 
        if ("2".equals(tipo_transferencia)){
            // Verifico si la cuenta de origen tiene el monto suficiente.
            if (saldoDisponible >= valorTransferir){
                
            JSONObject transferencia = new JSONObject();
                transferencia.put("cuentaOrigen", idCuenta);
                transferencia.put("cuentaDestino", cuenta_destino);    
                transferencia.put("importe", valorTransferir);
                transferencia.put("fechaInicio", fechaActual );
                transferencia.put("fechaFin","");
                transferencia.put("estado","");
                transferencia.put("idTipoMovimiento","2");
                
                
            }               
        }
            
        // si tipo_transferencia == 3 -> es una venta (hay impuesto). 
        if ("3".equals(tipo_transferencia)){
                
                
            
            String devolucion;
            double impuesto;
            
            // paso a la variable String devolucion el contenido del json impuesto
            devolucion = enviarHttpRequest("http://lsi.no-ip.org:8282/esferopolis/api/impuesto", "GET", new JSONObject () ); 
            
            // convierto el resultado en un JSON
            JSONObject resultado = new JSONObject(devolucion);
            
            // del JSON obtengo el valor impuesto en forma de double
            String impuestoString = (String) resultado.get("porcentaje");
            impuesto = Double.parseDouble(impuestoString);
            
            if (saldoDisponible < (valorTransferir * (impuesto/100 + 1))){
             } else {
                JSONObject transferencia = new JSONObject();
                transferencia.put("cuentaOrigen", idCuenta);
                transferencia.put("cuentaDestino", cuenta_destino);    
                transferencia.put("importe", valorTransferir);
                transferencia.put("fechaInicio", fechaActual );
                transferencia.put("fechaFin","");
                transferencia.put("estado","");
                transferencia.put("idTipoMovimiento","3"); 
                
                ejbMovimientoFacade.create(transferencia);
                
                
                double impuestoTotal;
                impuestoTotal = (valorTransferir * (impuesto/100));
                
                JSONObject impuestoTransferencia = new JSONObject();
                transferencia.put("cuentaOrigen", idCuenta);
                transferencia.put("cuentaDestino", cuenta_destino);    
                transferencia.put("importe", impuestoTotal);
                transferencia.put("fechaInicio", fechaActual );
                transferencia.put("fechaFin","");
                transferencia.put("estado","");
                transferencia.put("idTipoMovimiento","5");
                
                 ejbMovimientoFacade.create(impuestoTransferencia);
            }
        
        }
            // envio el json con la transferencia al banco central.
            
            
        }
       
        
        
        
        
        
        
        
        
        
        
        
        

	


}
}

