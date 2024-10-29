package Teste4Janeiro2024;

import java.util.ArrayList;
import java.util.List;

public interface RaidI {
List<String> players();

void waitStart() throws InterruptedException;

void leave();
    
}
