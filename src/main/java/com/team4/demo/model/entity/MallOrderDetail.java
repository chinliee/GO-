package com.team4.demo.model.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mall_order_detail")
public class MallOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_price", nullable = false)
    private Integer productPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "order_time", nullable = false)
    private Date orderTime;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = MallOrder.class)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private MallOrder mallOrder;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

}
