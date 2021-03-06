package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.models.entity.ComercialCoordinadoPreapartado;
import com.altima.springboot.app.models.entity.ComercialPreApartado;
import com.altima.springboot.app.models.entity.ComercialPrendaPreapartado;
import com.altima.springboot.app.models.entity.ComercialTelasPreapartado;

public interface IComercialPreApartadoService {

	void save (ComercialPreApartado comercialPreApartado);
	
	List<Object[]> findPreapartados (String nombreSUsuario);
	
	ComercialPreApartado findOne (Long id);
	
	List<ComercialCoordinadoPreapartado> findCoordinadosByPreapartado(Long id);
	
	void saveCoordinado(ComercialCoordinadoPreapartado comercialCoordinadoPreapartado);
	
	ComercialCoordinadoPreapartado findCoordinado(Long id);
	
	void deleteCoordinado(Long id);
	
	List<ComercialPrendaPreapartado> findPrendasByCoordinados(Long id);
	
	void savePrendaCoordinado(ComercialPrendaPreapartado comercialPrendaPreapartado);
	
	ComercialPrendaPreapartado findPrendaCoordinado(Long id);
	
	void deletePrendaCoordinado(Long id);
	
	List<Object[]> reportePreapartados(Long id);
	
	void saveTelasCoordinado(ComercialTelasPreapartado comercialTelasPreapartado);
	
	void deleteTelasCoordinado(Long id);
	
	ComercialTelasPreapartado findTelaCoordinado(Long id);
	
}
