package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rnameStr,int price1, int price2) {
        //封装PageBean
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示条数
        pageBean.setPageSize(pageSize);
        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid,rnameStr,price1,price2);
        pageBean.setTotalCount(totalCount);
        //设置当前页显示的数据
        List<Route> page = routeDao.findByPage(cid, pageSize * (currentPage - 1), pageSize, rnameStr,price1,price2);
        pageBean.setList(page);
        //设置总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pageBean.setTotalPage(totalPage);
        return pageBean;
    }

    @Override
    public Route findOne(String rid) {

        //1.根据id去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2.根据route的id查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        route.setRouteImgList(routeImgList);

        //3.根据route的id查询商家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        return route;
    }

    @Override
    public PageBean<Route> pageQueryFavorite(int uid, int currentPage, int pageSize) {
        //封装PageBean
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示条数
        pageBean.setPageSize(pageSize);
        //设置总记录数
        int totalCount = favoriteDao.findCountByUid(uid);
        pageBean.setTotalCount(totalCount);
        //设置当前页显示的数据
        List<Route> page = routeDao.findByFavorite(uid, pageSize * (currentPage - 1), pageSize);
        pageBean.setList(page);
        //设置总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pageBean.setTotalPage(totalPage);
        return pageBean;
    }

    @Override
    public List<Route> pageQueryHot(int sum,int cid) {
        return routeDao.findHot(sum,cid);
    }
}
