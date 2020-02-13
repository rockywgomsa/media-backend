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
import com.mitocode.model.Paciente;
import com.mitocode.service.IPacienteService;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

	@Autowired
	private IPacienteService service;
	
	@GetMapping
	public ResponseEntity<List<Paciente>> listar(){
		List<Paciente> lista = service.listar();
		return new ResponseEntity<List<Paciente>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Paciente> listarPorId(@PathVariable("id") Integer id) {
		Paciente obj = service.listarPorId(id);
		if(obj.getIdPaciente() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<Paciente>(obj, HttpStatus.OK);
	}
	
	//https://docs.spring.io/spring-hateoas/docs/current/reference/html/
		//Hateoas 1.0 > Spring Boot 2.2
		@GetMapping("/hateoas/{id}")
		public EntityModel<Paciente> listarPorIdHateoas(@PathVariable("id") Integer id){
			Paciente pac = service.listarPorId(id);
			
			//localhost:8080/pacientes/{id}
			EntityModel<Paciente> recurso = new EntityModel<Paciente>(pac);
			WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
			recurso.add(linkTo.withRel("paciente-resource"));
			return recurso;
		
		}
	
	//el status = 201 (creado) nivel 0,1
	/*@PostMapping
	public ResponseEntity<Paciente> registrar(@Valid @RequestBody Paciente objeto) {
		Paciente obj = service.registrar(objeto);
		return new ResponseEntity<Paciente>(obj, HttpStatus.CREATED);
	}*/
	
	//nivel 2, el status = 201 (creado) y se obtiene en el headers(postman) la url para mas detalle
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Paciente objeto) {
		Paciente pac = service.registrar(objeto);
		//localhost:8080/pacientes/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdPaciente()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Paciente> modificar(@Valid @RequestBody Paciente objeto) {
		Paciente obj = service.modificar(objeto);
		return new ResponseEntity<Paciente>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
	/*
	//generar un servicio
	@GetMapping
	public List<Paciente> listar(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public Paciente listarPorId(@PathVariable("id") Integer id) {
		Paciente obj = service.listarPorId(id);
		return obj;
	}
	
	@PostMapping
	public Paciente registrar(@RequestBody Paciente objeto) {
		return service.registrar(objeto);
	}
	
	@PutMapping
	public Paciente modificar(@RequestBody Paciente objeto) {
		return service.modificar(objeto);
	}
	
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
	}*/
}
