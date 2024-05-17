import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Etudiant extends JFrame implements ActionListener {
    private JPanel MainPanel;
    private JTable table_etud;
    private JTextField prenom_etud;
    private JTextField tel_etud;
    private JTextField cin_etud;
    private JTextField nom_etud;
    private JTextField adr_etud;
    private JTextField cin_etud2;
    private JButton chercherButton;
    private JButton supprimerButton;
    private JPanel Panel3;
    private JPanel Panel2;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton ajouterButton;
    private JButton actualiserButton;
    private JPanel ListeLabel;
    private JTabbedPane tabbedPane1;
    private JButton modifierButton;
    private JTextField nom_etud_edit;
    private JTextField prenom_etud_edit;
    private JTextField tel_etud_edit;
    private JTextField adr_etud_edit;
    private JComboBox comboBox1_edit;
    private JComboBox comboBox2_edit;
    private JTextField cin_etud_edit;
    private JButton chercherButton1;

    Connection connection;
    Statement statement;
    ResultSet result;

    public Etudiant(){
        setContentPane(MainPanel);
        setTitle("Gestion des Etudiants");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);


        ajouterButton.addActionListener(this);
        chercherButton.addActionListener(this);
        chercherButton1.addActionListener(this);
        actualiserButton.addActionListener(this);
        supprimerButton.addActionListener(this);
        modifierButton.addActionListener(this);

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/java_database", "root", "");
            statement = connection.createStatement();

            displayEtud();

        } catch (Exception e){
            System.out.println(e);
        };



    }


    public static void main(String[] args) {
        new Etudiant();
    }




    @Override
    public void actionPerformed(ActionEvent even) {
        String p = even.getActionCommand();
        if (p.equalsIgnoreCase("Ajouter")){
            saveEtud();
        }
        if (p.equalsIgnoreCase("Chercher")){
            displayFind();
        }
        if (p.equalsIgnoreCase("Afficher")){
            displayDetails();
        }
        if (p.equalsIgnoreCase("Actualiser")){
            displayEtud();
        }
        if (p.equalsIgnoreCase("Modifier")){
            editEtud();
        }
        if (p.equalsIgnoreCase("Supprimer")){
            delEtud();
        }
    }




    public boolean searchEtud(String cin){
        String m = "";
        boolean check = false;

        try{
            String SQL = "SELECT cin FROM etudiant WHERE cin ='" + cin +"'";
            result = statement.executeQuery(SQL);
            while (result.next()){
                m = result.getString(1);
                System.out.println(m);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        if (m.equalsIgnoreCase(cin)){
            check = true;
        }
        return check;
    }


    public void editEtud(){
        if (searchEtud(cin_etud_edit.getText())){
            try{
                String modify = "UPDATE etudiant SET nom='" + nom_etud_edit.getText() + "', prenom='" + prenom_etud_edit.getText() + "', tel='" + tel_etud_edit.getText() + "', adresse='" + adr_etud_edit.getText() + "', niveau='" + comboBox1_edit.getSelectedItem().toString()+ " " + comboBox2_edit.getSelectedItem().toString()+ "' WHERE cin ='"+ cin_etud_edit.getText()+"'";
                statement.executeUpdate(modify);
                JOptionPane.showMessageDialog(null, "Etudiant modifié");
            }
            catch (Exception e1){
                System.out.println(e1);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Etudiant non existant");
        }
    }

    public void delEtud(){
        Object[] options = {"Oui",
                "Non"};
        int opt = JOptionPane.showOptionDialog(null,
                "Etes-vous sûre?",
                "Supprimer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,     //do not use a custom Icon
                options,  //the titles of buttons
                options[0]); //default button title

        if (opt ==0){
            if (searchEtud(cin_etud2.getText())){
                try{
                    String delete = "DELETE FROM etudiant WHERE cin ='" + cin_etud2.getText() +"'";
                    statement.executeUpdate(delete);
                    JOptionPane.showMessageDialog(null, "Etudiant supprimé");
                }
                catch (Exception e1){
                    System.out.println(e1);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Etudiant non existant");
            }
        }

    }

    public void displayDetails(){
        try{
            String query = "SELECT cin, nom, prenom, tel, adresse, niveau FROM etudiant WHERE cin ='" + cin_etud_edit.getText() +"'";
            result = statement.executeQuery(query);

            while (result.next()) {
                nom_etud_edit.setText(result.getString("Nom"));
                prenom_etud_edit.setText(result.getString("Prenom"));
                tel_etud_edit.setText(result.getString("Tel"));
                adr_etud_edit.setText(result.getString("Adresse"));

                String[] splited = result.getString("Niveau").split("\\s+");
                comboBox1_edit.setSelectedItem(splited[0]);
                comboBox2_edit.setSelectedItem(splited[1]);


            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void displayFind(){
        try{
            String query = "SELECT cin, nom, prenom, tel, adresse, niveau FROM etudiant WHERE cin ='" + cin_etud2.getText() +"'";
            result = statement.executeQuery(query);

            // create a table model with the appropriate column headers
            String[] columnNames = {"CIN", "Nom", "Prenom", "Tel", "Adresse", "Niveau"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (result.next()) {
                String cin = result.getString("CIN");
                String nom = result.getString("Nom");
                String prenom = result.getString("Prenom");
                String tel = result.getString("Tel");
                String adresse = result.getString("Adresse");
                String niveau = result.getString("Niveau");

                // create a single array of one row of data
                String[] data = { cin, nom, prenom, tel, adresse, niveau } ;

                // add this row into the table model
                tableModel.addRow(data);
            }

            table_etud.setModel(tableModel); // place model into JTable

            JTableHeader tableHeader = table_etud.getTableHeader();

            table_etud.getColumnModel().getColumn(0).setMaxWidth(100);
            table_etud.getColumnModel().getColumn(3).setMaxWidth(100);
            table_etud.getColumnModel().getColumn(5).setPreferredWidth(250);
            table_etud.setIntercellSpacing(new Dimension(10,0));

        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void displayEtud(){
        try{
            String query = "SELECT cin, nom, prenom, tel, adresse, niveau FROM etudiant";
            result = statement.executeQuery(query);

            // create a table model with the appropriate column headers
            String[] columnNames = {"CIN", "Nom", "Prenom", "Tel", "Adresse", "Niveau"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (result.next()) {
                String cin = result.getString("CIN");
                String nom = result.getString("Nom");
                String prenom = result.getString("Prenom");
                String tel = result.getString("Tel");
                String adresse = result.getString("Adresse");
                String niveau = result.getString("Niveau");

                // create a single array of one row of data
                String[] data = { cin, nom, prenom, tel, adresse, niveau } ;

                // add this row into the table model
                tableModel.addRow(data);
            }

            table_etud.setModel(tableModel); // place model into JTable

            JTableHeader tableHeader = table_etud.getTableHeader();

            table_etud.getColumnModel().getColumn(0).setMaxWidth(100);
            table_etud.getColumnModel().getColumn(3).setMaxWidth(100);
            table_etud.getColumnModel().getColumn(5).setPreferredWidth(250);
            table_etud.setIntercellSpacing(new Dimension(10,0));

        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public void saveEtud(){
        if (!searchEtud(cin_etud.getText())){
            try{
                String insert = "INSERT INTO etudiant(cin, nom, prenom, tel, adresse, niveau) VALUES('"+cin_etud.getText()+"', '"+nom_etud.getText()+"', '"+prenom_etud.getText()+"', '"+tel_etud.getText()+"', '"+adr_etud.getText()+"', '" +comboBox1.getSelectedItem().toString()+ " " + comboBox2.getSelectedItem().toString()+ "')";
                statement.executeUpdate(insert);
                JOptionPane.showMessageDialog(null, "Etudiant ajouté");
            }
            catch (Exception e1){
                System.out.println(e1);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Etudiant déja existant");
        }
    }














}
