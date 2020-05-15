/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scraper.kijiji;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 *
 * @author Percy
 */
public class ItemBuilder {

    private static final String URL_BASE = "https://www.kijiji.ca";

    private static final String ATTRIBUTE_ID = "data-listing-id";
    private static final String ATTRIBUTE_IMAGE = "image";
    private static final String ATTRIBUTE_IMAGE_SRC = "data-src";
    private static final String ATTRIBUTE_IMAGE_NAME = "alt";
    private static final String ATTRIBUTE_PRICE = "price";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_LOCATION = "location";
    private static final String ATTRIBUTE_DATE = "date-posted";
    private static final String ATTRIBUTE_DESCRIPTION = "description";

    private Element element;
    private KijijiItem item;

    ItemBuilder() {
        element = new Element(Tag.valueOf("a"), URL_BASE);
    }

    public ItemBuilder setElement(Element element) {
        this.element = element;
        return this;
    }

    public KijijiItem build() {
        KijijiItem ki = new KijijiItem();

        ki.setUrl(URL_BASE + element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim());

        //Elements idElements = element.getElementsByClass("search-item");
//            if(idElements.isEmpty())
//                ki.setId("");
//            else
        ki.setId(element.attr(ATTRIBUTE_ID).trim());

        /*
            Elements imageElements = element.getElementsByClass(ATTRIBUTE_IMAGE);
            if(imageElements.isEmpty()){
                ki.setImageUrl("");
            }else{
                ki.setImageUrl(imageElements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim());
                ki.setImageName(imageElements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim());
            }
         */
//        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
//        if (elements.isEmpty()) {
//            ki.setImageUrl("");
//            ki.setImageName("");
//        } else {
//            String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
//            if (image.isEmpty()) {
//                image = elements.get(0).child(0).attr("src").trim();
//                if (image.isEmpty()) {
//                    image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
//                    ki.setImageUrl(image);
//                    
//                      ki.setImageName(elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim());
//                    /////////////
//                  //  ki.setImageName("hello");
//                }
//            }
//        }

        ki.setImageName(getImageName());
        ki.setImageUrl(getImageUrl());

        Elements priceElements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if (priceElements.isEmpty()) {
            ki.setPrice("");
        } else {
            ki.setPrice(priceElements.get(0).text().trim());
        }

        Elements titleElements = element.getElementsByClass(ATTRIBUTE_TITLE);
        if (titleElements.isEmpty()) {
            ki.setTitle("");
        } else {
            ki.setTitle(titleElements.get(0).child(0).text().trim());
        }

        Elements dateElements = element.getElementsByClass(ATTRIBUTE_DATE);
        if (dateElements.isEmpty()) {
            ki.setDate("");
        } else {
            ki.setDate(dateElements.get(0).text().trim());
        }

        Elements locationElements = element.getElementsByClass(ATTRIBUTE_LOCATION);
        if (locationElements.isEmpty()) {
            ki.setLocation("");
        } else {
            ki.setLocation(locationElements.get(0).childNode(0).outerHtml().trim());
        }

        Elements descriptionElements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
        if (descriptionElements.isEmpty()) {
            ki.setDescription("");
        } else {
            ki.setDescription(descriptionElements.get(0).text().trim());
        }

        return ki;

    }

    private String getImageUrl() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            return "";
        }
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).attr("src").trim();
            if (image.isEmpty()) {
                image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
            }
        }
        return image;
    }

    private String getImageName() {
        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        if (elements.isEmpty()) {
            return "";
        }
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim();

        }
        return image;
    }

}
