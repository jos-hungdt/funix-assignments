package funix.lab.asm24;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import funix.lab.asm24.model.*;

/**
 * Servlet implementation class Checkout
 */
@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
	private static final long serialVersionUID = 1L;
	CheckoutModel form = new CheckoutModel();
	public static final String SESSION_BASKET = "basket";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Checkout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		form.setName(request.getParameter("name"));
    	form.setCompany(request.getParameter("company"));
    	form.setAddressLine1(request.getParameter("addressLine1"));
    	form.setAddressLine2(request.getParameter("addressLine2"));
    	form.setZip(request.getParameter("zip"));
    	form.setCity(request.getParameter("city"));
    	form.setState(request.getParameter("state"));
    	form.setCountry(request.getParameter("country"));
    	form.setPhone(request.getParameter("phone"));
    	form.setEmail(request.getParameter("email"));
    	form.setComment(request.getParameter("comment"));

    	final HttpSession session = request.getSession();
    	session.setAttribute(SESSION_BASKET, new ArrayList<>());
    	response.sendRedirect("home.jsp");
	}

}
