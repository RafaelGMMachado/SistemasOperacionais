import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

public class RoundRobin extends Escalonador{

    public RoundRobin(List<Processo> processos, int quantum)
    {
        super(processos, quantum);
        processos.sort(Comparator.comparing(Processo::getChegada)); // Ordena os processos por ordem de chegada

        this.processoExecutando = processos.remove(0); // Remove o processo que chega primeiro (que deve estar no tempo 0) da lista de espera enquanto o coloca para ser executado
        this.processosEmEspera = processos;
        this.tituloArquivoSaida = "saida_roundrobin.txt";
        this.tituloArquivoGrafico = "grafico_roundrobin.txt";
    }

    @Override
    public void run() {
        try {
            Executar();
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao executar o escalonador");
        }
    } 
    
}
