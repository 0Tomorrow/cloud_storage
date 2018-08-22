package com.test.mqcore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "userinfos")
@Data
@AllArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String sex;
    private String userPass;
    private Long account;
    private String nickName;

    public UserInfo(String sex, String userPass, Long account, String nickName) {
        this.sex = sex;
        this.userPass = userPass;
        this.account = account;
        this.nickName = nickName;
    }
}
