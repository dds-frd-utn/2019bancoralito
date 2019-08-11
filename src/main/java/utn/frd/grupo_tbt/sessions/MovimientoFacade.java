/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.sessions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.json.JSONObject;
import utn.frd.grupo_tbt.entity.Movimiento;

/**
 *
 * @author Brian
 */
@Stateless
public class MovimientoFacade extends AbstractFacade<Movimiento> {

    @PersistenceContext(unitName = "utn.frd_grupo_tbt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public MovimientoFacade() {
        super(Movimiento.class);
    }

    public void create(JSONObject transferencia) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
