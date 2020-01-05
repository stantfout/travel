package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteService {

    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rnameStr);

    Route findOne(String rid);

    PageBean<Route> pageQueryFavorite(int uid, int currentPage, int pageSize);

    List<Route> pageQueryHot();
}
