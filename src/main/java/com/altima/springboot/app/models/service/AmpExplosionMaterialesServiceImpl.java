package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.models.entity.AmpAlmacenLogico;
import com.altima.springboot.app.models.entity.AmpExplosionMateriales;
import com.altima.springboot.app.models.entity.AmpTraspaso;
import com.altima.springboot.app.models.entity.AmpTraspasoDetalle;
import com.altima.springboot.app.repository.AmpExplosionMaterialesRepository;
import com.altima.springboot.app.repository.AmpTraspasoDetalleRepository;
import com.altima.springboot.app.repository.AmpTraspasoRepository;

@Service
public class AmpExplosionMaterialesServiceImpl implements IAmpExplosionMaterialesService {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AmpExplosionMaterialesRepository repository;

	@Autowired
	private AmpTraspasoRepository repositorytraspaso;

	@Autowired
	private AmpTraspasoDetalleRepository repositorytraspasodetalle;

	@Override
	public void SaveTraspaso(AmpTraspaso traspaso) {
		repositorytraspaso.save(traspaso);
	}
	
	@Override
	public AmpTraspaso findById(Long id) {
		return repositorytraspaso.findById(id).orElse(null);
	}

	@Override
	public void SaveTraspasoDetalle(AmpTraspasoDetalle traspasodetalle) {
		repositorytraspasodetalle.save(traspasodetalle);
	}

	@Override
	public AmpTraspasoDetalle findOneTraspasoDetalle(Long id) {
		return repositorytraspasodetalle.findById(id).orElse(null);
	}

	@Override
	public List<AmpExplosionMateriales> findAll() {
		return (List<AmpExplosionMateriales>) repository.findAll();
	}

