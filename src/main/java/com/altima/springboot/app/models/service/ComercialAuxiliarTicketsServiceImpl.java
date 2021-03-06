package com.altima.springboot.app.models.service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.dto.ComercialTicketDTO;
import com.altima.springboot.app.models.entity.ComercialTicket;
import com.altima.springboot.app.models.entity.ComercialTicketEstatus;
import com.altima.springboot.app.repository.ComercialClienteRepository;
import com.altima.springboot.app.repository.ComercialTicketEstatusRepository;
import com.altima.springboot.app.repository.ComercialTicketRepository;

@Service
public class ComercialAuxiliarTicketsServiceImpl implements IComercialAuxiliarTicketsService {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	ComercialTicketEstatusRepository repository2;
	
	@Autowired
	private ComercialTicketRepository repository;
	
	@Transactional(readOnly = true)
	@Override
	public String user(String user) {
	
		String re = em
				.createNativeQuery(""
						+ "SELECT\r\n" + 
						"	puesto.nombre_puesto  \r\n" + 
						"FROM\r\n" + 
						"	alt_hr_usuario AS usuario,\r\n" + 
						"	alt_hr_empleado AS empleado,\r\n" + 
						"	alt_hr_puesto AS puesto,\r\n" + 
						"	alt_hr_departamento AS depa \r\n" + 
						"WHERE\r\n" + 
						"	1 = 1 \r\n" + 
						"	AND usuario.id_empleado = empleado.id_empleado \r\n" + 
						"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
						"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
						"	AND usuario.nombre_usuario = '"+user+"'")
				.getSingleResult().toString();
		return re;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> VentasDep(String condicion) {
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	empleado.id_empleado,\r\n" + 
				"	CONCAT(puesto.nombre_puesto,' ',empleado.nombre_persona)\r\n" + 
				"FROM\r\n" + 
				"	alt_hr_empleado AS empleado,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND depa.nombre_departamento in ('VENTAS')\r\n" + 
				"	AND puesto.nombre_puesto "+condicion+" ('AYUDANTE DE VENTAS')").getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> Activo(String user) {
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	empleado.id_empleado,\r\n" + 
				"	CONCAT(puesto.nombre_puesto,' ',empleado.nombre_persona)\r\n" + 
				"FROM\r\n" + 
				"	alt_hr_usuario AS usuario,\r\n" + 
				"	alt_hr_empleado AS empleado,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND usuario.id_empleado = empleado.id_empleado \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND usuario.nombre_usuario = '"+user+"'").getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}

	@Override
	public boolean Verificar_Solicitante(String puesto) {
		try {
			String re = em.createNativeQuery(""
					+ "SELECT\r\n" + 
					"	TRUE\r\n" + 
					"FROM\r\n" + 
					"	alt_hr_puesto AS puesto,\r\n" + 
					"	alt_hr_departamento AS depa \r\n" + 
					"WHERE\r\n" + 
					"	1 = 1 \r\n" + 
					"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
					"	AND depa.nombre_departamento in ('VENTAS')\r\n" + 
					"	AND puesto.nombre_puesto not in ('AYUDANTE DE VENTAS')\r\n" + 
					"	and nombre_puesto in ('"+puesto+"')").getSingleResult().toString();
					
			return true;
			}
			catch(Exception e) {
				
				return false;
			}
	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> Categoria(String user) {
		String condicion ="";
		if ( user.equals("AYUDANTE DE VENTAS")) {
			condicion="AND look.atributo_1=1";
		}
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	look.id_lookup,\r\n" + 
				"	look.nombre_lookup\r\n" + 
				"FROM\r\n" + 
				"	alt_comercial_lookup as look\r\n" + 
				"WHERE\r\n" + 
				"	1 = 1\r\n" + 
				"	and look.tipo_lookup='Categoria'\r\n" + 
				"	and look.estatus=1 "+
				condicion).getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}

	@Override
	public void save(ComercialTicket ticket) {
		repository.save(ticket);
		
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
		
	}

