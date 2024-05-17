package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Integer menuId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private byte[] photo;

    // @JoinColumn，寫在多方(主控方，記憶有外來鍵的那邊)，name 屬性值是多方資料庫表格中，「外來鍵 foreign key 的欄位名稱」
    // @ToOne結尾的，fetch 預設都是 EAGER；@ToMany結尾的，fetch 預設是 LAZY
    // referencedColumnName(一般會省略) 屬性，是一方在資料庫表格中，被外來鍵參考的欄位名稱
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuCartItem.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MenuCartItem> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, orphanRemoval = true, targetEntity = MenuOrderDetail.class)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MenuOrderDetail> orderDetailList = new ArrayList<>();

}
