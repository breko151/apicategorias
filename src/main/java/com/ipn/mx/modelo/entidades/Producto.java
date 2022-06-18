package com.ipn.mx.modelo.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
@Table(name = "Producto")
public class Producto implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProducto;
	
	@NotEmpty(message = "No puede estar vacío")
	@Size(min = 5, max = 50, message = "Debe estar entre 5 y 50")
	@NotBlank(message = "Es obligatorio")
	@Column(name = "nombreProducto", length = 50, nullable = false)
	private String nombreProducto;
	
	@NotEmpty(message = "No puede estar vacío")
	@Size(min = 10, max = 150, message = "Debe estar entre 10 y 150")
	@NotBlank(message = "Es obligatorio")
	@Column(name = "descripcionProducto", length = 150, nullable = false)
	private String descripcionProducto;
	
	@Column(name = "precioProducto", nullable = false)
	private double precioProducto;
	
	@Column(name = "existencia", nullable = false)
	private int existencia;
	
	@Temporal(TemporalType.DATE)
	private Date fechaCreacion;
	
	@ManyToOne
	@JoinColumn(name = "idCategoria")
	private Categoria idCategoria;
	
	@PrePersist
	public void prePersist() {
		this.fechaCreacion = new Date();
	}
}
