/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Percy
 */

@WebServlet(name = "KijijiImage", urlPatterns = {"/image/*"})
public class KijijiImage extends HttpServlet {
    
    
  // https://stackoverflow.com/questions/8623709/output-an-image-file-from-a-servlet/8623747#8623747
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
      ServletContext cntx= request.getServletContext();
        
      String filename =  System.getProperty("user.home")+"/KijijiImages/" + request.getPathInfo().substring(1);
      
      String mime = cntx.getMimeType(filename);
      if (mime == null) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return;
      }
      
      response.setContentType(mime);
      File file = new File(filename);
      response.setContentLength((int)file.length());

     try( FileInputStream in = new FileInputStream(file);
     OutputStream out = response.getOutputStream()){ 

       byte[] buf = new byte[1024];
       int count = 0;
       while ((count = in.read(buf)) >= 0) {
         out.write(buf, 0, count);
      }
         
     }catch(IOException ex){
         log(ex.getMessage(), ex);
     }
    
}

}