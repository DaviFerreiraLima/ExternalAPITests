package resources;

import java.util.Date;

public class User {

    private String id;
    private String name;
    private String cpf;
    private String email;
    private Date dateOfBirth;

    public User( String name, String cpf, String email, Date dateOfBirth) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public User(){}
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
