package com.altima.springboot.app.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PendienteRecibirController {
	@Secured({"ROLE_ADMINISTRADOR","ROLE_COMERCIAL_AMP_PENDIENTERECIBIR_LISTAR"})
	@GetMapping("pendiente-por-recibir")
	public String Index()
	{
		return"pendiente-por-recibir";
	}
}
