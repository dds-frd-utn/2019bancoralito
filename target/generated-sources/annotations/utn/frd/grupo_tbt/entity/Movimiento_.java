package utn.frd.grupo_tbt.entity;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-06-20T19:46:31")
@StaticMetamodel(Movimiento.class)
public class Movimiento_ { 

    public static volatile SingularAttribute<Movimiento, Integer> estado;
    public static volatile SingularAttribute<Movimiento, Integer> idCuentaDestino;
    public static volatile SingularAttribute<Movimiento, Integer> idMovimiento;
    public static volatile SingularAttribute<Movimiento, Date> fechaHora;
    public static volatile SingularAttribute<Movimiento, Integer> idTipoMovimiento;
    public static volatile SingularAttribute<Movimiento, Integer> idCuentaOrigen;
    public static volatile SingularAttribute<Movimiento, Float> importe;

}