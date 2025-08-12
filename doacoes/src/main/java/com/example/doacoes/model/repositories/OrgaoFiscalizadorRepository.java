package com.example.doacoes.model.repositories;

import com.example.doacoes.model.entities.OrgaoFiscalizador;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrgaoFiscalizadorRepository {

    public List<OrgaoFiscalizador> listar() throws SQLException {
        String sql = "SELECT id, nome, descricao FROM OrgaoFiscalizador";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<OrgaoFiscalizador> lista = new ArrayList<>();
            while (rs.next()) {
                OrgaoFiscalizador o = new OrgaoFiscalizador();
                o.setId(rs.getInt("id"));
                o.setNome(rs.getString("nome"));
                o.setDescricao(rs.getString("descricao"));
                lista.add(o);
            }
            return lista;
        }
    }

    public OrgaoFiscalizador buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, descricao FROM OrgaoFiscalizador WHERE id = ?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrgaoFiscalizador o = new OrgaoFiscalizador();
                    o.setId(rs.getInt("id"));
                    o.setNome(rs.getString("nome"));
                    o.setDescricao(rs.getString("descricao"));
                    return o;
                }
            }
            return null;
        }
    }

    public void inserir(OrgaoFiscalizador o) throws SQLException {
        String sql = "INSERT INTO OrgaoFiscalizador (nome, descricao) VALUES (?, ?)";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getNome());
            ps.setString(2, o.getDescricao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) o.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(OrgaoFiscalizador o) throws SQLException {
        String sql = "UPDATE OrgaoFiscalizador SET nome=?, descricao=? WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, o.getNome());
            ps.setString(2, o.getDescricao());
            ps.setInt(3, o.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM OrgaoFiscalizador WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}