package com.example.doacoes.model.repositories;
import com.example.doacoes.model.entities.Produto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoRepository {

	public List<Produto> listarTodos() throws SQLException {
	    String sql = "SELECT id, nome, descricao, idLote, codigoBarras FROM Produto";
	    try (Connection c = ConnectionManager.getInstance().getConnection();
	         PreparedStatement ps = c.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        List<Produto> lista = new ArrayList<>();
	        while (rs.next()) {
	            Produto p = new Produto();
	            p.setId(rs.getInt("id"));
	            p.setNome(rs.getString("nome"));
	            p.setDescricao(rs.getString("descricao"));
	            p.setIdLote(rs.getObject("idLote") != null ? rs.getInt("idLote") : null);
	            p.setCodigoBarras(rs.getString("codigoBarras"));
	            lista.add(p);
	        }
	        return lista;
	    }
	}

	public List<Produto> listarPorLote(Integer idLote) throws SQLException {
	    String sql = "SELECT id, nome, descricao, idLote, codigoBarras FROM Produto WHERE idLote = ?";
	    try (Connection c = ConnectionManager.getInstance().getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setInt(1, idLote);
	        try (ResultSet rs = ps.executeQuery()) {
	            List<Produto> lista = new ArrayList<>();
	            while (rs.next()) {
	                Produto p = new Produto();
	                p.setId(rs.getInt("id"));
	                p.setNome(rs.getString("nome"));
	                p.setDescricao(rs.getString("descricao"));
	                p.setIdLote(rs.getInt("idLote"));
	                p.setCodigoBarras(rs.getString("codigoBarras"));
	                lista.add(p);
	            }
	            return lista;
	        }
	    }
	}

	public Produto buscarPorId(int id) throws SQLException {
	    String sql = "SELECT id, nome, descricao, idLote, codigoBarras FROM Produto WHERE id=?";
	    try (Connection c = ConnectionManager.getInstance().getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setInt(1, id);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Produto p = new Produto();
	                p.setId(rs.getInt("id"));
	                p.setNome(rs.getString("nome"));
	                p.setDescricao(rs.getString("descricao"));
	                p.setIdLote(rs.getObject("idLote") != null ? rs.getInt("idLote") : null);
	                p.setCodigoBarras(rs.getString("codigoBarras"));
	                return p;
	            }
	        }
	        return null;
	    }
	}

	public void inserir(Produto p) throws SQLException {
	    String sql = "INSERT INTO Produto (nome, descricao, idLote, codigoBarras) VALUES (?, ?, ?, ?)";
	    try (Connection c = ConnectionManager.getInstance().getConnection();
	         PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        ps.setString(1, p.getNome());
	        ps.setString(2, p.getDescricao());
	        if (p.getIdLote() != null) ps.setInt(3, p.getIdLote()); else ps.setNull(3, Types.INTEGER);
	        ps.setString(4, p.getCodigoBarras());
	        ps.executeUpdate();
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) p.setId(rs.getInt(1));
	        }
	    }
	}

	public void atualizar(Produto p) throws SQLException {
	    String sql = "UPDATE Produto SET nome=?, descricao=?, idLote=?, codigoBarras=? WHERE id=?";
	    try (Connection c = ConnectionManager.getInstance().getConnection();
	         PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setString(1, p.getNome());
	        ps.setString(2, p.getDescricao());
	        if (p.getIdLote() != null) ps.setInt(3, p.getIdLote()); else ps.setNull(3, Types.INTEGER);
	        ps.setString(4, p.getCodigoBarras());
	        ps.setInt(5, p.getId());
	        ps.executeUpdate();
	    }
	}


    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM Produto WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
