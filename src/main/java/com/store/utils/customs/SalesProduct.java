package com.store.utils.customs;

import com.store.entitys.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesProduct {
    private Product productId;
    private Double sales;
}
