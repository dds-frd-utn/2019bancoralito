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
import org.json.JSONException;
import org.json.JSONObject;

/**
*
* @author Brian
*/

@Path("/cliente")
public class ClienteRest {
    public String enviarHttpRequest(String urlParam, String method, JSONObject jsonParam){
        try {
            URL url = new URL(urlParam);

            HttpURLConnection urlConnection = null;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(jsonParam.toString().getBytes().length);

            urlConnection.setRequestProperty(
                                   "Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            urlConnection.connect();


            OutputStream os;
            os = new BufferedOutputStream(urlConnection.getOutputStream());
            os.write(jsonParam.toString().getBytes());
            os.flush();


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
    
    private ClienteFacade ejbClienteFacade;
    //obtener todas las entidades
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Cliente> findAll(){
    return ejbClienteFacade.findAll();
    }
    //crear entidades
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public String create(String cliente) throws JSONException, MalformedURLException, IOException{
        try{
            JSONObject jsonCliente = new JSONObject(cliente);
            String du = jsonCliente.get("du").toString();
            String url = "http://lsi.no-ip.org:8282/esferopolis/api/ciudadano/"+du;

            //hardcodeo el parametro vacio
            JSONObject jsonParam = new JSONObject();

            return this.enviarHttpRequest(url, "GET", jsonParam);
//            return (String)"Hola";
        }catch(Exception e){
            return e.getMessage();
        }
        /*
        
        if((int)jsonRespuesta.get("estadoCrediticio") <3){
            //ejbClienteFacade.crear(cliente);
            JsonObject jsonError = Json.createObjectBuilder()
                                        .add("flag_error", 0)
                                        .add("mensaje", "Es apto").build();
                
            return jsonError;
        }else{
            //devolver "error, el estado crediticio no es apto";
            JsonObject jsonError = Json.createObjectBuilder()
                                        .add("flag_error", 1)
                                        .add("error", "No es apto").build();
                
            return jsonError;
        }
        */
        //ejbClienteFacade.create(cliente);
        //crear cuenta con el idCliente que recien generamos
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
