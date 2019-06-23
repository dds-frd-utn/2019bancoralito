/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utn.frd.grupo_tbt.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
<<<<<<< HEAD
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
=======
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Brian
 */
@Entity
@Table(name = "cuenta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuenta.findAll", query = "SELECT c FROM Cuenta c")
    , @NamedQuery(name = "Cuenta.findByIdCuenta", query = "SELECT c FROM Cuenta c WHERE c.idCuenta = :idCuenta")
    , @NamedQuery(name = "Cuenta.findByIdTipoCuenta", query = "SELECT c FROM Cuenta c WHERE c.idTipoCuenta = :idTipoCuenta")
<<<<<<< HEAD
    , @NamedQuery(name = "Cuenta.findByIdCliente", query = "SELECT c FROM Cuenta c WHERE c.idCliente = :idCliente")
    , @NamedQuery(name = "Cuenta.saldoCuenta", query = "SELECT c AS saldo FROM (SELECT f_saldoCuenta(:idCuenta)) c")
    })
=======
    , @NamedQuery(name = "Cuenta.findByIdCliente", query = "SELECT c FROM Cuenta c WHERE c.idCliente = :idCliente")})
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
<<<<<<< HEAD
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
=======
    @Basic(optional = false)
    @NotNull
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
    @Column(name = "idCuenta")
    private int idCuenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoCuenta")
    private int idTipoCuenta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCliente")
    private int idCliente;

    public Cuenta() {
    }

<<<<<<< HEAD
    public Cuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Cuenta(int idTipoCuenta, int idCliente) {
=======
    public Cuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Cuenta(int idCuenta, int idTipoCuenta, int idCliente) {
        this.idCuenta = idCuenta;
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
        this.idTipoCuenta = idTipoCuenta;
        this.idCliente = idCliente;
    }

<<<<<<< HEAD
    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
=======
    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
        this.idCuenta = idCuenta;
    }

    public int getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public void setIdTipoCuenta(int idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

<<<<<<< HEAD
=======
/*
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuenta != null ? idCuenta.hashCode() : 0);
        return hash;
    }
<<<<<<< HEAD

=======
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuenta)) {
            return false;
        }
        Cuenta other = (Cuenta) object;
        if ((this.idCuenta == null && other.idCuenta != null) || (this.idCuenta != null && !this.idCuenta.equals(other.idCuenta))) {
            return false;
        }
        return true;
    }
<<<<<<< HEAD

=======
*/
>>>>>>> 5e073812aef0ebbe71f624337554064eef37b204
    @Override
    public String toString() {
        return "utn.frd.grupo_tbt.entity.Cuenta[ idCuenta=" + idCuenta + " ]";
    }
    
}
