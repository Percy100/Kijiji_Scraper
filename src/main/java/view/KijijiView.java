/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import common.FileUtility;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import entity.Category;
import entity.Image;
import entity.Item;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import scraper.kijiji.Kijiji;

/**
 *
 * @author Percy
 */

@WebServlet(name = "Kijiji", urlPatterns = {"/Kijiji"})
public class KijijiView extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Kijiji</title>");
            out.println("</head>");
            out.println("<body>");   

            ItemLogic itlogic = new ItemLogic();
            
            List<Item> itemList = itlogic.getAll();
            
            for (int i = 0; i<itemList.size(); i++) {
                out.print("<div class=\"center-column\">\n"
                        + "<!-- you can add many <div class=\"item\"> with in this div -->\n"
                        + "<div class=\"item\">\n"
                        + "<div class=\"image\">\n"
                        + "<img src=\"" + itemList.get(i).getImage().getUrl() + "\" style=\"max-width: 250px;\n"
                        + "max-height: 200px;\" />\n"
                        + "</div>\n"
                        + "<div class=\"details\">\n"
                        + "<div class=\"title\">\n"
                        + "<a href=\"" + itemList.get(i).getUrl() + "\"\n"
                        + "target=\"_blank\">" + itemList.get(i).getTitle() + "</a>\n"
                        + "<div class=\"Location\">\n" + itemList.get(i).getDate() + "\n" + "</div>\n"
                        + "<div class=\"Location\">\n" + "$" + itemList.get(i).getPrice() + "\n" + "</div>\n"
                        + "<div class=\"Location\">\n" + itemList.get(i).getLocation() + "\n" + "</div>\n"
                        + "<div class=\"Description\">\n" + itemList.get(i).getDescription() + "\n" + "</div>\n"
                        + "</div> \n");
                        out.println("<br>");
                        out.println("<br>");
                        out.println("<br>");
                        
            }
            out.println("</body>");
            out.println("</html>");
        }
    }
                
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        File file = new File(System.getProperty("user.home")+"/KijijiImages/");
        if(!file.exists()){
            file.mkdir();
        }
        
        CategoryLogic caLogic = new CategoryLogic();
        Kijiji kj = new Kijiji();
        kj.downloadPage(caLogic.getWithId(1).getUrl());
        kj.findAllItems();
        kj.proccessItems((item) -> {
            ItemLogic itLogic = new ItemLogic();
            int x = 0;
            try{
                x = Integer.parseInt(item.getId());
            }catch(NumberFormatException e){
                log(e.getMessage(), e);          
 
            }
            
            if (itLogic.getWithId(x) == null) {
                
               FileUtility.downloadAndSaveFile(item.getImageUrl(), System.getProperty("user.home")+"/KijijiImages/", item.getId()+".jpg"); 
               
                ImageLogic imLogic = new ImageLogic();
                Map<String, String[]> imMap = new HashMap<>();
                imMap.put(ImageLogic.NAME, new String[]{item.getImageName()});
                imMap.put(ImageLogic.URL, new String[]{item.getImageUrl()});
                imMap.put(ImageLogic.PATH, new String[]{System.getProperty("user.home")+"/KijijiImages/"+ item.getId()+".jpg"});
                Image im = imLogic.createEntity(imMap);
                imLogic.add(im);
                
                Map<String, String[]> caMap = new HashMap<>();
                caMap.put(CategoryLogic.ID, new String[]{String.valueOf(caLogic.getWithId(1).getId())});
                caMap.put(CategoryLogic.TITLE, new String[]{caLogic.getWithId(1).getTitle()});
                caMap.put(CategoryLogic.URL, new String[]{caLogic.getWithId(1).getUrl()});
                Category ca = caLogic.createEntity(caMap);

                Map<String, String[]> itMap = new HashMap<>();
                itMap.put(ItemLogic.ID, new String[]{item.getId()});
                itMap.put(ItemLogic.DESCRIPTION, new String[]{item.getDescription()});
                itMap.put(ItemLogic.LOCATION, new String[]{item.getLocation()});
                itMap.put(ItemLogic.PRICE, new String[]{item.getPrice()});
                itMap.put(ItemLogic.TITLE, new String[]{item.getTitle()});
                itMap.put(ItemLogic.URL, new String[]{item.getUrl()});
                itMap.put(ItemLogic.DATE, new String[]{item.getDate()});
                
                itMap.put(ItemLogic.IMAGE_ID, new String[]{im.getId()+""});
                itMap.put(ItemLogic.CATEGORY_ID, new String[]{caLogic.getWithId(1).getId()+""});
                
                Item kijijiItem = itLogic.createEntity(itMap);
               
                kijijiItem.setImage(im);
                kijijiItem.setCategory(ca);
                
                itLogic.add(kijijiItem);
            }
        });
                processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }   
}