	@Override
	public ComercialTicket findOne(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> view(Long id) {
		String condicion="";
		if (id == null) {
			
		}else {
			condicion="	and (ticket.id_empleado_solicitante = "+id+" or ticket.id_empleado_auxiliar="+id+")\r\n" ;
		}
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	ticket.id_ticket,\r\n" + 
				"	ticket.id_text,\r\n" + 
				"	empleado.nombre_persona as solicitante,\r\n" + 
				"	empleado2.nombre_persona as auxuliar,\r\n" + 
				"	DATE_FORMAT( ticket.fecha_creacion, '%d/%m/%Y %H:%i:%s' ),\r\n" + 
				"	look.nombre_lookup,\r\n" + 
				"	IFNULL((\r\n" + 
				"		SELECT\r\n" + 
				"			estatus.estatus_nombre \r\n" + 
				"		FROM\r\n" + 
				"			alt_comercial_ticket_estatus AS estatus \r\n" + 
				"		WHERE\r\n" + 
				"			1 = 1 \r\n" + 
				"			AND estatus.id_ticket = ticket.id_ticket \r\n" + 
				"			AND estatus.estatus = 1 \r\n" + 
				"		ORDER BY\r\n" + 
				"			estatus.id_ticket_estatus DESC \r\n" + 
				"			LIMIT 1 \r\n" + 
				"			),\r\n" + 
				"		\"En espera\" \r\n" + 
				"	),\r\n" + 
				"	IFNULL((\r\n" + 
				"		SELECT\r\n" + 
				"		DATE_FORMAT(estatus.fecha_creacion, '%d/%m/%Y %H:%i:%s' )\r\n" + 
				"			\r\n" + 
				"		FROM\r\n" + 
				"			alt_comercial_ticket_estatus AS estatus \r\n" + 
				"		WHERE\r\n" + 
				"			1 = 1 \r\n" + 
				"			AND estatus.id_ticket = ticket.id_ticket \r\n" + 
				"			AND estatus.estatus = 1 \r\n" + 
				"			and (estatus.estatus_nombre='Realizado' || estatus.estatus_nombre='Cancelado' )\r\n" + 
				"		ORDER BY\r\n" + 
				"			estatus.id_ticket_estatus DESC \r\n" + 
				"			LIMIT 1 \r\n" + 
				"			),\r\n" + 
				"		\"\" \r\n" + 
				"	)\r\n" + 
				"FROM\r\n" + 
				"	alt_comercial_ticket AS ticket,\r\n" + 
				"	alt_comercial_lookup AS look,\r\n" + 
				"	alt_hr_empleado AS empleado,\r\n" + 
				"	alt_hr_empleado AS empleado2\r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND ticket.id_empleado_solicitante = empleado.id_empleado \r\n" + 
				"	AND ticket.id_empleado_auxiliar = empleado2.id_empleado \r\n" + 
				"	AND look.id_lookup = ticket.id_lookup \r\n" + 
				"	AND ticket.estatus=1\r\n" + 
				condicion +
				"ORDER BY\r\n" + 
				"	ticket.id_ticket DESC").getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> viewEstatus(Long id) {
		
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	estatus.id_ticket_estatus,\r\n" + 
				"	DATE_FORMAT( estatus.fecha_creacion, '%d/%m/%Y %H:%i:%s' ),\r\n" + 
				"	estatus.estatus_nombre,\r\n" + 
				"	estatus.comentario,\r\n" + 
				"IF\r\n" + 
				"	( estatus.id_empleado = 0, \"ADMIN\", empleado.nombre_persona ) \r\n" + 
				"FROM\r\n" + 
				"	alt_comercial_ticket_estatus AS estatus,\r\n" + 
				"	alt_hr_empleado AS empleado \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND ( estatus.id_empleado = empleado.id_empleado || estatus.id_empleado = 0 ) \r\n" + 
				"	AND estatus.id_ticket = "+id+" \r\n" + 
				"	AND estatus.estatus = 1 \r\n" + 
				"GROUP BY\r\n" + 
				"	estatus.id_ticket_estatus"+
				" 	ORDER BY  estatus.id_ticket_estatus desc").getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}
	
	@Override
	public void saveSeguimiento(ComercialTicketEstatus estatus) {
		repository2.save(estatus);
		
	}
	
	@Transactional(readOnly = true)
	@Override
	public Integer idUsuario(String user) {
		String re = em.createNativeQuery(
				"select id_empleado from alt_hr_usuario where nombre_usuario ='"+user+"'")
				.getSingleResult().toString();
		return Integer.parseInt(re);

	}
	
