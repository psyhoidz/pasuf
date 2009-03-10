package com.umlet.control;

import java.awt.Color;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.umlet.element.base.Entity;

public class Search extends Command {
    
    static Color _failed = new Color(227,127,127);
    static Color _success = new Color(148,172,251);
    private String regex;
    private Pattern pattern;
    
    public Search(String regex){
        this.regex=regex;
    }
    
    public void execute() {
        super.execute();
        Selector.getInstance().deselectAll();
        Vector<Entity> entities = Selector.getInstance().getAllEntitiesOnPanel();
        pattern = Pattern.compile(regex);
        Matcher m;
        boolean found = false;
        for (int i=0; i<entities.size(); i++){
            m = pattern.matcher(entities.get(i).getState());
            if (m.find()) {
                found = true;
                Selector.getInstance().selectXXX(entities.get(i));
            }
        }
        if (!found) Umlet.getInstance().getSearchField().setBackground(_failed);
        else Umlet.getInstance().getSearchField().setBackground(_success);
      }
      public void undo() {
        super.undo();
        Selector.getInstance().deselectAll();
      }
    
    
}
