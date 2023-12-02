package com.bloodbankapp.bloodbankapp.Controllers;

import com.bloodbankapp.bloodbankapp.database.DataBase;
import com.bloodbankapp.bloodbankapp.database.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class AdminController implements Initializable {
    ObservableList<SystemUser> systemUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
    public void loadData(){
        List<SystemUser> list ;
        try {
            list = DataBase.getDataBase().getSystemUsers();


            ID.setCellValueFactory(new PropertyValueFactory<Person,Integer>("ID"));
            firstName.setCellValueFactory(new PropertyValueFactory<Person,String>("firstName"));
            lastName.setCellValueFactory(new PropertyValueFactory<Person,String >("lastName"));
            address.setCellValueFactory(new PropertyValueFactory<Person,String>("address"));
            phoneNumber.setCellValueFactory(new PropertyValueFactory<Person,String>("phone_number")); // Make sure this matches the field name in UserInformation
            email.setCellValueFactory(new PropertyValueFactory<Person,String>("email"));
            bloodType.setCellValueFactory(new  PropertyValueFactory<Person,String>("bloodType") );
            medicalHistory.setCellValueFactory(new  PropertyValueFactory<Person,String>("medicalHistory") );

            systemUsers = FXCollections.observableArrayList(list);
            personTableView.getSelectionModel().getSelectedItem();
            personTableView.setItems(systemUsers);

            searchFilter();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private TableColumn<Person, Integer> ID;

    @FXML
    private TextField IDText;

    @FXML
    private TableColumn<Person, String> address;

    @FXML
    private TextField addressText;

    @FXML
    private TableColumn<Person, String> email;

    @FXML
    private TextField emailText;

    @FXML
    private TableColumn<Person, String> firstName;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField historyText;

    @FXML
    private TableColumn<Person, String> lastName;

    @FXML
    private TextField lastNameText;

    @FXML
    private TableView<SystemUser> personTableView;


    @FXML
    private TableColumn<Person, String> phoneNumber;

    @FXML
    private TextField phoneText;

    @FXML
    private TextField bloodTypeText;

    @FXML
    private TextField searchPersonText;


    @FXML
    private TextField medicalHistoryText;

    @FXML
    private ListView<?> view;

    @FXML
    void reports(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }
    @FXML
    private TableColumn<Person, String> medicalHistory;

    @FXML
    private TableColumn<Person, String> bloodType;



    @FXML
    private void fillUserInfo(){
        SystemUser selectedPerson = personTableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            firstNameText.setText(selectedPerson.getFirstName());
            lastNameText.setText(selectedPerson.getLastName());
            phoneText.setText(selectedPerson.getPhone_number());
            emailText.setText(selectedPerson.getEmail());
            addressText.setText(selectedPerson.getAddress());
            IDText.setText(selectedPerson.getID()+"");
            bloodTypeText.setText(selectedPerson.getBloodType());
            medicalHistoryText.setText(selectedPerson.getMedicalHistory());
        }

    }
    @FXML
    private  void insertNewSystemUser() throws SQLException {


        try{
            int id = Integer.parseInt(IDText.getText());
            String firstName = firstNameText.getText();
            String lastname = lastNameText.getText();
            String email = emailText.getText();
            String phone = phoneText.getText();
            String address = addressText.getText();

            DataBase.getDataBase().insertNewUser(id,firstName,lastname,address,phone,email,bloodTypeText.getText(), medicalHistoryText.getText());
            EmailSender.getEmailSender().SendMessage(
                    email,
                    "you were added our Bank system",
                    "thank you for joining us"

            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("done successfully");
            alert.setHeaderText("new user is added");
            alert.setContentText("a message was send to the new user on email");
            alert.showAndWait();

            IDText.clear();
            firstNameText.clear();
            lastNameText.clear();
            emailText.clear();
            phoneText.clear();
            addressText.clear();
            bloodTypeText.clear();
            medicalHistoryText.clear();

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }

    @FXML
    private void updateUserInfo(){

        try{

            int id = Integer.parseInt(IDText.getText());
            String firstName = firstNameText.getText();
            String lastname = lastNameText.getText();
            String email = emailText.getText();
            String phone = phoneText.getText();
            String address = addressText.getText();
            String bloodType = bloodTypeText.getText();
            String medHistory = medicalHistoryText.getText();


            DataBase.getDataBase().updateUser(
                    id,
                    firstName,
                    lastname,
                    address,
                    phone,
                    email,
                    bloodType,
                    medHistory
            );

            EmailSender.getEmailSender().SendMessage(
                    email,
                    "you information was added in our Bank system",
                    "check the new changes"

            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("done successfully");
            alert.setHeaderText("new user information is edited");
            alert.setContentText("a message was send to user on email about the edits");
            alert.showAndWait();
            loadData();


        }catch (Exception e){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setHeaderText("");
            alert.setContentText(e.getMessage());
            alert.showAndWait();


        }
    }
    private void searchFilter(){
        FilteredList<SystemUser> filterData= new FilteredList<>(systemUsers, e->true);
        searchPersonText.setOnKeyReleased(e->{
            searchPersonText.textProperty().addListener((observable,oldValue,newValue)->{
                filterData.setPredicate((Predicate<? super SystemUser>) user ->{
                    if(newValue == null){
                        return true;
                    }
                    final String toLowerCaseFilter = newValue.toLowerCase();
                    if(String.valueOf(user.getID()).contains(newValue)){
                        return  true;
                    }else if(user.getFirstName().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getLastName().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getPhone_number().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getEmail().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getBloodType().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }else if(user.getMedicalHistory().toLowerCase().contains(toLowerCaseFilter)){
                        return  true;
                    }

                return  false;
                });
            });
        final SortedList<SystemUser> users = new SortedList<>(filterData);
        users.comparatorProperty().bind(personTableView.comparatorProperty());
        personTableView.setItems(users);
        });

    }

    @FXML
    private void removeUser(){


    }


}