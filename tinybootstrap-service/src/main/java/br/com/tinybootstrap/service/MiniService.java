package br.com.tinybootstrap.service;

import cz.vutbr.web.css.StyleSheet;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Murillo
 */
@Service
public class MiniService {
 
    @Autowired CssService cssService;
    @Autowired HtmlService htmlService;
    
    public String minificarCSS(String htmlString, String version){
        
        long tStart, tEnd, tMedS, tMedF, tResult;
        tStart = System.currentTimeMillis();
        
        
        tMedS = System.currentTimeMillis();
        Document html = htmlService.parseStringToHtmlDocument(htmlString);
        StyleSheet css = cssService.parseFileNameToCSS(version + ".css");
        tMedF = System.currentTimeMillis();
        tResult = tMedF - tMedS;
        System.out.println("TEMPO DE PARSE: " + tResult + "ms");
        
        
        tMedS = System.currentTimeMillis();
        css = cssService.removerSeletoresInuteis(html, css);
        tMedF = System.currentTimeMillis();
        tResult = tMedF - tMedS;
        System.out.println("TEMPO DE REMOVER SELETORES: " + tResult + "ms");
        
        
        tMedS = System.currentTimeMillis();
        String novo = cssService.removerEspacosEmBrancoESemiCollon(css);
        tMedF = System.currentTimeMillis();
        tResult = tMedF - tMedS;
        System.out.println("TEMPO DE REMOVER ESPAÇOS E AFINS: " + tResult + "ms");
        
        
        tEnd = System.currentTimeMillis();
        tResult = tEnd - tStart;
        System.out.println("TEMPO TOTAL DE EXECUÇÃO: " + tResult + "ms");
        
        return novo;
    }
}
