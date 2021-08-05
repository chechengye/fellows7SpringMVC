package com.lovecoding.mapper;

import com.lovecoding.pojo.Item;

import java.util.List;

public interface ItemMapper {

    Item getItemById(Integer id);

    int updateItem(Item item);

    List<Item> getItemList();

    int deleteItem(String[] ids);
}
