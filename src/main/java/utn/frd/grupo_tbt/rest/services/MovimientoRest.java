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
import java.text.SimpleDateFormat;
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
    @Produces({MediaType.TEXT_PLAIN})
    public String transferenciaspendientes() throws JSONException {
        
        String resultado = this.findByEstado('1');
        
        //Acomodar los campos del JSON que se envía a BC
        //Identificar cada elemento de la lista y actualizarlo en la DB, preferentemente despues de enviarlos
        
        //JSONArray jsonArray = new JSONArray(resultado);
        //JSONArray jsonResultado = new JSONArray();

/*        for (int i = 0; i < resultado.length(); i++) {
            
            //Toma cada movimiento pendiente de envio
            JSONObject object = resultado.getJSONObject(i);
            
            //Preparo para persistir en DB
            Integer idMovimiento = (Integer) object.get("idMovimiento");
            Integer idCuentaOrigen = (Integer) object.get("idCuentaOrigen");
            Integer idCuentaDestino = (Integer) object.get("idCuentaDestino");
            Float Importe = (Float) object.get("importe");
                    
          /*  Movimiento actualizoEstado;
            actualizoEstado = new Movimiento(
                    idMovimiento,
                    idCuentaOrigen,
                    idCuentaDestino,
                    Importe,
                    object.get("fechaHora"),
                    2
            );
            
            //Persisto en DB
            ejbMovimientoFacade.edit(actualizoEstado); 
            
            //Preparo para enviar respuesta a BC
            
                        JSONObject jsonElement = new JSONObject()
                        .put("cuentaOrigen", object.get("idCuentaOrigen") )
                        .put("cuentaDestino", object.get("idCuentaDestino"))
                        .put("importe", object.get("importe"))
                        .put("fechaInicio", object.get("fechaHora"))
                        .put("fechaFin", "")
                        .put("estado", "PENDIENTE")
            ;
                        
            jsonResultado.put(jsonElement);
        }*/
        
        return resultado;
    }
    
    @Path("/estado/{estado}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String findByEstado(@PathParam("estado") int estado) throws JSONException{
        try{
            Query query = ejbMovimientoFacade.getEntityManager().createQuery("SELECT c from Movimiento c WHERE c.estado = "+estado +" order by c.fechaHora DESC");
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
    
    @POST
    @Path("/realizar")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.APPLICATION_JSON})
    public String registrarMovimiento(String peticionInicial) throws JSONException{
        JSONObject jsonPeticion = new JSONObject(peticionInicial);
        int cuenta_origen = jsonPeticion.getInt("cuenta_origen");
        int cuenta_destino = jsonPeticion.getInt("cuenta_destino");
        int tipo_movimiento = jsonPeticion.getInt("tipo_movimiento");
        float monto = Float.parseFloat(jsonPeticion.getString("monto"));
        //Declaro variables
        float porcImpuesto = 0;
        float valorImpuesto = 0;
        String str = "";
        
        JSONObject jsonDevolver = new JSONObject();
        if(monto > 0){
            try{
                if(tipo_movimiento == 2 || tipo_movimiento == 1){
                    //Si es compra-venta, consulto el valor del impuesto
                    if(tipo_movimiento == 2){
                        
                        String respuestaImpuesto = this.enviarHttpRequest("http://lsi.no-ip.org:8282/esferopolis/api/impuesto","GET",new JSONObject());
                        porcImpuesto = Float.parseFloat((new JSONObject(respuestaImpuesto)).getString("porcentaje"));
                        valorImpuesto = monto * porcImpuesto / 100;
                    }   
                    // Averiguo cual es el saldo actual de la cuenta origen.
                    String respuesta = this.enviarHttpRequest("http://localhost:8080/tp2019/rest/cuenta/"+cuenta_origen,"GET",new JSONObject());
                    float saldoDisponible = Float.parseFloat((new JSONObject(respuesta)).getString("saldo"));
                    
                    float importeFinal = monto + valorImpuesto;
                    int importeFinalInt = (int)importeFinal;
                    if (saldoDisponible >= importeFinal){
                        //Consulto si la cuenta destino pertenece a mi banco
                        String cuentaDestino = this.enviarHttpRequest("http://localhost:8080/tp2019/rest/cuenta/"+cuenta_destino,"GET",new JSONObject());
                        String flagErrorCtaDestino = new JSONObject(cuentaDestino).getString("flag_error");
                        //0:la encontró, 1:no la encontró (es de otro banco)

                        //si es interbancaria realizo el movimiento e informo
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");  
                        String fechaActual= formatter.format(date);  
                        if("0".equals(flagErrorCtaDestino)){
                            //realizo el movimiento
                            //constructor: public Movimiento(int idCuentaOrigen,int idCuentaDestino, Float importe, Date fechaHora, int idTipoMovimiento,int estado) 
                            //nota estado movimiento en nuestra db 1:pendiente 2:finalizada
                            Movimiento mov = new Movimiento(cuenta_origen,cuenta_destino,importeFinal,date,tipo_movimiento,2);
                            ejbMovimientoFacade.create(mov);

                            // envio el json con la transferencia al banco central.
                            JSONObject transferencia = new JSONObject();
                                transferencia.put("cuentaOrigen", cuenta_origen);
                                transferencia.put("cuentaDestino", cuenta_destino);    
                                transferencia.put("importe", importeFinalInt);
                                transferencia.put("fechaInicio",fechaActual);
                                transferencia.put("fechaFin",fechaActual);
                                transferencia.put("estado",0);
                            String respuestaTrans = this.enviarHttpRequest("http://lsi.no-ip.org:8282/esferopolis/api/transferencia","POST",transferencia);
                            if(respuestaTrans.length() != 0){
                                jsonDevolver.put("flag_error", "1");
                                jsonDevolver.put("error", "Ocurrió un error al informar la transferencia. " +transferencia.toString()+ respuestaTrans);
                            }else{
                                jsonDevolver.put("flag_error", "0");
                                jsonDevolver.put("mensaje", "Ok");
                            }
                        }else{
                            //si la cta destino es de otro banco, solo registro en mi db como pendiente
                            Movimiento mov = new Movimiento(cuenta_origen,cuenta_destino,importeFinal,date,tipo_movimiento,1);
                            ejbMovimientoFacade.create(mov);
                        }
                    }else{
                        jsonDevolver.put("flag_error", "1");
                        jsonDevolver.put("error", "No tienen saldo suficiente para realizar la transacción.");
                    }
                }
                return jsonDevolver.toString();
            }catch(Exception e){
                jsonDevolver.put("flag_error", "1");
                jsonDevolver.put("error", e.getMessage());
                return jsonDevolver.toString();
            }
        
        }else{
            jsonDevolver.put("flag_error", "1");
            jsonDevolver.put("error", "El importe no puede ser negativo");
            return jsonDevolver.toString();        }
    }
}

