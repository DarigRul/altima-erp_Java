package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.models.entity.AmpRequisicionAlmacen;
import com.altima.springboot.app.models.entity.AmpRequisicionAlmacenMaterial;
import com.altima.springboot.app.models.entity.ComercialCoordinado;
import com.altima.springboot.app.models.entity.ComercialPedidoInformacion;
import com.altima.springboot.app.repository.AmpRequisicionAlmacenMaterialRepository;
import com.altima.springboot.app.repository.AmpRequisicionAlmacenRepository;
import com.altima.springboot.app.repository.ComercialAgentesVentaRepository;

@Service
public class AmpRequisicionAlmacenServiceImpl implements IAmpRequisicionAlmacenService {
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AmpRequisicionAlmacenRepository repositoryAlmacen;
	
	@Autowired
	private AmpRequisicionAlmacenMaterialRepository repositoryMaterial;
	@Override
	@Transactional
	public Object[] infoUsuario(Long user) {
		Query q = em.createNativeQuery("SELECT \r\n" + 
				"CONCAT(empleado.nombre_persona ,' ',empleado.apellido_paterno),\r\n" + 
				"nombre_departamento\r\n" + 
				"FROM\r\n" + 
				"alt_hr_usuario as usuario,\r\n" + 
				"alt_hr_empleado as empleado,\r\n" + 
				"alt_hr_departamento as depa,\r\n" + 
				"alt_hr_puesto as puesto\r\n" + 
				"WHERE \r\n" + 
				"2=2\r\n" + 
				"and usuario.id_empleado = empleado.id_empleado\r\n" + 
				"and empleado.id_puesto = puesto.id_puesto\r\n" + 
				"and puesto.id_departamento = depa.id_departamento\r\n" + 
				"and usuario.id_usuario = "+user );
	
		Object[] valores = (Object[]) q.getSingleResult();
		return valores;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> AllMateriales() {

		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	material.id_material,\r\n" + 
				"	material.id_text,\r\n" + 
				"	material.nombre_material AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	material.tamanio,\r\n" + 
				"	IFNULL(color.nombre_lookup,'Sin color'),\r\n" + 
				"	'm' \r\n" + 
				"FROM\r\n" + 
				"	alt_disenio_material AS material\r\n" + 
				"	INNER JOIN alt_disenio_lookup look ON material.id_unidad_medida = look.id_lookup\r\n" + 
				"	LEFT JOIN alt_disenio_lookup color ON material.id_color = color.id_lookup \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND material.estatus_material = 1 \r\n" + 
				"	AND material.estatus = 1 \r\n" + 
				"	AND material.nombre_material != 'Tela principal' \r\n" + 
				"	AND material.nombre_material != 'Forro' UNION  \r\n" + 
				"SELECT\r\n" + 
				"	tela.id_tela,\r\n" + 
				"	tela.id_text,\r\n" + 
				"	tela.nombre_tela AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	tela.ancho,\r\n" + 
				"	tela.color,\r\n" + 
				"	't' \r\n" + 
				"FROM\r\n" + 
				"	alt_disenio_tela AS tela,\r\n" + 
				"	alt_disenio_lookup AS look \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND look.id_lookup = tela.id_unidad_medida \r\n" + 
				"	AND tela.estatus_tela = 1 \r\n" + 
				"	AND tela.estatus = 1 UNION\r\n" + 
				"SELECT\r\n" + 
				"	forro.id_forro,\r\n" + 
				"	forro.id_text,\r\n" + 
				"	forro.nombre_forro AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	forro.ancho_forro,\r\n" + 
				"	forro.color,\r\n" + 
				"	'f' \r\n" + 
				"FROM\r\n" + 
				"	alt_disenio_forro AS forro,\r\n" + 
				"	alt_disenio_lookup AS look \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND look.id_lookup = forro.id_unidad_medida \r\n" + 
				"	AND forro.estatus_forro = 1 \r\n" + 
				"	AND forro.estatus = 1 UNION\r\n" + 
				"SELECT\r\n" + 
				"	inventario.id_inventario,\r\n" + 
				"	inventario.id_text,\r\n" + 
				"	inventario.articulo AS nombre,\r\n" + 
				"	look2.nombre_lookup AS medida,\r\n" + 
				"	'N/P',\r\n" + 
				"	inventario.color,\r\n" + 
				"	'aa' \r\n" + 
				"FROM\r\n" + 
				"	alt_amp_inventario AS inventario,\r\n" + 
				"	alt_amp_lookup AS look,\r\n" + 
				"	alt_amp_lookup AS look_clas,\r\n" + 
				"	alt_disenio_lookup AS look2 \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND inventario.id_unidad_medida = look2.id_lookup \r\n" + 
				"	AND inventario.estatus = 1 \r\n" + 
				"ORDER BY\r\n" + 
				"	id_text,\r\n" + 
				"	nombre").getResultList();
		
		return re;
	}

