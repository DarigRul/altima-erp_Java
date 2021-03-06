package com.altima.springboot.app.models.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.models.entity.ComprasProveedorCredito;
import com.altima.springboot.app.repository.ComprasProveedorCreditoRepository;

@Service
public class ComprasProveedorCreditoServiceImpl implements IComprasProveedorCreditoService {

	@Autowired
	private ComprasProveedorCreditoRepository repository;
	
	@Autowired
	private EntityManager em;

	@Override
	@Transactional
	public void save(ComprasProveedorCredito comprasProveedorCredito) {
		repository.save(comprasProveedorCredito);
		
	}

	@Override
	@Transactional
	public ComprasProveedorCredito findByProveedor(Long id) {
		return (ComprasProveedorCredito) em.createQuery("FROM ComprasProveedorCredito WHERE IdProveedor = "+id).getSingleResult();
	}
}
