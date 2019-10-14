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

public class UploadServlet extends HttpServlet {

	private boolean isMultipart;
	private String dataBasePath;
	private int maxFileSize = 6400000 * 1024;
	private int maxMemSize = 128000 * 1024;


	DiskFileItemFactory factory = null;
	ServletFileUpload upload = null;

	public void init() {
		factory = new DiskFileItemFactory();
		// max dimension stored in memory instead in repository
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		// factory.setRepository(new File("c:\temp"));
		factory.setRepository(new File(getServletConfig().getInitParameter("temp-folder")));
		upload = new ServletFileUpload(factory);
		// max size upload
		upload.setSizeMax(maxFileSize);

		// Get the file location where it would be stored.
		dataBasePath = getServletConfig().getInitParameter("data-folder");
		System.out.println("servlet configurata per ricevere video su" + dataBasePath);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		PrintWriter out = response.getWriter();
		out.println("sono nella servlet");
		/*
		 * id domanda opzione_corretta opzione_errata1 opzione_errata2 opzione_errata3
		 * video
		 */
		// Check that we have a file upload request
		if (ServletFileUpload.isMultipartContent(request)) {// e una richiesta di upload
			try {
				
				String videoPath = dataBasePath + "/";
				
				List<FileItem> fileItems = upload.parseRequest(request);
				
				for(FileItem fi : fileItems) {
					if (fi.isFormField()) {
						String fieldname = fi.getFieldName();
						String fieldvalue = fi.getString();
						if (fieldname.equals("path")) {
							videoPath += fieldvalue;
						}
					}
				}
				
				for(FileItem fi : fileItems) {
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();
						
						if (fieldName.equals("video")) {
							File file = new File(videoPath + "/" + fileName);
							if(!file.exists())
								file.createNewFile();
							out.println("carico spot");
							fi.write(file);
						}
					}
				}
				
				/*
				Iterator<FileItem> i = fileItems.iterator();
				out.println("entro nel ciclo");
				while (i.hasNext()) {
					FileItem fi = (FileItem) i.next();
					if (!fi.isFormField()) {
						String fieldName = fi.getFieldName();
						String fileName = fi.getName();
						String contentType = fi.getContentType();
						boolean isInMemory = fi.isInMemory();
						long sizeInBytes = fi.getSize();
						
						if(fieldName.contentEquals("path")) {
							videoPath += fileName;
						}

						if (fieldName.equals("video")) {
							// String prefix = getServletContext().getRealPath("/");
							// String path = filePath + fileName.substring( fileName.lastIndexOf("/"));
							videoPath += "/" + fileName;
							//videoHost = dataBasePath + fileName + generatedCode;
							File file = new File(videoPath);
							if(!file.exists())
								file.createNewFile();
							out.println("carico spot");
							fi.write(file);
						}
					} 
					else {
						String fieldname = fi.getFieldName();
						String fieldvalue = fi.getString();
						if (fieldname.equals("path")) {
							videoPath += "/" + fieldvalue;
						}
					}
				}*/
			} catch (Exception ex) {
				System.out.println(ex);
				response.sendRedirect("errore.html");
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		
	}

}