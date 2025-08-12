package com.example.doacoes.controllers;
import com.example.doacoes.model.entities.*;
import com.example.doacoes.model.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/lotes")
public class LoteController {

    @Autowired
    private LoteRepository loteRepo;

    @Autowired
    private OrgaoFiscalizadorRepository fiscalRepo;

    @Autowired
    private OrgaoDonatarioRepository donatarioRepo;

    @Autowired
    private ProdutoRepository produtoRepo;

    @GetMapping
    public String listar(@RequestParam(required = false) Integer idFiscalizador,
                        @RequestParam(required = false) Integer idDonatario,
                        Model model) throws Exception {
        model.addAttribute("lista", loteRepo.listar(idFiscalizador, idDonatario));
        model.addAttribute("fiscalizadores", fiscalRepo.listar());
        model.addAttribute("donatarios", donatarioRepo.listar());
        return "lote/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) throws Exception {
        model.addAttribute("lote", new Lote());
        model.addAttribute("fiscalizadores", fiscalRepo.listar());
        model.addAttribute("donatarios", donatarioRepo.listar());
        model.addAttribute("todosProdutos", produtoRepo.listarTodos());
        return "lote/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Lote lote,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntrega,
                         @RequestParam(required = false, name = "produtoIds") List<Integer> produtoIds) throws Exception {
        if (dataEntrega != null) lote.setDataEntrega(dataEntrega);

        // associar produtos selecionados ao lote (se houver)
        List<Produto> produtos = new ArrayList<>();
        if (produtoIds != null) {
            for (Integer pid : produtoIds) {
                Produto p = produtoRepo.buscarPorId(pid);
                if (p != null) produtos.add(p);
            }
        }
        lote.setProdutos(produtos);

        if (lote.getId() == null) loteRepo.inserir(lote);
        else loteRepo.atualizar(lote);
        return "redirect:/lotes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) throws Exception {
        Lote lote = loteRepo.buscarPorId(id);
        model.addAttribute("lote", lote);
        model.addAttribute("fiscalizadores", fiscalRepo.listar());
        model.addAttribute("donatarios", donatarioRepo.listar());
        model.addAttribute("todosProdutos", produtoRepo.listarTodos());
        return "lote/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable int id) throws Exception {
        loteRepo.deletar(id);
        return "redirect:/lotes";
    }
}
