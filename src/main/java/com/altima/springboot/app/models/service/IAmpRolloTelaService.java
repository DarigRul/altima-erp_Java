package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.models.entity.AmpRolloTela;

public interface IAmpRolloTelaService {
    
    List<AmpRolloTela> findAll();

	void save(AmpRolloTela salida);

	void delete(Long id);

	AmpRolloTela findOne(Long id);

	List<AmpRolloTela> findByIdAlmacenFisico(Long idAlmacenFisico,Long idTela);
}
