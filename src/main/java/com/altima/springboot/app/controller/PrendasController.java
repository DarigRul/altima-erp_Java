package com.altima.springboot.app.controller;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.altima.springboot.app.models.entity.DisenioPrenda;
import com.altima.springboot.app.models.service.IComercialClienteService;

import com.altima.springboot.app.models.service.IDisenioImagenPrendaService;
import com.altima.springboot.app.models.service.IDisenioLookupService;
import com.altima.springboot.app.models.service.IDisenioMaterialService;
import com.altima.springboot.app.models.service.IDisenioPrendaClienteService;
import com.altima.springboot.app.models.service.IDisenioPrendaMarcadorService;
import com.altima.springboot.app.models.service.IDisenioPrendaService;
import com.altima.springboot.app.models.service.IUploadService;

@Controller
public class PrendasController {
	@Autowired
	IDisenioPrendaService disenioPrendaService;
	@Autowired
	IDisenioMaterialService disenioMaterialService;
	@Autowired
	IDisenioImagenPrendaService disenioImagenPrendaService;
	@Autowired
	IUploadService uFileService;
	@Autowired
	IDisenioLookupService disenioLookupService;
	@Autowired
	IComercialClienteService clienteService;
	@Autowired
	IDisenioPrendaClienteService prendaClienteService;
	@Autowired
	private IDisenioPrendaMarcadorService disenioPrendaMarcadorService;
	
	@GetMapping("prendas")
	public String listClothes(Model model, Map<String, Object> m) throws InterruptedException {
		model.addAttribute("prendas", disenioPrendaService.findAllMin());
		
		return "prendas";
	}
	
