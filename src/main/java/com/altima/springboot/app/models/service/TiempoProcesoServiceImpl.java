package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.models.entity.ProduccionFechaExplosionProceso;
import com.altima.springboot.app.repository.ProduccionFechaExplosionProcesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TiempoProcesoServiceImpl implements ITiempoProcesoService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProduccionFechaExplosionProcesoRepository repository;

    @SuppressWarnings("unchecked")
	@Override
	@Transactional
    public List<Object[]> view(Long idProceso,String programa) {
        // nueva query holaa
		List<Object[]> re = null;
		re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	explosionP.secuencia_proceso,\r\n" + 
				"	disLook.nombre_lookup AS prenda,\r\n" + 
				"	prenda.descripcion_prenda,\r\n" + 
				"	tela.estampado,\r\n" + 
				"	(\r\n" + 
				"	SELECT\r\n" + 
				"		(\r\n" + 
				"		SUM( conse.cantidad ) + SUM( conse.cantidad_especial )) \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_concetrado_prenda AS conse,\r\n" + 
				"		alt_produccion_explosion_procesos AS explosionP2 \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND conse.id_coordinado_prenda = explosionP2.coordinado \r\n" + 
				"		AND explosionP2.secuencia_proceso = explosionP.secuencia_proceso \r\n" + 
				"		AND explosionP2.clave_proceso = explosionP.clave_proceso \r\n" + 
				"	) AS Confeccion,\r\n" + 
				"	(\r\n" + 
				"	SELECT\r\n" + 
				"		COUNT( cp.id_coordinado_prenda ) \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_coordinado_prenda AS cp,\r\n" + 
				"		alt_produccion_explosion_procesos AS explosionP2 \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND explosionP2.secuencia_proceso = explosionP.secuencia_proceso \r\n" + 
				"		AND explosionP2.clave_proceso = explosionP.clave_proceso \r\n" + 
				"		AND cp.id_coordinado_prenda = explosionP2.coordinado \r\n" + 
				"	) AS OP,\r\n" + 
				"	(\r\n" + 
				"	SELECT\r\n" + 
				"		COUNT( DISTINCT tallas.id_talla ) \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_concentrado_tallas AS tallas,\r\n" + 
				"		alt_comercial_coordinado_prenda AS cp,\r\n" + 
				"		alt_produccion_explosion_procesos AS explosionP2 \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND explosionP2.secuencia_proceso = explosionP.secuencia_proceso \r\n" + 
				"		AND explosionP2.clave_proceso = explosionP.clave_proceso \r\n" + 
				"		AND cp.id_coordinado_prenda = explosionP2.coordinado \r\n" + 
				"		AND tallas.id_prenda_cliente = cp.id_coordinado_prenda \r\n" + 
				"	) AS tallas,\r\n" + 
				"	(\r\n" + 
				"	SELECT\r\n" + 
				"		ifnull(max(explosionP2.tiempo_general),'Sin tiempo') \r\n" + 
				"	FROM\r\n" + 
				"		alt_produccion_explosion_procesos AS explosionP2 \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND explosionP2.secuencia_proceso = explosionP.secuencia_proceso \r\n" + 
				"		AND explosionP2.clave_proceso = explosionP.clave_proceso \r\n" + 
				"	) AS tiempo,\r\n" + 
				"	explosionP.fecha_proceso,\r\n" + 
				"	(\r\n" + 
				"	SELECT\r\n" + 
				"		IFNULL(max( explosionP2.tiempo_general),- 1 )  \r\n" + 
				"	FROM\r\n" + 
				"		alt_produccion_explosion_procesos AS explosionP2 \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND explosionP2.secuencia_proceso = explosionP.secuencia_proceso \r\n" + 
				"		AND explosionP2.clave_proceso = explosionP.clave_proceso \r\n" + 
				"	) AS validacion,min(explosionP.id_explosion_procesos) \r\n" + 
				"FROM\r\n" + 
				"	alt_produccion_explosion_procesos AS explosionP\r\n" + 
				"	INNER JOIN alt_comercial_pedido_informacion pedInfo ON explosionP.id_pedido = pedInfo.id_pedido_informacion\r\n" + 
				"	INNER JOIN alt_comercial_cliente cliente ON pedInfo.id_empresa = cliente.id_cliente\r\n" + 
				"	INNER JOIN alt_disenio_prenda prenda ON explosionP.clave_prenda = prenda.id_prenda\r\n" + 
				"	INNER JOIN alt_disenio_lookup disLook ON prenda.id_familia_prenda = disLook.id_lookup\r\n" + 
				"	INNER JOIN alt_comercial_coordinado_prenda coorPrenda ON explosionP.coordinado = coorPrenda.id_coordinado_prenda\r\n" + 
				"	INNER JOIN alt_comercial_coordinado coor ON coorPrenda.id_coordinado = coor.id_coordinado\r\n" + 
				"	INNER JOIN alt_disenio_tela tela ON coorPrenda.id_tela = tela.id_tela\r\n" + 
				"	INNER JOIN alt_comercial_concetrado_prenda\r\n" + 
				"	INNER JOIN alt_produccion_lookup lookup ON explosionP.clave_proceso = lookup.id_lookup\r\n" + 
				"	LEFT JOIN alt_produccion_lookup LOOK_RUTA ON coorPrenda.id_ruta = LOOK_RUTA.id_lookup\r\n" + 
				"	INNER JOIN alt_disenio_lookup LOOK_TELA ON LOOK_TELA.id_lookup = tela.id_familia_composicion\r\n" + 
				"	INNER JOIN alt_produccion_explosion_procesos explosicion ON explosicion.coordinado = coorPrenda.id_coordinado_prenda \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 AND explosionP.programa=:programa \r\n" + 
				"	AND explosionP.clave_proceso = "+idProceso+" \r\n" + 
				"	AND ( explosionP.secuencia_proceso IS NOT NULL OR explosionP.secuencia_proceso != '' ) \r\n" + 
				"GROUP BY\r\n" + 
				"	explosionP.secuencia_proceso,\r\n" + 
				"	explosionP.clave_proceso, explosionP.programa").setParameter("programa", programa).getResultList();

		return re;
    }

    @Override
    public void save(ProduccionFechaExplosionProceso obj) {
        repository.save(obj);

    }

    @Transactional(readOnly = true)
	@Override
    public ProduccionFechaExplosionProceso findOneFechaCoorPrenda(Integer id) {
        try {
			return  (ProduccionFechaExplosionProceso) em.createQuery("from ProduccionFechaExplosionProceso where id_explosion_proceso="+id).getSingleResult();
			}
			catch(Exception e) {
				return null;
			}
    }

    @SuppressWarnings("unchecked")
  	@Override
  	@Transactional
	public List<Object[]> viewDetalles(Long idProceso, String secuencia) {
	     // nueva query holaa
			List<Object[]> re = null;
			re = em.createNativeQuery(""
					+ "SELECT\r\n" + 
					"	explosion.id_explosion_procesos,\r\n" + 
					"	pedidoInf.id_text,\r\n" + 
					"	pedidoInf.fecha_entrega,\r\n" + 
					"	coor.numero_coordinado,\r\n" + 
					"	(\r\n" + 
					"	SELECT\r\n" + 
					"		(\r\n" + 
					"		SUM( conse.cantidad ) + SUM( conse.cantidad_especial )) \r\n" + 
					"	FROM\r\n" + 
					"		alt_comercial_concetrado_prenda AS conse \r\n" + 
					"	WHERE\r\n" + 
					"		conse.id_coordinado_prenda = coorPrenda.id_coordinado_prenda \r\n" + 
					"	) AS num,\r\n" + 
					"	TELA.id_text as idTextTela,\r\n" + 
					"	\r\n" + 
					"	explosion.tiempo_proceso\r\n" + 
					"FROM\r\n" + 
					"	alt_produccion_explosion_procesos AS explosion\r\n" + 
					"	INNER JOIN alt_comercial_coordinado_prenda coorPrenda ON explosion.coordinado = coorPrenda.id_coordinado_prenda\r\n" + 
					"	INNER JOIN alt_comercial_coordinado coor ON coor.id_coordinado = coorPrenda.id_coordinado\r\n" + 
					"	INNER JOIN alt_comercial_pedido_informacion pedidoInf ON pedidoInf.id_pedido_informacion = coor.id_pedido\r\n" + 
					"	INNER JOIN alt_disenio_prenda PRENDA ON PRENDA.id_prenda = coorPrenda.id_prenda\r\n" + 
					"	INNER JOIN alt_disenio_lookup LOOK_PRENDA ON PRENDA.id_familia_prenda = LOOK_PRENDA.id_lookup\r\n" + 
					"	LEFT JOIN alt_produccion_lookup LOOK_RUTA ON coorPrenda.id_ruta = LOOK_RUTA.id_lookup\r\n" + 
					"	INNER JOIN alt_disenio_tela TELA ON coorPrenda.id_tela = TELA.id_tela\r\n" + 
					"	INNER JOIN alt_disenio_lookup LOOK_TELA ON LOOK_TELA.id_lookup = TELA.id_familia_composicion \r\n" + 
					"WHERE\r\n" + 
					"	1 = 1 \r\n" + 
					"	AND explosion.secuencia_proceso = '"+secuencia+"' \r\n" + 
					"	AND explosion.clave_proceso = "+idProceso)
					.getResultList();

			return re;
	}

	
	

	@Transactional(readOnly = true)
    @Override
	public Integer validarHorasHabiles(String fecha, String secuencia, Integer idProceso) {
		
		String re = em.createNativeQuery("" +
				"SELECT\r\n" + 
					"if( ( ROUND( ( calen.hombre - calen.adeudo * 0.0166667 ), 2 ) ) < ROUND( ( SUM( explosion.tiempo_proceso )* 0.0166667 ), 2 ), 1, 0 )  \r\n" + 
				"FROM\r\n" + 
					"alt_produccion_explosion_procesos AS explosion,\r\n" + 
					"alt_produccion_calendario AS calen \r\n" + 
				"WHERE\r\n" + 
					"explosion.secuencia_proceso = '"+secuencia+"' \r\n" + 
					"AND explosion.clave_proceso = "+idProceso+" \r\n" + 
					"AND calen.fecha = '"+fecha+"'").getSingleResult().toString();

		return Integer.parseInt(re);
	}
    
}


