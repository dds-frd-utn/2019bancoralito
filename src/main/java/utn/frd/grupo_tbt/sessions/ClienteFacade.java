/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.sessions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utn.frd.grupo_tbt.entity.Cliente;

/**
 *
 * @author Brian
 */
@Stateless
public class ClienteFacade extends AbstractFacade<Cliente> {

    @PersistenceContext(unitName = "utn.frd_grupo_tbt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }
    /*
    public String findByDu(int du){
        javax.persistence.Query q = getEntityManager().createNamedQuery("findByDu",Cliente.class);
        q.setParameter("du", du);
        Object result = q.getSingleResult();
        return result.toString();
    }
*/
}
