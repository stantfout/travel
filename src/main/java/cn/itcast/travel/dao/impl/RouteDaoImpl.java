package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rnameStr) {
        String sql = "select count(*) from tab_route where 1=1";
        List params = new ArrayList();
        if(cid != 0){
            sql += " and cid=? ";
            params.add(cid);
        }
        if(rnameStr != null && rnameStr.length() > 0) {
            sql += " and rname like ?";
            params.add("%"+rnameStr+"%");
        }
//        System.out.println(sql);
//        for (Object param : params) {
//            System.out.println(param);
//        }
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rnameStr) {
        //String sql = "select * from tab_route where cid = ? limit ? , ?";
        String sql = "select * from tab_route where 1=1 ";
        List params = new ArrayList();
        if(cid != 0){
            sql += " and cid=? ";
            params.add(cid);
        }
        if(rnameStr != null && rnameStr.length() > 0) {
            sql += " and rname like ? ";
            params.add("%"+rnameStr+"%");
        }
        sql += " limit ? , ?";
        params.add(start);
        params.add(pageSize);
//        System.out.println(sql);
//        for (Object param : params) {
//            System.out.println(param);
//        }
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
        return routeList;

    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid=?";
        Route route = template.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class),rid);
        return route;
    }
}
