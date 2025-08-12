package com.example.doacoes.model.repositories;
import com.example.doacoes.model.entities.OrgaoDonatario;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrgaoDonatarioRepository {

    public List<OrgaoDonatario> listar() throws SQLException {
        String sql = "SELECT id, nome, endereco, telefone, horarioFuncionamento, descricao FROM OrgaoDonatario";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<OrgaoDonatario> lista = new ArrayList<>();
            while (rs.next()) {
                OrgaoDonatario o = new OrgaoDonatario();
                o.setId(rs.getInt("id"));
                o.setNome(rs.getString("nome"));
                o.setEndereco(rs.getString("endereco"));
                o.setTelefone(rs.getString("telefone"));
                o.setHorarioFuncionamento(rs.getString("horarioFuncionamento"));
                o.setDescricao(rs.getString("descricao"));
                lista.add(o);
            }
            return lista;
        }
    }

    public OrgaoDonatario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, endereco, telefone, horarioFuncionamento, descricao FROM OrgaoDonatario WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrgaoDonatario o = new OrgaoDonatario();
                    o.setId(rs.getInt("id"));
                    o.setNome(rs.getString("nome"));
                    o.setEndereco(rs.getString("endereco"));
                    o.setTelefone(rs.getString("telefone"));
                    o.setHorarioFuncionamento(rs.getString("horarioFuncionamento"));
                    o.setDescricao(rs.getString("descricao"));
                    return o;
                }
            }
            return null;
        }
    }

    public void inserir(OrgaoDonatario o) throws SQLException {
        String sql = "INSERT INTO OrgaoDonatario (nome, endereco, telefone, horarioFuncionamento, descricao) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getNome());
            ps.setString(2, o.getEndereco());
            ps.setString(3, o.getTelefone());
            ps.setString(4, o.getHorarioFuncionamento());
            ps.setString(5, o.getDescricao());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) o.setId(rs.getInt(1));
            }
        }
    }

    public void atualizar(OrgaoDonatario o) throws SQLException {
        String sql = "UPDATE OrgaoDonatario SET nome=?, endereco=?, telefone=?, horarioFuncionamento=?, descricao=? WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, o.getNome());
            ps.setString(2, o.getEndereco());
            ps.setString(3, o.getTelefone());
            ps.setString(4, o.getHorarioFuncionamento());
            ps.setString(5, o.getDescricao());
            ps.setInt(6, o.getId());
            ps.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM OrgaoDonatario WHERE id=?";
        try (Connection c = ConnectionManager.getInstance().getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}