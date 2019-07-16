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
import org.json.JSONArray;
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
  
    @Path("/ultimosMovimientos/{idCuenta}/{cantidad}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String listaMovimientos(@PathParam("idCuenta") int idCuenta,@PathParam("cantidad") int cantidad) throws JSONException{
        try{
            Query query = ejbMovimientoFacade.getEntityManager().createQuery("SELECT c from Movimiento c WHERE c.idCuentaOrigen = "+idCuenta +" OR c.idCuentaDestino ="+idCuenta+" order by c.fechaHora DESC");
            List<Movimiento> listMov = query.getResultList();
            
            JSONObject jsonArray = new JSONObject();
            JSONObject jsonElement;
            String movString;
            
            for(Movimiento unMov : listMov){
                jsonElement = new JSONObject()
                        .put("idMovimiento", unMov.getIdMovimiento())
                        .put("idCuentaOrigen", unMov.getIdCuentaOrigen())
                        .put("idCuentaDestino", unMov.getIdCuentaDestino())
                        .put("importe", unMov.getImporte())
                        .put("fechaHora", unMov.getFechaHora())
                        .put("estado", unMov.getEstado())
                        ;
                jsonArray.put(String.valueOf(unMov.getIdMovimiento()),jsonElement);
            }
            
            return jsonArray.toString();

        }catch(JSONException e){
            return e.getMessage();
        }
    }

    @Path("/transferenciaspendientes")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String transferenciaspendientes() throws JSONException {
        
        String resultado = this.findByEstado(1);
        
        //Identificar cada elemento de la lista y actualizarlo en la DB, preferentemente despues de enviarlos
        //Acomodar los campos del JSON que se envía a BC
                
        JSONArray jsonArray = new JSONArray(resultado);
        JSONArray jsonResultado = new JSONArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            
            //Toma cada movimiento pendiente de envio
            JSONObject object = jsonArray.getJSONObject(i);
            
            //Preparo para persistir en DB
            Integer idMovimiento = (Integer) object.get("idMovimiento");
            Integer idCuentaOrigen = (Integer) object.get("idCuentaOrigen");
            Integer idCuentaDestino = (Integer) object.get("idCuentaDestino");
            Integer Importe = (Integer) object.get("Importe");
                    
            Movimiento actualizoEstado = new Movimiento(
                    idMovimiento,
                    idCuentaOrigen,
                    idCuentaDestino,
                    Importe,
                    object.get("fechaHora"),
                    2
            );
            
            //Persisto en DB
            ejbMovimientoFacade.edit(actualizoEstado);
            
            //Preparo para enviar respuesta
            
                        JSONObject jsonElement = new JSONObject()
                        .put("cuentaOrigen", object.get("idCuentaOrigen") )
                        .put("cuentaDestino", object.get("idCuentaDestino"))
                        .put("importe", object.get("importe"))
                        .put("fechaInicio", object.get("fechaHora"))
                        .put("fechaFin", "")
                        .put("estado", "PENDIENTE")
            ;
                        
            jsonResultado.put(jsonElement);
        }
        
        return jsonResultado.toString();
    }
    
    //@Path("/estado/{estado}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //private String findByEstado(@PathParam("estado") int  estado) {
    private String findByEstado(int  estado) {
        try{
            Query query = ejbMovimientoFacade.getEntityManager().createQuery("SELECT c FROM Movimiento c WHERE c.estado = "+estado);
            List<Movimiento> listMov = query.getResultList();
            
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonElement;
            String movString;
            
            for(Movimiento unMov : listMov){
                jsonElement = new JSONObject()
                        .put("idMovimiento", unMov.getIdMovimiento())
                        .put("idCuentaOrigen", unMov.getIdCuentaOrigen())
                        .put("idCuentaDestino", unMov.getIdCuentaDestino())
                        .put("importe", unMov.getImporte())
                        .put("fechaHora", unMov.getFechaHora())
                        .put("estado", unMov.getEstado())
                        ;
                jsonArray.put(jsonElement);
            }
            
            return jsonArray.toString();

        }catch(JSONException e){
            return e.getMessage();
        }
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
        
        // Verifico si la cuenta de origen tiene el monto suficiente.
        if (saldoDisponible >= valorTransferir){
                        
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