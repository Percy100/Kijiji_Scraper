package logic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Account;
import common.TomcatStartUp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static logic.AccountLogic.DISPLAY_NAME;
import static logic.AccountLogic.ID;
import static logic.AccountLogic.PASSWORD;
import static logic.AccountLogic.USER;
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
class AccountLogicTest {

    private AccountLogic logic;
    private Map<String, String[]> sampleMap;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
     //   TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {
        logic = new AccountLogic();
        /*HashMap implements interface Map. Map is generic, it has two parameters, first is the Key (in our case String) and second is Value (in our case String[])*/
        sampleMap = new HashMap<>();
        /*Map stores date based on the idea of dictionaries. Each value is associated with a key. Key can be used to get a value very quickly*/
        sampleMap.put(AccountLogic.DISPLAY_NAME, new String[]{"Junit 5 Test"});
        /*Map::put is used to store a key and a value inside of a map and Map::get is used to retrieve a value using a key.*/
        sampleMap.put(AccountLogic.USER, new String[]{"junit"});
        /*In this case we are using static values stored in AccoundLogic which represent general names for Account Columns in DB to store values in Map*/
        sampleMap.put(AccountLogic.PASSWORD, new String[]{"junit5"});
        /*This account has Display Name: "Junit 5 Test", User: "junit", and Password: "junit5"*/
    }

    @AfterEach
    final void tearDown() throws Exception {
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Account> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //create a new Account and save it so we can delete later
        Account testAccount = logic.createEntity(sampleMap);
        //add the newly created account to DB
        logic.add(testAccount);

        //get all the accounts again
        list = logic.getAll();
        //the new size of accounts must be 1 larger than original size
        assertEquals(originalSize + 1, list.size());

        //delete the new account, so DB is reverted back to it original form
        logic.delete(testAccount);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize, list.size());
    }

    @Test
    final void testGetWithId() {
        //get all accounts
        List<Account> list = logic.getAll();

        //use the first account in the list as test Account
        Account testAccount = list.get(0);
        //using the id of test account get another account from logic
        Account returnedAccount = logic.getWithId(testAccount.getId());

        //the two accounts (testAcounts and returnedAccounts) must be the same
        //assert all field to guarantee they are the same
        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword());
    }
    
    @Test
    final void testGetAccountWithDisplayName() {

        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        Account returnedAccount = logic.getAccountWithDisplayName(testAccount.getDisplayName());

        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword());
    }

    @Test
    final void testGetAccountWIthUser() {

        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        Account returnedAccount = logic.getAccountWithUser(testAccount.getUser());

        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword());
    }

    @Test
    final void testGetAccountsWithPassword() {

        Account testAccount = logic.createEntity(sampleMap);
        logic.add(testAccount);
        List<Account> returnedList = logic.getAccountsWithPassword("asdf");
        if (returnedList.size() > 0) {
            for (int i = 0; i < returnedList.size(); i++) {
                assertEquals(testAccount.getPassword(), returnedList.get(i).getPassword());
            }
        }else{
            assertEquals(returnedList.size(), 0);
        }
        logic.delete(testAccount);
    }

    @Test
    final void testGetAccountWith() {

        List<Account> list = logic.getAll();
        Account testAccount = list.get(0);
        Account returnedAccount = logic.getAccountWith(testAccount.getUser(), testAccount.getPassword());

        assertEquals(testAccount.getId(), returnedAccount.getId());
        assertEquals(testAccount.getDisplayName(), returnedAccount.getDisplayName());
        assertEquals(testAccount.getUser(), returnedAccount.getUser());
        assertEquals(testAccount.getPassword(), returnedAccount.getPassword());
    }

    @Test
    final void testSearch() {

        Account testAccount = logic.createEntity(sampleMap);
        logic.add(testAccount);
        List<Account> returnedList = logic.search(sampleMap.get(DISPLAY_NAME)[0]);

        for (int i = 0; i < returnedList.size(); i++) {
            assertEquals(testAccount.getDisplayName(), returnedList.get(i).getDisplayName());
        }
        logic.delete(testAccount);
    }

    @Test
    final void testCreateEntity() {
        if (sampleMap.containsKey(DISPLAY_NAME) && sampleMap.containsKey(USER) && sampleMap.containsKey(PASSWORD)) {
            Account testAccount = logic.createEntity(sampleMap);

            assertEquals(sampleMap.get(DISPLAY_NAME)[0], testAccount.getDisplayName());
            assertEquals(sampleMap.get(USER)[0], testAccount.getUser());
            assertEquals(sampleMap.get(PASSWORD)[0], testAccount.getPassword());
        }else{
            Assertions.assertThrows(NullPointerException.class,()->logic.createEntity(sampleMap));
        }
    }

    @Test
    final void testGetColumnNames() {

        List<String> list = logic.getColumnNames();
        List<?> hardCodedList = Arrays.asList("ID", "Display Name", "User", "Password");
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testGetColumnCodes() {

        List<String> list = logic.getColumnCodes();
        List<?> hardCodedList = Arrays.asList(ID, DISPLAY_NAME, USER, PASSWORD);
        
        assertIterableEquals(list, hardCodedList);
    }

    @Test
    final void testExtractDataAsList() {

        Account testAccount = logic.createEntity(sampleMap);
        List<?> extracedList = logic.extractDataAsList(testAccount);

        assertEquals(testAccount.getId(), extracedList.get(0));
        assertEquals(testAccount.getDisplayName(), extracedList.get(1));
        assertEquals(testAccount.getUser(), extracedList.get(2));
        assertEquals(testAccount.getPassword(), extracedList.get(3));
    }
}

