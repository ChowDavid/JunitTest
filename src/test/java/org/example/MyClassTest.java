package org.example;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.util.*;

//import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MyClassTest {

    MyService test;
    private Answer defaultAnswer;

    @Before
    public void init(){
        defaultAnswer = (i-> ((String)i.getArguments()[0]).toLowerCase(Locale.ROOT));
        test = mock(MyService.class,withSettings()
                .name("mockTest")
                .defaultAnswer(defaultAnswer));
    }

    @Test
    public void test(){
        assertEquals("chow",test.feature1("CHOW"));
    }

    /**
     * mock myClass and exec a real method
     */
    @Test
    public void testDoRealMethod(){
        MyClass myClass = mock(MyClass.class);
        doCallRealMethod().when(myClass).toLower(anyString());
        assertEquals("somethingelse",myClass.toLower("SomethingELSE"));
    }

    /**
     * I like to test myClass to run method perform and see the dependence I mock is it call in order
     */
    @Test
    public void testInOrder(){
        MyClass myClass = new MyClass();
        MyService myMockService = mock(MyService.class);
        myClass.setMyService(myMockService);

        InOrder inOrder = inOrder(myMockService);

        myClass.feature();

        //This will pass
        inOrder.verify(myMockService).init();
        inOrder.verify(myMockService).exec();
        /*This will fail so remark it
        * inOrder.verify(myMockService).exec();
        * inOrder.verify(myMockService).init();
        */
    }

    /**
     * The other way to check the inorder order
     */
    @Test
    public void testCall(){
        MyClass myClass = new MyClass();
        MyService myMockService = mock(MyService.class);
        myClass.setMyService(myMockService);

        InOrder inOrder = inOrder(myMockService);

        myClass.feature();

        //This will pass
        inOrder.verify(myMockService,calls(1)).init();
        inOrder.verify(myMockService,calls(1)).clean();
        inOrder.verify(myMockService,calls(1)).exec();
        inOrder.verify(myMockService,calls(1)).clean();
        inOrder.verify(myMockService,calls(2)).checkDB();
    }

    /**
     * If I like to check does the method in mock is the only call for a method I can we Mockito.only
     */
    @Test
    public void testOnly(){
        MyClass myClass = new MyClass();
        MyService myMockService = mock(MyService.class);
        myClass.setMyService(myMockService);

        myClass.sing();

        verify(myMockService,only()).checkDB();
    }

    @Test(timeout = 100)
    public void testTimeOut(){
        MyClass myClass = new MyClass();
        MyService myMockService = mock(MyService.class);
        myClass.setMyService(myMockService);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                try {
                    Thread.sleep(50);
                } catch (Exception e){}
                return null;
            }
        }).when(myMockService).checkDB();

        myClass.sing();
    }

    /**
     * test some function with timing issue
     * simulate checkDB need 100ms then call clean
     */
    @Test
    public void testAfter(){
        MyClass myClass = new MyClass();
        MyService myMockService = mock(MyService.class);
        myClass.setMyService(myMockService);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                try {
                    Thread.sleep(100);
                } catch (Exception e){}
                return null;
            }
        }).when(myMockService).checkDB();

        myClass.aSong();
        verify(myMockService, timeout(1)).checkDB();
        verify(myMockService, after(100).times(1)).clean();
    }

    @Test
    public void assertThat_numberTest(){
        assertThat(3,equalTo(3));
        assertThat(4,greaterThan(3));
        assertThat(2,lessThan(3));
        assertThat(5,lessThanOrEqualTo(5));
    }

    @Test
    public void assertThat_StringTest(){
        assertThat("Java",equalToIgnoringCase("JAVA"));
    }

    @Test
    public void assertThat_mapTest(){
        Map oldMap = new HashMap();
        oldMap.put("Java","Java");
        oldMap.put("IT","David");

        //Since 1.5
        Map<String,String> aMap = Collections.checkedMap(oldMap,String.class, String.class);

        //OK for that but fail on aMap.put(Sting,Integer)
        oldMap.put("Something",123);


        assertThat(aMap, hasEntry("Java","Java"));
    }

    @Test
    public void assertThat_arrayTest(){
        String[] array = new String[] {"David","IT","Java"};
        assertThat(array,arrayWithSize(3));
        assertThat(array,arrayContaining("David","IT","Java"));
        assertThat(array,arrayContainingInAnyOrder("IT","David","Java"));
        assertThat(array,hasItemInArray("Java"));
        assertThat(Collections.emptyList().toArray(),emptyArray());
    }

    @Test
    public void assertThat_listTest(){
        List<Integer> aList = Arrays.asList(new Integer[]{1,2,3});
        List<Integer> bList = Arrays.asList(new Integer[]{3,2,1});
        Collections.sort(bList);
        assertThat(aList,equalTo(bList));
        assertThat(aList,hasItem(3));
        assertThat(aList,anything());
        assertThat(aList, describedAs("Described as ",hasItem(2)));
        assertThat(aList,allOf(hasItem(1),hasItem(2),equalTo(bList),not(hasItem(4))));
        assertThat(aList,anyOf(hasItem(1),hasItem(5),equalTo(bList),not(hasItem(4))));
        assertThat(aList,not(sameInstance(bList)));
    }

    @Test
    public void assertThat_ObjectTest(){
        MyClass myClass = new MyClass();
        assertThat(myClass,hasToString("123"));
        assertThat(myClass,notNullValue());
        assertThat(myClass,hasProperty("myService"));
    }

    @Test
    public void assertThat_bigDecimalTest(){
        BigDecimal a = new BigDecimal("1.1");
        BigDecimal b = BigDecimal.valueOf(1.1);
        assertThat(a,equalTo(b));
        assertThat(a,closeTo(b,BigDecimal.ZERO));
    }






}