	@Override
	@Transactional
	public void save(AmpRequisicionAlmacen obj) {
		repositoryAlmacen.save(obj);
		
	}

	@Override
	@Transactional
	public void save(AmpRequisicionAlmacenMaterial obj) {
		repositoryMaterial.save(obj);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> view(Long id, String departamento) {
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	requisicion.id_requisicion_almacen,\r\n" + 
				"	requisicion.id_text,\r\n" + 
				"	CONCAT( ahe.nombre_persona, ' ', ahe.apellido_paterno, ' ', ahe.apellido_materno ) solicitante,\r\n" + 
				"	DATE_FORMAT(requisicion.fecha_creacion,'%d/%m/%Y'),\r\n" + 
				"	ahd.nombre_departamento,\r\n" + 
				"CASE\r\n" + 
				"		\r\n" + 
				"		WHEN requisicion.estatus_envio = 0 THEN\r\n" + 
				"		'No enviado'\r\n" + 
				"		WHEN requisicion.estatus_envio = 1 THEN\r\n" + 
				"		'Enviado' \r\n" + 
				"		WHEN requisicion.estatus_envio = 2 THEN\r\n" + 
				"		'Aceptado'\r\n" + 
				"		WHEN requisicion.estatus_envio = 3 THEN\r\n" + 
				"		'Rechazado'\r\n" + 
				"	END ,\r\n" + 
				"	requisicion.estatus_envio,\r\n" + 
				"  IFNULL((SELECT compras.id_text FROM alt_compras_requisicion_almacen AS compras WHERE compras.id_solicitud_almacen = requisicion.id_requisicion_almacen ) , '0'),\r\n" + 
				"	requisicion.tipo_requisicion,\r\n" + 
				"	case\r\n" + 
				"	WHEN (SELECT COUNT(material.id_material) from alt_amp_requisicion_almacen_material as material WHERE material.id_requisicion_almacen = requisicion.id_requisicion_almacen and material.estatus=1  AND requisicion.estatus_envio=2 ) = (SELECT COUNT(material.id_material) from alt_amp_requisicion_almacen_material as material WHERE material.id_requisicion_almacen = requisicion.id_requisicion_almacen ) then 'En espera'\r\n" + 
				"	\r\n" + 
				"	WHEN (SELECT COUNT(material.id_material) from alt_amp_requisicion_almacen_material as material WHERE material.id_requisicion_almacen = requisicion.id_requisicion_almacen and material.estatus=2  AND requisicion.estatus_envio=2) >0 then 'Parcial'\r\n" + 
				"	\r\n" + 
				"	WHEN (SELECT COUNT(material.id_material) from alt_amp_requisicion_almacen_material as material WHERE material.id_requisicion_almacen = requisicion.id_requisicion_almacen and material.estatus=3 AND requisicion.estatus_envio=2  ) =(SELECT COUNT(material.id_material) from alt_amp_requisicion_almacen_material as material WHERE material.id_requisicion_almacen = requisicion.id_requisicion_almacen ) then 'Surtido'\r\n" + 
				"	ELSE 'N/A' \r\n" + 
				"	end \r\n" + 
				"	FROM\r\n" + 
				"		alt_amp_requisicion_almacen AS requisicion\r\n" + 
				"		INNER JOIN alt_hr_usuario ahu ON ahu.id_usuario = requisicion.id_solicitante\r\n" + 
				"		INNER JOIN alt_hr_empleado ahe ON ahe.id_empleado = ahu.id_empleado\r\n" + 
				"		INNER JOIN alt_hr_puesto ahp ON ahp.id_puesto = ahe.id_puesto\r\n" + 
				"		INNER JOIN alt_hr_departamento ahd ON ahd.id_departamento = ahp.id_departamento \r\n" + 
				"WHERE IF("+id+"=0,1=1,ahe.id_empleado="+id+")   "+
				" AND IF ( '"+departamento+"' = 'ADMIN', 1 = 1, ahd.nombre_departamento = '"+departamento+"' ) "+ 
				"ORDER BY\r\n" + 
				"	requisicion.id_requisicion_almacen DESC").getResultList();
		
		return re;
	}
	
