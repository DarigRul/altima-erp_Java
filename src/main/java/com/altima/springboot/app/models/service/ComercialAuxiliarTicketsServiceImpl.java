package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<Object[]> Categoria() {
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	look.id_lookup,\r\n" + 
				"	look.nombre_lookup\r\n" + 
				"FROM\r\n" + 
				"	alt_comercial_lookup as look\r\n" + 
				"WHERE\r\n" + 
				"	1 = 1\r\n" + 
				"	and look.tipo_lookup='Categoria'\r\n" + 
				"	and look.estatus=1").getResultList();
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
	public List<Object[]> view() {
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT\r\n" + 
				"	ticket.id_ticket,\r\n" + 
				"	ticket.id_text,\r\n" + 
				"	empleado.nombre_persona,\r\n" + 
				"	ticket.fecha_inicio,\r\n" + 
				"	look.nombre_lookup,\r\n" + 
				"	'estatus',\r\n" + 
				"	ticket.fecha_fin \r\n" + 
				"FROM\r\n" + 
				"	alt_comercial_ticket AS ticket,\r\n" + 
				"	alt_comercial_lookup AS look,\r\n" + 
				"	alt_hr_empleado AS empleado \r\n" + 
				"WHERE\r\n" + 
				"	1 = 1 \r\n" + 
				"	AND ticket.id_empleado_solicitante = empleado.id_empleado \r\n" + 
				"	AND look.id_lookup = ticket.id_lookup \r\n" + 
				"	AND ticket.estatus =1").getResultList();
		return re;
		//AND material.nombre_material NOT IN ('Tela principal')
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> viewEstatus(Long id) {
		
		List<Object[]> re = em.createNativeQuery(""
				+ "SELECT \r\n" + 
				"estatus.id_ticket_estatus,\r\n" + 
				"DATE_FORMAT(estatus.fecha_creacion, '%d/%l/%Y %H:%i:%s'),\r\n" + 
				"estatus.estatus_nombre,\r\n" + 
				"estatus.comentario,\r\n" + 
				"empleado.nombre_persona\r\n" + 
				"FROM\r\n" + 
				"alt_comercial_ticket_estatus as estatus,\r\n" + 
				"alt_hr_empleado as empleado\r\n" + 
				"WHERE \r\n" + 
				"1=1\r\n" + 
				"and estatus.id_empleado = empleado.id_empleado\r\n" + 
				"and estatus.id_ticket ="+id+"\r\n" + 
				"and estatus.estatus=1").getResultList();
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
}