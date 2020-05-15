package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Category;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import static logic.CategoryLogic.ID;
import static logic.CategoryLogic.TITLE;
import static logic.CategoryLogic.URL;
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
class CategoryLogicTest {

    private static Tomcat tomcat;
    private CategoryLogic logic;

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
      //  tomcat.stop();
    }

    private Map<String, String[]> sampleMap;

    @BeforeEach
    final void setUp() throws Exception {
        logic = new CategoryLogic();

        sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.TITLE, new String[]{"Junit 5 Test"});
        sampleMap.put(CategoryLogic.URL, new String[]{"junit"});
    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    final void testGetAll() {
        List<Category> list = logic.getAll();
        assertEquals(2, list.size());

        int originalSize = list.size();
        Category testCategory = logic.createEntity(sampleMap);
        logic.add(testCategory);
        list = logic.getAll();

        assertEquals(originalSize + 1, list.size());

        logic.delete(testCategory);
        list = logic.getAll();
        assertEquals(originalSize, list.size());

    }
    
    @Test
    final void testGetWithId() {

        List<Category> list = logic.getAll();
        Category testCategory = list.get(0);
        Category returnedCategory = logic.getWithId(testCategory.getId());

        if (returnedCategory != null) {
            assertEquals(testCategory.getId(), returnedCategory.getId());
            assertEquals(testCategory.getTitle(), returnedCategory.getTitle());
            assertEquals(testCategory.getUrl(), returnedCategory.getUrl());
        } else {
            Assert.assertNull(returnedCategory);
        }   
        
    }

    @Test
    final void testGetWithUrl() {

        List<Category> list = logic.getAll();
        Category testCategory = list.get(0);
  
        Category returnedCategory = logic.getWithUrl(testCategory.getUrl());

        assertEquals(testCategory.getId(), returnedCategory.getId());
        assertEquals(testCategory.getTitle(), returnedCategory.getTitle());
        assertEquals(testCategory.getUrl(), returnedCategory.getUrl());
    }

    @Test
    final void testGetWithTitle() {

        List<Category> list = logic.getAll();
        Category testCategory = list.get(0);
        Category returnedCategory = logic.getWithTitle(testCategory.getTitle());

        assertEquals(testCategory.getId(), returnedCategory.getId());
        assertEquals(testCategory.getTitle(), returnedCategory.getTitle());
        assertEquals(testCategory.getUrl(), returnedCategory.getUrl());
    }

    @Test
    final void testSearch() {

        Category testCategory = logic.createEntity(sampleMap);
        logic.add(testCategory);
        List<Category> returnedList = logic.search(sampleMap.get(TITLE)[0]);

        for (int i = 0; i < returnedList.size(); i++) {
            assertEquals(testCategory.getTitle(), returnedList.get(i).getTitle());
        }
        logic.delete(testCategory);
    }

    @Test
    final void testCreateEntity() {
        if (sampleMap.containsKey(TITLE) && sampleMap.containsKey(URL)) {
            Category testCategory = logic.createEntity(sampleMap);

            assertEquals(sampleMap.get(TITLE)[0], testCategory.getTitle());
            assertEquals(sampleMap.get(URL)[0], testCategory.getUrl());
        }else{
            Assertions.assertThrows(NullPointerException.class,()->logic.createEntity(sampleMap));
        }
    }

    @Test
    final void testGetColumnNames() {

        List<String> list = logic.getColumnNames();
        List<?> hardCodedList = Arrays.asList("ID", "Title", "Url");
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testGetColumnCodes() {

        List<String> list = logic.getColumnCodes();
        List<?> hardCodedList = Arrays.asList(ID, TITLE, URL);
        
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testExtractDataAsList() {

        Category testCategory = logic.createEntity(sampleMap);
        List<?> extracedList = logic.extractDataAsList(testCategory);

        assertEquals(testCategory.getId(), extracedList.get(0));
        assertEquals(testCategory.getTitle(), extracedList.get(1));
        assertEquals(testCategory.getUrl(), extracedList.get(2));
    }

}
