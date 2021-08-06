package com.lovecoding.controller;

import com.lovecoding.common.Constants;
import com.lovecoding.pojo.Item;
import com.lovecoding.service.ItemService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Controller :
 *      1、可以将类注入到Spring容器中
 *      2、可以标识此类是一个控制层的类
 * @RequestMapping : 可以作用于方法。此时针对于一张表的CURD都可以放在一个控制类中进行映射接收处理了。
 */
@Controller
//@RequestMapping("item")
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
     * @RequestParam :
     *          value 限制前端传递的参数名称
     *          required : 是否必传  默认必传
     *          defaultValue : 默认值 通常与 required = false 配合使用
     *          若是required为默认必传时，不建议添加defaultValue
     *
     *          Boolean 参数 若前端传递的是 1 会自动转为true ，若为0 会转为false
     * @param id
     * @return
     */
    @RequestMapping(value = {"/getItemById.do" , "/itemById.do"} , method = RequestMethod.GET)
    public ModelAndView getItemById(@RequestParam(value = "item_id" ) Integer itemId
            , @RequestParam(value = "name" , required = false) String name
            , @RequestParam(value = "pageNum" , required = false , defaultValue = "1") Integer pageNum , Boolean status){
        //String id = req.getParameter("id");
        System.out.println("id = " + itemId);
        System.out.println("status = " + status);
        System.out.println("name = " + name);
        Item item = itemService.getItemById(itemId);
        System.out.println("item = " + item);
        ModelAndView mav = new ModelAndView();
        mav.addObject("item" , item);
        mav.setViewName("editItem");
        return mav;
    }

    /**
     * 对象类型，也可以直接从前端接收到
     * 推荐使用Model和String 配合 由Model向域中存值，用String来控制跳转页面。
     * 无需实例化对象，由Tomcat引擎帮我们实例化好了Model对象
     *
     * MultipartFile ： 用来接收图片相关
     * @return
     */
    @RequestMapping(value = "/updateItem.do" , method = {RequestMethod.POST})
    public String updateItem(@RequestParam(value = "pictureFile" , required = false) MultipartFile pictureFile  , Model model , Item item , HttpServletRequest req){

        try {
            System.out.println("item = " + item);
            //1、接收前端上传的文件
            System.out.println("pictureFile : " + pictureFile);
            //2、获取图片存储的真实路径
            String realPath = req.getServletContext().getRealPath(Constants.UPLOAD_URL);
            System.out.println("realPath = " + realPath);
            //3、修改图片名称,防止多张图片重名的问题
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmssSSS");
            String fileNewName = df.format(new Date());
            //4、通过文件工具类获取文件扩展名 png  不会携带.
            String extension = FilenameUtils.getExtension(pictureFile.getOriginalFilename());
            System.out.println("extension = " + extension);
            //5、拼接新文件名称
            String newOriginalFilename = fileNewName + "." + extension;
            System.out.println("newOriginalFilename = " + newOriginalFilename);
            //6、上传到服务端、wind上传到磁盘目录
            pictureFile.transferTo(new File(realPath + "/" +newOriginalFilename));
            //7、将相对路径存于数据库
            item.setPic(Constants.UPLOAD_URL + "/" + newOriginalFilename);


        } catch (IOException e) {
            e.printStackTrace();
        }


        int rows = itemService.updateItem(item);
        if(rows > 0){
            List<Item> itemList = itemService.getItemList();
            model.addAttribute("itemList" , itemList);
            return "itemList";
        }
        return "editItem";
    }

    @RequestMapping("/deleteItem.do")
    public String deleteItem(Model model , String[] ids){
        System.out.println("ids = " + Arrays.toString(ids));
        int rows = itemService.deleteItem(ids);
        if(rows > 0){
            List<Item> itemList = itemService.getItemList();
            model.addAttribute("itemList" , itemList);
        }
        return "itemList";
    }

    /**
     * void : 接收异步请求，数据返回不存于域中
     *
     */
    @RequestMapping("/ajax")
    public void testFn(HttpServletResponse resp){
        try {
            resp.getWriter().write("{]\"name\":\"小米\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收Json格式数据
     * @RequestBody : 接收前端传递的Json数据。比如 添加功能方法
     * @ResponseBody : 可以将返回的参数 对象、列表、Map等等 自动转换为Json格式
     * 注意： 通常 表单参数 与Json格式参数不建议放在一个方法的参数中。
     */

    @RequestMapping(value = "/itemJson.do" , method = RequestMethod.POST)
    @ResponseBody
    public Item testJson(@RequestBody Item item){
        System.out.println("item = " + item);

        item.setName("修改后的Item");
        return item;
    }
}
