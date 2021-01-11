package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Query;

import com.altima.springboot.app.models.entity.ProduccionCalendario;
import com.altima.springboot.app.repository.ProduccionCalendarioRepository;

@Service
public class ProduccionCalendarioServiceImpl implements IProduccionCalendarioService {

    @Autowired
    private ProduccionCalendarioRepository repository;
    
    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public Integer validarAnio(String anio) {
        String re = em.createNativeQuery("" + "SELECT\n" + "COUNT( fecha ) \n" + "FROM\n"
                + "alt_produccion_calendario \n" + "WHERE\n" + "YEAR ( fecha )= " + anio).getSingleResult().toString();

        return Integer.parseInt(re);

    }

    @Override
    @Transactional
    public void crearCalendario(String fecha_incial, String fecha_final, String creado_por,String fecha_creacion) {

        Query query = em.createNativeQuery("call proc_pa_sumar_fechas('" + fecha_incial + "','" + fecha_final + "', '"+creado_por+"','"+fecha_creacion+"');");
        query.executeUpdate();

    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Object[]> mostrar_calendario(String fechaInicio, String fehaFin) {
        List<Object[]> re = em.createNativeQuery("" + 
            "SELECT\r\n" + 
                "id_calendario_fecha,\r\n" + 
                "fecha,\r\n"+ 
                "IFNULL( hombre, '00.00' ) AS hombre,\r\n" +
                "IFNULL( adeudo, '00.00' ) AS adeudo,\r\n"+
                "IFNULL( ROUND(( hombre - adeudo ),2), '' ) AS habiles  \r\n" + 
                "FROM\r\n" + "alt_produccion_calendario \r\n"
                + "WHERE\r\n" + "fecha BETWEEN '" + fechaInicio + "' AND '" + fehaFin + "'").getResultList();
        return re;
    }

    @Override
	@Transactional
    public ProduccionCalendario findOne(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
	@Transactional
    public void save(ProduccionCalendario calendario) {
        repository.save(calendario);

    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public String restarHoras(String fechaInicio, String fehaFin) {
        String re = String.valueOf(em.createNativeQuery("SELECT ROUND('"+fechaInicio+"'-'"+fehaFin+"',2)").getSingleResult());
        return re;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Object[]> mostrar_fechas_disponibles_folio(String folio) {
        List<Object[]> re = em.createNativeQuery(""+
            "SELECT\r\n"+
                "id_calendario_fecha,\r\n"+
                "fecha\r\n"+
            "FROM\r\n"+
                "alt_produccion_calendario\r\n"+
            "WHERE\r\n"+
                "hombre IS NOT NULL\r\n"+
                "AND (\r\n"+
                "hombre - adeudo >= ( SELECT ROUND( ( SUM( cp.tiempo )* 0.0166667 ), 2 )  FROM alt_comercial_coordinado_prenda AS cp WHERE cp.folio = '"+folio+"' ))").getResultList();
        return re;
    }

}