	@Override
	public void save(AmpExplosionMateriales explosionmateriales) {
		repository.save(explosionmateriales);

	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);

	}

	@Override
	public AmpExplosionMateriales findOne(Long id) {
		return repository.findById(id).orElse(null);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> findAllExplosion() {
		return em.createNativeQuery(
				"select  acpi.fecha_cierre,acpi.id_text,acpi.id_pedido_informacion,ifnull(acpi.fecha_explosion_habilitacion,'Pendiente'),IF(acpi.estatus_explosion_habilitacion=1 and acpi.estatus_explosion_materia_prima=1,'Explosionado','Sin explosionar'),ifnull(acpi.fecha_explosion_materia_prima,'Pendiente')\r\n"
						+ "from alt_comercial_pedido_informacion acpi\r\n" + "where acpi.estatus=3")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> findTotalMaterials(Long idpedido,String tipo) {
		return em.createNativeQuery("SELECT resultado3.proceso, resultado3.id_text,\r\n"
				+ "       resultado3.nombre_material,\r\n"
				+ "       resultado3.tipo_material,\r\n"
				+ "       resultado3.tamanio,\r\n"
				+ "       resultado3.uom,\r\n"
				+ "       resultado3.clasificacion,\r\n"
				+ "       Ifnull(Ifnull(resultado3.color, colores.color), \"sin color\")AS color,\r\n"
				+ "       resultado3.surtir_total,\r\n"
				+ "       resultado3.surtir_sin_spf,\r\n"
				+ "       resultado3.surtir_con_spf,\r\n"
				+ "       resultado3.disponible_inicio,\r\n"
				+ "       resultado3.disponible_en_almacenes,\r\n"
				+ "       resultado3.apartado,\r\n"
				+ "       resultado3.faltante,\r\n"
				+ "       resultado3.id_material,\r\n"
				+ "       resultado3.id_pedido,\r\n"
				+ "       resultado3.id_prenda\r\n"
				+ "FROM   (SELECT resultado2.proceso,\r\n"
				+ "               resultado2.id_text,\r\n"
				+ "               resultado2.nombre_material,\r\n"
				+ "               resultado2.tipo_material,\r\n"
				+ "               resultado2.tamanio,\r\n"
				+ "               resultado2.uom,\r\n"
				+ "               resultado2.clasificacion,\r\n"
				+ "               resultado2.color\r\n"
				+ "                      AS color,\r\n"
				+ "               Round(resultado2.surtir_inicio, 2)\r\n"
				+ "                      AS 'surtir_total',\r\n"
				+ "               Round(resultado2.surtir_inicio_sin_spf, 2)\r\n"
				+ "                      AS 'surtir_sin_spf',\r\n"
				+ "               Round(Ifnull(resultado2.surtir_inicio_spf, 0), 2)\r\n"
				+ "                      AS 'surtir_con_spf',\r\n"
				+ "               resultado2.disponible_inicio,\r\n"
				+ "               resultado2.disponible_inicio - Ifnull(\r\n"
				+ "               alt_amp_traspaso_detalle2.apartados, 0) AS\r\n"
				+ "               'disponible_en_almacenes',\r\n"
				+ "               Ifnull(APARTADO.apartado, 0)\r\n"
				+ "                      AS 'apartado',\r\n"
				+ "               Round(Ifnull(( resultado2.surtir_inicio -\r\n"
				+ "                              Ifnull(APARTADO.apartado, 0) )\r\n"
				+ "                     , 0), 2)\r\n"
				+ "                           AS faltante,\r\n"
				+ "               resultado2.id_material,\r\n"
				+ "               resultado2.id_pedido_informacion\r\n"
				+ "                      AS 'id_pedido',\r\n"
				+ "               resultado2.id_prenda,\r\n"
				+ "               resultado2.id_coordinado,\r\n"
				+ "               resultado2.id_coordinado_prenda\r\n"
				+ "        FROM   (SELECT resultado.*,\r\n"
				+ "                       ( Sum(resultado.total22) )\r\n"
				+ "                       Surtir_actual,\r\n"
				+ "                       ( Sum(resultado.total22) )\r\n"
				+ "                       Faltante,\r\n"
				+ "                       Sum(resultado.total22)\r\n"
				+ "                       Surtir_inicio,\r\n"
				+ "                       Sum(resultado.total24)\r\n"
				+ "                               Surtir_inicio_sin_spf,\r\n"
				+ "                       Sum(( total_prendas_pedido2.total * resultado.cantidad ))\r\n"
				+ "                       AS\r\n"
				+ "                               Surtir_inicio_spf\r\n"
				+ "                FROM   (SELECT alt_disenio_material_prenda.id_material,\r\n"
				+ "                               alt_disenio_material_prenda.cantidad,\r\n"
				+ "                               alt_disenio_material.id_text,\r\n"
				+ "                               alt_disenio_material.tamanio,\r\n"
				+ "                               alt_disenio_material.nombre_material,\r\n"
				+ "                               procesolook.nombre_lookup\r\n"
				+ "                                       proceso,\r\n"
				+ "                               uomlook.nombre_lookup\r\n"
				+ "                                       uom,\r\n"
				+ "                               clasificacionlook.nombre_lookup\r\n"
				+ "                                       clasificacion,\r\n"
				+ "                               tipolook.nombre_lookup\r\n"
				+ "                                       tipo_material,\r\n"
				+ "                               colorlook.nombre_lookup\r\n"
				+ "                                       color,\r\n"
				+ "                               total_prendas_pedido.*,\r\n"
				+ "                               ( total_prendas_pedido.total *\r\n"
				+ "                                 alt_disenio_material_prenda.cantidad )\r\n"
				+ "                                       AS total22,\r\n"
				+ "                               ( total_prendas_pedido3.total *\r\n"
				+ "                                 alt_disenio_material_prenda.cantidad )\r\n"
				+ "                                       AS\r\n"
				+ "                               total24,\r\n"
				+ "                               IF(Sum(IF(alt_amp_almacen_logico.tipo <> 2,\r\n"
				+ "                                      alt_amp_multialmacen.existencia, 0))\r\n"
				+ "                                  <\r\n"
				+ "                                  0, 0, Sum(IF(alt_amp_almacen_logico.tipo <> 2,\r\n"
				+ "                                            alt_amp_multialmacen.existencia, 0))\r\n"
				+ "                               )\r\n"
				+ "                                       disponible_inicio\r\n"
				+ "                        FROM   (SELECT emp2.id_pedido_informacion,\r\n"
				+ "                                       cor_pre2.id_coordinado,\r\n"
				+ "                                       cor_pre2.id_coordinado_prenda,\r\n"
				+ "                                       cor_pre2.id_prenda,\r\n"
				+ "                                       Concat(dp2.id_text, \" - \", dt2.id_text)\r\n"
				+ "                                       AS\r\n"
				+ "                                       Modelo,\r\n"
				+ "                                       con_pre2.cantidad\r\n"
				+ "                                       AS\r\n"
				+ "                                       Cantidad2,\r\n"
				+ "                                       con_pre2.cantidad_especial\r\n"
				+ "                                       AS\r\n"
				+ "                                       CantidadEspecial,\r\n"
				+ "                                       Sum(cantidad + cantidad_especial)\r\n"
				+ "                                       AS TOTAL\r\n"
				+ "                                FROM   alt_comercial_coordinado AS cor2\r\n"
				+ "                       INNER JOIN alt_comercial_coordinado_prenda AS\r\n"
				+ "                                  cor_pre2\r\n"
				+ "                               ON cor2.id_coordinado =\r\n"
				+ "                                  cor_pre2.id_coordinado\r\n"
				+ "                       INNER JOIN alt_comercial_concetrado_prenda AS\r\n"
				+ "                                  con_pre2\r\n"
				+ "                               ON cor_pre2.id_coordinado_prenda =\r\n"
				+ "                                  con_pre2.id_coordinado_prenda\r\n"
				+ "                       INNER JOIN alt_disenio_prenda AS dp2\r\n"
				+ "                               ON cor_pre2.id_prenda = dp2.id_prenda\r\n"
				+ "                       INNER JOIN alt_disenio_tela AS dt2\r\n"
				+ "                               ON cor_pre2.id_tela = dt2.id_tela\r\n"
				+ "                       INNER JOIN alt_comercial_cliente_empleado AS\r\n"
				+ "                                  emp2\r\n"
				+ "                               ON con_pre2.id_empleado =\r\n"
				+ "                                  emp2.id_empleado\r\n"
				+ "                                WHERE  emp2.id_pedido_informacion = "+idpedido+"\r\n"
				+ "                                GROUP  BY cor_pre2.id_prenda) AS\r\n"
				+ "                               total_prendas_pedido,\r\n"
				+ "                               (SELECT emp2.id_pedido_informacion,\r\n"
				+ "                                       cor_pre2.id_prenda,\r\n"
				+ "                                       Concat(dp2.id_text, \" - \", dt2.id_text)\r\n"
				+ "                                       AS\r\n"
				+ "                                       Modelo,\r\n"
				+ "                                       con_pre2.cantidad\r\n"
				+ "                                       AS\r\n"
				+ "                                       Cantidad2,\r\n"
				+ "                                       con_pre2.cantidad_especial\r\n"
				+ "                                       AS\r\n"
				+ "                                       CantidadEspecial,\r\n"
				+ "                                       Sum(cantidad + cantidad_especial)\r\n"
				+ "                                       AS TOTAL\r\n"
				+ "                                FROM   alt_comercial_coordinado AS cor2\r\n"
				+ "                       INNER JOIN alt_comercial_coordinado_prenda AS\r\n"
				+ "                                  cor_pre2\r\n"
				+ "                               ON cor2.id_coordinado =\r\n"
				+ "                                  cor_pre2.id_coordinado\r\n"
				+ "                       INNER JOIN alt_comercial_concetrado_prenda AS\r\n"
				+ "                                  con_pre2\r\n"
				+ "                               ON cor_pre2.id_coordinado_prenda =\r\n"
				+ "                                  con_pre2.id_coordinado_prenda\r\n"
				+ "                       INNER JOIN alt_disenio_prenda AS dp2\r\n"
				+ "                               ON cor_pre2.id_prenda = dp2.id_prenda\r\n"
				+ "                       INNER JOIN alt_disenio_tela AS dt2\r\n"
				+ "                               ON cor_pre2.id_tela = dt2.id_tela\r\n"
				+ "                       INNER JOIN alt_comercial_cliente_empleado AS\r\n"
				+ "                                  emp2\r\n"
				+ "                               ON con_pre2.id_empleado =\r\n"
				+ "                                  emp2.id_empleado\r\n"
				+ "                                  AND emp2.nombre_empleado NOT LIKE\r\n"
				+ "                                      \"%spf%\"\r\n"
				+ "                                WHERE  emp2.id_pedido_informacion = "+idpedido+"\r\n"
				+ "                                GROUP  BY cor_pre2.id_prenda) AS\r\n"
				+ "                               total_prendas_pedido3,\r\n"
				+ "                               alt_disenio_material_prenda,\r\n"
				+ "                               alt_disenio_material\r\n"
				+ "                               LEFT JOIN alt_disenio_lookup procesolook\r\n"
				+ "                                      ON alt_disenio_material.id_proceso =\r\n"
				+ "                                         procesolook.id_lookup\r\n"
				+ "                               LEFT JOIN alt_disenio_lookup uomlook\r\n"
				+ "                                      ON alt_disenio_material.id_unidad_medida =\r\n"
				+ "                                         uomlook.id_lookup\r\n"
				+ "                               LEFT JOIN alt_disenio_lookup clasificacionlook\r\n"
				+ "                                      ON alt_disenio_material.id_clasificacion =\r\n"
				+ "                                         clasificacionlook.id_lookup\r\n"
				+ "                               LEFT JOIN alt_disenio_lookup tipolook\r\n"
				+ "                                      ON alt_disenio_material.id_tipo_material =\r\n"
				+ "                                         tipolook.id_lookup\r\n"
				+ "                               INNER JOIN alt_amp_lookup aml\r\n"
				+ "                                       ON tipolook.atributo_2 = aml.id_lookup\r\n"
				+ "                                          AND aml.atributo_1 = \""+tipo+"\"\r\n"
				+ "                               LEFT JOIN alt_disenio_lookup colorlook\r\n"
				+ "                                      ON alt_disenio_material.id_color =\r\n"
				+ "                                         colorlook.id_lookup\r\n"
				+ "                               LEFT JOIN alt_amp_multialmacen\r\n"
				+ "                                      ON alt_amp_multialmacen.id_articulo =\r\n"
				+ "                                         alt_disenio_material.id_material\r\n"
				+ "                                         AND alt_amp_multialmacen.tipo =\r\n"
				+ "                                             'material'\r\n"
				+ "                               LEFT JOIN alt_amp_almacen_logico\r\n"
				+ "                                      ON alt_amp_multialmacen.id_almacen_logico\r\n"
				+ "                                         =\r\n"
				+ "       alt_amp_almacen_logico.id_almacen_logico\r\n"
				+ "       AND alt_amp_almacen_logico.tipo <> 2\r\n"
				+ "       WHERE  total_prendas_pedido.id_prenda =\r\n"
				+ "       alt_disenio_material_prenda.id_prenda\r\n"
				+ "       AND total_prendas_pedido3.id_prenda =\r\n"
				+ "       alt_disenio_material_prenda.id_prenda\r\n"
				+ "       AND alt_disenio_material_prenda.id_material =\r\n"
				+ "       alt_disenio_material.id_material\r\n"
				+ "       GROUP  BY alt_disenio_material_prenda.id_material,\r\n"
				+ "       alt_disenio_material_prenda.id_prenda) AS resultado\r\n"
				+ "       LEFT JOIN (SELECT emp2.id_pedido_informacion,\r\n"
				+ "       cor_pre2.id_prenda,\r\n"
				+ "       Concat(dp2.id_text, \" - \", dt2.id_text) AS Modelo,\r\n"
				+ "       con_pre2.cantidad                       AS Cantidad2,\r\n"
				+ "       con_pre2.cantidad_especial              AS\r\n"
				+ "       CantidadEspecial,\r\n"
				+ "       Sum(cantidad + cantidad_especial)       AS TOTAL\r\n"
				+ "       FROM   alt_comercial_coordinado AS cor2\r\n"
				+ "       INNER JOIN alt_comercial_coordinado_prenda AS cor_pre2\r\n"
				+ "       ON cor2.id_coordinado = cor_pre2.id_coordinado\r\n"
				+ "       INNER JOIN alt_comercial_concetrado_prenda AS con_pre2\r\n"
				+ "       ON cor_pre2.id_coordinado_prenda =\r\n"
				+ "        con_pre2.id_coordinado_prenda\r\n"
				+ "       INNER JOIN alt_disenio_prenda AS dp2\r\n"
				+ "       ON cor_pre2.id_prenda = dp2.id_prenda\r\n"
				+ "       INNER JOIN alt_disenio_tela AS dt2\r\n"
				+ "       ON cor_pre2.id_tela = dt2.id_tela\r\n"
				+ "       INNER JOIN alt_comercial_cliente_empleado AS emp2\r\n"
				+ "       ON con_pre2.id_empleado = emp2.id_empleado\r\n"
				+ "        AND emp2.nombre_empleado LIKE \"%spf%\"\r\n"
				+ "       WHERE  emp2.id_pedido_informacion = "+idpedido+"\r\n"
				+ "       GROUP  BY cor_pre2.id_prenda) AS total_prendas_pedido2\r\n"
				+ "       ON total_prendas_pedido2.id_prenda = resultado.id_prenda\r\n"
				+ "       GROUP  BY resultado.id_material) AS resultado2\r\n"
				+ "       LEFT JOIN (SELECT alt_amp_traspaso_detalle.*,\r\n"
				+ "       Sum(alt_amp_traspaso_detalle.cantidad) AS apartados\r\n"
				+ "       FROM   `alt_amp_traspaso_detalle`,\r\n"
				+ "       alt_amp_traspaso\r\n"
				+ "       WHERE  alt_amp_traspaso_detalle.tipo = 'material'\r\n"
				+ "       AND alt_amp_traspaso.id_traspaso =\r\n"
				+ "       alt_amp_traspaso_detalle.id_traspaso\r\n"
				+ "       AND alt_amp_traspaso.tipo = 2\r\n"
				+ "       GROUP  BY alt_amp_traspaso_detalle.id_articulo)\r\n"
				+ "       alt_amp_traspaso_detalle2\r\n"
				+ "       ON resultado2.id_material = alt_amp_traspaso_detalle2.id_articulo\r\n"
				+ "       LEFT JOIN alt_amp_traspaso\r\n"
				+ "       ON alt_amp_traspaso.id_traspaso = alt_amp_traspaso_detalle2.id_traspaso\r\n"
				+ "       LEFT JOIN alt_amp_almacen_logico al2\r\n"
				+ "       ON al2.id_almacen_logico = alt_amp_traspaso.id_almacen_logico_destino\r\n"
				+ "       AND alt_amp_traspaso.id_almacen_logico_destino = (SELECT\r\n"
				+ "       id_almacen_logico\r\n"
				+ "                               FROM\r\n"
				+ "       alt_amp_almacen_logico\r\n"
				+ "                               WHERE  tipo = 2\r\n"
				+ "                               LIMIT  1)\r\n"
				+ "       LEFT JOIN (SELECT Ifnull(Sum(alt_amp_traspaso_detalle.cantidad), 0)\r\n"
				+ "                         apartado,\r\n"
				+ "       alt_amp_traspaso_detalle.id_articulo              AS id,\r\n"
				+ "       am.id_articulo                                    AS id2\r\n"
				+ "       FROM   alt_amp_multialmacen am,\r\n"
				+ "       alt_amp_almacen_logico al\r\n"
				+ "       LEFT JOIN alt_amp_almacen_logico al2\r\n"
				+ "       ON al2.id_almacen_logico = (SELECT id_almacen_logico\r\n"
				+ "                        FROM\r\n"
				+ "       alt_amp_almacen_logico\r\n"
				+ "                        WHERE  tipo = 2\r\n"
				+ "                        LIMIT  1)\r\n"
				+ "       LEFT JOIN alt_amp_traspaso amt\r\n"
				+ "       ON al2.id_almacen_logico =\r\n"
				+ "       amt.id_almacen_logico_destino\r\n"
				+ "       AND amt.referencia = "+idpedido+"\r\n"
				+ "       AND al.id_almacen_logico =\r\n"
				+ "       amt.id_almacen_logico_origen\r\n"
				+ "       LEFT JOIN alt_amp_traspaso_detalle\r\n"
				+ "       ON amt.id_traspaso =\r\n"
				+ "       alt_amp_traspaso_detalle.id_traspaso\r\n"
				+ "       AND alt_amp_traspaso_detalle.tipo = 'material'\r\n"
				+ "       WHERE  am.id_almacen_logico = al.id_almacen_logico\r\n"
				+ "       AND am.tipo = 'material'\r\n"
				+ "       GROUP  BY am.id_articulo,\r\n"
				+ "       alt_amp_traspaso_detalle.id_articulo) AS APARTADO\r\n"
				+ "       ON APARTADO.id = resultado2.id_material\r\n"
				+ "       AND APARTADO.id2 = resultado2.id_material\r\n"
				+ "        WHERE  resultado2.tipo_material <> \"tela material\")AS resultado3\r\n"
				+ "       LEFT JOIN (SELECT CM.id_coordinado_prenda,\r\n"
				+ "                         material.nombre_material,\r\n"
				+ "                         material.id_material,\r\n"
				+ "                         CM.color,\r\n"
				+ "                         CM.color_codigo\r\n"
				+ "                  FROM   alt_disenio_material AS material,\r\n"
				+ "                         alt_comercial_coordinado_material AS CM\r\n"
				+ "                  WHERE  1 = 1\r\n"
				+ "                         AND CM.id_material = material.id_material\r\n"
				+ "                  UNION ALL\r\n"
				+ "                  SELECT coorForro.id_coordinado_prenda,\r\n"
				+ "                         forro.id_forro,\r\n"
				+ "                         Concat('Forro ', forro.nombre_forro),\r\n"
				+ "                         forro.color,\r\n"
				+ "                         forro.codigo_color\r\n"
				+ "                  FROM   alt_comercial_coordinado_forro AS coorForro,\r\n"
				+ "                         alt_disenio_forro AS forro\r\n"
				+ "                  WHERE  1 = 1\r\n"
				+ "                         AND forro.id_forro = coorForro.id_forro)AS colores\r\n"
				+ "              ON resultado3.id_coordinado_prenda = colores.id_coordinado_prenda\r\n"
				+ "                 AND colores.id_material = resultado3.id_material ").getResultList();

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> findAvailableMaterials(Long IdArticulo, Long Idpedido) {
		return em.createNativeQuery("SELECT al2.id_almacen_logico                        id_destino,\r\n"
				+ "       al.id_almacen_logico                         id_origen,\r\n"
				+ "       al.nombre_almacen_logico,\r\n" + "       am.id_articulo,\r\n" + "       amt.id_traspaso,\r\n"
				+ "       alt_amp_traspaso_detalle.id_traspaso_detalle,\r\n" + "       am.existencia,\r\n"
				+ "       Ifnull(alt_amp_traspaso_detalle.cantidad, 0) apartado,\r\n"
				+ "       IFNULL(am.existencia-QUERYDISPONIBLE.disponible, am.existencia)\r\n" + "FROM   alt_amp_multialmacen am,\r\n"
				+ "       alt_amp_almacen_logico al\r\n" + "       LEFT JOIN alt_amp_almacen_logico al2\r\n"
				+ "              ON al2.id_almacen_logico = (SELECT id_almacen_logico\r\n"
				+ "                                          FROM   alt_amp_almacen_logico\r\n"
				+ "                                          WHERE  tipo = 2\r\n"
				+ "                                          LIMIT  1)\r\n"
				+ "       LEFT JOIN alt_amp_traspaso amt\r\n"
				+ "              ON al2.id_almacen_logico = amt.id_almacen_logico_destino\r\n"
				+ "                 AND al.id_almacen_logico = amt.id_almacen_logico_origen\r\n"
				+ "                 AND amt.tipo = 2\r\n" + "                 AND amt.referencia = " + Idpedido + "\r\n"
				+ "       LEFT JOIN alt_amp_traspaso_detalle\r\n"
				+ "              ON amt.id_traspaso = alt_amp_traspaso_detalle.id_traspaso\r\n"
				+ "                 AND alt_amp_traspaso_detalle.id_articulo = " + IdArticulo + "\r\n"
				+ "                 AND alt_amp_traspaso_detalle.tipo = 'material'\r\n"
				+ "       LEFT JOIN(SELECT alt_amp_traspaso.id_almacen_logico_origen,\r\n"
				+ "                        Sum(alt_amp_traspaso_detalle.cantidad) AS DISPONIBLE\r\n"
				+ "                 FROM   alt_amp_traspaso_detalle,\r\n"
				+ "                        alt_amp_traspaso\r\n"
				+ "                 WHERE  alt_amp_traspaso_detalle.id_articulo = " + IdArticulo + "\r\n"
				+ "                        AND alt_amp_traspaso_detalle.tipo = \"material\"\r\n"
				+ "                        AND alt_amp_traspaso.id_traspaso =\r\n"
				+ "                            alt_amp_traspaso_detalle.id_traspaso\r\n"
				+ "                        AND alt_amp_traspaso.tipo = 2\r\n"
				+ "                 GROUP  BY alt_amp_traspaso.id_almacen_logico_origen) AS\r\n"
				+ "       QUERYDISPONIBLE\r\n"
				+ "              ON QUERYDISPONIBLE.id_almacen_logico_origen = al.id_almacen_logico\r\n"
				+ "WHERE  am.id_almacen_logico = al.id_almacen_logico\r\n" + "       AND am.id_articulo = " + IdArticulo
				+ "\r\n" + "   and al.tipo != 2    AND am.tipo = 'material'\r\n" + "GROUP  BY am.id_multialmacen ").getResultList();

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Object[]> findMaterialsHeader(Long IdArticulo, Long IdPedido) {
		return em.createNativeQuery(" select\r\n" + "   resultado2.id_material,\r\n"
				+ "   if(resultado2.disponible_inicio - alt_amp_traspaso_detalle2.apartados < 0 , 0 , resultado2.disponible_inicio - alt_amp_traspaso_detalle2.apartados )disponible_actual,\r\n"
				+ "   resultado2.disponible_inicio,\r\n" + "   alt_amp_traspaso_detalle2.apartados,\r\n" + "   (\r\n"
				+ "      resultado2.Surtir_actual - alt_amp_traspaso_detalle2.apartados\r\n" + "   )\r\n"
				+ "   Surtir_actual,\r\n" + "   (\r\n"
				+ "      resultado2.Faltante - alt_amp_traspaso_detalle2.apartados\r\n" + "   )\r\n"
				+ "   Faltante,\r\n" + "   resultado2.Surtir_inicio \r\n" + "from\r\n" + "   (\r\n" + "      SELECT\r\n"
				+ "         resultado.*,\r\n" + "         (\r\n" + "            SUM( resultado.total22 ) \r\n"
				+ "         )\r\n" + "         Surtir_actual,\r\n" + "         (\r\n"
				+ "            SUM( resultado.total22 ) \r\n" + "         )\r\n" + "         Faltante,\r\n"
				+ "         SUM( resultado.total22 ) Surtir_inicio \r\n" + "      FROM\r\n" + "         (\r\n"
				+ "            SELECT\r\n" + "               alt_disenio_material_prenda.id_material,\r\n"
				+ "               alt_disenio_material_prenda.cantidad,\r\n"
				+ "               alt_disenio_material.id_text,\r\n"
				+ "               alt_disenio_material.tamanio,\r\n"
				+ "               alt_disenio_material.nombre_material,\r\n"
				+ "               procesolook.nombre_lookup proceso,\r\n"
				+ "               uomlook.nombre_lookup uom,\r\n"
				+ "               clasificacionlook.nombre_lookup clasificacion,\r\n"
				+ "               tipolook.nombre_lookup tipo_material,\r\n"
				+ "               colorlook.nombre_lookup color,\r\n" + "               total_prendas_pedido.*,\r\n"
				+ "               (\r\n"
				+ "                  total_prendas_pedido.TOTAL * alt_disenio_material_prenda.cantidad \r\n"
				+ "               )\r\n" + "               AS total22,\r\n"
				+ "               if(SUM(IF(alt_amp_almacen_logico.tipo <> 2, alt_amp_multialmacen.existencia, 0)) < 0, 0, SUM(IF(alt_amp_almacen_logico.tipo <> 2, alt_amp_multialmacen.existencia, 0))) disponible_inicio \r\n"
				+ "            FROM\r\n" + "               (\r\n" + "                  SELECT\r\n"
				+ "                     emp2.id_pedido_informacion,\r\n"
				+ "                     cor_pre2.id_prenda,\r\n"
				+ "                     CONCAT( dp2.id_text, \" - \", dt2.id_text ) AS Modelo,\r\n"
				+ "                     con_pre2.cantidad AS Cantidad2,\r\n"
				+ "                     con_pre2.cantidad_especial AS CantidadEspecial,\r\n"
				+ "                     sum( Cantidad + cantidad_especial ) AS TOTAL \r\n"
				+ "                  FROM\r\n" + "                     alt_comercial_coordinado AS cor2 \r\n"
				+ "                     INNER JOIN\r\n"
				+ "                        alt_comercial_coordinado_prenda AS cor_pre2 \r\n"
				+ "                        ON cor2.id_coordinado = cor_pre2.id_coordinado \r\n"
				+ "                     INNER JOIN\r\n"
				+ "                        alt_comercial_concetrado_prenda AS con_pre2 \r\n"
				+ "                        ON cor_pre2.id_coordinado_prenda = con_pre2.id_coordinado_prenda \r\n"
				+ "                     INNER JOIN\r\n" + "                        alt_disenio_prenda AS dp2 \r\n"
				+ "                        ON cor_pre2.id_prenda = dp2.id_prenda \r\n"
				+ "                     INNER JOIN\r\n" + "                        alt_disenio_tela AS dt2 \r\n"
				+ "                        ON cor_pre2.id_tela = dt2.id_tela \r\n"
				+ "                     INNER JOIN\r\n"
				+ "                        alt_comercial_cliente_empleado AS emp2 \r\n"
				+ "                        ON con_pre2.id_empleado = emp2.id_empleado \r\n"
				+ "                  WHERE\r\n" + "                     emp2.id_pedido_informacion = " + IdPedido
				+ " \r\n" + "                  GROUP BY\r\n" + "                     cor_pre2.id_prenda \r\n"
				+ "               )\r\n" + "               AS total_prendas_pedido,\r\n"
				+ "               alt_disenio_material_prenda,\r\n" + "               alt_disenio_material \r\n"
				+ "               LEFT JOIN\r\n" + "                  alt_disenio_lookup procesolook \r\n"
				+ "                  ON alt_disenio_material.id_proceso = procesolook.id_lookup \r\n"
				+ "               LEFT JOIN\r\n" + "                  alt_disenio_lookup uomlook \r\n"
				+ "                  ON alt_disenio_material.id_unidad_medida = uomlook.id_lookup \r\n"
				+ "               LEFT JOIN\r\n" + "                  alt_disenio_lookup clasificacionlook \r\n"
				+ "                  ON alt_disenio_material.id_clasificacion = clasificacionlook.id_lookup \r\n"
				+ "               LEFT JOIN\r\n" + "                  alt_disenio_lookup tipolook \r\n"
				+ "                  ON alt_disenio_material.id_tipo_material = tipolook.id_lookup \r\n"
				+ "               LEFT JOIN\r\n" + "                  alt_disenio_lookup colorlook \r\n"
				+ "                  ON alt_disenio_material.id_color = colorlook.id_lookup \r\n"
				+ "               LEFT JOIN\r\n" + "                  alt_amp_multialmacen \r\n"
				+ "                  ON alt_amp_multialmacen.id_articulo = alt_disenio_material.id_material \r\n"
				+ "                  AND alt_amp_multialmacen.tipo = 'material' \r\n" + "               LEFT JOIN\r\n"
				+ "                  alt_amp_almacen_logico \r\n"
				+ "                  ON alt_amp_multialmacen.id_almacen_logico = alt_amp_almacen_logico.id_almacen_logico \r\n"
				+ "                  AND alt_amp_almacen_logico.tipo <> 2 \r\n" + "            WHERE\r\n"
				+ "               total_prendas_pedido.id_prenda = alt_disenio_material_prenda.id_prenda \r\n"
				+ "               AND alt_disenio_material_prenda.id_material = alt_disenio_material.id_material \r\n"
				+ "            GROUP BY\r\n" + "               alt_disenio_material_prenda.id_material,\r\n"
				+ "               alt_disenio_material_prenda.id_prenda \r\n" + "         )\r\n"
				+ "         AS resultado \r\n" + "      GROUP BY\r\n" + "         resultado.id_material\r\n"
				+ "   )\r\n" + "   as resultado2 \r\n" + "   LEFT JOIN\r\n" + "      (\r\n" + "         SELECT\r\n"
				+ "            *,\r\n" + "            sum(alt_amp_traspaso_detalle.cantidad) as apartados \r\n"
				+ "         FROM\r\n" + "            `alt_amp_traspaso_detalle` \r\n" + "         where\r\n"
				+ "            alt_amp_traspaso_detalle.tipo = 'material' \r\n" + "         GROUP BY\r\n"
				+ "            alt_amp_traspaso_detalle.id_articulo\r\n" + "      )\r\n"
				+ "      alt_amp_traspaso_detalle2 \r\n"
				+ "      on resultado2.id_material = alt_amp_traspaso_detalle2.id_articulo \r\n" + "   LEFT JOIN\r\n"
				+ "      alt_amp_traspaso \r\n"
				+ "      on alt_amp_traspaso.id_traspaso = alt_amp_traspaso_detalle2.id_traspaso \r\n"
				+ "   left join\r\n" + "      alt_amp_almacen_logico al2 \r\n"
				+ "      on al2.id_almacen_logico = alt_amp_traspaso.id_almacen_logico_destino \r\n"
				+ "      and alt_amp_traspaso.id_almacen_logico_destino = \r\n" + "      (\r\n" + "         select\r\n"
				+ "            id_almacen_logico \r\n" + "         from\r\n" + "            alt_amp_almacen_logico \r\n"
				+ "         where\r\n" + "            tipo = 2 LIMIT 1 \r\n" + "      )\r\n"
				+ "			where resultado2.id_material=" + IdArticulo + "\r\n" + ";").getResultList();

	}
	
	
	@Transactional
	@Override
	public AmpAlmacenLogico EntradaSalida(){
		return (AmpAlmacenLogico) em.createQuery("from AmpAlmacenLogico where nombreAlmacenLogico='apartados' and tipo=2").setMaxResults(1).getSingleResult();
		
	}
	
	@Transactional
	@Override
	public boolean VerificarAlmacenApartados(String material) {
		boolean respuesta=false;      
		List resultado = em.createNativeQuery("SELECT\r\n"
				+ "	al.nombre_almacen_logico,\r\n"
				+ "	al.tipo\r\n"
				+ "FROM\r\n"
				+ "	alt_amp_multialmacen am,\r\n"
				+ "	alt_amp_almacen_logico al \r\n"
				+ "WHERE\r\n"
				+ "	am.id_almacen_logico = al.id_almacen_logico \r\n"
				+ "	AND am.id_articulo = "+material+" \r\n"
				+ "	and al.tipo=2\r\n"
				+ "	AND am.tipo = 'material'\r\n"
				+ "	and al.nombre_almacen_logico=\"apartados\"\r\n"
				+ "").getResultList();
		      if(resultado.size()>0) {
		    	  respuesta=true;
		    	  
		      }else {
		    	  
		    	  respuesta=false;
		      }
		return respuesta;
	}

}
