import java.util.List;
import java.io.FileNotFoundException;
import java.util.Comparator;

public class SJF extends Escalonador
{
    public SJF(List<Processo> processos, int quantum){
        super(processos, quantum);
        processos.sort(Comparator.comparing(Processo::getDuracao)); // Ordena processos por tempo de duração em ordem crescente

        this.processoExecutando = processos.remove(0); // Remove o processo que chega primeiro (que deve estar no tempo 0) da lista de espera enquanto o coloca para ser executado
        this.processosEmEspera = processos;
        this.tituloArquivoSaida = "saida_sjf.txt";
        this.tituloArquivoGrafico = "grafico_sjf";
    }

    @Override
    protected void trocarProcesso(Boolean fim){
        fila.remove(processoExecutando);
        if (!fim) fila.add(0, processoExecutando); // Volta o processo para o primeiro lugar da fila

        fila.sort(Comparator.comparing(Processo::getDuracao)); // Reordena a fila com base na duração dos processos em ordem crescente
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
