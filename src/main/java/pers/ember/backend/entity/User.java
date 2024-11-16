package pers.ember.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private String name;

    private String password;

    private String email;

    private String status;

    @TableField("createTime")
    private String createTime;

}
