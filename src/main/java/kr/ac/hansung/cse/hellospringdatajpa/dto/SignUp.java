package kr.ac.hansung.cse.hellospringdatajpa.dto;

public class SignUp {
    private String email;
    private String password;

    // 기본 생성자
    public SignUp() {
    }

    // getter/setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

