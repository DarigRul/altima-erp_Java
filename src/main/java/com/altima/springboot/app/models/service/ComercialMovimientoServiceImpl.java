package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.models.entity.ComercialMovimiento;
import com.altima.springboot.app.repository.ComercialMovimientoRepository;

@Service
@SuppressWarnings("unchecked")
public class ComercialMovimientoServiceImpl implements IComercialMovimientoService {

	@Autowired
	private ComercialMovimientoRepository repository;
	
	@Autowired
	private EntityManager em;
	
	@Override
	@Transactional
	public void save(ComercialMovimiento movimiento) {
		repository.save(movimiento);
	}
	
	@Override
	@Transactional
	public ComercialMovimiento findOne(Long id) {
		
		return em.find(ComercialMovimiento.class, id);
	}

	@Override
	@Transactional
	public List<Object> listarMuestras() {
		return em.createNativeQuery("select prenda.descripcion_prenda, pedido.talla, telas.nombre_tela, pedido.id_detalle_pedido, prenda.id_text AS text, telas.id_text from alt_produccion_detalle_pedido pedido" + 
									"	INNER JOIN alt_disenio_prenda prenda ON pedido.id_prenda = prenda.id_prenda" + 
									"	INNER JOIN alt_disenio_tela telas ON pedido.id_tela = telas.id_tela").getResultList();
	}
	
	@Override
	@Transactional
	public Object EncontrarMuestra(Long id) {
		return em.createNativeQuery("select prenda.descripcion_prenda, pedido.talla, telas.nombre_tela, pedido.id_detalle_pedido, prenda.id_text AS text, telas.id_text, prenda.id_prenda, telas.id_tela from alt_produccion_detalle_pedido pedido" + 
									"	INNER JOIN alt_disenio_prenda prenda ON pedido.id_prenda = prenda.id_prenda" + 
									"	INNER JOIN alt_disenio_tela telas ON pedido.id_tela = telas.id_tela" +
									"   WHERE pedido.id_detalle_pedido ="+id).getSingleResult();
	}
	
	@Override
	@Transactional
	public List<Object> findAllWithNames(){
		
		
		return em.createNativeQuery("SELECT movimiento.id_movimiento, movimiento.id_text, cliente.nombre,cliente.apellido_paterno, cliente.apellido_materno, empleado.nombre_persona, empleado.apellido_paterno as paterno, empleado.apellido_materno as materno, movimiento.fecha_salida, movimiento.fecha_entrega, movimiento.estatus, (SELECT SUM(DISTINCT precio.precio_muestrario) FROM alt_comercial_movimiento_muestra_detalle as muest\r\n" + 
				"											INNER JOIN alt_disenio_prenda prenda ON muest.modelo_prenda = prenda.id_prenda\r\n" + 
				"											INNER JOIN alt_disenio_lista_precio_prenda precio ON muest.modelo_prenda = precio.id_prenda\r\n" + 
				"											WHERE muest.id_movimiento = movimiento.id_movimiento) AS Total, movimiento.encargado\r\n" + 
				"											from alt_comercial_movimiento as movimiento\r\n" + 
				"							INNER JOIN alt_comercial_cliente cliente ON movimiento.empresa = id_cliente \r\n" + 
				"							INNER JOIN alt_hr_empleado empleado ON movimiento.vendedor = empleado.id_empleado\r\n" + 
				"							ORDER BY movimiento.id_text").getResultList();
	}
	
	@Override
	@Transactional
	public List<Object> listarMuestrasTraspaso(Long id) {
		return em.createNativeQuery("select prenda.descripcion_prenda, movimiento.codigo_barras, telas.nombre_tela, movimiento.id_detalle_pedido, prenda.id_text AS text, telas.id_text from alt_comercial_movimiento_muestra_detalle movimiento\r\n" + 
									"		INNER JOIN alt_disenio_prenda prenda ON movimiento.modelo_prenda = prenda.id_prenda\r\n" + 
									"		INNER JOIN alt_disenio_tela telas ON movimiento.codigo_tela = telas.id_tela\r\n" + 
									"		\r\n" + 
									"WHERE movimiento.id_movimiento = "+id+"\r\n" + 
									"AND (movimiento.estatus = 4 or movimiento.estatus = 6 or movimiento.estatus = 8)\r\n" + 
									"GROUP BY movimiento.id_detalle_pedido").getResultList();
	}
	
	@Override
	@Transactional
	public List<Object> datosMovimiento(Long id) {
		return em.createNativeQuery("SELECT movimiento.empresa, movimiento.vendedor, prenda.descripcion_prenda, movidetalle.codigo_barras, movidetalle.id_detalle_pedido, prenda.id_text AS text, telas.id_text, prenda.id_prenda, telas.id_tela, movidetalle.id_movimiento, movimiento.encargado FROM alt_comercial_movimiento AS movimiento\r\n" + 
									"\r\n" + 
									"INNER JOIN alt_comercial_movimiento_muestra_detalle movidetalle ON movimiento.id_movimiento = movidetalle.id_movimiento\r\n" + 
									"INNER JOIN alt_disenio_prenda prenda ON movidetalle.modelo_prenda = prenda.id_prenda\r\n" + 
									"INNER JOIN alt_disenio_tela telas ON movidetalle.codigo_tela = telas.id_tela\r\n" + 
									"WHERE movimiento.id_movimiento = "+id+"\r\n" + 
									"GROUP BY movidetalle.id_detalle_pedido").getResultList();
	}

	@Override
	public List<Object> findAllHistorico() {
		// TODO Auto-generated method stub
		return em.createNativeQuery("call alt_pr_historico_muestras()").getResultList();
	}
	
	

}

