package com.altima.springboot.app.models.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.altima.springboot.app.dto.ArticulosMultialmacenDto;
import com.altima.springboot.app.dto.EntradasSalidasDTO;
import com.altima.springboot.app.models.entity.AmpAlmacenLogico;
import com.altima.springboot.app.models.entity.AmpMultialmacen;
import com.altima.springboot.app.repository.AmpMultialmacenRepository;

@Service
public class AmpMultialmacenServiceImpl implements IAmpMultialmacenService {
	@Autowired
	AmpMultialmacenRepository repository;

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public void save(AmpMultialmacen multialmacen){
		if(multialmacen.getExistencia()<0){
			System.out.println("si entra al error");
			throw new RuntimeException("La cantidad debe ser mayor o igual a 0");
		}
		repository.save(multialmacen);
	}

	@Override
	@Transactional
	public AmpMultialmacen findById(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public List<AmpMultialmacen> findAll() {
		return (List<AmpMultialmacen>) repository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<AmpAlmacenLogico> findAllActiveAMPLogic() {
		return em.createQuery("From AmpAlmacenLogico where estatus='1'").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Object[]> findAllAMPLogicItem(Long articulo, String tipo) {
		return em.createNativeQuery(
				"SELECT am.id_multialmacen,al.nombre_almacen_logico,am.id_articulo FROM alt_amp_multialmacen  am, alt_amp_almacen_logico al where am.id_almacen_logico=al.id_almacen_logico and am.id_articulo="
						+ articulo + " and am.tipo='" + tipo + "'")
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<AmpMultialmacen> findDuplicates(String tipoPost, Long almacenLogico, Long articulo) {

		return em.createQuery("From AmpMultialmacen where idArticulo=" + articulo + " and idAlmacenLogico="
				+ almacenLogico + " and tipo='" + tipoPost + "'  ").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ArticulosMultialmacenDto> findArticulosByMultialmacen(Long idAlmacenLogico) {
		// TODO Auto-generated method stub
		return em.createNativeQuery("call alt_pr_articulos_multialmacen("+idAlmacenLogico+")",ArticulosMultialmacenDto.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<EntradasSalidasDTO> findAllMovimientos() {
		// TODO Auto-generated method stub
		return em.createNativeQuery("call alt_pr_movimientos_amp()",EntradasSalidasDTO.class).getResultList();
	}

	@Override
	@Transactional
	public Long findIdMultialmacen(Long idAlmacenLogico, Long idArticulo, String tipo) {
		// TODO Auto-generated method stub
		return repository.findByIdAlmacenLogicoAndTipoAndIdArticulo(idAlmacenLogico, tipo, idArticulo).getIdAMultialmacen();
	}


}
