package com.example.doacoes.controllers;
import com.example.doacoes.model.entities.Produto;
import com.example.doacoes.model.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repo;

    @GetMapping
    public String listar(Model model) throws Exception {
        model.addAttribute("lista", repo.listarTodos());
        return "produto/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) throws Exception {
        if (produto.getId() == null) repo.inserir(produto);
        else repo.atualizar(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) throws Exception {
        Produto p = repo.buscarPorId(id);
        model.addAttribute("produto", p);
        return "produto/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        repo.deletar(id);
        return "redirect:/produtos";
    }
}