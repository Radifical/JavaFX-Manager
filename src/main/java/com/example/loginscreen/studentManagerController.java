package com.example.loginscreen;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class studentManagerController {

    @FXML
    private Label dateText;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField classInput;
    @FXML
    private TextField numberInput;
    @FXML
    private Button addStudentButton;
    @FXML
    private Text infoText;
    @FXML
    private Text arrayText;

    @FXML
    private ListView<String> studentListView;
    @FXML
    private ListView<String> arrayTable;
    @FXML
    private TableColumn<String, String> one;
    @FXML
    private Button makePDFButton;

    //Reciept FXML
    @FXML
    private TextField recieptName;
    @FXML
    private TextField recieptMoney;
    @FXML
    private TextField recieptExtra;

    //Student Value FXML
    @FXML
    private TextField classesName;
    @FXML
    private TextField classesValue;
    @FXML
    private  Button editButton;
    @FXML
    private Text classesInfo;

    public ArrayList<String> studentArray = new ArrayList<>();

    @FXML
    public void initialize() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        dateText.setText((dtf.format(now)));
        studentListView.setStyle("-fx-font-size: 18px;-fx-control-inner-background: #003566;");
        arrayTable.setStyle("-fx-font-size: 18px;-fx-control-inner-background: #003566;");
        loadStudents();

        ObservableList<String> list = FXCollections.observableArrayList();

        studentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        arrayTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);



        studentListView.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                ObservableList<String> selectedItems =  studentListView.getSelectionModel().getSelectedItems();

                for(String s : selectedItems){
                    System.out.println("selected item " + s);
                    addStudentToArray(s);
                    System.out.println(studentArray);
                    arrayText.setText((studentArray.toString()));
                }

            }

        });

        arrayTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                //arrayTable.getItems().clear();

                studentArray.remove(arrayTable.getSelectionModel().getSelectedItem());
                arrayText.setText((studentArray.toString()));

                System.out.println(studentArray);
            }
        });
    }

    public void generateReceipt(ActionEvent e) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime now = LocalDateTime.now();
        Document document = new Document();

        Font boldFont = new Font(Font.FontFamily.COURIER, 16, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC);

        try{

            String pdfName =dtf.format(now) +"-" +recieptName.getText() + "-receipt.pdf";
            OutputStream outputStream =
                    new FileOutputStream(new File("C:\\Users\\Radif\\IdeaProjects\\LoginScreen\\Receipts\\"+pdfName));


            PdfWriter.getInstance(document, outputStream);

            document.open();
            String imageUrl = "https://cdn.discordapp.com/attachments/724028713636986980/1131442853747367966/ll.png";
            Image image2 = Image.getInstance(new URL(imageUrl));
            image2.scaleAbsolute(40, 40);
            document.add(image2);
            document.add(new Paragraph(dtf.format(now),normalFont));
            document.add(new Paragraph("Thanks for your purchase.",normalFont));
            document.add(new Paragraph(""));
            document.add(new Paragraph("Receipt to "+recieptName.getText()+"'s parents.",boldFont));
            document.add(new Paragraph("Purchase Amount: $"+recieptMoney.getText(),boldFont));
            document.add(new Paragraph("For: "+recieptExtra.getText(),boldFont));


            document.close();
            outputStream.close();
        }catch(IOException f){
            throw(f);
        } catch (DocumentException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void loadStudents() {
        studentListView.getItems().clear();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            String studentSelect = "SELECT studentname FROM studentdata";


            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(studentSelect);

            while (queryResult.next()) {
                System.out.println(queryResult.getString("studentname"));
                String name = queryResult.getString("studentname");
                studentListView.getItems().add(name);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudentToArray(String name){
        //one.setCellValueFactory(new PropertyValueFactory<>(name));
        arrayTable.getItems().clear();
        studentArray.add(name);
        for (String s : studentArray){
            arrayTable.getItems().add(s);

        }
    }
    public void generatePDF(ActionEvent e) throws SQLException {
        createDoc();
    }

    public void loadStudentsButton(ActionEvent e) {
        studentListView.getItems().clear();
        loadStudents();
    }


    public void setAddStudentButton(ActionEvent e) {
        //createDoc();
        if (!nameInput.getText().isBlank() && !classInput.getText().isBlank() && !numberInput.getText().isBlank()) {

            addStudent();
        } else {
            infoText.setText("Please fill in all the fields!");
        }
    }

    private void addStudent() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        //String verifyLogin = "SELECT count(1) FROM UserAccounts WHERE username = '" + usernameTextField.getText() + "' AND password = '" + passwordPasswordField.getText() +"'";
        String q1 = "INSERT INTO studentdata (studentname, studentclasses, studentcontactnumber) Values('" + nameInput.getText() + "', '" + classInput.getText() +
                "', '" + numberInput.getText() + "')";
        try {
            Statement statement = connectDB.createStatement();
            //ResultSet queryResult = statement.executeQuery(q1);

            int x = statement.executeUpdate(q1);
            if (x > 0)
                infoText.setText("Successfully Inserted");
            else
                infoText.setText("Insert Failed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createDoc() throws SQLException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime now = LocalDateTime.now();
        Document document = new Document();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String name = "";


        ResultSet queryResult = null;
        Statement statement = null;
        try {

            statement = connectDB.createStatement();
            String pdfName = (dtf.format(now) + "-attendance.pdf");
            OutputStream outputStream =
                    new FileOutputStream(new File("C:\\Users\\Radif\\IdeaProjects\\LoginScreen\\PDFRecord\\" + pdfName));

            // String q2 = "UPDATE studentdata SET studentclasses = studentclasses-1 WHERE studentname =" +"'"name"'";


            //Create PDFWriter instance.
            PdfWriter.getInstance(document, outputStream);

            document.open();
            String imageUrl = "https://cdn.discordapp.com/attachments/724028713636986980/1131442853747367966/ll.png";
            Image image2 = Image.getInstance(new URL(imageUrl));
            image2.scaleAbsolute(200, 200);
            document.add(image2);
            document.add(new Paragraph(dtf.format(now)));

            for (String s : studentArray) {
                name = s;

                String q = "UPDATE studentdata SET studentclasses = studentclasses-1 WHERE studentname ="
                        + " '" + name + "' ";
                String q1 = "SELECT * FROM studentdata WHERE studentname="
                        + " '" + name + "' ";


                int x = statement.executeUpdate(q);
                if (x > 0)
                    System.out.println("Removed Class");
                else
                    infoText.setText("Failed");

                queryResult = statement.executeQuery(q1);
                while (queryResult.next()) {
                    System.out.println(queryResult.getString("studentclasses"));
                    String docString = queryResult.getString("studentclasses");
                    document.add(new Paragraph("Name: " + s + "- Classes: " + docString));

                }


            }

            document.close();
            outputStream.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (queryResult != null) {
                try {
                    queryResult.close();
                } catch (SQLException e) { /* Ignored */}
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) { /* Ignored */}
            }
            if (connectDB != null) {
                try {
                    connectDB.close();
                } catch (SQLException e) { /* Ignored */}
            }
        }

    }

        public void changeStudentValue (ActionEvent e){

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String name = "";


            ResultSet queryResult = null;
            Statement statement = null;

            try{
                statement = connectDB.createStatement();

                name = classesName.getText();
                String value = classesValue.getText();
                String q = "UPDATE studentdata SET studentclasses = "+value+" WHERE studentname ="
                        + " '" + name + "' ";

                int x = statement.executeUpdate(q);
                if (x > 0){
                    classesInfo.setText(name + " now has " + value + " classes.");
                }

                else
                    classesValue.setText("ERROR. Please make sure the student name is correct.");


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

    }




