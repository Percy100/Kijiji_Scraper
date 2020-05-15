package logic;

import entity.Image;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import static logic.AccountLogic.ID;
import static logic.CategoryLogic.URL;
import static logic.ImageLogic.NAME;
import static logic.ImageLogic.PATH;
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
 * @author Percy
 */
class ImageLogicTest {

    private static Tomcat tomcat;
    private ImageLogic logic;
    Image testImage;

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
        logic = new ImageLogic();

        sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.PATH, new String[]{"Junit 5 Test"});
        sampleMap.put(ImageLogic.NAME, new String[]{"junit"});
        sampleMap.put(ImageLogic.URL, new String[]{"junit5"});    
    }

    @AfterEach
    final void tearDown() throws Exception {
       
    }

    @Test
    final void testGetAll() {
        List<Image> list = logic.getAll();
        assertEquals(0,list.size());

        int originalSize = list.size();
        testImage = logic.createEntity(sampleMap);
        logic.add(testImage);
        list = logic.getAll();

        assertEquals(originalSize + 1, list.size());

        logic.delete(testImage);
        list = logic.getAll();
        assertEquals(originalSize, list.size());
    }

    @Test
    final void testGetWithId() {
        
        testImage = logic.createEntity(sampleMap);
        logic.add(testImage);
        List<Image> list = logic.getAll();
        testImage = list.get(0);
        Image returnedImage = logic.getWithId(testImage.getId());

        if (returnedImage != null) {
            assertEquals(testImage.getId(), returnedImage.getId());
            assertEquals(testImage.getPath(), returnedImage.getPath());
            assertEquals(testImage.getName(), returnedImage.getName());
            assertEquals(testImage.getUrl(), returnedImage.getUrl());
        } else {
            Assert.assertNull(returnedImage);
        }
        logic.delete(testImage);
        
    }
    
    @Test
    final void testGetWithUrl() {
        
        testImage = logic.createEntity(sampleMap);
        logic.add(testImage);
        List<Image> list = logic.getAll();
        testImage = list.get(0);
        List<Image> returnedList = logic.getWithUrl(testImage.getUrl());
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testImage.getUrl(), returnedList.get(i).getUrl());
            }
        }else{
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testImage);
    }

    @Test
    final void testGetWithPath() {
        
        testImage = logic.createEntity(sampleMap);
        logic.add(testImage);
        List<Image> list = logic.getAll();
        testImage = list.get(0);
        Image returnedImage = logic.getWithPath(testImage.getPath());

        if (returnedImage != null) {
            assertEquals(testImage.getId(), returnedImage.getId());
            assertEquals(testImage.getPath(), returnedImage.getPath());
            assertEquals(testImage.getName(), returnedImage.getName());
            assertEquals(testImage.getUrl(), returnedImage.getUrl());
        } else {
            Assert.assertNull(returnedImage);
        }
        logic.delete(testImage);
        
    }

    @Test
    final void testGetWIthName() {
        
        testImage = logic.createEntity(sampleMap);
        logic.add(testImage);
        List<Image> list = logic.getAll();
        testImage = list.get(0);
        List<Image> returnedList = logic.getWithName(testImage.getName());
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testImage.getName(), returnedList.get(i).getName());
            }
        }else{
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testImage);
    }

    @Test
    final void testSearch() {

        testImage = logic.createEntity(sampleMap);
        logic.add(testImage);
        List<Image> returnedList = logic.search(sampleMap.get(PATH)[0]);

        for (int i = 0; i < returnedList.size(); i++) {
            assertEquals(testImage.getPath(), returnedList.get(i).getPath());
        }
        logic.delete(testImage);
    }

    @Test
    final void testCreateEntity() {
        if (sampleMap.containsKey(PATH) && sampleMap.containsKey(NAME) && sampleMap.containsKey(URL)) {
            testImage = logic.createEntity(sampleMap);

            assertEquals(sampleMap.get(NAME)[0], testImage.getName());
            assertEquals(sampleMap.get(URL)[0], testImage.getUrl());
            assertEquals(sampleMap.get(PATH)[0], testImage.getPath());
        }else{
            Assertions.assertThrows(NullPointerException.class,()->logic.createEntity(sampleMap));
        }
    }

    @Test
    final void testGetColumnNames() {

        List<String> list = logic.getColumnNames();
        List<?> hardCodedList = Arrays.asList("ID", "Name", "Url", "Path");
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testGetColumnCodes() {

        List<String> list = logic.getColumnCodes();
        List<?> hardCodedList = Arrays.asList(ID, NAME, URL, PATH);
        
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testExtractDataAsList() {

        testImage = logic.createEntity(sampleMap);
        List<?> extracedList = logic.extractDataAsList(testImage);

        assertEquals(testImage.getId(), extracedList.get(0));
        assertEquals(testImage.getName(), extracedList.get(1));
        assertEquals(testImage.getUrl(), extracedList.get(2));
        assertEquals(testImage.getPath(), extracedList.get(3));
    }

}
