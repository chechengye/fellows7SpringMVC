package com.lovecoding.service;

import com.lovecoding.pojo.Item;

import java.util.List;

public interface ItemService {

    Item getItemById(Integer id);

    int updateItem(Item item);

    List<Item> getItemList();

    int deleteItem(String[] ids);
}
