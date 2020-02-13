package com.mitocode.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mitocode.model.Especialidad;
import com.mitocode.repo.IEspecialidadRepo;
import com.mitocode.service.IEspecialidadService;

@Service
public class EspecialidadServiceImlp implements IEspecialidadService{
	
	@Autowired
	private IEspecialidadRepo repo;

	@Override
	public Especialidad registrar(Especialidad pac) {
		return repo.save(pac);
	}

	@Override
	public Especialidad modificar(Especialidad pac) {
		return repo.save(pac);
	}

	@Override
	public List<Especialidad> listar() {
		return repo.findAll();
	}

	@Override
	public Especialidad listarPorId(Integer id) {
		Optional<Especialidad> op =  repo.findById(id);
		return op.isPresent() ? op.get() : new Especialidad();
	}

	@Override
	public boolean eliminar(Integer id) {
		repo.deleteById(id);
		return true;
	}

}
