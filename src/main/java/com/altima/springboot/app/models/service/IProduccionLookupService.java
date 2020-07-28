package com.altima.springboot.app.models.service;

import java.util.List;

import com.altima.springboot.app.models.entity.DisenioLookup;
import com.altima.springboot.app.models.entity.ProduccionLookup;

public interface IProduccionLookupService {
	List<ProduccionLookup> findAll();

	void save(ProduccionLookup ProduccionLookup);

	void delete(Long id);

	ProduccionLookup findOne(Long id);

	boolean findDuplicate(String Lookup,String Tipo);

	List<ProduccionLookup> findAllLookup(String Tipo);

	ProduccionLookup findLastLookupByType(String Tipo);

	boolean findDuplicate(String Lookup, String Tipo, String atributo1,String atributo2, String descripcion);

	List<ProduccionLookup> findAllByType(String Tipo);

}