	@Override
	public ComercialTicketEstatus findOneEstatus(Long id) {
		return repository2.findById(id).orElse(null);
	}
	
	
	@Override
	public String Verificar_Estatus(Long id) {
		try {
			String re = em.createNativeQuery(""
					+ "SELECT\r\n" + 
					"	estatus.estatus_nombre \r\n" + 
					"FROM\r\n" + 
					"	alt_comercial_ticket_estatus AS estatus \r\n" + 
					"WHERE\r\n" + 
					"	1 = 1 \r\n" + 
					"	AND estatus.id_ticket = "+id+" \r\n" + 
					"	AND estatus.estatus = 1 \r\n" + 
					"	AND (estatus.estatus_nombre = 'Cancelado' || estatus.estatus_nombre = 'Realizado')\r\n" + 
					"	LIMIT 1").getSingleResult().toString();
					
			return re;
			}
			catch(Exception e) {
				
				return null;
			}
	
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> detalles_estatus(Long id) {
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	CONCAT( solicitante.nombre_persona, ' ', solicitante.apellido_paterno, ' ', solicitante.apellido_materno ),\r\n" + 
				"	CONCAT( auxiliar.nombre_persona, ' ', auxiliar.apellido_paterno, ' ', auxiliar.apellido_materno ),\r\n" + 
				"	look.nombre_lookup,\r\n" + 
				"	ticket.descripcion,\r\n" + 
				"	DATE_FORMAT( ticket.fecha_inicio, '%d/%m/%Y %H:%i:%s' ),\r\n" + 
				"	DATE_FORMAT( ticket.fecha_fin, '%d/%m/%Y %H:%i:%s' ) ,\r\n" + 
				"	(\r\n" + 
				"	SELECT\r\n" + 
				"		estatus.estatus_nombre \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_ticket_estatus AS estatus \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND estatus.id_ticket = ticket.id_ticket \r\n" + 
				"		AND estatus.estatus = 1 \r\n" + 
				"	ORDER BY\r\n" + 
				"		estatus.id_ticket_estatus DESC \r\n" + 
				"		LIMIT 1 \r\n" + 
				"	) \r\n" + 
				"FROM\r\n" + 
				"	alt_comercial_ticket AS ticket,\r\n" + 
				"	alt_hr_empleado AS solicitante,\r\n" + 
				"	alt_hr_empleado AS auxiliar,\r\n" + 
				"	alt_comercial_lookup AS look \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND ticket.id_empleado_solicitante = solicitante.id_empleado \r\n" + 
				"	AND ticket.id_empleado_auxiliar = auxiliar.id_empleado \r\n" + 
				"	AND ticket.id_lookup = look.id_lookup \r\n" + 
				"	AND ticket.id_ticket = "+id).getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}

	@Override
	@Transactional
	public List<ComercialTicket> findAllTicket() {
		return em.createQuery("From ComercialTicket ct where ct.estatus=1 and ct.fechaInicio is not null and ct.fechaFin is not null").getResultList();
	}

	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long AleatorioAuxiliar(String fechaInicio , String fechaFin) {
	
		List<Long> auxuliares = em.createNativeQuery("SELECT\r\n" + 
				"	empleado.id_empleado \r\n" + 
				"FROM\r\n" + 
				"	alt_hr_empleado AS empleado,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND depa.nombre_departamento IN ( 'VENTAS' ) \r\n" + 
				"	AND puesto.nombre_puesto IN ('AYUDANTE DE VENTAS')").getResultList();
		
		List<Long> disponibles = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	empleado.id_empleado \r\n" + 
				"FROM\r\n" + 
				"	alt_hr_empleado AS empleado\r\n" + 
				"	LEFT OUTER JOIN (\r\n" + 
				"	SELECT\r\n" + 
				"		ticket.id_empleado_auxiliar \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_ticket AS ticket \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND ticket.fecha_inicio  BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"' \r\n" + 
				"		AND ticket.fecha_fin  BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"' \r\n" + 
				
			/*	"	GROUP BY	ticket.id_empleado_auxiliar \r\n"+
				"	ORDER BY\r\n" + 
				"		ticket.id_ticket DESC \r\n" + 
				"		LIMIT "+auxuliares.size()+" \r\n" + */
				
				"	) AS t ON empleado.id_empleado = t.id_empleado_auxiliar,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND depa.nombre_departamento IN ( 'VENTAS' ) \r\n" + 
				"	AND puesto.nombre_puesto IN ( 'AYUDANTE DE VENTAS' ) \r\n" + 
				"	AND t.id_empleado_auxiliar IS NULL").getResultList();
		
	

		
		/*List<Long> disponiblesx2 = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	empleado.id_empleado \r\n" + 
				"FROM\r\n" + 
				"	alt_hr_empleado AS empleado,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND depa.nombre_departamento IN ( 'VENTAS' ) \r\n" + 
				"	AND puesto.nombre_puesto IN ( 'AYUDANTE DE VENTAS' ) \r\n" + 
				"	AND NOT EXISTS (\r\n" + 
				"	SELECT\r\n" + 
				"		ticket.id_empleado_auxiliar \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_ticket AS ticket \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		AND ticket.id_empleado_auxiliar = empleado.id_empleado \r\n" + 
				"		AND ticket.fecha_inicio NOT BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"' \r\n" + 
				"		AND ticket.fecha_fin NOT BETWEEN '"+fechaInicio+"' AND '"+fechaFin+"' )").getResultList();*/
		
	
		
