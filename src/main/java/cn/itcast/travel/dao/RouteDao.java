package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据cid查询总记录数
     * @param cid
     * @param rnameStr
     * @return
     */
    int findTotalCount(int cid, String rnameStr);

    /**
     * 查询单页记录
     * @param cid
     * @param start
     * @param pageSize
     * @param rnameStr
     * @return
     */
    List<Route> findByPage(int cid, int start, int pageSize, String rnameStr);

    /**
     * 根据id查询
     * @param rid
     * @return
     */
    Route findOne(int rid);

    List<Route> findByFavorite(int uid,int start,int pageSize);
}
