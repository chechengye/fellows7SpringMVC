package com.lovecoding.controller;

import com.lovecoding.pojo.Item;
import com.lovecoding.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Controller :
 *      1、可以将类注入到Spring容器中
 *      2、可以标识此类是一个控制层的类
 * @RequestMapping : 可以作用于方法。此时针对于一张表的CURD都可以放在一个控制类中进行映射接收处理了。
 */
@Controller
public class ItemController {

    @Autowired
    JdbcTemplate jt;


    @Autowired
    ItemService itemService;
    /**
     * 相当于Servlet中 @WebServlet(urlParttern = "/itemList")
     * 由于在配置文件中加了以.do结尾，所以路由必须编写。否则找寻不到.
     */
    @RequestMapping("/itemList.do")
    public ModelAndView getItemList(HttpServletRequest req){
        System.out.println("----enter----");
        List<Item> itemList = jt.query("select * from items", new RowMapper<Item>() {

            @Override
            public Item mapRow(ResultSet rs, int i) throws SQLException {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setCreatetime(rs.getString("createtime"));
                item.setDetail(rs.getString("detail"));
                item.setPrice(rs.getString("price"));
                return item;
            }
        });

        System.out.println("itemList = " + itemList);
        //req.setAttribute();
        //Model : 底层实现是LinkedHashMap ，可以存放键值对  相当于 req.setAttribute(key , val);
        //View : 管理视图 - 跳转页面  相当于 req.getRequestDispatcher("页面")
        ModelAndView mav = new ModelAndView();

        mav.addObject("itemList" , itemList);
        mav.setViewName("itemList");//跳转到某一个视图

        return mav;//封装好的对象需要返回才可以被执行。相当于 - forward(req , resp)
    }

    /**
     * SpringMVC的入参直接支持基本数据类型与引用类型
     * @param id
     * @return
     */
    @RequestMapping("/getItemById.do")
    public ModelAndView getItemById(Integer id){
        //String id = req.getParameter("id");
        System.out.println("id = " + id);
        Item item = itemService.getItemById(id);
        System.out.println("item = " + item);
        ModelAndView mav = new ModelAndView();
        mav.addObject("item" , item);
        mav.setViewName("editItem");
        return mav;
    }

}
