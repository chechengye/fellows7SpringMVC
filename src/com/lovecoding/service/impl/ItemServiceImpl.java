package com.lovecoding.service.impl;

import com.lovecoding.mapper.ItemMapper;
import com.lovecoding.pojo.Item;
import com.lovecoding.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemMapper itemMapper;
    /**
     * 根据主键获取一个Item信息
     * @param id
     * @return
     */
    @Override
    public Item getItemById(Integer id) {
        return itemMapper.getItemById(id);
    }

    /**
     * 更新商品
     * @param item
     * @return
     */
    @Override
    public int updateItem(Item item) {
        return itemMapper.updateItem(item);
    }

    @Override
    public List<Item> getItemList() {
        return itemMapper.getItemList();
    }

    /**
     * 根据复选框选中的内容删除Item
     * @param ids
     * @return
     */
    @Override
    public int deleteItem(String[] ids) {
        return itemMapper.deleteItem(ids);
    }
}
