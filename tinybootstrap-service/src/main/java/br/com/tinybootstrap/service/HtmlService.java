package br.com.tinybootstrap.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * @author Murillo
 */
@Service
public class HtmlService {
    
    public Document parseStringToHtmlDocument(String html){
        
        Document htmlDoc = Jsoup.parse(html);
        return htmlDoc;
    }
    
    public Document parseFileNameToHtmlDocument(String html){
        
        File input = new File(html);
        
        try{
            Document htmlDoc = Jsoup.parse(input, "UTF-8");
            return htmlDoc;
        } catch (IOException ioex){
            System.out.println(ioex);
        }
        
        return null;
    }
    
    public boolean seletorInutil(Document html, String seletor){
        
        seletor = modelarSeletor(seletor);
        
        try{
            
            Elements elementosEncontrados = html.select(seletor);
            return elementosEncontrados.isEmpty();
            
        } catch (Exception ex) {
            
            try{
                
                seletor = modelarPseudoSeletor(seletor);
                
                Elements elementosEncontrados = html.select(seletor);
                return elementosEncontrados.isEmpty();
                
            } catch (Exception e) {
                return false;
            }
        }
    }
    
    private String modelarPseudoSeletor(String seletor){
        
        if(seletor.contains("::")){
            seletor = seletor.split("::")[0];
            return seletor;
        } else if (seletor.contains(":")){
            seletor = seletor.split(":")[0];
            return seletor;
        } 
        
        return seletor;
    }
    
    private String modelarSeletor(String seletor){
    
        List<String> palavras = Arrays.asList("active","disabled","open","collapse","in","fade","modal-backdrop","model-open","collapsing","affix","dropdown-backdrop");
        
        for(String palavra : palavras){
            
            if(seletor.contains(palavra)){

                String sel = palavra;
                if (seletor.contains("." + sel + ">")){
                    seletor = seletor.replace("." + sel + ">","");
                    return seletor;
                } else if (seletor.contains(">." + sel)){
                    seletor = seletor.replace(">." + sel,"");
                    return seletor;
                } else if (seletor.contains("." + sel)){
                    seletor = seletor.replace("." + sel,"");
                    return seletor;
                }
            }
        }
        
        return seletor;
    }
}
