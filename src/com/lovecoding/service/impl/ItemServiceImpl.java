package com.lovecoding.service.impl;

import com.lovecoding.mapper.ItemMapper;
import com.lovecoding.pojo.Item;
import com.lovecoding.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
