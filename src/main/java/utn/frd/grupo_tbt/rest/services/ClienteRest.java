/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.rest.services;
import java.math.BigDecimal;
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
import javax.json.JsonString;
import javax.json.Json;
/*
import javax.persistence.EntityManager;
//import org.json.simple.JSONObject;
*/
/**
*
* @author Brian
*/

@Path("/cliente")
public class ClienteRest {
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
    public void create(Cliente cliente){
    ejbClienteFacade.create(cliente);
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
