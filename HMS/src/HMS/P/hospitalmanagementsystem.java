package HMS.P;

import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class hospitalmanagementsystem
{
    private static final String url = "jdbc:mysql://127.0.0.1:3306/hospital";
    private static final String username = "root";
    private static final String password = "ashwatthama09@@&&**";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");



                }catch(ClassNotFoundException e){
            e.printStackTrace();

        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patient = new Patients(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("Hospital management system");
                System.out.println("1. Add patient");
                System.out.println("2. View patient");
                System.out.println("3. View doctors");
                System.out.println("4. Book appointments");
                System.out.println("5. Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();

                switch(choice){
                    case 1:
                    patient.addpatient();
                    System.out.println();
                    case 2:
                    patient.viewpatient();
                    System.out.println();
                    case 3:
                    doctor.viewdoctors();
                    System.out.println();
                    case 4:
                    bookappointment(patient, doctor, connection, scanner);
                    System.out.println();
                    case 5:
                    return;
                    default:
                        System.out.println("Enter valid choice");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void bookappointment(Patients patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.println("Enter patient Id");
        int patientid = scanner.nextInt();
        System.out.println("Enter doctor id");
        int doctorid = scanner.nextInt();
        System.out.println("Enter appointment date (YYYY-MM-DD)");
        String appointmentDate = scanner.next();
        if(patient.getpatientbyid(patientid) && doctor.getdoctorbyid(doctorid)){
            if(checkdoctoravailaibility(doctorid, appointmentDate, connection)){
                String appointmentquery = "INSERT INTO appointments(patient_Id, doctor_Id, appointment_date) VALUES (?, ?, ?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentquery);
                    preparedStatement.setInt(1,  patientid);
                    preparedStatement.setInt(2, doctorid);
                    preparedStatement.setString(3, appointmentDate);
                    int affectedrows = preparedStatement.executeUpdate();
                    if(affectedrows>0){
                        System.out.println("appointment booked");
                    }else{
                        System.out.println("failed to bood appointment");
                    }


                }catch(SQLException e){
                    e.printStackTrace();
                }

            }else{
                System.out.println("doctor not availaible on this date");
            }

        }else{
            System.out.println("either doctor or patient is not exist");
        }
    }
    public static boolean checkdoctoravailaibility (int doctorid, String appintmentDate, Connection connection){
        String query = "SELECT COUNT (*) FROM appointments WHERE doctor_id = ? AND appointment+date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorid);
            preparedStatement.setString(2, appintmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
