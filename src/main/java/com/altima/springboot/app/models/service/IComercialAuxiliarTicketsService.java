package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.dto.ComercialTicketDTO;
import com.altima.springboot.app.models.entity.ComercialMovimiento;
import com.altima.springboot.app.models.entity.ComercialTicket;
import com.altima.springboot.app.models.entity.ComercialTicketEstatus;
import com.altima.springboot.app.models.entity.DisenioCalidad;
import com.altima.springboot.app.models.entity.HrCalendario;

public interface IComercialAuxiliarTicketsService {

	
	String user (String user);
	
	 List<Object[]> VentasDep (String condicion);
	 List<Object[]> Activo (String user);
	 
	 boolean Verificar_Solicitante (String puesto);
	 
	 List<Object[]> Categoria (String user);
	 
	 void save(ComercialTicket ticket);

		void delete(Long id);

		ComercialTicket findOne(Long id);
		
	List<Object[]> view ( Long id);
	
	List<Object[]> viewEstatus ( Long id );
	
	void saveSeguimiento (ComercialTicketEstatus estatus);
	
	 Integer idUsuario(String user);
	 
	 ComercialTicketEstatus findOneEstatus(Long id);
	 
	 String Verificar_Estatus (Long id);
	 
	 List<Object[]> detalles_estatus (Long id);

	 List<ComercialTicket> findAllTicket();
	 
	 Long AleatorioAuxiliar(String fechaInicio , String fechaFin);
	 
	 Long AleatorioAuxiliarMasivo(String validacion);
}
