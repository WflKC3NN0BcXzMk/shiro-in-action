package cn.mycloudx.shiroweb.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author zhanghao
 * @date 2018/05/19
 */
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Integer userId;
    @Column(name = "username")
    private String user;
    private String password;
}
