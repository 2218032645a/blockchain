package cn.my.domain;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.ServletContextResourceLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.UUID;

public class NodeID implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        servletContext.setAttribute("nodeID",uuid);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
