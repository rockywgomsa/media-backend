package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

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
import com.mitocode.model.Examen;
import com.mitocode.service.IExamenService;

@RestController
@RequestMapping("/examenes")
public class ExamenController {

	@Autowired
	private IExamenService service;
	
	@GetMapping
	public ResponseEntity<List<Examen>> listar(){
		List<Examen> lista = service.listar();
		return new ResponseEntity<List<Examen>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Examen> listarPorId(@PathVariable("id") Integer id) {
		Examen obj = service.listarPorId(id);
		if(obj.getIdExamen() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<Examen>(obj, HttpStatus.OK);
	}
	
	//https://docs.spring.io/spring-hateoas/docs/current/reference/html/
		//Hateoas 1.0 > Spring Boot 2.2
		@GetMapping("/hateoas/{id}")
		public EntityModel<Examen> listarPorIdHateoas(@PathVariable("id") Integer id){
			Examen pac = service.listarPorId(id);
			
			//localhost:8080/Examens/{id}
			EntityModel<Examen> recurso = new EntityModel<Examen>(pac);
			WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
			recurso.add(linkTo.withRel("medico-resource"));
			return recurso;
		
		}
	
	//el status = 201 (creado)
	/*@PostMapping
	public ResponseEntity<Examen> registrar(@Valid @RequestBody Examen objeto) {
		Examen obj = service.registrar(objeto);
		return new ResponseEntity<Examen>(obj, HttpStatus.CREATED);
	}*/
	
	//nivel 2, el status = 201 (creado) y se obtiene en el headers(postman) la url para mas detalle
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Examen objeto) {
		Examen pac = service.registrar(objeto);
		//localhost:8080/Examens/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdExamen()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Examen> modificar(@Valid @RequestBody Examen objeto) {
		Examen obj = service.modificar(objeto);
		return new ResponseEntity<Examen>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
	/*
	//generar un servicio
	@GetMapping
	public List<Examen> listar(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public Examen listarPorId(@PathVariable("id") Integer id) {
		Examen obj = service.listarPorId(id);
		return obj;
	}
	
	@PostMapping
	public Examen registrar(@RequestBody Examen objeto) {
		return service.registrar(objeto);
	}
	
	@PutMapping
	public Examen modificar(@RequestBody Examen objeto) {
		return service.modificar(objeto);
	}
	
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
	}*/
}
