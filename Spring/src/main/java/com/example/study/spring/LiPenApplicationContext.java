package com.example.study.spring;

import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LiPenApplicationContext {

    private Class configClass;

    /**
     * key=userService
     * value=
     */
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    /**
     * key=beanName
     * value=bean
     */
    private ConcurrentHashMap<String, Object> singletonObject = new ConcurrentHashMap<>();


    private List<BeanPostProcessor> beanPostProcessorList=new ArrayList<>();

    public LiPenApplicationContext(Class configClass) {
        this.configClass = configClass;
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            path = path.replace(".", "/");
            ClassLoader classLoader = this.getClass().getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    try {
                        Class<?> aClass = classLoader.loadClass(className);
                        if (aClass.isAnnotationPresent(Component.class)) {
                            //后面的类是不是前面的接口派生的
                            if (BeanPostProcessor.class.isAssignableFrom(aClass)){
                                Object instance = aClass.newInstance();
                                beanPostProcessorList.add((BeanPostProcessor)instance);
                            }

                            //key=userService
                            Component componentAnnotation = aClass.getAnnotation(Component.class);
                            String beanName = componentAnnotation.value();
                            if (StringUtils.isEmpty(beanName)) {
                                //OrderService->orderService
                                beanName = Introspector.decapitalize(aClass.getSimpleName());
                            }
                            //value=beanDefinition
                            BeanDefinition beanDefinition = new BeanDefinition(aClass);
                            Scope scopeAnnotation = aClass.getAnnotation(Scope.class);
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                beanDefinition.setScope(scopeAnnotation.value());
                            } else {
                                beanDefinition.setScope("singleton");
                            }
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * 获取bean
     *
     * @param beanName bean 名称
     * @return bean 对象
     */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        } else {
            String scope = beanDefinition.getScope();
            if ("singleton".contentEquals(scope)) {
                Object bean = singletonObject.get(beanName);
                if (bean == null) {
                    bean = createBean(beanName, beanDefinition);
                    singletonObject.put(beanName, bean);
                }
                return bean;
            } else {
                return createBean(beanName, beanDefinition);
            }
        }
    }

    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        try {
            Class clazz = beanDefinition.getaClass();
            Object instance = clazz.getConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Autowired annotation = field.getAnnotation(Autowired.class);
                    String autoWireName = annotation.value();
                    if (StringUtils.isEmpty(autoWireName)) {
                        autoWireName = field.getName();
                    }
                    field.set(instance, getBean(autoWireName));
                }
            }


            // beanName的回调
            if (instance instanceof BeanNameAired){
                ((BeanNameAired) instance).setBeanName(beanName);
            }
            // 初始化实例前的回调
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }
            //初始化前
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PostConstruct.class)){
                    method.invoke(instance,null);
                }
            }

            // init
            if (instance instanceof InitializingBean){
                ((InitializingBean) instance).afterPropertiesSet();
            }



            // 初始化实例后的回调
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
