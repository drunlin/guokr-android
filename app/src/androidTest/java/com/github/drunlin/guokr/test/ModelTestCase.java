package com.github.drunlin.guokr.test;

import com.github.drunlin.guokr.TestContext;
import com.github.drunlin.guokr.mock.MockNetworkModel;
import com.github.drunlin.guokr.model.UserModel;
import com.github.drunlin.guokr.module.tool.Injector;
import com.github.drunlin.guokr.test.util.Constants;

import javax.inject.Inject;

import static org.mockito.Mockito.when;

/**
 * @author drunlin@outlook.com
 */
public abstract class ModelTestCase<M> extends AsyncTestCase {
    /**测试模块通用的依赖注入器。*/
    protected final Injector injector = TestContext.getInjector();

    @Inject protected UserModel userModel;

    /**当前测试的模型。*/
    protected M model;

    @Override
    public void setUp() throws Throwable {
        injector.inject(this);

        when(userModel.checkLoggedIn()).thenReturn(true);
        when(userModel.getUserKey()).thenReturn(Constants.USER_KEY);
        when(userModel.getToken()).thenReturn(Constants.ACCESS_TOKEN);

        super.setUp();
    }

    @Override
    public void tearDown() throws Throwable {
        MockNetworkModel.clear();

        super.tearDown();
    }
}
