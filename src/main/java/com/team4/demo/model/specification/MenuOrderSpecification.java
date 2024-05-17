package com.team4.demo.model.specification;

import com.team4.demo.model.entity.MenuOrder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuOrderSpecification implements Specification<MenuOrder> {

    private Integer restaurantId;

    private String restaurantName;

    private String memberName;

    private Integer memberId;

    private String status;

    private String orderType;

    // 複合查詢中，查詢訂單時間，範圍起始時間、結束時間
    private Long orderCreatedTimeStart;
    private Long orderCreatedTimeEnd;

    @Override
    public Predicate toPredicate(Root<MenuOrder> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // 複合查詢：組合查詢條件
        // 每一個查詢條件不為 null、不為空，就創建一個 Predicate 物件加入陣列中
        // 最後把陣列中的所有 Predicate 物件，用 criteriaBuilder.and() 或 criteriaBuilder.or() 組合起來

        if (restaurantId != null) {
            // root 代表 MenuOrder 實體，get("restaurant") 取得 MenuOrder 的關聯屬性 restaurant
            // get("restaurantId") 代表 Restaurant 實體的主鍵屬性 restaurantId
            predicates.add(criteriaBuilder.equal(root.get("restaurant").get("restaurantId"), restaurantId));
        }

        if (StringUtils.isNotBlank(restaurantName)) {
            predicates.add(criteriaBuilder.like(root.get("restaurant").get("name"), "%" + restaurantName + "%"));
        }

        if (StringUtils.isNotBlank(memberName)) {
            // 訂單的會員姓名，模糊查詢
            // root 代表 MenuOrder 實體，get("member") 取得 MenuOrder 的關聯屬性 member
            // get("name") 代表 Member 實體的屬性 name
            // 當加入此查詢條件，Spring Data JPA 會自動幫我們做 join，也就是 menu_order join member 資料表，並對 member.name 欄位做模糊查詢
            predicates.add(criteriaBuilder.like(root.get("member").get("name"), "%" + memberName + "%"));
        }

        if (memberId != null) {
            predicates.add(criteriaBuilder.equal(root.get("member").get("memberId"), memberId));
        }

        if (StringUtils.isNotBlank(status)) {

            // 查詢進行中的訂單
            if (status.equals("CREATED") || status.equals("PROCESSING")) {

                // 查詢狀態為 CREATED 或 PROCESSING，使用 criteriaBuilder.or 組合條件，或下面的 in 寫法，都可以
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("status"), "CREATED"),
                        criteriaBuilder.equal(root.get("status"), "PROCESSING")
                ));

                // 使用 in 寫法也可以
//                predicates.add(criteriaBuilder.in(root.get("status")).value("CREATED").value("PROCESSING"));
            }
            // 查詢已完成、已取消的訂單
            else {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
        }

        if (StringUtils.isNotBlank(orderType)) {
            predicates.add(criteriaBuilder.equal(root.get("orderType"), orderType));
        }

        if (orderCreatedTimeStart != null && orderCreatedTimeEnd != null) {
            Date startDate = new Date(orderCreatedTimeStart);
            Date endDate = new Date(orderCreatedTimeEnd);
            predicates.add(criteriaBuilder.between(root.get("createdTime"), startDate, endDate));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    // 另外一種寫法，MenuOrderSpecification 就不用實作 Specification<MenuOrder> 介面，不用 override toPredicate() 方法
    // 自己創建一個方法 createSpecification()，並使用匿名內部類別實作，回傳 Specification<MenuOrder> 介面
    public Specification<MenuOrder> createSpecification() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (restaurantId != null) {
                predicates.add(criteriaBuilder.equal(root.get("restaurant").get("restaurantId"), restaurantId));
            }

            if (StringUtils.isNotBlank(memberName)) {
                predicates.add(criteriaBuilder.like(root.get("member").get("name"), "%" + memberName + "%"));
            }

            if (StringUtils.isNotBlank(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (StringUtils.isNotBlank(orderType)) {
                predicates.add(criteriaBuilder.equal(root.get("orderType"), orderType));
            }

            if (orderCreatedTimeStart != null && orderCreatedTimeEnd != null) {
                Date startDate = new Date(orderCreatedTimeStart);
                Date endDate = new Date(orderCreatedTimeEnd);
                predicates.add(criteriaBuilder.between(root.get("createdTime"), startDate, endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
