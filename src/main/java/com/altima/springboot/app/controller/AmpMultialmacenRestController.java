package com.altima.springboot.app.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.altima.springboot.app.dto.ArticulosMultialmacenDto;
import com.altima.springboot.app.models.entity.AmpMultialmacen;
import com.altima.springboot.app.models.service.IAmpMultialmacenService;

@RestController
public class AmpMultialmacenRestController {
	@Autowired
	IAmpMultialmacenService AmpMultialmacenService;

	@GetMapping("/multialmacen-articulos")
	public List<Object[]> MultialmacenArticulos(Long articulo, String tipo) {

		return AmpMultialmacenService.findAllAMPLogicItem(articulo, tipo);
	}

	@PostMapping("/guardar-multialmacen")
	public boolean GuardarMultialmacen(Long AlmacenLogico, String Tipo, Long Articulo) {
		boolean result = false;
		String TipoPost = null;
		if (Tipo.contains("m")) {
			TipoPost = "material";
		} else if (Tipo.contains("aa")) {
			TipoPost = "materialAlmacen";

		} else if (Tipo.contains("f")) {
			TipoPost = "forro";

		} else if (Tipo.contains("t")) {

			TipoPost = "tela";
		}

		if (AmpMultialmacenService.findDuplicates(TipoPost, AlmacenLogico, Articulo).size() <= 0) {
			try {
				AmpMultialmacen multialmacen = new AmpMultialmacen();
				multialmacen.setIdAlmacenLogico(AlmacenLogico);
				multialmacen.setIdArticulo(Articulo);
				multialmacen.setTipo(TipoPost);
				multialmacen.setExistencia(0L);
				multialmacen.setEstatus("1");
				AmpMultialmacenService.save(multialmacen);
				result = true;
			} catch (Exception e) {
				result = false;
			}

		} else {

			result = false;
		}

		return result;
	}

	@DeleteMapping("/eliminar-multialmacen")
	public boolean EliminarMultialmacen(Long idmultialmacen) {
		boolean result = false;
		try {
			AmpMultialmacenService.deleteById(idmultialmacen);
			result = true;
		} catch (Exception e) {
			// TODO: handle exception
			result = false;
		}
		return result;
	}

	@GetMapping("/getArticulosMultialmacen")
	public List<ArticulosMultialmacenDto> MultialmacenArticulos(Long idAlmacenLogico) {

		return AmpMultialmacenService.findArticulosByMultialmacen(idAlmacenLogico);
	}
	
	@GetMapping("/multialmacen-articulos-disponibles")
	public Integer disponibles(Long articulo, String tipo) {

		return AmpMultialmacenService.disponibles(articulo, tipo);
	}

	@GetMapping("getExistenciaArticulo")
	public Float getExistenciaArticulo(@RequestParam Long idAlmacenLogico,@RequestParam Long idArticulo,@RequestParam String Tipo) {
		return AmpMultialmacenService.existenciaArticuloByAlmacen(idAlmacenLogico, idArticulo, Tipo);
	}

	@GetMapping("getExistArticuloInAlmacen")
	public String getExistArticuloInAlmacen(@RequestParam Long idAlmacenLogico,@RequestParam Long idArticulo,@RequestParam String Tipo) {
		return AmpMultialmacenService.existArticuloInAlmacen(idAlmacenLogico, idArticulo, Tipo);
	}
}
