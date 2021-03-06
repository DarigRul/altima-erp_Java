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
@Table(name = "alt_comercial_total_razon_social")
public class ComercialTotalRazonSocial implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="id_total_razon_social")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GenericGenerator(name="native",strategy="native")
	private Long idTotalRazonSocial;
	
	@Column(name="id_pedido")
	private Long idPedido;
	
	@Column(name="id_cliente_factura")
	private Long idClienteFacturaF;
	
	@Column(name="numero_prenda")
	private String numeroPrenda;
	
	@Column(name="subtotal_total")
	private String subtotalTotal;
	
	@Column(name="descuento")
	private String descuento;
	
	@Column(name="subtotal_total2")
	private String subtotalTotal2;
	
	@Column(name="iva")
	private String iva;
	
	@Column(name="total_pedido")
	private String totalPedido;
	
	@Column(name="anticipo")
	private String anticipo;
	
	@Column(name="entrega")
	private String entrega;
	
	@Column(name="saldo")
	private String saldo;
	
	@Column(name="creado_por")
	private String creadoPor;
	
	@Column(name="actualizado_por")
	private String actualizadoPor;
	
	@Column(name="fecha_creacion")
	private String fechaCreacion;
	
	@Column(name="ultima_fecha_modificacion")
	private String ultimaFechaModificacion;
	
	@Column(name="descuento_porcentaje")
	private String descuPorcentaje;
	
	@Column(name="anticipo_porcentaje")
	private String anticiPorcentaje;
	
	@Column(name="entrega_porcentaje")
	private String entregaPorcentaje;
	
	@Column(name="ajustes_porcentaje")
	private String ajustesPorcentaje;

	public String getDescuPorcentaje() {
		return descuPorcentaje;
	}

	public void setDescuPorcentaje(String descuPorcentaje) {
		this.descuPorcentaje = descuPorcentaje;
	}

	public String getAnticiPorcentaje() {
		return anticiPorcentaje;
	}

	public void setAnticiPorcentaje(String anticiPorcentaje) {
		this.anticiPorcentaje = anticiPorcentaje;
	}

	public String getEntregaPorcentaje() {
		return entregaPorcentaje;
	}

	public void setEntregaPorcentaje(String entregaPorcentaje) {
		this.entregaPorcentaje = entregaPorcentaje;
	}
	
	
	
	

	public String getAjustesPorcentaje() {
		return ajustesPorcentaje;
	}

	public void setAjustesPorcentaje(String ajustesPorcentaje) {
		this.ajustesPorcentaje = ajustesPorcentaje;
	}

	public Long getIdTotalRazonSocial() {
		return idTotalRazonSocial;
	}

	public void setIdTotalRazonSocial(Long idTotalRazonSocial) {
		this.idTotalRazonSocial = idTotalRazonSocial;
	}

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Long getIdClienteFacturaF() {
		return idClienteFacturaF;
	}

	public void setIdClienteFacturaF(Long idClienteFacturaF) {
		this.idClienteFacturaF = idClienteFacturaF;
	}

	public String getNumeroPrenda() {
		return numeroPrenda;
	}

	public void setNumeroPrenda(String numeroPrenda) {
		this.numeroPrenda = numeroPrenda;
	}

	public String getSubtotalTotal() {
		return subtotalTotal;
	}

	public void setSubtotalTotal(String subtotalTotal) {
		this.subtotalTotal = subtotalTotal;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getSubtotalTotal2() {
		return subtotalTotal2;
	}

	public void setSubtotalTotal2(String subtotalTotal2) {
		this.subtotalTotal2 = subtotalTotal2;
	}

	public String getIva() {
		return iva;
	}

	public void setIva(String iva) {
		this.iva = iva;
	}

	public String getTotalPedido() {
		return totalPedido;
	}

	public void setTotalPedido(String totalPedido) {
		this.totalPedido = totalPedido;
	}

	public String getAnticipo() {
		return anticipo;
	}

	public void setAnticipo(String anticipo) {
		this.anticipo = anticipo;
	}

	public String getEntrega() {
		return entrega;
	}

	public void setEntrega(String entrega) {
		this.entrega = entrega;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
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
	
	
	
	

}
