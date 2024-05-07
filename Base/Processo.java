package Base;
import java.util.ArrayList;
import java.util.List;

public class Processo {
    public String PID;
    public int duracao;
    public int chegada;
    public List<Integer> operacoesIO;
    public int instante;
    public int tempoFinal;

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

    public Processo(String PID, int duracao, int chegada, List<Integer> operacoesIO, int instante, int tempoFinal){
        this.PID = PID;
        this.duracao = duracao;
        this.chegada = chegada;
        this.operacoesIO = operacoesIO;
        this.instante = instante;
        this.tempoFinal = tempoFinal;
    }

    public Processo(Processo copiando){
        this(copiando.getPID(),
        copiando.getDuracao(),
        copiando.getChegada(),
        copiando.getOperacoesIO(),
        copiando.getInstante(),
        copiando.getTempoFinal());
    }
    
    public String getPID(){
        return this.PID;
    }

    public int getDuracao(){
        return this.duracao;
    }

    public int getChegada(){
        return this.chegada;
    }

    public List<Integer> getOperacoesIO(){
        return this.operacoesIO;
    }

    public int getInstante(){
        return this.instante;
    }

    public int getTempoFinal(){
        return this.tempoFinal;
    }

    public int getTempoRestante(){
        return duracao-instante;
    }
}