	@Override
	public AmpRequisicionAlmacen findOne(Long id) {
		// TODO Auto-generated method stub
		return repositoryAlmacen.findById(id).orElse(null);
	}
	@Override
	public AmpRequisicionAlmacenMaterial findOneMaterial(Long id) {
		// TODO Auto-generated method stub
		return repositoryMaterial.findById(id).orElse(null);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> viewMaterial(Long id) {

		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	material.id_material,\r\n" + 
				"	'm',\r\n" + 
				"	AM.cantidad,\r\n" + 
				"	material.id_text,\r\n" + 
				"	material.nombre_material AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	material.tamanio,\r\n" + 
				"	IFNULL(color.nombre_lookup,'Sin color'),\r\n" + 
				"	AM.id_requisicion_almacen_material \r\n" + 
				"FROM\r\n" + 
				"	alt_amp_requisicion_almacen_material AS AM\r\n" + 
				"	INNER JOIN alt_disenio_material material ON AM.id_material = material.id_material\r\n" + 
				"	INNER JOIN alt_disenio_lookup look ON material.id_unidad_medida = look.id_lookup\r\n" + 
				"	LEFT JOIN alt_disenio_lookup color ON material.id_color = color.id_lookup \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND AM.tipo_material = 'm' \r\n" + 
				"	AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"	AND material.estatus_material = 1 \r\n" + 
				"	AND material.estatus = 1 UNION  \r\n" + 
				"SELECT\r\n" + 
				"	tela.id_tela,\r\n" + 
				"	't',\r\n" + 
				"	AM.cantidad,\r\n" + 
				"	tela.id_text,\r\n" + 
				"	tela.nombre_tela AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	tela.ancho,\r\n" + 
				"	tela.color, \r\n" + 
				"	AM.id_requisicion_almacen_material \r\n" + 
				"FROM\r\n" + 
				"	alt_disenio_tela AS tela,\r\n" + 
				"	alt_disenio_lookup AS look,\r\n" + 
				"	alt_amp_requisicion_almacen_material AS AM \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND AM.id_material = tela.id_tela \r\n" + 
				"	AND AM.tipo_material = 't' \r\n" + 
				"	AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"	AND look.id_lookup = tela.id_unidad_medida \r\n" + 
				"	AND tela.estatus_tela = 1 \r\n" + 
				"	AND tela.estatus = 1 UNION\r\n" + 
				"SELECT\r\n" + 
				"	forro.id_forro,\r\n" + 
				"	'f',\r\n" + 
				"	AM.cantidad,\r\n" + 
				"	forro.id_text,\r\n" + 
				"	forro.nombre_forro AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	forro.ancho_forro,\r\n" + 
				"	forro.color, \r\n" + 
				"	AM.id_requisicion_almacen_material \r\n" + 
				"FROM\r\n" + 
				"	alt_disenio_forro AS forro,\r\n" + 
				"	alt_disenio_lookup AS look,\r\n" + 
				"	alt_amp_requisicion_almacen_material AS AM \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND AM.id_material = forro.id_forro \r\n" + 
				"	AND AM.tipo_material = 'f' \r\n" + 
				"	AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"	AND look.id_lookup = forro.id_unidad_medida \r\n" + 
				"	AND forro.estatus_forro = 1 \r\n" + 
				"	AND forro.estatus = 1 UNION\r\n" + 
				"SELECT\r\n" + 
				"	inventario.id_inventario,\r\n" + 
				"	'aa',\r\n" + 
				"	AM.estatus,\r\n" + 
				"	inventario.id_text,\r\n" + 
				"	inventario.articulo AS nombre,\r\n" + 
				"	look2.nombre_lookup AS medida,\r\n" + 
				"	'N/A',\r\n" + 
				"	inventario.color, \r\n" + 
				"	AM.id_requisicion_almacen_material \r\n" + 
				"FROM\r\n" + 
				"	alt_amp_inventario AS inventario,\r\n" + 
				"	alt_amp_lookup AS look,\r\n" + 
				"	alt_amp_lookup AS look_clas,\r\n" + 
				"	alt_disenio_lookup AS look2,\r\n" + 
				"	alt_amp_requisicion_almacen_material AS AM \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND AM.id_material = inventario.id_inventario \r\n" + 
				"	AND AM.tipo_material = 'aa' \r\n" + 
				"	AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"	AND inventario.id_unidad_medida = look2.id_lookup \r\n" + 
				"	AND inventario.estatus = 1 \r\n" + 
				"ORDER BY\r\n" + 
				"	id_text,\r\n" + 
				"	nombre").getResultList();
		
		return re;
	}

