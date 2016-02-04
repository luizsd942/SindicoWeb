package com.algaworks.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.model.StatusTitulo;
import com.algaworks.model.Titulo;
import com.algaworks.repository.filter.TituloFilter;
import com.algaworks.service.TituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {

	
	@Autowired
	private TituloService tituloService;

	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView("CadastroTitulo");
		mv.addObject(new Titulo());
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors erros,
			RedirectAttributes atributos) {
		if (erros.hasErrors()) {
			return "cadastroTitulo";
		}
		try {
			tituloService.salvar(titulo);

			atributos.addFlashAttribute("mensagem",
					"Título cadastrado com sucesso!");
			return "redirect:titulos/novo";
			
		} catch (IllegalArgumentException e) {
			erros.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}
	
	//Pesquisa automaticamente com Spring Data um objeto Titulo pela
	//variavel codigo e adiciona esse objeto ao atributo titulo
	@RequestMapping(value = "{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;
	}

	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
		
		List<Titulo> lista = tituloService.filtrar(filtro);
		
		ModelAndView mv = new ModelAndView("PesquisaTitulo");
		mv.addObject("todos", lista);
		return mv;
	}
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String exlcuir(@PathVariable Long codigo,
			RedirectAttributes atributos) {
		tituloService.delete(codigo);

		atributos.addFlashAttribute("mensagem", "Título excluído com sucesso!");

		return "redirect:/titulos";
	}
	
	@RequestMapping(value="{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo){		
		return tituloService.receber(codigo);
	}
	
	
	
	
	
	
	
	
	
	
	

}
