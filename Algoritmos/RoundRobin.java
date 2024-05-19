package Algoritmos;

import java.io.FileNotFoundException;
import java.util.List;

import Base.Escalonador;
import Base.Processo;

public class RoundRobin extends Escalonador {
    protected int quantum; // Tempo máximo que um processo pode rodar no escalonador quando tem um outro na fila

    public RoundRobin(List<Processo> processos, int quantum) throws FileNotFoundException {
        super(processos, "output/roundRobinSaida.txt", "output/roundRobinGrafico.txt");
        this.quantum = quantum;
    }    

    @Override
    public void Executar() throws FileNotFoundException{
        outputFile.print("********** TEMPO 0 **********\n");

        while (!processosEmEspera.isEmpty() || !fila.isEmpty()){
            verificarListaEspera();

            if (tempoProcessando >= quantum){ // Verifica se o tempo de execução chegou no limite do quantum
                trocarProcesso(false);
                continue;
            }

            if ((tempo == 0 || tempoProcessando != 0) && processoExecutando.operacoesIO.contains(processoExecutando.instante)){ // Verifica se o processo está fazendo alguma operação de I/O
                outputFile.print("#[evento] OPERACAO I/O < " + processoExecutando.PID + " >\n");
                trocarProcesso(false);
                continue;
            }

            if (processoExecutando.instante == processoExecutando.duracao){ // Verifica se o processo chegou no final
                outputFile.print("#[evento] ENCERRANDO < " + processoExecutando.PID + " >\n");
                
                processoExecutando.tempoFinal = tempo;
                processosConcluidos.add(processoExecutando);
                
                trocarProcesso(true);
                continue;
            }

            atualizaArquivos();
        }

        printFinalArquivos();
    }
}




