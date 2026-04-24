package br.com.kumabe.catalogs.infrastructure.adapters.in.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class CatalogWebController {
    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${spring.application.version:unknown}")
    private String applicationVersion;
    
    @GetMapping("/")
    public ModelAndView index() throws UnknownHostException {
        ModelAndView modelAndView = new ModelAndView("index");
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String hostName = InetAddress.getLocalHost().getHostName();
        modelAndView.addObject("applicationName", applicationName);
        modelAndView.addObject("applicationVersion", applicationVersion);
        modelAndView.addObject("hostAddress", hostAddress);
        modelAndView.addObject("hostName", hostName);        
        return modelAndView;
    }
    
}
