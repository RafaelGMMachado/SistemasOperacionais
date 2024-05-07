import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Base.Escalonador;
import Base.Processo;
import Algoritmos.RoundRobin;
import Algoritmos.FCFS;
import Algoritmos.SJF;

public class Main {
    public static void main(String[] args) throws IOException{
        List<Processo> processos = new ArrayList<>();

        FileReader file = new FileReader("code.txt");
        try (BufferedReader reader = new BufferedReader(file)) {
            String linha = reader.readLine();
            
            while(linha != null){
                processos.add(new Processo(linha)); 
                linha = reader.readLine();
            }
        }
 
        List<Escalonador> escalonadores = new ArrayList<>();
        escalonadores.add(new RoundRobin(processos, 4));
        escalonadores.add(new FCFS(processos));
        escalonadores.add(new SJF(processos));

        for (Escalonador escalonador : escalonadores){
            escalonador.Executar();
        }

    }
}
