package serviceServlets;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/requestDataServlets")
public class RequestDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String dataFolder;

	public void init() {
		dataFolder = getServletConfig().getInitParameter("data-folder");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requested_folder = request.getParameter("requested_folder");
		if(requested_folder == null)
			requested_folder = "";
		File baseFolder = new File(dataFolder + requested_folder);
		if(baseFolder.isDirectory()) {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			for(File file : baseFolder.listFiles()) {
				if(file.isFile()) {
					response.getWriter().println("file");
				}
				else
					response.getWriter().println("folder");
				response.getWriter().println(file.getName());
			}
			response.getWriter().println("free_space");
			String freeSpace = String.valueOf(baseFolder.getFreeSpace());
			response.getWriter().println(freeSpace);
		}else {
			response.sendRedirect("error_page.html");
			return;
		}

		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

}
