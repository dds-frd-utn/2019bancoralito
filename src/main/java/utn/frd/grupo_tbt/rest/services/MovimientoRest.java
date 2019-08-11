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
    
    @POST
    @Path("/realizar")
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.TEXT_PLAIN})
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
        if(monto > 0){
            try{
                str += "valor > 0";
                if(tipo_movimiento == 2 || tipo_movimiento == 1){
                    str += " | mov 2 o 1";
                    //Si es compra-venta, consulto el valor del impuesto
                    if(tipo_movimiento == 2){
                        str += " | mov es 2";
                        String respuestaImpuesto = this.enviarHttpRequest("http://lsi.no-ip.org:8282/esferopolis/api/impuesto","GET",new JSONObject());
                        porcImpuesto = Float.parseFloat((new JSONObject(respuestaImpuesto)).getString("porcentaje"));
                        valorImpuesto = monto * porcImpuesto / 100;
                    }   
                    // Averiguo cual es el saldo actual de la cuenta origen.
                    String respuesta = this.enviarHttpRequest("http://localhost:8080/tp2019/rest/cuenta/"+cuenta_origen,"GET",new JSONObject());
                    float saldoDisponible = Float.parseFloat((new JSONObject(respuesta)).getString("saldo"));
                    str += "| saldo " +(new JSONObject(respuesta)).getString("saldo");
                    float importeFinal = monto + valorImpuesto;
                    if (saldoDisponible >= importeFinal){
                        str += " | entro if saldo disponible";
                        //Consulto si la cuenta destino pertenece a mi banco
                        String cuentaDestino = this.enviarHttpRequest("http://localhost:8080/tp2019/rest/cuenta/"+cuenta_destino,"GET",new JSONObject());
                        String flagErrorCtaDestino = new JSONObject(cuentaDestino).getString("flag_error");
                        //0:la encontró, 1:no la encontró (es de otro banco)

                        //si es interbancaria realizo el movimiento e informo
                        Date fechaActual = new Date();
                        if("0".equals(flagErrorCtaDestino)){
                            str += " | es cuenta interbancaria";
                            //realizo el movimiento
                            //constructor: public Movimiento(int idCuentaOrigen,int idCuentaDestino, Float importe, Date fechaHora, int idTipoMovimiento,int estado) 
                            //nota estado movimiento en nuestra db 1:pendiente 2:finalizada
                            Movimiento mov = new Movimiento(cuenta_origen,cuenta_destino,importeFinal,fechaActual,tipo_movimiento,2);
                            ejbMovimientoFacade.create(mov);

                            // envio el json con la transferencia al banco central.
                            JSONObject transferencia = new JSONObject();
                                transferencia.put("cuentaOrigen", cuenta_origen);
                                transferencia.put("cuentaDestino", cuenta_destino);    
                                transferencia.put("importe", importeFinal);
                                transferencia.put("fechaInicio", fechaActual );
                                transferencia.put("fechaFin","");
                                transferencia.put("estado","COMPLETA");
                            String respuestaTrans = this.enviarHttpRequest("http://lsi.no-ip.org:8282/esferopolis/api/transferencia","POST",transferencia);
                            str += " | rta trans "+ respuestaTrans;

                        }else{
                            str += " | es a otro banco";
                            //si la cta destino es de otro banco, solo registro en mi db como pendiente
                            Movimiento mov = new Movimiento(cuenta_origen,cuenta_destino,importeFinal,fechaActual,tipo_movimiento,1);
                            ejbMovimientoFacade.create(mov);

                        }
                    }
                }
                return str;
            }catch(Exception e){
                return e.getMessage();
            }
        
        }else{
            return String.valueOf(monto);
        }
    }
}

