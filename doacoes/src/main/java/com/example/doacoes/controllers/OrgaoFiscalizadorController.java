package com.example.doacoes.controllers;
import com.example.doacoes.model.entities.OrgaoFiscalizador;
import com.example.doacoes.model.repositories.OrgaoFiscalizadorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/fiscalizador")
public class OrgaoFiscalizadorController {

    @Autowired
    private OrgaoFiscalizadorRepository repo;

    @GetMapping
    public String listar(Model model) throws Exception {
        model.addAttribute("lista", repo.listar());
        return "fiscalizador/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("fiscalizador", new OrgaoFiscalizador());
        return "fiscalizador/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute OrgaoFiscalizador fiscalizador) throws Exception {
        if (fiscalizador.getId() == null) repo.inserir(fiscalizador);
        else repo.atualizar(fiscalizador);
        return "redirect:/fiscalizador";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) throws Exception {
        OrgaoFiscalizador o = repo.buscarPorId(id);
        model.addAttribute("fiscalizador", o);
        return "fiscalizador/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        repo.deletar(id);
        return "redirect:/fiscalizador";
    }
}