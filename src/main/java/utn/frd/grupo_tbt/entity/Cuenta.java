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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    @NamedQuery(name = "Cuenta.findAll", query = "SELECT c.idCuenta,c.idTipoCuenta,c.idCliente FROM Cuenta c")
    , @NamedQuery(name = "Cuenta.findByIdCuenta", query = "SELECT c FROM Cuenta c WHERE c.idCuenta = :idCuenta")
    , @NamedQuery(name = "Cuenta.findByIdTipoCuenta", query = "SELECT c FROM Cuenta c WHERE c.idTipoCuenta = :idTipoCuenta")
    , @NamedQuery(name = "Cuenta.findByIdCliente", query = "SELECT c FROM Cuenta c WHERE c.idCliente = :idCliente")
    })
@NamedStoredProcedureQuery(
        name="Cuenta.saldoCuenta",
        procedureName="sp_saldoCuenta",
        resultClasses = { Cuenta.class },
        parameters={
            @StoredProcedureParameter(name="idCuenta", type=Integer.class, mode=ParameterMode.IN),
            @StoredProcedureParameter(name="p_saldo", type=Float.class, mode=ParameterMode.OUT)
        }
    )

public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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


    public Cuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Cuenta(int idCuenta, int idTipoCuenta, int idCliente) {
        this.idCuenta = idCuenta;
        this.idTipoCuenta = idTipoCuenta;
        this.idCliente = idCliente;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
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

/*
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuenta != null ? idCuenta.hashCode() : 0);
        return hash;
    }
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

*/
    @Override
    public String toString() {
        return "utn.frd.grupo_tbt.entity.Cuenta[ idCuenta=" + idCuenta + " ]";
    }
    
}