	@Override
	@Transactional
	public void deleteRequisicionMaterial(Long  idRequision) {
		repositoryMaterial.deleteById(idRequision);
		
	}

	@Transactional(readOnly = true)
	@Override
	public AmpRequisicionAlmacenMaterial findOne(String idMateriales, String tipo, String cantidad, Long idRequisicion) {
		AmpRequisicionAlmacenMaterial obj = null;
	
		try {
			return  (AmpRequisicionAlmacenMaterial) em.createQuery("from AmpRequisicionAlmacenMaterial where  "
					+ " id_requisicion_almacen="+idRequisicion +"  "
					+ "AND  id_material = "+ idMateriales +"  "
					+ "AND tipo_material = '"+tipo+"' "
					+ "AND  cantidad = "+ cantidad +" ").getSingleResult();
			}
			catch(Exception e) {
				return obj;
			}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> viewListEmpleado() {
			List<Object[]> re = em.createNativeQuery(""+
				"SELECT \r\n "+
					"ahu.id_usuario, \r\n "+
					"CONCAT( ahe.nombre_persona, ' ', ahe.apellido_paterno, ' ', ahe.apellido_materno ) solicitante, \r\n "+
					"ahd.nombre_departamento \r\n "+
				"FROM \r\n "+
					"alt_hr_usuario AS ahu \r\n "+
					"INNER JOIN alt_hr_empleado ahe ON ahe.id_empleado = ahu.id_empleado \r\n "+
					"INNER JOIN alt_hr_puesto ahp ON ahp.id_puesto = ahe.id_puesto \r\n "+
					"INNER JOIN alt_hr_departamento ahd ON ahd.id_departamento = ahp.id_departamento \r\n ").getResultList();
		
		return re;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> clasificacion(String tipo) {
		List<Object[]> re = em.createNativeQuery(""+
		"SELECT \r\n "+
		"lookup.id_lookup, \r\n "+
		"lookup.nombre_lookup  \r\n "+
		"FROM  \r\n"+
		"alt_amp_lookup AS lookup  \r\n"+
		"WHERE  \r\n"+
		"2 = 2 \r\n "+
		"AND lookup.tipo_lookup = 'Clasificacion'\r\n"+
		"AND lookup.atributo_1 ='"+tipo+"' \r\n"+
		"AND lookup.estatus=1 \r\n"+
		"ORDER BY lookup.nombre_lookup").getResultList();
		
		return re;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> materialesbyclasificacion(Long id) {
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	inventario.id_inventario,\r\n" + 
				"	inventario.id_text,\r\n" + 
				"	inventario.articulo AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	'N/P',\r\n" + 
				"	inventario.color,\r\n" + 
				"	'aa' \r\n" + 
				"FROM\r\n" + 
				"	alt_amp_inventario AS inventario,\r\n" + 
				"	alt_disenio_lookup AS look,\r\n" + 
				"	alt_amp_lookup AS amp \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND inventario.id_unidad_medida = look.id_lookup \r\n" + 
				"	AND inventario.id_clasificacion = amp.id_lookup \r\n" + 
				"	AND inventario.estatus = 1 \r\n" + 
				"	AND amp.id_lookup = "+id+" UNION\r\n" + 
				"SELECT\r\n" + 
				"	material.id_material,\r\n" + 
				"	material.id_text,\r\n" + 
				"	material.nombre_material AS nombre,\r\n" + 
				"	look.nombre_lookup AS medida,\r\n" + 
				"	material.tamanio,\r\n" + 
				"	IFNULL( color.nombre_lookup, 'Sin color' ) AS color,\r\n" + 
				"	'm' \r\n" + 
				"FROM\r\n" + 
				"	alt_disenio_material AS material\r\n" + 
				"	INNER JOIN alt_disenio_lookup look ON material.id_unidad_medida = look.id_lookup\r\n" + 
				"	LEFT JOIN alt_disenio_lookup color ON material.id_color = color.id_lookup\r\n" + 
				"	INNER JOIN alt_disenio_lookup tipo ON tipo.id_lookup = material.id_tipo_material\r\n" + 
				"	INNER JOIN alt_amp_lookup amp ON tipo.atributo_2 = amp.id_lookup \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND material.estatus_material = 1 \r\n" + 
				"	AND material.estatus = 1 \r\n" + 
				"	AND amp.id_lookup = "+id+" \r\n" + 
				"	AND material.nombre_material != 'Tela Principal' \r\n" + 
				"	AND material.nombre_material != 'Forro'").getResultList();
		
		return re;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> forro() {
		List<Object[]> re = em.createNativeQuery(""+
				"SELECT \r\n"+
				"forro.id_forro, \r\n"+
				"forro.id_text, \r\n"+
				"forro.nombre_forro AS nombre, \r\n"+
				"look.nombre_lookup AS medida, \r\n"+
				"forro.ancho_forro, \r\n"+
				"forro.color, \r\n"+
				"'f' \r\n"+
			"FROM \r\n"+
				"alt_disenio_forro AS forro, \r\n"+
				"alt_disenio_lookup AS look \r\n"+
			"WHERE \r\n"+
				"1 = 1 \r\n"+
				"AND look.id_lookup = forro.id_unidad_medida \r\n"+
				"AND forro.estatus_forro = 1 \r\n"+
				"AND forro.estatus = 1 \r\n" ).getResultList();
		
		return re;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> tela() {
		List<Object[]> re = em.createNativeQuery(""+
				"SELECT \r\n"+
				"tela.id_tela, \r\n"+
				"tela.id_text, \r\n"+
				"tela.nombre_tela AS nombre, \r\n"+
				"look.nombre_lookup AS medida, \r\n"+
				"tela.ancho, \r\n"+
				"tela.color, \r\n"+
				"'t' \r\n"+
			"FROM \r\n"+
				"alt_disenio_tela AS tela, \r\n"+
				"alt_disenio_lookup AS look \r\n"+
			"WHERE \r\n"+
				"1 = 1 \r\n"+
				"AND look.id_lookup = tela.id_unidad_medida \r\n"+
				"AND tela.estatus_tela = 1 \r\n"+
				"AND tela.estatus = 1 \r\n").getResultList();
					
		return re;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> detalles(Long id) {

		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	AM.id_requisicion_almacen_material AS id,\r\n" + 
				"	AM.tipo_material AS tipo,\r\n" + 
				"	AM.cantidad AS cantidad,\r\n" + 
				"	material.id_text AS id_text,\r\n" + 
				"	material.nombre_material AS nombre,\r\n" + 
				"	IFNULL(color.nombre_lookup,'Sin color') as color,\r\n" + 
				"CASE\r\n" + 
				"		WHEN AM.estatus = 1 THEN\r\n" + 
				"		'En espera'\r\n" + 
				"		WHEN AM.estatus = 2 THEN\r\n" + 
				"		'Parcial'\r\n" + 
				"		WHEN AM.estatus = 3 THEN\r\n" + 
				"		'Surtido'\r\n" + 
				"	END\r\n" + 
				"	FROM\r\n" + 
				"		alt_amp_requisicion_almacen_material AS AM\r\n" + 
				"		INNER JOIN alt_disenio_material material on  AM.id_material = material.id_material\r\n" + 
				"		INNER JOIN alt_disenio_lookup look on material.id_unidad_medida = look.id_lookup\r\n" + 
				"		LEFT JOIN alt_disenio_lookup  color on material.id_color = color.id_lookup\r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1\r\n" + 
				"	\r\n" + 
				"		AND AM.tipo_material = 'm'\r\n" + 
				"		AND AM.id_requisicion_almacen = "+id+"\r\n" + 
				"		\r\n" + 
				"		AND material.estatus_material = 1 \r\n" + 
				"		AND material.estatus = 1 UNION \r\n" + 
				"	SELECT\r\n" + 
				"		AM.id_requisicion_almacen_material AS id,\r\n" + 
				"		AM.tipo_material AS tipo,\r\n" + 
				"		AM.cantidad AS cantidad,\r\n" + 
				"		tela.id_text AS id_text,\r\n" + 
				"		tela.nombre_tela AS nombre,\r\n" + 
				"		tela.color AS color,\r\n" + 
				"	CASE\r\n" + 
				"			WHEN AM.estatus = 1 THEN\r\n" + 
				"			'En espera' \r\n" + 
				"			WHEN AM.estatus = 2 THEN\r\n" + 
				"			'Parcial' \r\n" + 
				"			WHEN AM.estatus = 3 THEN\r\n" + 
				"			'Surtido' \r\n" + 
				"		END \r\n" + 
				"		FROM\r\n" + 
				"			alt_disenio_tela AS tela,\r\n" + 
				"			alt_disenio_lookup AS look,\r\n" + 
				"			alt_amp_requisicion_almacen_material AS AM \r\n" + 
				"		WHERE\r\n" + 
				"			1 = 1 \r\n" + 
				"			AND AM.id_material = tela.id_tela \r\n" + 
				"			AND AM.tipo_material = 't' \r\n" + 
				"			AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"			AND look.id_lookup = tela.id_unidad_medida \r\n" + 
				"			AND tela.estatus_tela = 1 \r\n" + 
				"			AND tela.estatus = 1 UNION\r\n" + 
				"		SELECT\r\n" + 
				"			AM.id_requisicion_almacen_material AS id,\r\n" + 
				"			AM.tipo_material AS tipo,\r\n" + 
				"			AM.cantidad AS cantidad,\r\n" + 
				"			forro.id_text AS id_text,\r\n" + 
				"			forro.nombre_forro AS nombre,\r\n" + 
				"			forro.color AS color,\r\n" + 
				"		CASE\r\n" + 
				"				WHEN AM.estatus = 1 THEN\r\n" + 
				"				'En espera' \r\n" + 
				"				WHEN AM.estatus = 2 THEN\r\n" + 
				"				'Parcial' \r\n" + 
				"				WHEN AM.estatus = 3 THEN\r\n" + 
				"				'Surtido' \r\n" + 
				"			END \r\n" + 
				"			FROM\r\n" + 
				"				alt_disenio_forro AS forro,\r\n" + 
				"				alt_disenio_lookup AS look,\r\n" + 
				"				alt_amp_requisicion_almacen_material AS AM \r\n" + 
				"			WHERE\r\n" + 
				"				1 = 1 \r\n" + 
				"				AND AM.id_material = forro.id_forro \r\n" + 
				"				AND AM.tipo_material = 'f' \r\n" + 
				"				AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"				AND look.id_lookup = forro.id_unidad_medida \r\n" + 
				"				AND forro.estatus_forro = 1 \r\n" + 
				"				AND forro.estatus = 1 UNION\r\n" + 
				"			SELECT\r\n" + 
				"				AM.id_requisicion_almacen_material AS id,\r\n" + 
				"				AM.tipo_material AS tipo,\r\n" + 
				"				AM.cantidad AS cantidad,\r\n" + 
				"				inventario.id_text AS id_text,\r\n" + 
				"				inventario.articulo AS nombre,\r\n" + 
				"				inventario.color,\r\n" + 
				"			CASE\r\n" + 
				"					WHEN AM.estatus = 1 THEN\r\n" + 
				"					'En espera' \r\n" + 
				"					WHEN AM.estatus = 2 THEN\r\n" + 
				"					'Parcial' \r\n" + 
				"					WHEN AM.estatus = 3 THEN\r\n" + 
				"					'Surtido' \r\n" + 
				"				END \r\n" + 
				"				FROM\r\n" + 
				"					alt_amp_inventario AS inventario,\r\n" + 
				"					alt_amp_lookup AS look,\r\n" + 
				"					alt_amp_lookup AS look_clas,\r\n" + 
				"					alt_disenio_lookup AS look2,\r\n" + 
				"					alt_amp_requisicion_almacen_material AS AM \r\n" + 
				"				WHERE\r\n" + 
				"					1 = 1 \r\n" + 
				"					AND AM.id_material = inventario.id_inventario \r\n" + 
				"					AND AM.tipo_material = 'aa' \r\n" + 
				"					AND AM.id_requisicion_almacen = "+id+" \r\n" + 
				"				AND inventario.id_unidad_medida = look2.id_lookup \r\n" + 
				"	AND inventario.estatus = 1\r\n" + 
				"	ORDER BY\r\n" + 
				"	id_text,\r\n" + 
				"	nombre").getResultList();
		
		return re;
	}



	
}
