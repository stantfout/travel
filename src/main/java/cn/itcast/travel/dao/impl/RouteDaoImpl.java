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
    public int findTotalCount(int cid, String rnameStr, int price1, int price2) {
        String sql = "select count(*) from tab_route where 1=1";
        List params = new ArrayList();
        if(cid != 0 && cid != -1){
            sql += " and cid=? ";
            params.add(cid);
        } else if (cid == -1){
            sql += " and count > 0 ";
        }
        if(rnameStr != null && rnameStr.length() > 0) {
            sql += " and rname like ?";
            params.add("%"+rnameStr+"%");
        }
        if(price1 != -1) {
            sql += " and price >= ? ";
            params.add(price1);

        }
        if(price2 != -1) {
            sql += " and price <= ? ";
            params.add(price2);
        }
        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rnameStr, int price1, int price2) {
        //String sql = "select * from tab_route where cid = ? limit ? , ?";
        String sql = "select * from tab_route where 1=1 ";
        List params = new ArrayList();
        if(cid != 0 && cid != -1){
            sql += " and cid=? ";
            params.add(cid);
        } else if (cid == -1){
            sql += " and count > 0 ";
        }
        if(rnameStr != null && rnameStr.length() > 0) {
            sql += " and rname like ? ";
            params.add("%"+rnameStr+"%");
        }
        if(price1 != -1) {
            sql += " and price >= ? ";
            params.add(price1);

        }
        if(price2 != -1) {
            sql += " and price <= ? ";
            params.add(price2);
        }
        if(cid == -1) {
            sql += " order by count desc ";
        }
        sql += " limit ? , ?";
        params.add(start);
        params.add(pageSize);
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
        return routeList;

    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid=?";
        Route route = template.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class),rid);
        return route;
    }

    @Override
    public List<Route> findByFavorite(int uid, int start, int pageSize) {

        String sql = "SELECT * from tab_route WHERE rid in (SELECT rid FROM tab_favorite WHERE uid = ?) limit ?,?";
        List<Route> query = template.query(sql, new BeanPropertyRowMapper<>(Route.class), uid, start, pageSize);
        return query;
    }

    @Override
    public void addFavorite(int rid) {
        String sql = "update tab_route set count = count + 1 where rid = ?";
        template.update(sql,rid);
    }

    @Override
    public List<Route> findHot(int sum,int cid) {
        String sql = "SELECT * FROM tab_route where 1 = 1 ";
        List params = new ArrayList();
        if(cid != 0) {
            sql += " and cid = ? ";
            params.add(cid);
        }
        sql += "ORDER BY count DESC LIMIT 0,?";
        params.add(sum);
        List<Route> query = template.query(sql, new BeanPropertyRowMapper<>(Route.class),params.toArray());
        return query;
    }
}
