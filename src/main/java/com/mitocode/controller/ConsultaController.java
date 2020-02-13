package com.mitocode.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
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

import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Consulta;
import com.mitocode.service.IConsultaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

	@Autowired
	private IConsultaService service;
	
	@GetMapping
	public ResponseEntity<List<Consulta>> listar(){
		List<Consulta> lista = service.listar();
		return new ResponseEntity<List<Consulta>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Consulta> listarPorId(@PathVariable("id") Integer id) {
		Consulta obj = service.listarPorId(id);
		if(obj.getIdConsulta() == null) {
			throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
		}
		return new ResponseEntity<Consulta>(obj, HttpStatus.OK);
	}
	
	//https://docs.spring.io/spring-hateoas/docs/current/reference/html/
		//Hateoas 1.0 > Spring Boot 2.2
		@GetMapping("/hateoas/{id}")
		public EntityModel<Consulta> listarPorIdHateoas(@PathVariable("id") Integer id){
			Consulta pac = service.listarPorId(id);
			
			//localhost:8080/Consultas/{id}
			EntityModel<Consulta> recurso = new EntityModel<Consulta>(pac);
			WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarPorId(id));
			recurso.add(linkTo.withRel("consulta-resource"));
			return recurso;
		
		}
	
	//el status = 201 (creado)
	/*@PostMapping
	public ResponseEntity<Consulta> registrar(@Valid @RequestBody Consulta objeto) {
		Consulta obj = service.registrar(objeto);
		return new ResponseEntity<Consulta>(obj, HttpStatus.CREATED);
	}*/
	
	//nivel 2, el status = 201 (creado) y se obtiene en el headers(postman) la url para mas detalle
	/*@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody Consulta objeto) {
		Consulta pac = service.registrar(objeto);
		//localhost:8080/Consultas/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdConsulta()).toUri();
		return ResponseEntity.created(location).build();
	}*/
	
	
	//DTO
	@Transactional
	@PostMapping
	public ResponseEntity<Object> registrar(@Valid @RequestBody ConsultaListaExamenDTO dto) {
		Consulta pac = service.registrarTransaccional(dto);
		//localhost:8080/Consultas/5
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pac.getIdConsulta()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping
	public ResponseEntity<Consulta> modificar(@Valid @RequestBody Consulta objeto) {
		Consulta obj = service.modificar(objeto);
		return new ResponseEntity<Consulta>(obj, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
	
	
	/*
	//generar un servicio
	@GetMapping
	public List<Consulta> listar(){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public Consulta listarPorId(@PathVariable("id") Integer id) {
		Consulta obj = service.listarPorId(id);
		return obj;
	}
	
	@PostMapping
	public Consulta registrar(@RequestBody Consulta objeto) {
		return service.registrar(objeto);
	}
	
	@PutMapping
	public Consulta modificar(@RequestBody Consulta objeto) {
		return service.modificar(objeto);
	}
	
	@DeleteMapping("/{id}")
	public void eliminar(@PathVariable("id") Integer id) {
		service.eliminar(id);
	}*/
}
