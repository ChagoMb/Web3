package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String senderName = req.getParameter("senderName");
            String senderPass = req.getParameter("senderPass");
            long count = Long.parseLong(req.getParameter("count"));
            String nameTo = req.getParameter("nameTo");

            boolean sc = bankClientService.sendMoneyToClient(new BankClient(senderName, senderPass, 1000L), nameTo, count);

            if (sc) {
                resp.getWriter().println("The transaction was successful");
            } else {
                resp.getWriter().println("transaction rejected");
            }

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
