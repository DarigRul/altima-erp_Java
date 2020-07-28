package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.altima.springboot.app.models.entity.AmpLookup;
import com.altima.springboot.app.repository.AmpLookupRepository;

@Service
public class AmpLoookupServiceImpl implements IAmpLoookupService {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private AmpLookupRepository repository;
	@Override
	@Transactional(readOnly=true)
	public List<AmpLookup> findAll() {
		// TODO Auto-generated method stub
		return (List<AmpLookup>) repository.findAll();
	}

	@Override
	@Transactional
	public void save(AmpLookup lookup) {
		// TODO Auto-generated method stub
		repository.save(lookup);

	}

	@Override
	@Transactional
	public void delete(Long id) {
		// TODO Auto-generated method stub
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public AmpLookup findOne(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).orElse(null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	@OrderBy("idLookup ASC")
	public List<AmpLookup> findAllLookup(String Tipo) {
		return em.createQuery("from AmpLookup where tipo_lookup='"+Tipo+"'").getResultList();
	}
	
	@Override
	@Transactional
	public AmpLookup findLastLookupByType(String Tipo){
		return (AmpLookup) em.createQuery("from AmpLookup where tipo_lookup='"+Tipo+"' ORDER BY idLookup DESC").setMaxResults(1).getSingleResult();
	}
	 
	
	@Override
	@Transactional
	public boolean findDuplicate(String Lookup,String Tipo){
		boolean duplicate;
		@SuppressWarnings("unchecked")
		
		List<AmpLookup> result = em.createQuery("from AmpLookup where nombreLookup='"+Lookup+"' and tipoLookup='"+Tipo+"'").getResultList();
		if(result.isEmpty()) {
			duplicate=false;
			
		}
		else {
			duplicate=true;
		}
		 return duplicate;
	}
	
	@Override
	@Transactional
	public boolean findDuplicate(String Lookup,String Tipo,String atributo){
		
		boolean duplicate;
		@SuppressWarnings("unchecked")
		List<AmpLookup> result = em.createQuery("from AmpLookup where nombreLookup='"+Lookup+"' and tipoLookup='"+Tipo+"' and atributo1='"+atributo+"'").getResultList();
		if(result.isEmpty()) {
			duplicate=false;
		}
		else {
			duplicate=true;
		}
		 return duplicate;
	}

}