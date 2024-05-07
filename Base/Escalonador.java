package Base;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Escalonador {  
    protected int tempo; // Tempo atual do escalonador
    protected int tempoProcessando; // Tempo gasto no processo desde que ele começou a ser executado
    protected Processo processoExecutando; // Processo que está sendo executado atualmente pelo escalonador
    protected List<Processo> processosEmEspera; // Lista dos processos que estão esperando para entrar no escalonador
    protected List<Processo> processosConcluidos; // Lista dos processos que o escalonador já concluiu
    protected List<Processo> fila; // Fila dos processos que estão rodando no escalonador
    protected PrintWriter outputFile;
    protected PrintWriter graficoFile;

    public Escalonador(List<Processo> processos, String saidaFileName, String graficoFileName) throws FileNotFoundException {
        List<Processo> copiaProcessos = new ArrayList<>();
        for (Processo processo : processos){
            copiaProcessos.add(new Processo(processo));
        }
        copiaProcessos.sort(Comparator.comparing(Processo::getChegada)); // Ordena os processos por ordem de chegada
        
        tempo = 0;
        tempoProcessando = 0;
        processoExecutando = copiaProcessos.remove(0); // Remove o processo que chega primeiro (que deve estar no tempo 0) da lista de espera enquanto o coloca para ser executado
        processosEmEspera = copiaProcessos;
        processosConcluidos = new ArrayList<>();
        fila = new ArrayList<>();

        outputFile = new PrintWriter(saidaFileName);
        graficoFile = new PrintWriter(graficoFileName);
    }    

    public void Executar() throws FileNotFoundException{
    }

    protected boolean verificarListaEspera(){ // Verifica se entrou algum processo novo para ser executado
        List<Processo> novoNaFila = processosEmEspera.stream().filter(o -> o.chegada == tempo).collect(Collectors.toList());
        if (!novoNaFila.isEmpty()){
            fila.addAll(novoNaFila);
            processosEmEspera.removeAll(novoNaFila);
            outputFile.print("#[evento] CHEGADA < " + getProcessosNovosText(novoNaFila) + ">\n");
            return true;
        }
        return false;
    }

    protected void trocarProcesso(Boolean fim){
        fila.remove(processoExecutando);
        if (!fim) fila.add(processoExecutando);

        processoExecutando = fila.size() >= 1 ? fila.get(0) : processoExecutando;
        tempoProcessando = 0;
    }

    protected String getProcecssosFilaText(){
        String texto = "";
        List<Processo> filaReal = fila.stream().filter(o -> o.PID != processoExecutando.PID).collect(Collectors.toList());

        if (filaReal.isEmpty())
            return "Nao ha processos na fila";

        for (Processo processo : filaReal){
            texto += processo.PID + "(" + String.valueOf(processo.duracao - processo.instante) + ") ";
        }

        return texto;
    }

    protected String getProcessosNovosText(List<Processo> novosProcessos){
        String texto = "";

        for (Processo processo : novosProcessos){
            texto += processo.PID + " ";
        }

        return texto;   
    }

    protected void atualizaArquivos(){
        outputFile.print("Fila: " + getProcecssosFilaText() + "\n");
        outputFile.print("CPU: " + processoExecutando.PID + "(" + String.valueOf(processoExecutando.duracao - processoExecutando.instante) + ")\n");
        graficoFile.print(processoExecutando.PID + " | ");
            
        processoExecutando.instante ++;
        tempoProcessando ++;
        tempo ++;
        outputFile.print("********** TEMPO " + tempo + " **********\n");
    }

    protected void printFinalArquivos(){
        outputFile.print("Fila: Nao ha processos na fila\n");
        outputFile.print("ACABARAM OS PROCESSOS!!!\n");
        outputFile.close();
        
        graficoFile.print("\n\n********** Tempo de espera **********\n");
        int somaTempo = 0;
        for (Processo processo : processosConcluidos){
            int tempoEspera = processo.tempoFinal-processo.chegada;
            somaTempo += tempoEspera;
            graficoFile.print(processo.PID + ": " + tempoEspera + "\n");
        }
        graficoFile.print("Media: " + somaTempo/(processosConcluidos.size()-1));
        graficoFile.close();
    }
}
