package Teste4Janeiro2024;

import java.util.ArrayList;
import java.util.List;

public class Raid implements RaidI{
    List<String> jogadores=new ArrayList<String>();

    public void add_jogador(String e){
        this.jogadores.add(e);
    }

    public List<String> players(){
     List<String> jogadores2=new ArrayList<String>();
     for(int i=0;i<jogadores.size();i++){
       String novo=new String(jogadores.get(i));
       jogadores2.add(novo);
     }
     return jogadores2;
    }

    public void waitStart() throws InterruptedException{

    }

    public void leave(){

    }
}
