import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException{
        List<Processo> processosRoundRobin = new ArrayList<>();
        List<Processo> processosFIFO = new ArrayList<>();
        List<Processo> processosSJF = new ArrayList<>();
        // List<Processo> processos = new ArrayList<>();


        FileReader file = new FileReader("code.txt");
        try (BufferedReader reader = new BufferedReader(file)) {
            String linha = reader.readLine();
            
            while(linha != null){
                processosRoundRobin.add(new Processo(linha)); 
                processosFIFO.add(new Processo(linha));
                processosSJF.add(new Processo(linha));
                // processos.add(new Processo(linha));
                linha = reader.readLine();
            }
        }

        int quantum = 4;
        List<Thread> escalonadores = new ArrayList<>();
        escalonadores.add(new Thread(new RoundRobin(processosRoundRobin, quantum)));
        escalonadores.add(new Thread(new FIFO(processosFIFO, quantum)));
        escalonadores.add(new Thread(new SJF(processosSJF, quantum)));

        // Execução dos escalonadores
        for (Thread escalonador : escalonadores) {
            escalonador.start();
        }
    }
}
