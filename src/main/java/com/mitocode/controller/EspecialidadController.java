package com.mitocode.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Especialidad;
import com.mitocode.service.IEspecialidadService;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadController {

	@Autowired
	private IEspecialidadService service;
	
	@GetMapping
	public ResponseEntity<List<Especialidad>> listar(){
		List<Especialidad> lista = service.listar();
		return new ResponseEntity<List<Especialidad>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Especialidad> listarPorId(@PathVariable("id") Integer id) {
		Especialidad obj = service.listarPorId(id);
		if(obj.getIdEspecialidad() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<Especialidad>(obj, HttpStatus.OK);
	}
	
	//https://docs.spring.io/spring-hateoas/docs/current/reference/html/
		//Hateoas 1.0 > Spring Boot 2.2
		@GetMapping("/hateoas/{id}")
		public EntityModel<Especialidad> listarPorIdHateoas(@PathVariable("id") Integer id){
			Especialidad pac = service.listarPorId(id);
			
			//localhost:8080/especialidads/{id}
			EntityModel<Especialidad> recurso = new EntityModel<Especialidad>(pac);
			WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
			recurso.add(linkTo.withRel("especialidad-resource"));
			return recurso;
		
		}
	
	//el status = 201 (creado)
	/*@PostMapping
	public ResponseEntity<Especialidad> registrar(@Valid @RequestBody Especialidad objeto) {
		Especialidad obj = service.registrar(objeto);
		return new ResponseEntity<Especialidad>(obj, HttpStatus.CREATED);
	}*/
	
	//nivel 2, el status = 201 (creado) y se obtiene en el headers(postman) la url para mas detalle
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Especialidad objeto) {
		Especialidad pac = service.registrar(objeto);
		//localhost:8080/especialidads/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdEspecialidad()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Especialidad> modificar(@Valid @RequestBody Especialidad objeto) {
		Especialidad obj = service.modificar(objeto);
		return new ResponseEntity<Especialidad>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
	/*
	//generar un servicio
	@GetMapping
	public List<Especialidad> listar(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public Especialidad listarPorId(@PathVariable("id") Integer id) {
		Especialidad obj = service.listarPorId(id);
		return obj;
	}
	
	@PostMapping
	public Especialidad registrar(@RequestBody Especialidad objeto) {
		return service.registrar(objeto);
	}
	
	@PutMapping
	public Especialidad modificar(@RequestBody Especialidad objeto) {
		return service.modificar(objeto);
	}
	
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
	}*/
}
