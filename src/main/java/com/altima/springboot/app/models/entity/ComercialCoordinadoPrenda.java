package com.altima.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "alt_comercial_coordinado_prenda")
public class ComercialCoordinadoPrenda implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id_coordinado_prenda")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name="native",strategy="native")
	private Long idCoordinadoPrenda;
	
	@Column(name="id_familia_genero")
	private Long idFamilaGenero;
	
	@Column(name="id_prenda")
	private Long idPrenda;
	
	@Column(name="id_tela")
	private Long idTela;
	
	@Column(name="id_coordinado")
	private Long idCoordinado;
	
	@Column(name="creado_por")
	private String creadoPor;
	
	@Column(name="actualizado_por")
	private String actualizadoPor;
	
	@Column(name="fecha_creacion")
	private String fechaCreacion;
	
	@Column(name="ultima_fecha_modificacion")
	private String ultimaFechaModificacion;
	
	@Column(name="estatus")
	private String estatus;
	
	
	@Column(name="adicional")
	private String adicional;
	
	@Column(name="monto_adicional")
	private String montoAdicional;
	
	@Column(name="precio_final")
	private String precioFinal;
	
	@Column(name="observaciones")
	private String observaciones;
	
	
	
	

	public Long getIdCoordinadoPrenda() {
		return idCoordinadoPrenda;
	}

	public void setIdCoordinadoPrenda(Long idCoordinadoPrenda) {
		this.idCoordinadoPrenda = idCoordinadoPrenda;
	}

	public Long getIdFamilaGenero() {
		return idFamilaGenero;
	}

	public void setIdFamilaGenero(Long idFamilaGenero) {
		this.idFamilaGenero = idFamilaGenero;
	}

	public Long getIdPrenda() {
		return idPrenda;
	}

	public void setIdPrenda(Long idPrenda) {
		this.idPrenda = idPrenda;
	}

	public Long getIdTela() {
		return idTela;
	}

	public void setIdTela(Long idTela) {
		this.idTela = idTela;
	}

	public Long getIdCoordinado() {
		return idCoordinado;
	}

	public void setIdCoordinado(Long idCoordinado) {
		this.idCoordinado = idCoordinado;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getActualizadoPor() {
		return actualizadoPor;
	}

	public void setActualizadoPor(String actualizadoPor) {
		this.actualizadoPor = actualizadoPor;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getUltimaFechaModificacion() {
		return ultimaFechaModificacion;
	}

	public void setUltimaFechaModificacion(String ultimaFechaModificacion) {
		this.ultimaFechaModificacion = ultimaFechaModificacion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getAdicional() {
		return adicional;
	}

	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}

	public String getMontoAdicional() {
		return montoAdicional;
	}

	public void setMontoAdicional(String montoAdicional) {
		this.montoAdicional = montoAdicional;
	}

	public String getPrecioFinal() {
		return precioFinal;
	}

	public void setPrecioFinal(String precioFinal) {
		this.precioFinal = precioFinal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	
	

}