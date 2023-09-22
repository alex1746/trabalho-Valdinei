package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Categorias extends JFrame {
    private JTextField textFieldCategoryName;
    private JButton addButton, editButton, deleteButton, listButton;
    private Connection connect;
    private String cadastro = "INSERT INTO categorias (nome) VALUES (?)";

    public Categorias() throws SQLException {
        setTitle("Cadastro de Categorias");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connect = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));


        // Botões de operação CRUD
        addButton = new JButton("Adicionar");
        listButton = new JButton("Listar");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Excluir");


        // Botões do painel
        panel.add(addButton);
        panel.add(listButton);
        panel.add(editButton);
        panel.add(deleteButton);


        // Eventos dos botões
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para adicionar uma nova categoria
                String nome = JOptionPane.showInputDialog("Nome da Categoria:");
                if (nome != null && !nome.isEmpty()) {
                    inserirCategoria(nome);
                    System.out.println("Nova categoria cadastrada: " + nome);
                } else {
                    JOptionPane.showMessageDialog(null, "Operação cancelada ou nome de categoria vazio. Nenhuma categoria cadastrada.");
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtém o ID da categoria a ser editada e o novo nome da categoria

                System.out.println("Listando categorias:");
                String query = "SELECT id, nome FROM categorias";
                PreparedStatement consulta = null;
                try {
                    consulta = connect.prepareStatement(query);
                    ResultSet resultSet = consulta.executeQuery();

                    while (resultSet.next()) {
                        int idCategoria = resultSet.getInt("id");
                        String nomeCategoria = resultSet.getString("nome");
                        System.out.println(idCategoria + " - " + nomeCategoria);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (consulta != null) {
                            consulta.close();
                        }
                    } catch (SQLException ex) {
                    }
                }

                String idCategoria = JOptionPane.showInputDialog("Insira o ID da Categoria que deseja editar:");

                String novoNomeCategoria = JOptionPane.showInputDialog("Insira um novo nome para a categoria:");

                // Verifica se o ID e o novo nome são válidos (não vazios)
                if (idCategoria != null && !idCategoria.trim().isEmpty() &&
                        novoNomeCategoria != null && !novoNomeCategoria.trim().isEmpty()) {
                    editarCategoria(idCategoria, novoNomeCategoria);
                } else {
                    JOptionPane.showMessageDialog(null, "ID ou nome da categoria inválido(s)");


                }
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Solicita o ID da Categoria
                System.out.println("Listando categorias:");
                String query = "SELECT id, nome FROM categorias";
                PreparedStatement consulta = null;
                try {
                    consulta = connect.prepareStatement(query);
                    ResultSet resultSet = consulta.executeQuery();

                    while (resultSet.next()) {
                        int idCategoria = resultSet.getInt("id");
                        String nomeCategoria = resultSet.getString("nome");
                        System.out.println(idCategoria + " - " + nomeCategoria);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    try {
                        if (consulta != null) {
                            consulta.close();
                        }
                    } catch (SQLException ex) {
                    }
                }
                String idCategoria = JOptionPane.showInputDialog("Insira o ID da categoria que deseja excluir:");
                String nomeCategoria = ("nome");

                // Verifica se o ID é válido (não vazio)
                if (idCategoria != null && !idCategoria.trim().isEmpty()) {
                    deletarCategoria(idCategoria);
                } else {
                    JOptionPane.showMessageDialog(null, "ID da categoria inválido");
                }
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para listar todas as categorias
                System.out.println("Listando categorias:");
                listarCategorias();
            }
        });

        add(panel);
    }

    public void inserirCategoria(String nomeCategoria) {
        PreparedStatement insercao = null;
        try {
            insercao = connect.prepareStatement(cadastro);
            insercao.setString(1, nomeCategoria);
            insercao.execute();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (insercao != null) {
                    insercao.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void editarCategoria(String idCategoria, String novoNomeCategoria) {
        String selectQuery = "SELECT id, nome FROM categorias WHERE id = ?";
        String updateQuery = "UPDATE categorias SET nome = ? WHERE id = ?";
        PreparedStatement selectStmt = null;
        PreparedStatement updateStmt = null;

        try {
            selectStmt = connect.prepareStatement(selectQuery);
            selectStmt.setString(1, idCategoria);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                int categoriaID = resultSet.getInt("id");
                String nomeCategoriaAtual = resultSet.getString("nome");

                updateStmt = connect.prepareStatement(updateQuery);
                updateStmt.setString(1, novoNomeCategoria);
                updateStmt.setString(2, idCategoria);
                updateStmt.executeUpdate();

                System.out.println("Categoria " + nomeCategoriaAtual + " editada com sucesso para " + novoNomeCategoria);
            } else {
                System.out.println("Nenhuma categoria encontrada com o ID: " + idCategoria);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (selectStmt != null) {
                    selectStmt.close();
                }
                if (updateStmt != null) {
                    updateStmt.close();
                }
            } catch (SQLException ex) {
            }
        }
    }


    public void deletarCategoria(String idCategoria) {
        String selectQuery = "SELECT id, nome FROM categorias WHERE id = ?";
        String deleteQuery = "DELETE FROM categorias WHERE id = ?";

        PreparedStatement selectStmt = null;
        PreparedStatement deleteStmt = null;

        try {
            // Seleciona a categoria pelo ID e obtém o nome
            selectStmt = connect.prepareStatement(selectQuery);
            selectStmt.setString(1, idCategoria);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                int categoriaID = resultSet.getInt("id");
                String nomeCategoria = resultSet.getString("nome");

                // Exclui a categoria
                deleteStmt = connect.prepareStatement(deleteQuery);
                deleteStmt.setString(1, idCategoria);
                deleteStmt.executeUpdate();

                // Imprime o ID e o nome da categoria excluída
                System.out.println("Categoria " + nomeCategoria + " excluída com sucesso!");
            } else {
                System.out.println("Nenhuma categoria encontrada com o ID: " + idCategoria);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (selectStmt != null) {
                    selectStmt.close();
                }
                if (deleteStmt != null) {
                    deleteStmt.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public void listarCategorias() {
        String query = "SELECT id, nome FROM categorias";
        PreparedStatement consulta = null;

        try {
            consulta = connect.prepareStatement(query);
            ResultSet resultSet = consulta.executeQuery();

            while (resultSet.next()) {
                int idCategoria = resultSet.getInt("id");
                String nomeCategoria = resultSet.getString("nome");
                System.out.println(idCategoria + " - " + nomeCategoria);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (consulta != null) {
                    consulta.close();
                }
            } catch (SQLException ex) {
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Categorias().setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
