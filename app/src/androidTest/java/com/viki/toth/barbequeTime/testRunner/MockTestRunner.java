package com.viki.toth.barbequeTime.testRunner;

import android.app.Application;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;
import com.viki.toth.barbequeTime.MainActivityTest;

public class MockTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return super.newApplication(cl, MainActivityTest.class.getName(), context);
    }
}
