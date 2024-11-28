package pers.ember.backend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Progress {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    @TableField("email")
    private String userEmail;
    private String progress;
}
