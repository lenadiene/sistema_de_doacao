package com.example.doacoes.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Lote {
    private Integer id;
    private LocalDate dataEntrega;
    private String observacao;
    private Integer idOrgaoFiscalizador;
    private Integer idOrgaoDonatario;
    private String nomeOrgaoFiscalizador; // novo
    private String nomeOrgaoDonatario;    // novo

    private List<Produto> produtos = new ArrayList<>();

    public Lote() {}

    // getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public Integer getIdOrgaoFiscalizador() { return idOrgaoFiscalizador; }
    public void setIdOrgaoFiscalizador(Integer idOrgaoFiscalizador) { this.idOrgaoFiscalizador = idOrgaoFiscalizador; }
    public Integer getIdOrgaoDonatario() { return idOrgaoDonatario; }
    public void setIdOrgaoDonatario(Integer idOrgaoDonatario) { this.idOrgaoDonatario = idOrgaoDonatario; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    // getters e setters para os novos campos
    public String getNomeOrgaoFiscalizador() { return nomeOrgaoFiscalizador; }
    public void setNomeOrgaoFiscalizador(String nomeOrgaoFiscalizador) { this.nomeOrgaoFiscalizador = nomeOrgaoFiscalizador; }
    public String getNomeOrgaoDonatario() { return nomeOrgaoDonatario; }
    public void setNomeOrgaoDonatario(String nomeOrgaoDonatario) { this.nomeOrgaoDonatario = nomeOrgaoDonatario; }
}
