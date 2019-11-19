package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import javafx.scene.transform.Rotate;
import org.springframework.beans.factory.wiring.BeanWiringInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rnameStr = request.getParameter("rname");
        rnameStr = new String(rnameStr.getBytes("iso-8859-1"), "utf-8");
        //2.处理参数
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !cidStr.equals("null")) {
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 1;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }

        int pageSize = 20;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        if (rnameStr.equals("null")) {
            rnameStr = null;
        }
        //3。调用service查询pageBean对象
        RouteService routeService = new RouteServiceImpl();
        PageBean<Route> routePageBean = routeService.pageQuery(cid, currentPage, pageSize, rnameStr);
        writeValue(routePageBean, response);
    }

    /**
     * 根据ID查询一个旅游线路的信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接受id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象
        RouteService routeService = new RouteServiceImpl();
        Route route = routeService.findOne(rid);
        //3.转换为json对象写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前用户是否收藏过该路线
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String rid = request.getParameter("rid");

        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if (user == null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }

        boolean flag = favoriteService.isFavorite(rid, uid);

        writeValue(flag, response);

    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取rid
        String rid = request.getParameter("rid");
        System.out.println(rid);
        //2.获取当前登陆的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;
        if(user == null) {
            return;
        } else {
            uid = user.getUid();
        }
        //3.调用service添加
        favoriteService.add(rid,uid);
    }

}
