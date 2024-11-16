package pers.ember.backend.dao;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDao {
    private String name;
    private String password;
    private String email;
    private String code;
}
