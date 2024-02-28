package com.fusheng.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class KVPair<K, V> implements Serializable {
    private K key;
    private V value;
}
