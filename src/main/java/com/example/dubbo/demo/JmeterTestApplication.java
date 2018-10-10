package com.example.dubbo.demo;

import com.example.dubbo.demo.Service.DemoService;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class JmeterTestApplication extends AbstractJavaSamplerClient {


    DemoService demoService;

    private static Logger logger = LoggerFactory.getLogger(JmeterTestApplication.class);
    private static int count=1;

    {
        logger.info("第{}次创建对象,{}",count,this);
        count++;
    }



    @Override
    public void setupTest(JavaSamplerContext context) {
        logger.info("setupTest===============");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/dubbo.xml");
        demoService = applicationContext.getBean(DemoService.class);
    }




    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        logger.info("runTest===============");
        SampleResult sr = new SampleResult();
        sr.sampleStart();
        try {
            String hello = demoService.sayHello();
            sr.setResponseData(hello, "utf8");
            sr.setDataType(SampleResult.TEXT);
            sr.setSuccessful(true);
        }catch (Exception e){
            logger.error("测试用例失败，异常={}",e);
            sr.setSuccessful(false);
        }finally {
            sr.sampleEnd();
        }
        return sr;
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        logger.info("teardownTest===============");
        logger.info("用例测试完成");
    }

    /*public static void main(String[] args) {
        try {
            JmeterTestApplication jmeterTestApplication = new JmeterTestApplication();
        }catch (Exception e){
            e.printStackTrace();
        }
        jmeterTestApplication.setupTest(null);
        jmeterTestApplication.runTest(null);
        jmeterTestApplication.teardownTest(null);
    }*/
}