		if (disponibles.isEmpty() ) {
			System.out.println("Se acabaron los disponibles");
			/*int pos=0;
		    int nCartas = disponiblesx2.size() ;
		    Stack < Integer > pCartas = new Stack < Integer > ();
		    for (int i = 0; i < nCartas ; i++) {
		      pos = (int) Math.floor(Math.random() * nCartas );
		      while (pCartas.contains(pos)) {
		        pos = (int) Math.floor(Math.random() * nCartas );
		      }
		      pCartas.push(pos);
		    }
			
		    return ((Number) disponiblesx2.get(pos)).longValue();*/
				return Long.valueOf(0);
		}else {
			System.out.println("Continuan disponibles");
			 	int pos=0;
			    int nCartas = disponibles.size(); 
			    Stack < Integer > pCartas = new Stack < Integer > ();
			    for (int i = 0; i < nCartas ; i++) {
			      pos = (int) Math.floor(Math.random() * nCartas );
			      while (pCartas.contains(pos)) {
			        pos = (int) Math.floor(Math.random() * nCartas );
			      }
			      pCartas.push(pos);
			    }
			    return ((Number) disponibles.get(pos)).longValue();
			  }
			
			
			
	        

		
			
			
			 
			
		}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long AleatorioAuxiliarMasivo(String validacion) {
	
		List<Long> auxuliares = em.createNativeQuery("SELECT\r\n" + 
				"	empleado.id_empleado \r\n" + 
				"FROM\r\n" + 
				"	alt_hr_empleado AS empleado,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND depa.nombre_departamento IN ( 'VENTAS' ) \r\n" + 
				"	AND puesto.nombre_puesto IN ('AYUDANTE DE VENTAS')").getResultList();
		
		
	
		
		List<Long> disponibles = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	empleado.id_empleado \r\n" + 
				"FROM\r\n" + 
				"	alt_hr_empleado AS empleado\r\n" + 
				"	LEFT OUTER JOIN (\r\n" + 
				"	SELECT\r\n" + 
				"		ticket.id_empleado_auxiliar \r\n" + 
				"	FROM\r\n" + 
				"		alt_comercial_ticket AS ticket \r\n" + 
				"	WHERE\r\n" + 
				"		1 = 1 \r\n" + 
				"		"+validacion+" \r\n"+ 
			/*	"	GROUP BY	ticket.id_empleado_auxiliar \r\n"+
				"	ORDER BY\r\n" + 
				"		ticket.id_ticket DESC \r\n" + 
				"		LIMIT "+auxuliares.size()+" \r\n" + */
				"	) AS t ON empleado.id_empleado = t.id_empleado_auxiliar,\r\n" + 
				"	alt_hr_puesto AS puesto,\r\n" + 
				"	alt_hr_departamento AS depa \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND empleado.id_puesto = puesto.id_puesto \r\n" + 
				"	AND depa.id_departamento = puesto.id_departamento \r\n" + 
				"	AND depa.nombre_departamento IN ( 'VENTAS' ) \r\n" + 
				"	AND puesto.nombre_puesto IN ( 'AYUDANTE DE VENTAS' ) \r\n" + 
				"	AND t.id_empleado_auxiliar IS NULL").getResultList();
		

		
	
		
		if (disponibles.isEmpty() ) {
		
				return Long.valueOf(0);
		}else {
			System.out.println("Continuan disponibles");
			 	int pos=0;
			    int nCartas = disponibles.size(); 
			    Stack < Integer > pCartas = new Stack < Integer > ();
			    for (int i = 0; i < nCartas ; i++) {
			      pos = (int) Math.floor(Math.random() * nCartas );
			      while (pCartas.contains(pos)) {
			        pos = (int) Math.floor(Math.random() * nCartas );
			      }
			      pCartas.push(pos);
			    }
			    return ((Number) disponibles.get(pos)).longValue();
			  }
		}
		
	}
