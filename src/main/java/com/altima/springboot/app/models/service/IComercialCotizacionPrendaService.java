package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.models.entity.ComercialCotizacionPrenda;

public interface IComercialCotizacionPrendaService {

	void save(ComercialCotizacionPrenda comercialCotizacionPrenda);
	
	ComercialCotizacionPrenda findOne (Long id);
	
	List<ComercialCotizacionPrenda> findAll (Long id);
	
	Object[] FindDatosCotizacionPrenda (Long idTela, Long idModelo, Long idPrenda, Long idFamComposicion);
	
	List<Object[]> FindCotizacionPrendas(Long id, int tipoCotizacion);
	
	double findSubtotalCotizacionPrendas(Long id);
	
	void removePrendas (List<ComercialCotizacionPrenda> comercialCotizacionPrenda);

	double subtotalPrendas(Long idCotizacion);

	List<Object[]> findPrendasByCotizacion(Long id);
}
