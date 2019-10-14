package serviceServlets;

import java.io.*;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class CreateFolderServlet extends HttpServlet {
	private String dataBasePath;

	DiskFileItemFactory factory = null;
	ServletFileUpload upload = null;

	public void init() {
		dataBasePath = getServletConfig().getInitParameter("data-folder");
		System.out.println("servlet configurata per creare cartelle su" + dataBasePath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		String folder = (String) request.getParameter("folderPath");
		File file = new File(dataBasePath + "/" + folder);
		file.mkdir();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
	}

}