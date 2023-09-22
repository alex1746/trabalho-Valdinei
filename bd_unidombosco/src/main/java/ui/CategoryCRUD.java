package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CategoryCRUD extends JFrame {
    private JTextField textFieldCategoryName;
    private JButton addButton, updateButton, deleteButton, listButton;

    public CategoryCRUD() throws SQLException {
        setTitle("Cadastro de Categorias");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Label e TextField para o nome da categoria
        JLabel labelCategoryName = new JLabel("Nome da Categoria: ");
        textFieldCategoryName = new JTextField();
        panel.add(labelCategoryName);
        panel.add(textFieldCategoryName);

        // Botões para as operações de CRUD
        addButton = new JButton("Adicionar");
        updateButton = new JButton("Editar");
        deleteButton = new JButton("Deletar");
        listButton = new JButton("Listar");

        // Adiciona os botões ao painel
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(listButton);
        Connection connect = DriverManager.getConnection("jdbc:sqlite:biblioteca.db");
        String cadastro = "insert into categorias (nome) values (?)";
        // Eventos para os botões (a implementar)
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para adicionar uma nova categoria
                System.out.println("Adicionando categoria: " + textFieldCategoryName.getText());
                //Inserir um cadastro
                String nome = JOptionPane.showInputDialog("Nome da Categoria:");
                PreparedStatement insercao = null;
                try {
                    insercao = connect.prepareStatement(cadastro);
                    insercao.setString(1, nome);
                    insercao.execute();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para atualizar uma categoria
                System.out.println("Editando categoria: " + textFieldCategoryName.getText());
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para deletar uma categoria
                System.out.println("Deletando categoria: " + textFieldCategoryName.getText());
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Código para listar todas as categorias
                System.out.println("Listando todas as categorias");

                String query = "SELECT id, nome FROM categorias";
                PreparedStatement consulta = null;

                try {
                    consulta = connect.prepareStatement(query);
                    ResultSet resultSet = consulta.executeQuery();

                    while (resultSet.next()) {
                        int idCategoria = resultSet.getInt("id");
                        String nomeCategoria = resultSet.getString("nome");
                        System.out.println("ID: " + idCategoria);
                        System.out.println("Nome: " + nomeCategoria);
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
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CategoryCRUD().setVisible(true);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
