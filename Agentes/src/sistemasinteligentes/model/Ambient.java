/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasinteligentes.model;

import sistemasinteligentes.view.IRenderizable;
import sistemasinteligentes.view.graphics.RenderPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Ambient implements IRenderizable{
    private final Set<State> states;
    private final List<List<Integer>> weights;
    
    public Ambient(){
        states = new TreeSet<State>();
        weights = new ArrayList<List<Integer>>();
    }

    //Adciona o estado (estados de id ja inserido não são adicionados)
    public void addState(State state){
        states.add(state);
    }
    
    //Impõe o custo sobre uma transição entre estado inicial st1 e estado final st2
    public void addWeight(State st1, State st2, int weight){
        if(st1 == null)
            if(!states.contains(st1))
                return;
        
        if(st2 == null)
            if(!states.contains(st2))
                return;
        
        if(weights.size() - 1 < st1.getID()){
            int s = st1.getID() - weights.size() + 1;
            for(int i = 0; i < s; i++)
                weights.add(null);
        }
        
        if(weights.get(st1.getID()) == null)
            weights.set(st1.getID(), new ArrayList<>());
        
         if(weights.get(st1.getID()).size() - 1 < st2.getID()){
            int s = st2.getID() - weights.get(st1.getID()).size() + 1;
            for(int i = 0; i < s; i++)
                weights.get(st1.getID()).add(null);
        }
        
            weights.get(st1.getID()).set(st2.getID(), new Integer(weight)); //seta a coluna apartir da linha
    }
    
    //Cria um link entre o estado de id init e fin
    public Link getLink(int init, int fin){
        State s[] = new State[states.size()];
        s = states.toArray(s);
        State st1 = getState(init);
        if(st1 == null)
            return null;
        State st2 = getState(fin);
        if(st2 == null)
            return null;
        return getLink(s[init],s[fin]);
    }
    
    //Cria um link entre os estados init e fin
    private Link getLink(State init, State fin){
        if(init == null || fin == null)
            return null;
        if(!(weights.size() > init.getID()))
            return null;
        if(!(weights.get(init.getID()).size() > fin.getID()))
            return null;
        if(weights.get(init.getID()).get(fin.getID()) == null)
            return null;
        return new Link(init, fin, weights.get(init.getID()).get(fin.getID()).intValue());
    }
    
    //Retorna o estado representado por id
    public State getState(int id){
        State s[] = new State[states.size()];
        s = states.toArray(s);
        int topRange = id;
        int botRange = 0;
        int check = id;
        
        while(topRange != botRange){
            if(id == s[check].getID())
                break;
            if(id < s[check].getID())
                topRange = check;
            if(id > s[check].getID())
                botRange = check;
            check = (topRange + botRange)/2;
        }
        
        if(id == s[check].getID())
            return s[check];
        else
            return null;
    }
    
    //Retorna peso entre o estado init e estado fin
    public int getWeight(State init, State fin){
        return weights.get(init.getID()).get(fin.getID()).intValue();
    }

    //Invoca render dos seus componentes
    @Override
    public void render(RenderPanel mp) {
        Link link;
        for(int i = 0; i < weights.size(); i++)
            if(weights.get(i) != null)
                for(int j = 0; j < weights.get(i).size(); j++)
                    if(weights.get(i).get(j) != null)
                        if((link = getLink(i,j)) != null)
                            link.render(mp);        
        
        for(State s: states)
            s.render(mp);
    }
    
    public int getStateSize(){
        return states.size();
    }
    
}