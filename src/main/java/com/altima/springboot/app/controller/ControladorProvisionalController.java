package com.altima.springboot.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorProvisionalController {
    @GetMapping("/asignacion-ruta-programa")
	public String asignacionRuta() {
		return "asignacion-ruta-programa";
	}
	@GetMapping("/asignacion-programa")
	public String asignacionPrograma() {
		return "asignacion-programa";
	}

	@GetMapping("/control-de-telas")
	public String CtrlTelas() {
		return "control-de-telas";
	}
	@GetMapping("/control-habilitacion")
	public String CtrlHab() {
		return "control-habilitacion";
	}
	@GetMapping("/control-habilitacion-material")
	public String CtrlHabMat() {
		return "control-habilitacion-material";
	}


}
