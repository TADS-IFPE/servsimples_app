/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;
    private final Agenda agenda = new Agenda();
    private final Wallet wallet = new Wallet();
    private final List<Notification> notifications = new ArrayList<>();
    private final List<Service> services = new ArrayList<>();
    private String name;
    private String CPF;
    private String userName;
    private String password;
    private UserType userType = UserType.USER;
    private String token;

    public enum UserType {
        USER, PROFESSIONAL, ADMIN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Service> getServices() {
        return services;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
