import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;

public class FIFO extends Escalonador{

    public FIFO(List<Processo> processos, int quantum){
        super(processos, quantum);

        processos.sort(Comparator.comparing(Processo::getChegada)); // Ordena os processos por ordem de chegada
        this.processoExecutando = processos.remove(0); // Remove o processo que chega primeiro (que deve estar no tempo 0) da lista de espera enquanto o coloca para ser executado
        this.processosEmEspera = processos;
        this.tituloArquivoSaida = "saida_fifo.txt";
        this.tituloArquivoGrafico = "grafico_fifo.txt";
    }

    
    @Override
    protected void trocarProcesso(Boolean fim){
        fila.remove(processoExecutando);
        if (!fim) fila.add(0, processoExecutando); // Volta o processo para o primeiro lugar da fila

        processoExecutando = fila.size() >= 1 ? fila.get(0) : processoExecutando;
        tempoProcessando = 0;
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
 
