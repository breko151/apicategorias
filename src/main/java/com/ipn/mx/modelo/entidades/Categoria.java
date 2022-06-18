package com.ipn.mx.modelo.entidades;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Categoria", schema = "public")
public class Categoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCategoria;
	
	@NotEmpty(message = "No puede estar vacío")
	@Size(min = 5, max = 50, message = "Debe estar entre 5 y 50")
	@NotBlank(message = "Es obligatorio")
	@Column(name = "nombreCategoria", length = 50, nullable = false)
	private String nombreCategoria;
	
	@NotEmpty(message = "No puede estar vacío")
	@Size(min = 10, max = 150, message = "Debe estar entre 10 y 150")
	@NotBlank(message = "Es obligatorio")
	@Column(name = "descripcionCategoria", length = 150, nullable = false)
	private String descripcionCategoria;
	
	@JsonIgnoreProperties(value = {"idCategoria","hybernateLazyInitializer", "handler"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "idCategoria", cascade = CascadeType.ALL)
	private List<Producto> losProductos;
	
}
