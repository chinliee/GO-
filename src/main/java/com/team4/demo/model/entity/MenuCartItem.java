package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "menu_cart_item")
public class MenuCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name= "created_time")
    private Date createdTime;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MenuCart.class)
    @JoinColumn(name = "cart_id")
    private MenuCart menuCart;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Menu.class)
    @JoinColumn(name = "menu_id")
    private Menu menu;

}
