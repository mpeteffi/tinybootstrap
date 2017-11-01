package br.com.tinybootstrap.service;

import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.CombinedSelector;
import cz.vutbr.web.css.RuleMedia;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.StyleSheet;
import cz.vutbr.web.csskit.OutputUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Murillo
 */
@Service
public class CssService {
    
    @Autowired HtmlService htmlService;
    
    public StyleSheet parseStringToCSS(String css){

        try {
            StyleSheet cssDoc = CSSFactory.parseString(css, null);
            return cssDoc;
        } catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }
    
    public StyleSheet parseFileNameToCSS(String css){
        
        File currentDir = new File("");
        String path = currentDir.getAbsolutePath(); 
        
        if(path.endsWith("app")){
            path = path + "/tinybootstrap-web";
        }
        
        String caminho = path + "/src/main/resources/static/original-css/" + css;
        
        try {
            StyleSheet cssDoc = CSSFactory.parse(caminho, "UTF-8");
            return cssDoc;
        } catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }
    
    public StyleSheet removerSeletoresInuteis(Document html, StyleSheet css){
        
        StyleSheet cssNew = parseStringToCSS("");
        
        css.forEach((estilo) -> {
            if (estilo instanceof RuleSet){
                RuleSet set = (RuleSet)estilo;
                set = processarRuleSet(set, html);
                if(set != null){
                    cssNew.add(set); 
                }
                
            } else if (estilo instanceof RuleMedia){
                
                RuleMedia media = (RuleMedia)estilo;
                List<RuleSet> sets = new ArrayList<>();
                
                media.stream().map((set) -> processarRuleSet(set, html)).filter((set) -> (set != null)).forEach((set) -> {
                    sets.add(set);
                });
                
                if(sets.size() > 0){
                    media.replaceAll(sets);
                    cssNew.add(media); 
                }
                
            } else {
                cssNew.add(estilo);
            }
        });
        return cssNew;
    }
    
    public String removerEspacosEmBrancoESemiCollon(StyleSheet css){
        
        String credits = "/*! * TinyBootstrap (http://tinybootstrap.herokuapp.com) (https://github.com/mpeteffi/tinybootstrap) * Copyright 2016 Murillo Peteffi * Based on Bootstrap and bootswatch */" +
                        "/*! * bootswatch v3.3.6 * Homepage: http://bootswatch.com * Copyright 2012-2016 Thomas Park * Licensed under MIT * Based on Bootstrap */" +
                        "/*! * Bootstrap v3.3.6 (http://getbootstrap.com) * Copyright 2011-2015 Twitter, Inc. * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE) */";
        
        StringBuilder sb = new StringBuilder();
        sb.append(credits);
        OutputUtil.appendList(sb, css, "");
        
        return sb.toString()
                .replaceAll("\\s+\\}+\\s+\\}", "}}")
                .replaceAll("\\s+\\}+\\s+\\s", "}")
                .replaceAll("\\s+\\}+\\s", "}")
                .replaceAll("\\s+\\{+\\s+\\s", "{")
                .replaceAll("\\:+\\s", ":")
                .replaceAll("\\;}", "}")
                .replaceAll("\\;+\\s", ";")
                .replaceAll("\\,+\\s", ",")
                .replaceAll("\\)+\\s", ")")
                .replaceAll("\\s+\\@", "@")
                .replaceAll("\\;+\\s", ";");
    }
    
    private RuleSet processarRuleSet(RuleSet set, Document html){
        
        CombinedSelector[] comb = set.getSelectors();
        List<CombinedSelector> seletores = new ArrayList<>();
        
        for(CombinedSelector ss : comb){
            if(!htmlService.seletorInutil(html, ss.toString())){
                seletores.add(ss);
            }
        }
        
        if(seletores.size() > 0){
            set.setSelectors(seletores);
            return set;
        } else {
            return null;
        }
    }
}
