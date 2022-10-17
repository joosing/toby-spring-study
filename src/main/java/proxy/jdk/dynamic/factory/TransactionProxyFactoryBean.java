package proxy.jdk.dynamic.factory;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import proxy.jdk.dynamic.invocationhandler.TransactionHandler;

import java.lang.reflect.Proxy;

@Setter
public class TransactionProxyFactoryBean implements FactoryBean<Object> {
    private PlatformTransactionManager transactionManager;
    private Class<?> serviceInterface;
    private Object target;
    private String pattern;

    @Override
    public Object getObject() throws Exception {
        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTransactionManager(transactionManager);
        transactionHandler.setTarget(target);
        transactionHandler.setPattern(pattern);

        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { serviceInterface }, transactionHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