	@Secured({"ROLE_ADMINISTRADOR","ROLE_DISENIO_PRENDAS_LISTAR"})
	@GetMapping("/detalle-prenda/{id}")
	public String infoClothes(@PathVariable(value = "id") Long id, Model model, Map<String, Object> m) {
		DisenioPrenda prenda = disenioPrendaService.findOne(id);
		
		model.addAttribute("BuscarPrendaById", disenioPrendaService.BuscarPrendaById(id));
		model.addAttribute("ListaClientesPrenda", disenioPrendaService.ListaClientesPrenda(id));
		model.addAttribute("ImagenPrenda", disenioPrendaService.ImagenPrenda(id));
		model.addAttribute("ListaMarcadoresPrendas", disenioPrendaService.ListaMarcadoresPrendas(id));
		model.addAttribute("tipos", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("materiales", disenioMaterialService.findAllForCreate());
		model.addAttribute("materialesPrenda", disenioMaterialService.findAllFromPrenda(id));
		model.addAttribute("patronajes", disenioMaterialService.findLookUps());
		model.addAttribute("patronajesPrenda", disenioMaterialService.findAllPatronajeFromPrenda(id));
		model.addAttribute("imagenesPrenda", disenioImagenPrendaService.findByPrenda(id));
		m.put("prenda", prenda);
		return "detalle-prenda";
	}
	
	@Secured({"ROLE_ADMINISTRADOR","ROLE_DISENIO_PRENDAS_AGREGAR"})
	@GetMapping("agregar-prenda")
	public String addClothes(Model model, Map<String, Object> m) {
		DisenioPrenda prenda = new DisenioPrenda();
		
		model.addAttribute("familias", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("materiales", disenioMaterialService.findAllForCreate());
		model.addAttribute("patronajes", disenioMaterialService.findLookUps());
		model.addAttribute("clientes", clienteService.findAll(null));
		model.addAttribute("generos", disenioLookupService.findByTipoLookup("Familia Genero"));
		model.addAttribute("marcadores", disenioLookupService.findByTipoLookup("Marcador"));
		model.addAttribute("imagenesPrenda", disenioImagenPrendaService.findByPrenda(0L));
		model.addAttribute("view", false);
		m.put("prenda", prenda);
		m.put("accion", "agregar");
		m.put("nombreOriginal", "Nuevo");
		return "agregar-confirmar-prenda";
	}
	
	@Secured({"ROLE_ADMINISTRADOR","ROLE_DISENIO_PRENDAS_CONFIRMAR_PRENDA"})
	@RequestMapping(value = "/confirmar-prenda/{id}")
	public String confirmar(@PathVariable(value = "id") Long id, Model model, Map<String, Object> m) {
		DisenioPrenda disenio = new DisenioPrenda();
		DisenioPrenda prenda = disenioPrendaService.findOne(id);
		model.addAttribute("marcadores", disenioLookupService.findByTipoLookup("Marcador"));
		model.addAttribute("familias", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("materiales", disenioMaterialService.findAllForCreate());
		model.addAttribute("patronajes", disenioMaterialService.findLookUps());
		model.addAttribute("prenda", prenda);
		m.put("disenio", disenio);
		m.put("accion", "confirmar");
		return "agregar-confirmar-prenda";
	}
	
	@Secured({"ROLE_ADMINISTRADOR","ROLE_DISENIO_PRENDAS_EDITAR"})
	@RequestMapping(value = "/editar-prenda/{id}")
	public String editar(@PathVariable(value = "id") Long id, Model model, Map<String, Object> m) {
		DisenioPrenda disenio = new DisenioPrenda();
		DisenioPrenda prenda = disenioPrendaService.findOne(id);
		model.addAttribute("prendas", disenioPrendaService.findAll());
		model.addAttribute("tipos", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("marcadores", disenioLookupService.findByTipoLookup("Marcador"));
		model.addAttribute("prendasmarcadores", disenioPrendaMarcadorService.findByIdPrenda(id));
		model.addAttribute("familias", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("materiales", disenioMaterialService.findAllForCreate());
		model.addAttribute("materialesPrenda", disenioMaterialService.findAllFromPrenda(id));
		model.addAttribute("patronajes", disenioMaterialService.findLookUps());
		model.addAttribute("patronajesPrenda", disenioMaterialService.findAllPatronajeFromPrenda(id));
		model.addAttribute("imagenesPrenda", disenioImagenPrendaService.findByPrenda(id));
		model.addAttribute("clientes", clienteService.findAll(null));
		model.addAttribute("clientesPrenda", prendaClienteService.findAllByPrenda(id));
		model.addAttribute("generos", disenioLookupService.findByTipoLookup("Familia Genero"));
		model.addAttribute("prenda", prenda);
		m.put("accion", "editar");
		m.put("disenio", disenio);
		model.addAttribute("view", false);

		m.put("nombreOriginal", prenda.getDescripcionPrenda());
		
		return "agregar-confirmar-prenda";
	}
	
	@Secured({"ROLE_ADMINISTRADOR","ROLE_DISENIO_PRENDAS_COPIAR"})
	@RequestMapping(value = "/copiar-prenda/{id}")
	public String copiar(@PathVariable(value = "id") Long id, Model model, Map<String, Object> m) {
		DisenioPrenda disenio = new DisenioPrenda();
		DisenioPrenda prenda = disenioPrendaService.findOne(id);
		model.addAttribute("prendas", disenioPrendaService.findAll());
		model.addAttribute("tipos", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("marcadores", disenioLookupService.findByTipoLookup("Marcador"));
		model.addAttribute("prendasmarcadores", disenioPrendaMarcadorService.findByIdPrenda(id));
		model.addAttribute("familias", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("materiales", disenioMaterialService.findAllForCreate());
		model.addAttribute("materialesPrenda", disenioMaterialService.findAllFromPrenda(id));
		model.addAttribute("patronajes", disenioMaterialService.findLookUps());
		model.addAttribute("patronajesPrenda", disenioMaterialService.findAllPatronajeFromPrenda(id));
		model.addAttribute("imagenesPrenda", disenioImagenPrendaService.findByPrenda(0L));
		//model.addAttribute("imagenesPrenda", disenioImagenPrendaService.findByPrenda(id));
		model.addAttribute("clientes", clienteService.findAll(null));
		model.addAttribute("clientesPrenda", prendaClienteService.findAllByPrenda(id));
		model.addAttribute("generos", disenioLookupService.findByTipoLookup("Familia Genero"));
		model.addAttribute("prenda", prenda);
		m.put("accion", "copiar");
		m.put("disenio", disenio);
		model.addAttribute("view", false);
		
		return "agregar-confirmar-prenda";
	}
	
	@GetMapping(value = "/uploads/prendas/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		
		Resource recurso = null;
		try {
			recurso = uFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	private String currentDate() {
        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("America/Mexico_City");
        DateFormat hourdateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        hourdateFormat.setTimeZone(timeZone);
        String sDate = hourdateFormat.format(date);
        return sDate;
    }
	
	@RequestMapping(value = "/view-prenda/{id}")
	public String verPrenda(@PathVariable(value = "id") Long id, Model model, Map<String, Object> m) {
		DisenioPrenda disenio = new DisenioPrenda();
		DisenioPrenda prenda = disenioPrendaService.findOne(id);
		model.addAttribute("prendas", disenioPrendaService.findAll());
		model.addAttribute("tipos", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("marcadores", disenioLookupService.findByTipoLookup("Marcador"));
		model.addAttribute("prendasmarcadores", disenioPrendaMarcadorService.findByIdPrenda(id));
		model.addAttribute("familias", disenioMaterialService.findAllFamiliaPrenda());
		model.addAttribute("materiales", disenioMaterialService.findAllForCreate());
		model.addAttribute("materialesPrenda", disenioMaterialService.findAllFromPrenda(id));
		model.addAttribute("patronajes", disenioMaterialService.findLookUps());
		model.addAttribute("patronajesPrenda", disenioMaterialService.findAllPatronajeFromPrenda(id));
		model.addAttribute("imagenesPrenda", disenioImagenPrendaService.findByPrenda(id));
		model.addAttribute("clientes", clienteService.findAll(null));
		model.addAttribute("clientesPrenda", prendaClienteService.findAllByPrenda(id));
		model.addAttribute("generos", disenioLookupService.findByTipoLookup("Familia Genero"));
		model.addAttribute("prenda", prenda);
		m.put("accion", "editar");
		m.put("view", true);
		m.put("disenio", disenio);
		m.put("nombreOriginal", prenda.getDescripcionPrenda());
		
		return "agregar-confirmar-prenda";
	}
}
