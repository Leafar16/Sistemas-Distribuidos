package Teste4Janeiro2024;

public class Main {
    
    public static void main(String[] args) {
        Manager m=new Manager();
        try{
            m.join("Rafa",4);
            m.join("Manel", 4);
            m.join("Rui", 4);
            m.join("Ze", 4);
        }catch (Exception e){
        System.out.println("Erro");
        }
        
    }
}
