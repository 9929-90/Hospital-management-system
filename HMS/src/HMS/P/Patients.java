package HMS.P;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
    private Connection connection;
    private Scanner scanner;
    public Patients(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }
    public void addpatient(){
        System.out.println("Enter patient name");
        String name = scanner.next();
        System.out.println("enter patient age:");
        int age = scanner.nextInt();
        System.out.println("enter patient gender");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedrows = preparedStatement.executeUpdate();
            if(affectedrows>0){
                System.out.println("patient added succesfully");
            }else {
                System.out.println("failed to add patient");
            }

        }catch(SQLException e){
            e.printStackTrace();
    }
        }
        public void viewpatient(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients:");
            System.out.println("+-------------+-------------------------+-----------+----------------+");
            System.out.println("| Patient ID |Name                      |Age       |Gender           ");
            System.out.println("+-------------+-------------------------+-----------+----------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("ID");
                String name = resultSet.getNString("name");
                int age = resultSet.getInt("Age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-26s|%-10s|%-17s|\n", id, name, age, gender);
                System.out.println("+-------------+-------------------------+-----------+----------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        }
        public boolean getpatientbyid (int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
        }
}

