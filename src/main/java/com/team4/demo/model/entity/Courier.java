package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "courier")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "courier_id")
    private Integer courierId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private Integer status;

    @Column(name = "online_status")
    private Integer onlineStatus;

    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "registeration_date")
    private Date registeration_date;

    @Column(name = "longitude")
    private BigDecimal longitude;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuOrder.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MenuOrder> menuOrderList = new ArrayList<>();

}
