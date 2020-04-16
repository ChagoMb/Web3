package servlet;


import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String password = req.getParameter("password");
            long money = Long.parseLong(req.getParameter("money"));

            boolean sc = new BankClientService().addClient(new BankClient(name, password, money));

            if (sc) {
                resp.getWriter().println("Add client successful");
            } else {
                resp.getWriter().println("Client not add");
            }

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
