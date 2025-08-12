package com.example.doacoes.model.entities;

public class Produto {
    private Integer id;
    private String nome;
    private String descricao;
    private Integer idLote; // FK para Lote (pode ser null enquanto não atrelado)
    private String codigoBarras;
    public Produto() {}

    // getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getIdLote() { return idLote; }
    public void setIdLote(Integer idLote) { this.idLote = idLote; }
    public String getCodigoBarras() {
        return codigoBarras;
    }
    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

}
