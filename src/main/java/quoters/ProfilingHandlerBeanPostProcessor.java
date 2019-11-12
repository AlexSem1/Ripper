package quoters;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {

  private Map<String, Class> map = new HashMap<String, Class>();
  private ProfiligController controller = new ProfiligController();

  public ProfilingHandlerBeanPostProcessor() throws Exception {
    MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
    platformMBeanServer.registerMBean(controller, new ObjectName("profiling", "name", "controller"));
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    Class<?> beanClass = bean.getClass();
    if (beanClass.isAnnotationPresent(Profiling.class)) {
      map.put(beanName, beanClass);
    }
    return bean;
  }

  public Object postProcessAfterInitialization(final Object bean, String beanName)
      throws BeansException {
    Class beanClass = map.get(beanName);
    if (beanClass != null) {
      return Proxy.newProxyInstance(beanClass.getClassLoader(),
          beanClass.getInterfaces(), new InvocationHandler() {
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
              if (controller.isEnabled()) {
                System.out.println("Профилирую");
                long before = System.nanoTime();
                Object retVal = method.invoke(bean, objects);
                long after = System.nanoTime();
                System.out.println(after - before);
                System.out.println("Все");
                return retVal;
              }
              else {
                return method.invoke(bean, objects);
              }
            }
          });
    }
    return bean;
  }
}
