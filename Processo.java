import java.util.ArrayList;
import java.util.List;

public class Processo {
    String PID;
    int duracao;
    int chegada;
    List<Integer> operacoesIO;
    int instante;
    int tempoFinal;

    public Processo(String linha){
        List<Integer> operacoesIO = new ArrayList<>();
        String[] vetor = linha.split(" ");

        this.PID = vetor[0];
        this.duracao = Integer.parseInt(vetor[1]);
        this.chegada = Integer.parseInt(vetor[2]);
        
        if (vetor.length == 4){
            String[] operacoes = vetor[3].split(",");
            for (String operacao : operacoes){
                operacoesIO.add(Integer.parseInt(operacao));
            }
        }
        this.operacoesIO = operacoesIO;
    }
    public int getChegada(){
        return this.chegada;
    }

    public int getDuracao(){
        return this.duracao;
    }
}