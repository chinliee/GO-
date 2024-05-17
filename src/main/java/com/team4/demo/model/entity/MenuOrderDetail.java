package com.team4.demo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "menu_order_detail")
public class MenuOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = MenuOrder.class)
    @JoinColumn(name = "order_id")
    private MenuOrder menuOrder;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Menu.class)
    @JoinColumn(name = "menu_id")
    private Menu menu;

}
