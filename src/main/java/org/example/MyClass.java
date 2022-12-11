package org.example;

import java.util.Locale;

public class MyClass {

    private MyService myService;

    public void setMyService(MyService myService){
        this.myService = myService;
    }

    public String toLower(String input){
        return input.toLowerCase(Locale.ROOT);
    }

    public void feature(){
        myService.init();
        myService.clean();
        myService.exec();
        myService.clean();
        myService.checkDB();
        //TO Something else
        myService.checkDB();
    }

    public void sing(){
        myService.checkDB();
    }
    public void aSong(){
        myService.checkDB();
        myService.clean();
    }

    @Override
    public String toString() {
        return "123";
    }
}
