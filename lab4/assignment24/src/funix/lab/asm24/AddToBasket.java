package funix.lab.asm24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddToBasket
 */
@WebServlet(name = "AddToBasket", urlPatterns = "/AddToBasket")
public class AddToBasket extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String SESSION_BASKET = "basket";
    public static final String PRODUCT_ID = "productId";
    public static final String CALLBACK_URL = "link";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToBasket() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String productId = request.getParameter(PRODUCT_ID);
        final String link = Optional.ofNullable(request.getParameter(CALLBACK_URL)).orElseGet(getServletContext()::getContextPath);
        if (productId != null) {
            final HttpSession session = request.getSession();
            List<String> basket = (List<String>) session.getAttribute(SESSION_BASKET);
            if (basket == null) basket = new ArrayList<>();
            basket.add(productId);
            session.setAttribute(SESSION_BASKET, basket);
        }
        response.sendRedirect(link);
	}
}
