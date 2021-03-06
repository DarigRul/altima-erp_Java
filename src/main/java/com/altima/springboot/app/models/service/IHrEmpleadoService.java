package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.dto.AgenteVentasListDTO;
import com.altima.springboot.app.models.entity.HrEmpleado;

public interface IHrEmpleadoService {

	List<HrEmpleado> findAll();

	void save(HrEmpleado hrempleado);

	void delete(Long id);

	HrEmpleado findOne(Long id);
	
	List<Object> findAllByPuesto(String nombrePuesto);
	
	List<Object> findAllByDepartamento(String nombreDepartamento);
	
	List<Object> findEmpleadoPersona();

	Object findEmpleadoById(Long id);

	List<HrEmpleado> findAllEmpleado();

	List<Object> findAllByPuestoWithoutAgenteLogued(String nombrePuesto, Long idAgente);

	List<Object[]> findAllByPuestoDepartamentoArea(Long idPuesto, Long idDepartamento, Long idLookup);

	Object[] findDatosPuesto(Long idEmpleado);

	public List<AgenteVentasListDTO> findAllAgenteVentas();

	List<HrEmpleado> findEmpleadosSelect();
}
