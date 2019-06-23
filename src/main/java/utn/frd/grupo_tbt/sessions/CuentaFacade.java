/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.sessions;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import utn.frd.grupo_tbt.entity.Cuenta;

/**
 *
 * @author Brian
 */
@Stateless
public class CuentaFacade extends AbstractFacade<Cuenta> {

    @PersistenceContext(unitName = "utn.frd_grupo_tbt_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
<<<<<<< HEAD
    public EntityManager getEntityManager() {
=======
    protected EntityManager getEntityManager() {
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
        return em;
    }

    public CuentaFacade() {
        super(Cuenta.class);
    }
    
}
