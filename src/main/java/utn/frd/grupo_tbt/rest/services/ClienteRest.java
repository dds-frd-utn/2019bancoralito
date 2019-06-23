/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.rest.services;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utn.frd.grupo_tbt.entity.Cliente;
import utn.frd.grupo_tbt.sessions.ClienteFacade;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import javax.json.JsonObject;
import javax.json.Json;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import utn.frd.grupo_tbt.entity.Cuenta;
import utn.frd.grupo_tbt.entity.Movimiento;
import utn.frd.grupo_tbt.sessions.CuentaFacade;
import utn.frd.grupo_tbt.sessions.MovimientoFacade;
import java.io.IOException;
import java.text.ParseException;
import javax.servlet.http.HttpServlet;

//import javax.ws.rs.FormParam;

/**
*
* @author Brian
*/

@Path("/cliente")
public class ClienteRest extends HttpServlet{
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
    public JSONObject stringArrayToJSON(String[] datos) throws JSONException{
        JSONObject json = new JSONObject();
//        int i = 0;
        String key = "";
        String value ="";
        String[] aux = null;
        try{
            for(String d:datos){
                aux = d.split("=>");
                key = aux[0];
                value = aux[1];
                json.put(key,value);
            }
            return json;
        }catch(JSONException e){
            return new JSONObject();
        }
        
    }
    /*
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String du = request.getParameter("du");
        String saldoInicial = request.getParameter("saldoInicial");
        String usuario = request.getParameter("usuario");
        String contrasenia = request.getParameter("contrasenia");
        String email = request.getParameter("email");
        
        out.println("Nombre: "+request.getParameter("du"));
        
        /*String peticionAlta = "{\"du\":"+du+",\"saldoInicial\":"+saldoInicial+",\"usuario\":"
                + usuario+",\"constrasenia\":"+contrasenia+",\"email\":"+email;
        
        String respuesta = this.create(peticionAlta);
        
        response.getWriter().write(respuesta);
        }*/
    
    @EJB
    
    private ClienteFacade ejbClienteFacade;

    //obtener todas las entidades
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cliente> findAll(){
    return ejbClienteFacade.findAll();
    }
    //crear entidades
 /*
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(@FormParam("du") String du,
                        @FormParam("saldoInicial") String saldoInicial,
                        @FormParam("usuario") String usuario,
                        @FormParam("contrasenia") String contrasenia,
                        @FormParam("email") String email
            ) throws JSONException, MalformedURLException, IOException{
                JSONObject jsonPeticionAlta = new JSONObject();

    jsonPeticionAlta.put("du", du);
                jsonPeticionAlta.put("saldo", saldoInicial);
                jsonPeticionAlta.put("usuario", usuario);
                jsonPeticionAlta.put("contrasenia", contrasenia);
                jsonPeticionAlta.put("email", email);
      */

