package com.ipn.mx.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.modelo.entidades.Categoria;
import com.ipn.mx.modelo.service.ICategoriaService;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiCategorias")
public class CategoriaController {

	@Autowired
	private ICategoriaService service;
	
	public CategoriaController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/categoria")
	public List<Categoria> index() {
		return service.findAll();
	}
	
	@PostMapping("/categoria")
	public ResponseEntity<?> create(@RequestBody Categoria categoria, BindingResult resultado) {
		Categoria categoriaNueva = null;
		Map<String, Object> response = new HashMap<>();
		if(resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err -> "Error" + err.getField() + "" + err.getDefaultMessage()).collect(Collectors.toList());		
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>> (response, HttpStatus.BAD_REQUEST);
		}
		try {
			categoriaNueva = service.save(categoria);
		} catch (DataAccessException e) {
			// TODO: handle exception
			response.put("Mensaje", "Error al insertar");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Insertado Satisfactoriamente");
		response.put("Categoria", categoriaNueva);
		return new ResponseEntity<Map<String, Object>> (response, HttpStatus.CREATED);
	}
	
	@GetMapping("/categoria/{id}")
	public ResponseEntity<?> read(@PathVariable Long id) {
		Categoria categoria = null;
		Map<String, Object> response = new HashMap<>();
		try {
			categoria = service.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(categoria == null) {
			response.put("mensaje", "La Categoria ID: ".concat(id.toString().concat(" no existe en la base de dats!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
	}
	
	@PutMapping("/categoria/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Categoria categoria, BindingResult resultado, @PathVariable Long id) {
		Categoria categoriaActual = service.findById(id);
		Categoria categoriaActualizada = null;
		Map<String, Object> respuesta = new HashMap<>();
		if(resultado.hasErrors()) {
			List<String> errores = resultado.getFieldErrors().stream().map(err ->"La columna"+err.getField()+""+err.getDefaultMessage()).collect(Collectors.toList());
			respuesta.put("errores", errores);
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.BAD_REQUEST);
		}
		if(categoriaActual == null) {
			respuesta.put("mensaje", "Error al actualizar categorias ".concat(id.toString()).concat("no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
		}
		try {
			categoriaActual.setNombreCategoria(categoria.getNombreCategoria());
			categoriaActual.setDescripcionCategoria(categoria.getDescripcionCategoria());
			categoriaActualizada = service.save(categoriaActual);
		} catch (DataAccessException e) {
			respuesta.put("mensaje", "Error al actualizar");
			respuesta.put("error", e.getMessage().concat(" = ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respuesta.put("mensaje", "La categoria se actualizó satisfactoriamente");
		respuesta.put("categoria", categoriaActualizada);
		return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/categoria/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			service.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la Categoria de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "La Categoria se ha eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}