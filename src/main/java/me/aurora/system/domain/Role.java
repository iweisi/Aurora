package me.aurora.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 角色
 * @author 郑杰
 * @date 2018/08/20 20:10:15
 */
@Entity
@Table(name = "zj_role")
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = {Update.class})
    private Long id;
    
    @NotBlank(groups = {New.class,Update.class})
    @Column(nullable = false)
    private String name;

    @Column
    private String remark;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Department> departments;

    @ManyToMany
    @JoinTable(name = "zj_permissions_roles", joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id",referencedColumnName = "id")})
    private Set<Permission> permissions;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Menu> menus;

    @Column
    @CreationTimestamp
    private Timestamp createDateTime;

    @Column
    @UpdateTimestamp
    private Timestamp updateDateTime;

    public interface New{};
    public interface Update{};

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                '}';
    }
}
