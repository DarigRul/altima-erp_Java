package com.altima.springboot.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.altima.springboot.app.models.entity.ComercialCoordinadoPrenda;
import com.altima.springboot.app.models.entity.ComercialPedidoInformacion;
import com.altima.springboot.app.models.service.ComercialClienteEmpleadoService;
import com.altima.springboot.app.models.service.ICargaPedidoService;
import com.altima.springboot.app.models.service.IComercialCalendarioService;
import com.altima.springboot.app.models.service.IComercialClienteService;
import com.altima.springboot.app.models.service.IComercialConcentradoPrendasService;
import com.altima.springboot.app.models.service.IComercialCoordinadoService;
import com.altima.springboot.app.models.service.IComercialPrendaBordadoService;

@CrossOrigin(origins = { "*" })
@Controller
public class ExpedienteController {

	@Autowired
	IComercialCalendarioService calendarioservice;
	@Autowired
	private IComercialClienteService clienteservice;
	@Autowired
	private ICargaPedidoService cargaPedidoService;
	@Autowired
	private ComercialClienteEmpleadoService cargaclienteempleadoservice;
	@Autowired
	private IComercialPrendaBordadoService bordadoService;
	@Autowired
	private IComercialCoordinadoService CoordinadoService;
	@Autowired
	private IComercialConcentradoPrendasService concentradoPrendasService;

	// Metodo de Listar
	@GetMapping("/expediente")
	public String expediente(Model model) {
		model.addAttribute("clientes", clienteservice.findAll(null));
		model.addAttribute("pedidos", cargaPedidoService.CargaPedidoVista(null));
		return "expediente";
	}

	// Metodo para imprimir la informacion general
	@GetMapping("/expediente-imprimir-informacion-general/{id}")
	public String imprimirInformacionGeneral(@PathVariable(value = "id") Long id, Map<String, Object> model, Model m) {
		ComercialPedidoInformacion pedido = cargaPedidoService.findOne(id);
		m.addAttribute("clientes", clienteservice.findAll(null));
		model.put("pedido", pedido);
		return "/imprimir-expediente-informacion-general";
	}

	// Metodo para imprimir la informacion general
	@GetMapping("/expediente-imprimir-coordinados/{id}")
	public String imprimirCoordinados(@PathVariable(value = "id") Long id, Model model) {
		model.addAttribute("coordinados", CoordinadoService.findAllEmpresa(id));
		ComercialPedidoInformacion pedido = cargaPedidoService.findOne(id);
		model.addAttribute("pedido", pedido);
		model.addAttribute("cliente", clienteservice.findOne(cargaPedidoService.findOne(id).getIdEmpresa()));
		return "/imprimir-expediente-coordinado";
	}

	// Metodo para imprimir el detalle de precios de un pedido
	@GetMapping("/expediente-imprimir-detalle-precios/{id}")
	public String listPrecios(@PathVariable(value = "id") Long id, Model model) {
		List<Object[]> aux = bordadoService.findAllCoordinado(id);
		for (Object[] a : aux) {
			Long id_coor = Long.parseLong(a[0].toString());
			Float precio_bordado = Float.parseFloat(a[7].toString());
			Float precio_usar = Float.parseFloat(a[8].toString());
			Float monto = Float.parseFloat(a[10].toString());
			ComercialCoordinadoPrenda prenda = CoordinadoService.findOneCoorPrenda(id_coor);
			Float preciofinal = precio_bordado + precio_usar + monto;
			prenda.setPrecioFinal(Float.toString(preciofinal));
			CoordinadoService.saveCoorPrenda(prenda);
		}
		model.addAttribute("listCoor", bordadoService.findAllCoordinado(id));
		model.addAttribute("pedido", cargaPedidoService.findOne(id));
		model.addAttribute("cliente", clienteservice.findOne(cargaPedidoService.findOne(id).getIdEmpresa()));
		return "/imprimir-expediente-detalle-precios";
	}

	// Metodo para imprimir los empleados de un pedido
	@RequestMapping(value = { "/expediente-imprimir-empleados/{id}/{idcliente}" }, method = RequestMethod.GET)
	public String listGeneral(@PathVariable(value = "id") Long id, @PathVariable(value = "idcliente") Long idcliente,
			Model model) {
		model.addAttribute("empleados", cargaclienteempleadoservice.findAllEmpleadosRazonSocialYSucursal(id));
		model.addAttribute("pedido", cargaPedidoService.findOne(id));
		model.addAttribute("cliente", clienteservice.findOne(cargaPedidoService.findOne(id).getIdEmpresa()));
		return "/imprimir-expediente-empleados";
	}

	// Metodo para imprimir el concentrado de prenas
	@GetMapping("/expediente-imprimir-concentrado-prendas/{id}")
	public String imprimirConcentradoPrendas(@PathVariable(value = "id") Long id, Model model) {
		ComercialPedidoInformacion pedido = cargaPedidoService.findOne(id);
		model.addAttribute("pedido", pedido);
		model.addAttribute("coordinados", concentradoPrendasService.findCoordinadosfromPedido(id));
		model.addAttribute("cliente", clienteservice.findOne(cargaPedidoService.findOne(id).getIdEmpresa()));
		model.addAttribute("empleados", concentradoPrendasService.findEmpleadosParaExpediente(id));

		return "/imprimir-expediente-concentrado-prenda";
	}

	// Metodo para imprimir el concentrado de prenas
	@GetMapping("/expediente-imprimir-concentrado-prendas/{id}/{id2}")
	public String imprimirConcentradoPrendasIndividual(@PathVariable(value = "id") Long id,
			@PathVariable(value = "id2") Long id2, Model model) {
		ComercialPedidoInformacion pedido = cargaPedidoService.findOne(id);
		System.out.println("El id del pedido: " + id);
		System.out.println("El id del coordinado: " + id2);
		model.addAttribute("pedido", pedido);
		model.addAttribute("coordinados", concentradoPrendasService.findCoordinadofromPedido(id2));
		model.addAttribute("cliente", clienteservice.findOne(cargaPedidoService.findOne(id).getIdEmpresa()));
		model.addAttribute("empleados", concentradoPrendasService.findEmpleadosParaExpediente(id));
		return "/imprimir-expediente-concentrado-prenda-individual";
	}

	@GetMapping("/agregar-expediente")
	public String agregarExpediente() {
		return "agregar-expediente";
	}

	@GetMapping("/agregar-expediente-empleados")
	public String agregarEmpleadosExpediente() {
		return "agregar-expediente-empleados";
	}

	@GetMapping("/detalle-expediente")
	public String detalleExpediente() {
		return "detalle-expediente";
	}

}