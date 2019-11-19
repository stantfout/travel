package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        //2.判断查询的集合是否为空
        List<Category> cs = null;
        if(categorys == null || categorys.size() == 0) {
            //3.如果为空，从数据库查询，将数据存入redis
            cs = categoryDao.findAll();
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category",cs.get(i).getCid(),cs.get(i).getCname());
            }
        } else {
            //4.如果不为空，返回
            cs = new ArrayList<Category>();
            for (Tuple name : categorys) {
                Category category = new Category();
                category.setCname(name.getElement());
                category.setCid((int) name.getScore());
                cs.add(category);
            }
        }
        return cs;
    }
}
