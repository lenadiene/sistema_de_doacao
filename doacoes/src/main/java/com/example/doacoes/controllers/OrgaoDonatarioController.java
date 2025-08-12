package com.example.doacoes.controllers;
import com.example.doacoes.model.entities.OrgaoDonatario;
import com.example.doacoes.model.repositories.OrgaoDonatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/donatarios")
public class OrgaoDonatarioController {

    @Autowired
    private OrgaoDonatarioRepository repo;

    @GetMapping
    public String listar(Model model) throws Exception {
        model.addAttribute("lista", repo.listar());
        return "donatario/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("donatario", new OrgaoDonatario());
        return "donatario/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute OrgaoDonatario donatario) throws Exception {
        if (donatario.getId() == null) repo.inserir(donatario);
        else repo.atualizar(donatario);
        return "redirect:/donatarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) throws Exception {
        OrgaoDonatario o = repo.buscarPorId(id);
        model.addAttribute("donatario", o);
        return "donatario/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        repo.deletar(id);
        return "redirect:/donatarios";
    }
}