    /**
     *
     * @param peticionAlta
     * @return
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IOException
     */

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public String altaCliente(String peticionAlta) throws JSONException, MalformedURLException, IOException{
        try{
            JSONObject jsonPeticionAlta = new JSONObject(peticionAlta);
                
            String du = jsonPeticionAlta.get("du").toString();
            int duInt = jsonPeticionAlta.getInt("du");
            //si no existe el cliente en nuestro banco
            if(this.findByDu(duInt).getInt("flag_error") == 1){
                String saldoInicial = jsonPeticionAlta.get("saldo").toString();
                String usuario = jsonPeticionAlta.get("usuario").toString();
                String contrasenia = jsonPeticionAlta.get("contrasenia").toString();
                String email = jsonPeticionAlta.get("email").toString();

                String url = "http://lsi.no-ip.org:8282/esferopolis/api/ciudadano/"+du;

                //hardcodeo el parametro vacio
                JSONObject jsonParam = new JSONObject();

                String ciudadanoString = this.enviarHttpRequest(url, "GET", jsonParam);
                JSONObject ciudadano = new JSONObject(ciudadanoString);
                if(ciudadano.getInt("estadoCrediticio")<3){
                    //return (String)"Es apto";
                    //Creo cliente
                    //public Cliente(String contrasenia, String usuario,String nombre,String direccion, int idTipoCliente, int du)
                    String fechaString = ciudadano.getString("fechaNacimiento").substring(0, 10);
                    Cliente nuevoCliente = new Cliente(contrasenia,usuario,ciudadano.getString("nombre"),ciudadano.getString("direccion"),new SimpleDateFormat("yyyy-MM-dd").parse(fechaString),email,1,duInt);
                    ejbClienteFacade.create(nuevoCliente);

                    JsonObject jsonNuevoCliente = this.findByDu(duInt);
                    
                    int idCliente = jsonNuevoCliente.getInt("idCliente");

                    //Creo cuenta en banco central
      //Según la RTA de SERGIO
                    JSONObject jsonIdCuenta = new JSONObject("{\"id\":16}");
                    JSONObject jsonIdCiudadano = new JSONObject("{\"id\":"+du+"}");
                    
                    JSONObject jsonCrearCuenta = new JSONObject();
                    jsonCrearCuenta.put("idBanco", jsonIdCuenta);
                    jsonCrearCuenta.put("idCiudadano", jsonIdCiudadano);
                    jsonCrearCuenta.put("saldo", saldoInicial);
                    
                    /*
                    String urlCrearCuenta = "http://lsi.no-ip.org:8282/esferopolis/api/cuenta";
                    String idCuenta = this.enviarHttpRequest(urlCrearCuenta,"POST",jsonCrearCuenta);
                    //return jsonCrearCuenta.toString()+idCuenta;
                    */
                    int idCuenta = 4;
                    
                    //luego crear cuenta en nuestro banco
//                    Cuenta nuevaCuenta = new Cuenta(idCuenta,1,idCliente);
                    JSONObject nuevaCuenta = new JSONObject().put("idCuenta",idCuenta).put("idCliente", idCliente).put("idTipoCuenta", 1);
                    String cta = this.enviarHttpRequest("http://localhost:8080/tp2019/rest/cuenta","POST",nuevaCuenta);
                    return (String) "Listo";
                    /*
                    //cargo saldo inicial
                    //public Movimiento(int idCuentaOrigen,int idCuentaDestino, Float importe, Date fechaHora, int idTipoMovimiento, int estado)
                    Movimiento movSaldoInicial = new Movimiento(0,Integer.parseInt(idCuenta),Float.parseFloat(saldoInicial),new Date(),1,1);

                    ejbMovFacade.create(movSaldoInicial);
*/
                    
                }else{
                    return (String) "No es apto";

                }
            }else{
                return (String)"El cliente ya existe";
            }
        }catch(NumberFormatException | ParseException | JSONException e){
            return e.getMessage();
        }
    }
    
    
    //actualizar entidades
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public void edit(@PathParam("id")long id, Cliente cliente){
    ejbClienteFacade.edit(cliente);
    }
    //eliminar entidades
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("/{id}")
    public void remove(@PathParam("id")long id){
    ejbClienteFacade.remove( ejbClienteFacade.find(id) );
    }
    //obtener una entidad por id
    @GET
    @Path("/{du}")
    @Produces({MediaType.APPLICATION_JSON})
        public JsonObject findByDu(@PathParam("du")int du){
            try{
                Cliente resultado;
                Query query = ejbClienteFacade.getEntityManager().createNamedQuery("Cliente.findByDu",Cliente.class);
                query.setParameter("du", du);
                
                resultado = (Cliente)query.getSingleResult();
                
                JsonObject jsonResultado = Json.createObjectBuilder()
                                        .add("flag_error", 0)
                                        .add("idCliente",resultado.getIdCliente())
                                        .add("du", resultado.getDu())
                                        .add("nombre", resultado.getNombre())
                                        .add("apellido", resultado.getApellido())
                                        .add("email", resultado.getEmail())
                                        .add("fechaNacimiento", resultado.getFechaNacimiento().toString())
                                        .add("usuario", resultado.getUsuario())
                                        .build();
                return jsonResultado;
            } catch(NoResultException e) {
                 JsonObject jsonError = Json.createObjectBuilder()
                                        .add("flag_error", 1)
                                        .add("error", "No encontrado").build();
                
                return jsonError;
            }
            
            
    }
}
