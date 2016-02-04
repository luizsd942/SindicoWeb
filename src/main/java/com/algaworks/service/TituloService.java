package com.algaworks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.model.StatusTitulo;
import com.algaworks.model.Titulo;
import com.algaworks.repository.Titulos;
import com.algaworks.repository.filter.TituloFilter;

@Service
public class TituloService {

	@Autowired
	private Titulos titulos;

	public void salvar(Titulo titulo) {
		try {
			titulos.save(titulo);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formado de data inválido.");
		}
	}

	public void delete(Long codigo) {
		titulos.delete(codigo);
	}

	public String receber(Long codigo) {
		Titulo t = titulos.findOne(codigo);
		t.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(t);
		return t.getStatus().getDescricao();
	}
	
	public List<Titulo> filtrar(TituloFilter filtro){
		String descricao = filtro.getDescricao() == null? "%" : filtro.getDescricao();
		return titulos.findByDescricaoContaining(descricao);
	}
}
