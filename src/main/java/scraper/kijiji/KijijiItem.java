package scraper.kijiji;

import java.util.Objects;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 *
 * @author Shariar (Shawn) Emami
 */
public final class KijijiItem {
    
    private String id;
    private String url;
    private String imageUrl;
    private String imageName;
    private String price;
    private String title;
    private String date;
    private String location;
    private String description;
    
    
    KijijiItem() {
    } 
    
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getPrice() {
        return price;
    }

    void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }
    
     public String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KijijiItem other = (KijijiItem) obj;
        if (!Objects.equals(getId(), other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
          return String.format("[id:%s, image_url:%s, image_name:%s, price:%s, title:%s, date:%s, location:%s, description:%s]",
                getId(), getImageUrl(), getImageName(), getPrice(), getTitle(), getDate(), getLocation(), getDescription());
    }
    
//     public  static void main(String[] ag){
//           KijijiItem car = new scraper.kijiji.ItemBuilder().setId("aa").setDate("aa").setDescription("aa").setImageName("aa").setImageUrl("aa").build();
//           System.out.println(car);
//       }

    
    
    
    

    
    
    
    
        public static class ItemBuilder{
        
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
        
        ItemBuilder(){
         element = new Element(Tag.valueOf("a"), URL_BASE);
        }      
        
        public ItemBuilder setElement(Element element){
            this.element = element;
            return this;
        }
        
        public KijijiItem build(){
            KijijiItem ki = new KijijiItem();
            
            ki.setUrl(URL_BASE+element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim());
            
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
            
            Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
            if (elements.isEmpty()) {
                ki.setImageUrl("");
                ki.setImageName("");
            }
            else{
                String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
                if (image.isEmpty()) {
                    image = elements.get(0).child(0).attr("src").trim();
                    if (image.isEmpty()) {
                        image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
                        ki.setImageUrl(image);
                        ki.setImageName(elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim());
                    }
                }
            }
            
            Elements priceElements = element.getElementsByClass(ATTRIBUTE_PRICE);
            if(priceElements.isEmpty())
                ki.setPrice("");
            else
                ki.setPrice(priceElements.get(0).text().trim());
            
            Elements titleElements = element.getElementsByClass(ATTRIBUTE_TITLE);
            if(titleElements.isEmpty())
                ki.setTitle("");
            else
                ki.setTitle(titleElements.get(0).child(0).text().trim());
            
            Elements dateElements = element.getElementsByClass(ATTRIBUTE_DATE);
            if(dateElements.isEmpty())
                ki.setDate("");
            else
                ki.setDate(dateElements.get(0).text().trim());
            
            Elements locationElements = element.getElementsByClass(ATTRIBUTE_LOCATION);
            if(locationElements.isEmpty())
                ki.setLocation("");
            else
                ki.setLocation(locationElements.get(0).childNode(0).outerHtml().trim());
            
            Elements descriptionElements = element.getElementsByClass(ATTRIBUTE_DESCRIPTION);
            if(descriptionElements.isEmpty())
                ki.setDescription("");
            else
                ki.setDescription(descriptionElements.get(0).text().trim());
            
           
            return ki;
           
        }
    
        }
}

   