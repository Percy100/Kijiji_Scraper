package logic;

import entity.Category;
import entity.Image;
import entity.Item;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import static logic.AccountLogic.ID;
import static logic.CategoryLogic.TITLE;
import static logic.ItemLogic.CATEGORY_ID;
import static logic.ItemLogic.DATE;
import static logic.ItemLogic.DESCRIPTION;
import static logic.ItemLogic.IMAGE_ID;
import static logic.ItemLogic.LOCATION;
import static logic.ItemLogic.PRICE;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Shariar
 */
class ItemLogicTest {

    private static Tomcat tomcat;
    private ItemLogic logic;
    ImageLogic imLogic;
    Image im;
    Category ca;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        System.out.println(new File("src\\main\\webapp\\").getAbsolutePath());
        tomcat = new Tomcat();
        tomcat.enableNaming();
        tomcat.setPort(8080);
        Context context = tomcat.addWebapp("/WebScraper", new File("src\\main\\webapp").getAbsolutePath());
        context.addApplicationListener("dal.EMFactory");
        tomcat.init();
        tomcat.start();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
       // tomcat.stop();
    }

    private Map<String, String[]> sampleMap;
    private Map<String, String[]> imMap;
    private Map<String, String[]> caMap;
    private Item testItem;

    @BeforeEach
    final void setUp() throws Exception {
        logic = new ItemLogic();
       
        CategoryLogic caLogic = new CategoryLogic();
        caMap = new HashMap<>();
        caMap.put(CategoryLogic.ID, new String[]{"1"});
        caMap.put(CategoryLogic.TITLE, new String[]{"title"});
        caMap.put(CategoryLogic.URL, new String[]{"url"});
        ca = caLogic.createEntity(caMap);

        imLogic = new ImageLogic();
        imMap = new HashMap<>();
        imMap.put(ImageLogic.PATH, new String[]{"a"});
        imMap.put(ImageLogic.NAME, new String[]{"b"});
        imMap.put(ImageLogic.URL, new String[]{"c"});
        im = imLogic.createEntity(imMap);
        

        sampleMap = new HashMap<>();
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{"Junit 5 Test"});
        sampleMap.put(ItemLogic.CATEGORY_ID, new String[]{ca.getId()+""});
        sampleMap.put(ItemLogic.IMAGE_ID, new String[]{im.getId()+""});
        sampleMap.put(ItemLogic.LOCATION, new String[]{"junit"});
        sampleMap.put(ItemLogic.PRICE, new String[]{"10.20"});
        sampleMap.put(ItemLogic.TITLE, new String[]{"junit"});
        sampleMap.put(ItemLogic.DATE, new String[]{"20020212"});
        sampleMap.put(ItemLogic.URL, new String[]{"junit"});
        sampleMap.put(ItemLogic.ID, new String[]{"6564"});
        
        testItem = logic.createEntity(sampleMap);
        testItem.setCategory(ca);
        testItem.setImage(im);

    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    final void testGetAll() {

        List<Item> list = logic.getAll();
        assertEquals(0, list.size());

        int originalSize = list.size();
        
        System.out.println(testItem);
        
        imLogic.add(im);
        logic.add(testItem);
        list = logic.getAll();

        assertEquals(originalSize + 1, list.size());

        logic.delete(testItem);
        imLogic.delete(im);
       
        list = logic.getAll();
        assertEquals(originalSize, list.size());

    }

    @Test
    final void testGetWithId() {
        
        imLogic.add(im);
        logic.add(testItem);
        List<Item> list = logic.getAll();
        testItem = list.get(0);
        Item returnedItem = logic.getWithId(testItem.getId());

        if (returnedItem != null) {
            assertEquals(testItem.getId(), returnedItem.getId());
            assertEquals(testItem.getDescription(), returnedItem.getDescription());
            assertEquals(testItem.getLocation(), returnedItem.getLocation());
            assertEquals(testItem.getPrice(), returnedItem.getPrice());
            assertEquals(testItem.getTitle(), returnedItem.getTitle());
            assertEquals(testItem.getDate(), returnedItem.getDate());
            assertEquals(testItem.getUrl(), returnedItem.getUrl());
        } else {
            Assert.assertNull(returnedItem);
        }
        logic.delete(testItem);
        imLogic.delete(im);
    }

    @Test
    final void testGetWithPrice() {
        BigDecimal bd1 = new BigDecimal("100");
        BigDecimal bd2 = new BigDecimal("100.00");
        assertEquals( bd1.compareTo( bd2), 0);    
    }

    @Test
    final void testGetWithTitle() {
        
        imLogic.add(im);
        logic.add(testItem);
        List<Item> list = logic.getAll();
        testItem = list.get(0);
        List<Item> returnedList = logic.getWithTitle(testItem.getTitle());
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testItem.getTitle(), returnedList.get(i).getTitle());
            }
        } else {
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testItem);
        imLogic.delete(im);
    }

    @Test
    final void testGetWithDate() {
        
        imLogic.add(im);
        logic.add(testItem);
        List<Item> list = logic.getAll();
        testItem = list.get(0);
        List<Item> returnedList = logic.getWithDate(testItem.getDate().toString());
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testItem.getDate(), returnedList.get(i).getDate());
            }
        } else {
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testItem);
        imLogic.delete(im);
    }

    @Test
    final void testGetWithLocation() {
        
        imLogic.add(im);
        logic.add(testItem);
        List<Item> list = logic.getAll();
        testItem = list.get(0);
        List<Item> returnedList = logic.getWithLocation(testItem.getLocation());
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testItem.getLocation(), returnedList.get(i).getLocation());
            }
        } else {
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testItem);
        imLogic.delete(im);
    }

    @Test
    final void testGetWithDescription() {
        
        imLogic.add(im);
        logic.add(testItem);
        List<Item> list = logic.getAll();
        testItem = list.get(0);
        List<Item> returnedList = logic.getWithDescription(testItem.getDescription());
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testItem.getDescription(), returnedList.get(i).getDescription());
            }
        } else {
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testItem);
        imLogic.delete(im);
    }

    final void testGetWithUrl() {
        
        imLogic.add(im);
        logic.add(testItem);
        List<Item> list = logic.getAll();
        testItem = list.get(0);
        Item returnedItem = logic.getWithUrl(testItem.getUrl());

        assertEquals(testItem.getId(), returnedItem.getId());
        assertEquals(testItem.getDescription(), returnedItem.getDescription());
        assertEquals(testItem.getLocation(), returnedItem.getLocation());
        assertEquals(testItem.getPrice(), returnedItem.getPrice());
        
        logic.delete(testItem);
        imLogic.delete(im);
    }

 
    @Test
    final void testSearch() {
        
            imLogic.add(im);
            logic.add(testItem);
             List<Item> returnedList = logic.search(sampleMap.get(DESCRIPTION)[0]);

        for (int i = 0; i < returnedList.size(); i++) {
            assertEquals(testItem.getDescription(), returnedList.get(i).getDescription());
        }
        logic.delete(testItem);
        imLogic.delete(im);
    }

    @Test
    final void testCreateEntity() {
        if (sampleMap.containsKey(ItemLogic.DESCRIPTION) && sampleMap.containsKey(ItemLogic.LOCATION) && sampleMap.containsKey(ItemLogic.PRICE)
                && sampleMap.containsKey(ItemLogic.TITLE) && sampleMap.containsKey(ItemLogic.DATE) && sampleMap.containsKey(ItemLogic.URL)) {
            testItem = logic.createEntity(sampleMap);
            assertEquals(sampleMap.get(ItemLogic.DESCRIPTION)[0], testItem.getDescription());
            assertEquals(sampleMap.get(ItemLogic.LOCATION)[0], testItem.getLocation());
            assertEquals(sampleMap.get(ItemLogic.PRICE)[0], testItem.getPrice().toString());
            assertEquals(sampleMap.get(ItemLogic.TITLE)[0], testItem.getTitle());
            assertEquals(sampleMap.get(ItemLogic.URL)[0], testItem.getUrl());

        } else {
            Assertions.assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        }
    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        List<?> hardCodedList = Arrays.asList("ID", "Description", "CategoryId", "ImageId", "Location", "Price", "Title", "Date", "Url");
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testGetColumnCodes() {

        List<String> list = logic.getColumnCodes();
        List<?> hardCodedList = Arrays.asList(ID, DESCRIPTION, CATEGORY_ID, IMAGE_ID, LOCATION, PRICE, TITLE, DATE, ItemLogic.URL);

        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testExtractDataAsList() {

        imLogic.add(im);
        logic.add(testItem);
        List<?> extracedList = logic.extractDataAsList(testItem);

        assertEquals(testItem.getId(), extracedList.get(0));
        assertEquals(testItem.getDescription(), extracedList.get(1));
        assertEquals(testItem.getLocation(), extracedList.get(4));
        assertEquals(testItem.getPrice(), extracedList.get(5));
        assertEquals(testItem.getTitle(), extracedList.get(6));
        assertEquals(testItem.getDate(), extracedList.get(7));
        assertEquals(testItem.getUrl(), extracedList.get(8));
        
        logic.delete(testItem);
        imLogic.delete(im);
    }
       
}
