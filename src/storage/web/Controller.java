package storage.web;

import storage.domain.Customer;
import storage.domain.Goods;
import storage.service.CustomerService;
import storage.service.GoodsService;
import storage.service.OrderService;
import storage.service.ServiceException;
import storage.service.imp.CustomerServiceImp;
import storage.service.imp.GoodsServiceImp;
import storage.service.imp.OrderServiceImp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

//@javax.servlet.annotation.WebServlet(name = "Controller", urlPatterns = {"/controller"})
public class Controller extends javax.servlet.http.HttpServlet {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private CustomerService customerService = new CustomerServiceImp();
    private GoodsService goodsService = new GoodsServiceImp();
    private OrderService orderService = new OrderServiceImp();

    private int totalPageNumber = 0;
    private int pageSize = 10;
    private int currentPage = 1;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        pageSize = new Integer(config.getInitParameter("pageSize"));
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        String action = request.getParameter("action");

        if ("reg".equals(action)) {
            // ------- User Register -------
            String userID = request.getParameter("userid");
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String password2 = request.getParameter("password2");
            String birthday = request.getParameter("birthday");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");

            // Verify user info on server
            List<String> errors = new ArrayList<>();
            if (userID == null || userID.equals("")) {
                errors.add("User ID cannot be empty!");
            }

            if (name == null || name.equals("")) {
                errors.add("User name cannot be empty!");
            }

            if (password == null || password.equals("") || password2 == null || password2.equals("")) {
                errors.add("Password cannot be empty!");
            }

            if (password != null && password2 != null && !password.equals(password2)) {
                errors.add("Passwords do not match!");
            }

            String pattern = "^((((19|20)(([02468][048])|([13579][26]))-02-29))|((20[0-9][0-9])|(19[0-9][0-9]))-((((0[1-9])|(1[0-2]))-((0[1-9])|(1\\d)|(2[0-8])))|((((0[13578])|(1[02]))-31)|(((0[1,3-9])|(1[0-2]))-(29|30)))))$";
            if (!Pattern.matches(pattern, birthday)) {
                errors.add("Invalid birthday！");
            }

            if (errors.size() > 0) { // Verification failed
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
            } else { // Verification succed

                Customer customer = new Customer();
                customer.setId(userID);
                customer.setName(name);
                customer.setPassword(password);
                customer.setAddress(address);
                customer.setPhone(phone);

                try {
                    Date date = dateFormat.parse(birthday);
                    customer.setBirthday(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Register
                try {
                    customerService.register(customer);
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                } catch (ServiceException e) {
                    // User ID already exists
                    errors.add("User ID already exists!");
                    request.setAttribute("errors", errors);
                    request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
                }
            }
        } else if ("login".equals(action)) {
            // ------- User Login -------
            String userid = request.getParameter("userid");
            String password = request.getParameter("password");

            Customer customer = new Customer();
            customer.setId(userid);
            customer.setPassword(password);

            if (customerService.login(customer)) {
                // Login succeed
                HttpSession session = request.getSession();
                session.setAttribute("customer", customer);
                request.getRequestDispatcher("main.jsp").forward(request, response);
            } else {
                // Login failed
                List<String> errors = new ArrayList<>();
                errors.add("Incorrect userid or password!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } else if ("list".equals(action)) {
            // ------- Show item list -------
            List<Goods> goodsList = goodsService.queryAll();

            if (goodsList.size() % pageSize == 0) {
                totalPageNumber = goodsList.size() / pageSize;
            } else {
                totalPageNumber = goodsList.size() / pageSize + 1;
            }

            request.setAttribute("totalPageNumber", totalPageNumber);
            request.setAttribute("currentPage", currentPage);
            int start = (currentPage - 1) * pageSize;
            int end = (currentPage == totalPageNumber) ? goodsList.size() : currentPage * pageSize;

            request.setAttribute("goodsList", goodsList.subList(start, end));
            request.getRequestDispatcher("goods_list.jsp").forward(request, response);

        } else if ("paging".equals(action)) {
            // ------- Item list paging -------
            String page = request.getParameter("page");

            if (page.equals("prev")) { // previous page
                currentPage--;
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } else if (page.equals("next")) { // next page
                currentPage++;
                if (currentPage > totalPageNumber) {
                    currentPage = totalPageNumber;
                }
            } else {
                currentPage = Integer.valueOf(page);
            }
            query(request, response);

        } else if ("detail".equals(action)) {
            // ------- show goods details -------
            Long goodsid = new Long(request.getParameter("id"));
            Goods goods = goodsService.queryDetail(new Long(goodsid));
            request.setAttribute("goods", goods);
            request.getRequestDispatcher("goods_detail.jsp").forward(request, response);
        } else if ("add".equals(action)) {
            // ------- add to cart -------
            Long goodsid = new Long(request.getParameter("id"));
            String name = request.getParameter("name");
            Float price = new Float(request.getParameter("price"));

            // Get cart from session
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            // First access - null
            if (cart == null) {
                cart = new ArrayList<>();
                request.getSession().setAttribute("cart", cart);
            }

            boolean found = false;

            for (Map<String, Object> item : cart) {
                Long currID = (Long) item.get("goodsid");
                // Item exists
                if (goodsid.equals(currID)) {
                    found = true;
                    Integer quantity = (Integer) item.get("quantity");
                    quantity++;
                    item.put("quantity", quantity);
                }
            }

            if (!found) {
                Map<String, Object> item = new HashMap<>();
                item.put("goodsid", goodsid);
                item.put("goodsname", name);
                item.put("quantity", 1);
                item.put("price", price);
                cart.add(item);
            }

            String pageName = request.getParameter("pagename");

            if (pageName.equals("list")) {
                query(request, response);
            } else if (pageName.equals("detail")) {
                Goods goods = goodsService.queryDetail(new Long(goodsid));
                request.setAttribute("goods", goods);
                request.getRequestDispatcher("goods_detail.jsp").forward(request, response);
            }
        } else if ("cart".equals(action)) {
            // ------- display cart -------
            // Get cart from session
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            double total = 0.0;

            if (cart != null) {
                for (Map<String, Object> item : cart) {
                    Integer quantity = (Integer) item.get("quantity");
                    Float price = (Float) item.get("price");
                    double subtotal = price * quantity;
                    total += subtotal;
                }
            }

            request.setAttribute("total", total);
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } else if ("sub_ord".equals(action)) {
            // ------- submit order -------
            // Get cart from session
            List<Map<String, Object>> cart = (List<Map<String, Object>>) request.getSession().getAttribute("cart");

            if (cart != null) {
                for (Map<String, Object> item : cart) {
                    Long goodsid = (Long) item.get("goodsid");
                    String quantityStr = request.getParameter("quantity_" + goodsid);
                    int quantity = 0;
                    try {
                        quantity = new Integer(quantityStr);
                    } catch (Exception e) {
                        // Do nothing
                    }
                    item.put("quantity", quantity);
                }

                // Submit orders
                String ordersid = orderService.submitOrders(cart);
                request.setAttribute("ordersid", ordersid);
                request.getRequestDispatcher("order_finish.jsp").forward(request, response);
                request.getSession().removeAttribute("cart");
            }
        } else if ("main".equals(action)) {
            // ------- Jump to main page -------
            request.getRequestDispatcher("main.jsp").forward(request, response);
        } else if ("logout".equals(action)) {
            // ------- logout -------
            // Clear session
            request.getSession().removeAttribute("cart");
            request.getSession().removeAttribute("customer");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else if ("reg_init".equals(action)) {
            // ------- enter register page -------
            request.getRequestDispatcher("customer_reg.jsp").forward(request, response);
        }

    }

    private void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int start = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        List<Goods> goodsList = goodsService.queryByStartEnd(start, end);
        request.setAttribute("totalPageNumber", totalPageNumber);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("goodsList", goodsList);
        request.getRequestDispatcher("goods_list.jsp").forward(request, response);
    }
}
