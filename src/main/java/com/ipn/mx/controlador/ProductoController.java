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

import com.ipn.mx.modelo.entidades.Producto;
import com.ipn.mx.modelo.service.IProductoService;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/apiProductos")
public class ProductoController {
	
	@Autowired
	private IProductoService service;

	public ProductoController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/producto")
	public List<Producto> index() {
		return service.findAll();
	}
	
	@PostMapping("/producto")
	public ResponseEntity<?> create(@RequestBody Producto producto, BindingResult resultado) {
		Producto productoNuevo = null;
		Map<String, Object> response = new HashMap<>();
		if(resultado.hasErrors()) {
			List <String> errores = resultado.getFieldErrors().stream().map(err -> "Error" + err.getField() + "" + err.getDefaultMessage()).collect(Collectors.toList());		
			response.put("errores", errores);
			return new ResponseEntity<Map<String, Object>> (response, HttpStatus.BAD_REQUEST);
		}
		try {
			productoNuevo = service.save(producto);
		} catch (DataAccessException e) {
			response.put("Mensaje", "Error al insertar");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Insertado Satisfactoriamente");
		response.put("Producto", productoNuevo);
		return new ResponseEntity<Map<String, Object>> (response, HttpStatus.CREATED);
	}
	
	@GetMapping("/producto/{id}")
	public ResponseEntity<?> read(@PathVariable Long id) {
		Producto producto = null;
		Map<String, Object> response = new HashMap<>();
		try {
			producto = service.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(producto == null) {
			response.put("mensaje", "El Producto ID: ".concat(id.toString().concat(" no existe en la base de dats!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(producto, HttpStatus.OK);
	}
	
	@PutMapping("/producto/{id}")
	    public ResponseEntity<?> update(@Valid @RequestBody Producto producto, BindingResult resultado, @PathVariable Long id){
		Producto ProductoActual = service.findById(id);
		Producto ProductoActualizado = null;
		Map<String,Object> respuesta = new HashMap<>();
		if(resultado.hasErrors()) {
		    List<String> errores = resultado.getFieldErrors().stream()
			    .map(err -> "La columna" +  err.getField() + " " + err.getDefaultMessage())
			    .collect(Collectors.toList());
		    respuesta.put("erorres", errores);
		    return new ResponseEntity<Map<String,Object>> (respuesta, HttpStatus.NOT_FOUND);
		}
		try {
		    ProductoActual.setNombreProducto(producto.getNombreProducto());
		    ProductoActual.setDescripcionProducto(producto.getDescripcionProducto());
		    ProductoActual.setExistencia(producto.getExistencia());
		    ProductoActual.setPrecioProducto(producto.getPrecioProducto());
		    ProductoActual.setIdCategoria(producto.getIdCategoria());
		    ProductoActualizado = service.save(ProductoActual);
		}catch(DataAccessException e){
		    respuesta.put("mensaje", " Error al actualizar");
		    respuesta.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		    return new ResponseEntity<Map<String,Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR); 
		}

		respuesta.put("mensaje", "El producto se actualizo correctamente");
		respuesta.put("producto", ProductoActualizado);
		return new ResponseEntity<Map<String,Object>>(respuesta, HttpStatus.CREATED);

	    }

	@DeleteMapping("/producto/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			service.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la Categoria de la base de datos");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El producto se ha eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
