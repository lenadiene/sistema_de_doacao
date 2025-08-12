package com.example.doacoes.model.repositories;
import com.example.doacoes.model.entities.Lote;
import com.example.doacoes.model.entities.Produto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LoteRepository {

    private final ProdutoRepository produtoRepo = new ProdutoRepository();

    public List<Lote> listar(Integer idOrgaoFiscalizador, Integer idOrgaoDonatario) throws SQLException {
    	StringBuilder sql = new StringBuilder(
    		    "SELECT l.id, l.dataEntrega, l.observacao, l.idOrgaoFiscalizador, l.idOrgaoDonatario, " +
    		    "f.nome AS nomeFiscalizador, d.nome AS nomeDonatario " +
    		    "FROM Lote l " +
    		    "LEFT JOIN OrgaoFiscalizador f ON l.idOrgaoFiscalizador = f.id " +
    		    "LEFT JOIN OrgaoDonatario d ON l.idOrgaoDonatario = d.id " +
    		    "WHERE 1=1");

    		if (idOrgaoFiscalizador != null) sql.append(" AND l.idOrgaoFiscalizador = ").append(idOrgaoFiscalizador);
    		if (idOrgaoDonatario != null) sql.append(" AND l.idOrgaoDonatario = ").append(idOrgaoDonatario);

    		try (Connection c = ConnectionManager.getInstance().getConnection();
    		     PreparedStatement ps = c.prepareStatement(sql.toString());
    		     ResultSet rs = ps.executeQuery()) {

    		    List<Lote> lista = new ArrayList<>();
    		    while (rs.next()) {
    		        Lote l = new Lote();
    		        l.setId(rs.getInt("id"));
    		        Date d = rs.getDate("dataEntrega");
    		        if (d != null) l.setDataEntrega(d.toLocalDate());
    		        l.setObservacao(rs.getString("observacao"));
    		        l.setIdOrgaoFiscalizador(rs.getObject("idOrgaoFiscalizador") != null ? rs.getInt("idOrgaoFiscalizador") : null);
    		        l.setIdOrgaoDonatario(rs.getObject("idOrgaoDonatario") != null ? rs.getInt("idOrgaoDonatario") : null);
    		        l.setNomeOrgaoFiscalizador(rs.getString("nomeFiscalizador"));
    		        l.setNomeOrgaoDonatario(rs.getString("nomeDonatario"));
    		        l.setProdutos(produtoRepo.listarPorLote(l.getId()));
    		        lista.add(l);
    		    }
    		    return lista;
        }
    }

    public Lote buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, dataEntrega, observacao, idOrgaoFiscalizador, idOrgaoDonatario FROM Lote WHERE id = ?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Lote l = new Lote();
                    l.setId(rs.getInt("id"));
                    Date d = rs.getDate("dataEntrega");
                    if (d != null) l.setDataEntrega(d.toLocalDate());
                    l.setObservacao(rs.getString("observacao"));
                    l.setIdOrgaoFiscalizador(rs.getObject("idOrgaoFiscalizador") != null ? rs.getInt("idOrgaoFiscalizador") : null);
                    l.setIdOrgaoDonatario(rs.getObject("idOrgaoDonatario") != null ? rs.getInt("idOrgaoDonatario") : null);
                    l.setProdutos(produtoRepo.listarPorLote(l.getId()));
                    return l;
                }
            }
            return null;
        }
    }

    public void inserir(Lote l) throws SQLException {
        String sql = "INSERT INTO Lote (dataEntrega, observacao, idOrgaoFiscalizador, idOrgaoDonatario) VALUES (?, ?, ?, ?)";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (l.getDataEntrega() != null) ps.setDate(1, Date.valueOf(l.getDataEntrega())); else ps.setNull(1, Types.DATE);
            ps.setString(2, l.getObservacao());
            if (l.getIdOrgaoFiscalizador() != null) ps.setInt(3, l.getIdOrgaoFiscalizador()); else ps.setNull(3, Types.INTEGER);
            if (l.getIdOrgaoDonatario() != null) ps.setInt(4, l.getIdOrgaoDonatario()); else ps.setNull(4, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) l.setId(rs.getInt(1));
            }
            // relacionar produtos: para cada produto que veio com Lote, atualizar idLote
            if (l.getProdutos() != null) {
                for (Produto p : l.getProdutos()) {
                    p.setIdLote(l.getId());
                    if (p.getId() == null) {
                        produtoRepo.inserir(p);
                    } else {
                        produtoRepo.atualizar(p);
                    }
                }
            }
        }
    }

    public void atualizar(Lote l) throws SQLException {
        String sql = "UPDATE Lote SET dataEntrega=?, observacao=?, idOrgaoFiscalizador=?, idOrgaoDonatario=? WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (l.getDataEntrega() != null) ps.setDate(1, Date.valueOf(l.getDataEntrega())); else ps.setNull(1, Types.DATE);
            ps.setString(2, l.getObservacao());
            if (l.getIdOrgaoFiscalizador() != null) ps.setInt(3, l.getIdOrgaoFiscalizador()); else ps.setNull(3, Types.INTEGER);
            if (l.getIdOrgaoDonatario() != null) ps.setInt(4, l.getIdOrgaoDonatario()); else ps.setNull(4, Types.INTEGER);
            ps.setInt(5, l.getId());
            ps.executeUpdate();
            // atualizar produtos: lógica simples — set idLote para os produtos dados; produtos removidos ficam com idLote=NULL
            // Primeiro, desvincular todos produtos do lote
            try (PreparedStatement ps2 = c.prepareStatement("UPDATE Produto SET idLote = NULL WHERE idLote = ?")) {
                ps2.setInt(1, l.getId());
                ps2.executeUpdate();
            }
            if (l.getProdutos() != null) {
                for (Produto p : l.getProdutos()) {
                    p.setIdLote(l.getId());
                    if (p.getId() == null) {
                        produtoRepo.inserir(p);
                    } else {
                        produtoRepo.atualizar(p);
                    }
                }
            }
        }
    }

    public void deletar(int id) throws SQLException {
        // opcional: desvincular produtos primeiro
        try (Connection c = ConnectionManager.getInstance().getConnection()) {
            try (PreparedStatement ps = c.prepareStatement("UPDATE Produto SET idLote = NULL WHERE idLote = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps2 = c.prepareStatement("DELETE FROM Lote WHERE id = ?")) {
                ps2.setInt(1, id);
                ps2.executeUpdate();
            }
        }
    }
}