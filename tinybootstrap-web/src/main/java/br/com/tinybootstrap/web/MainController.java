package br.com.tinybootstrap.web;

import br.com.tinybootstrap.service.MiniService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Murillo Peteffi
 */
@Controller
public class MainController {

    @Autowired
    MiniService miniService;
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    String index(Model model) {
        return "index";
    }
    
    @RequestMapping(value="/", method = RequestMethod.POST)
    String indexPost(Model model, String html, String version) {
        
        if(veriricaHtml(html)){
            String css = miniService.minificarCSS(html, version);
            model.addAttribute("css", css);
            model.addAttribute("economia", calcularEconomia(css));
            model.addAttribute("valid", true);
            return "_minified";
        } else {
            return "redirect:error";
        }
    }
    
    @RequestMapping(value="/error", method = RequestMethod.GET)
    String errorMessage(Model model) {
        return "_error";
    }    
    
    private float calcularEconomia(String css){
        
        float cssLength = (float)css.length();
        float bootstrap = (float)120970;
        float used = Math.round((float)(cssLength / bootstrap)*1000);
        return used/10;
    }
    
    private boolean veriricaHtml(String html){
        
        return html.contains("<") && html.contains(">");
    }
}