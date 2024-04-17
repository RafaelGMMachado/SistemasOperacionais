import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Escalonador implements Runnable{
    protected int quantum; // Tempo máximo que um processo pode rodar no escalonador quando tem um outro na fila
    protected int tempo; // Tempo atual do escalonador
    protected int tempoProcessando; // Tempo gasto no processo desde que ele começou a ser executado
    protected Processo processoExecutando; // Processo que está sendo executado atualmente pelo escalonador
    protected List<Processo> processosEmEspera; // Lista dos processos que estão esperando para entrar no escalonador
    protected List<Processo> processosConcluidos; // Lista dos processos que o escalonador já concluiu
    protected List<Processo> fila; // Fila dos processos que estão rodando no escalonador
    protected String tituloArquivoSaida; // Titulo do arquivo de saída
    protected String tituloArquivoGrafico; // Titulo do arquivo de saída


    public Escalonador(List<Processo> processos, int quantum){
        this.quantum = quantum;
        this.tempo = 0;
        this.tempoProcessando = 0;
        this.processosConcluidos = new ArrayList<>();
        this.fila = new ArrayList<>();
    }

    public void Executar() throws FileNotFoundException{
        PrintWriter outputFile = new PrintWriter(tituloArquivoSaida);
        PrintWriter graficoFile = new PrintWriter(tituloArquivoGrafico);

        outputFile.print("********** TEMPO 0 **********\n");

        while (!processosEmEspera.isEmpty() || !fila.isEmpty()){
            verificarListaEspera(outputFile);

            if (tempoProcessando >= quantum){ // Verifica se o tempo de execução chegou no limite do quantum
                trocarProcesso(false);
                continue;
            }

            if ((tempo == 0 || tempoProcessando != 0) && processoExecutando.operacoesIO.contains(processoExecutando.instante)){ // Verifica se o processo está fazendo alguma operação de I/O
                outputFile.print("#[evento] OPERACAO I/O <" + processoExecutando.PID + ">\n");
                trocarProcesso(false);
                continue;
            }

            if (processoExecutando.instante == processoExecutando.duracao){ // Verifica se o processo chegou no final
                outputFile.print("#[evento] ENCERRANDO <" + processoExecutando.PID + ">\n");
                
                processoExecutando.tempoFinal = tempo;
                processosConcluidos.add(processoExecutando);
                
                trocarProcesso(true);
                continue;
            }

            outputFile.print("Fila: " + getProcecssosFilaText() + "\n");
            outputFile.print("CPU: " + processoExecutando.PID + "(" + String.valueOf(processoExecutando.duracao - processoExecutando.instante) + ")\n");
            graficoFile.print(processoExecutando.PID + " | ");
            
            processoExecutando.instante ++;
            tempoProcessando ++;
            tempo ++;
            outputFile.print("********** TEMPO " + tempo + " **********\n");
        }

        printFinalArquivos(outputFile, graficoFile);
    }

    protected void verificarListaEspera(PrintWriter outputFile){ // Verifica se entrou algum processo novo para ser executado
        List<Processo> novoNaFila = processosEmEspera.stream().filter(o -> o.chegada == tempo).collect(Collectors.toList());
        if (!novoNaFila.isEmpty()){
            fila.addAll(novoNaFila);
            processosEmEspera.removeAll(novoNaFila);
            outputFile.print("#[evento] CHEGADA <" + getProcessosNovosText(novoNaFila) + ">\n");
        }
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
            texto += processo.PID;
        }

        return texto;   
    }

    protected void printFinalArquivos(PrintWriter outputFile, PrintWriter graficoFile){
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

    @Override
    public void run() {
        try {
            Executar();
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao executar o escalonador");
        }
    } 
}
