/**
 * Este package fornece todas as classes para o funcionamento do jogo.
 */
package pootpstarwarslunarlandergame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Classe que funcionará como motor do jogo. Inicializa o primeiro nível do jogo
 * e sempre que se chega ao fim do nível passa, conforme escolha do utilizador
 * para o nível seguinte, repete o mesmo ou recomeça o jogo. Funciona de forma
 * independente e actualiza o Interface Gráfico do Utilizador conforme vai
 * jogando. Em suma, é o centro do funcionamento correcto do jogo.
 *
 * @author C.M.P.Soares
 */
public class Nivel {

    /**
     * Variável que contém o valor da altitude vigente.
     */
    private int altitude;
    /**
     * Valor da altitude inicial do nível.
     */
    private int altitudeInicial;
    /**
     * Valor da velocidade máxima para se conseguir passar de nível.
     */
    private int velocidadeMaxima;
    /**
     * Valor da velocidade actualizada de segundo em segundo.
     */
    private int velocidadeActual;
    /**
     * Variável que nos indica quantos 'jatos' ainda se encontram em espera.
     */
    private int jatos;
    /**
     * Nível de dificuldade do jogo.
     *
     * @see Nave#NAVE_EASY
     * @see Nave#NAVE_MEDIUM
     * @see Nave#NAVE_HARD
     * @see Nave#NAVE_VERY_HARD
     */
    private int difficulty;
    /**
     * Valor inteiro que define dentro do nível/ grau de dificuldade qual é o
     * nível(level) em que se encontra o jogador.
     */
    private int level;
    /**
     * Percentagem em que a nave se encontra de finalizar o nível(level). O
     * valor 1.00 significa que aterrou.
     */
    private double percentNivel;
    /**
     * Instância de um Astro para guardar/utilizar dados actuais do nível.
     *
     * @see Astro
     */
    private Astro astro;
    /**
     * Instância de uma Nave para guardar/utilizar dados actuais do nível.
     *
     * @see Nave
     */
    private Nave nave;
    /**
     * Timer para se poder efectuar a animação e calculo do jogo de x em x
     * segundos (1000 microsegundos).
     *
     * @see Timer
     */
    private Timer timer;
    /**
     * Apontador para o GUI em utilização.
     *
     * @see GUI
     */
    private GUI guiUtilizado;
    /**
     * Apontador da instância actual do Nivel para a utilização dentro de
     * classes anónimas.
     */
    private Nivel nivelActual;

    /**
     * Construtor obrigatório para inicializar o Nivel/motor de jogo.
     *
     * @param difficulty Nível de dificuldade do jogo.
     * @param guiUtilizado Apontador para o GUI em utilização.
     */
    public Nivel(int difficulty, GUI guiUtilizado) {
        this.difficulty = difficulty;
        this.guiUtilizado = guiUtilizado;

        level = 1;

        if (difficulty == Nave.NAVE_EASY) {
            percentNivel = 0;
            jatos = 0;
            velocidadeActual = 0;
            velocidadeMaxima = 50;
            altitudeInicial = 100;
            altitude = altitudeInicial;
            astro = new Astro(5, level);
            nave = new Nave(8, 51, 1);
        } else {
            setNewLevelParametres();
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                getGUI().updateSpaceShip(percentNivel);
                velocidadeActual = velocidadeActual - astro.getAceleracao();
                if (altitude <= 0) {
                    stopLevel();
                    altitude = 0;
                    percentNivel = (double) 1;
                    getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                    getGUI().updateSpaceShip(percentNivel);
                    nivelActual.EOLevel();
                } else if (jatos > 0) {
                    if (nave.useCombustivel()) {
                        velocidadeActual = velocidadeActual + nave.getAceleracaoPorJato();
                        jatos--;
                    }
                }
                altitude = altitude + velocidadeActual;
                percentNivel = (double) (altitudeInicial - altitude) / altitudeInicial;
                if (percentNivel > 1) {
                    percentNivel = 1;
                }
            }
        });
        nivelActual = getNivel();
    }

    /**
     * Metodo criado para poder utilizar o apontador do GUI em classes anónimas.
     *
     * @return O apontador do GUI em utilização
     * @see GUI
     */
    public GUI getGUI() {
        return guiUtilizado;
    }

    /**
     * Metodo criado para poder utilizar o apontador do Nivel vigente em classes
     * anónimas.
     *
     * @return O apontador do Nivel em utilização
     * @see Nivel
     */
    private Nivel getNivel() {
        return this;
    }

    /**
     * Método que faz um reset as variáveis necessárias para começar um novo
     * Nível(level).
     */
    private void setNewLevelParametres() {
        percentNivel = 0;
        velocidadeActual = 0;
        jatos = 0;

        velocidadeMaxima = 50 + (5 + difficulty) * level / difficulty;

        altitude = altitudeInicial = 100 * (level + difficulty);

        astro = new Astro(5 + (level * difficulty / 3), level);
        nave = new Nave(8 + (level * difficulty / 2), 50 + level * 5, (1 + difficulty) / difficulty);
    }

    /**
     * Método que adiciona um jato quanto este é chamado.
     */
    public void hitJato() {
        jatos++;
    }

    /**
     * @return String com a informação do nível actual para formatado para
     * títulos.
     */
    public String LevelTitleToString() {
        switch (difficulty) {
            case Nave.NAVE_VERY_HARD: {
                return ("Very Hard " + "- nível " + level + " - " + astro.getNomeAstro() + ": ");
            }
            case Nave.NAVE_HARD: {
                return ("Hard " + "- nível " + level + " - " + astro.getNomeAstro() + ": ");
            }
            case Nave.NAVE_MEDIUM: {
                return ("Medium " + "- nível " + level + " - " + astro.getNomeAstro() + ": ");
            }
            default: {
                return ("Easy " + "- nível " + level + " - " + astro.getNomeAstro() + ": ");
            }
        }
    }

    /**
     * Inicia o timer.
     *
     * @see Timer
     */
    public void startLevel() {
        timer.start();
    }

    /**
     * @return 'true' se jogo está a correr e 'false' caso contrário.
     */
    public boolean isGameRunning() {
        return timer.isRunning();
    }

    /**
     * Para o timer.
     *
     * @see Timer
     */
    void stopLevel() {
        timer.stop();
    }

    /**
     * O nome está para EndOfLevel e quando o nivel chega ao fim este método
     * decide se ganhou ou não e como o utilizador pretende prosseguir.
     */
    private void EOLevel() {
        Object[] optionsLevelCompleted = {"Cancelar", "Começar de Novo", "Recomeçar Nível", "Próximo Nível"};
        Object[] optionsLevelFailed = {"Cancelar", "Começar de Novo", "Recomeçar Nível"};
        int option;

        if (-velocidadeActual <= velocidadeMaxima) {
            option = JOptionPane.showOptionDialog(getGUI(), "Parabéns! Completaste o Nível " + Integer.toString(level) + "!", "Nível Bem Sucedido!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionsLevelCompleted, optionsLevelCompleted[3]);
            if (option == 1) {
                level = 1;
                getGUI().updateGUIDashBoardTitle();
                JOptionPane.showMessageDialog(getGUI(), "Pronto?", LevelTitleToString(), JOptionPane.DEFAULT_OPTION);
                setNewLevelParametres();
                getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                startLevel();
            } else if (option == 2) {
                JOptionPane.showMessageDialog(getGUI(), "Pronto?", LevelTitleToString(), JOptionPane.DEFAULT_OPTION);
                setNewLevelParametres();
                getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                startLevel();
            } else if (option == 3) {
                level++;
                setNewLevelParametres();
                getGUI().updateGUIDashBoardTitle();
                getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                JOptionPane.showMessageDialog(getGUI(), "Pronto?", LevelTitleToString(), JOptionPane.DEFAULT_OPTION);
                startLevel();
            } else {
                JOptionPane.showMessageDialog(getGUI(), "Desistes?!", "Oh!", JOptionPane.DEFAULT_OPTION);
            }
        } else {
            option = JOptionPane.showOptionDialog(getGUI(), "Game Over! Perdeu o Nível " + Integer.toString(level) + "!", "Nível Bem Sucedido!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, optionsLevelFailed, optionsLevelFailed[2]);
            if (option == 1) {
                level = 1;
                getGUI().updateGUIDashBoardTitle();
                JOptionPane.showMessageDialog(getGUI(), "Pronto?", LevelTitleToString(), JOptionPane.DEFAULT_OPTION);
                setNewLevelParametres();
                getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                startLevel();
            } else if (option == 2) {
                JOptionPane.showMessageDialog(getGUI(), "Pronto?", LevelTitleToString(), JOptionPane.DEFAULT_OPTION);
                setNewLevelParametres();
                getGUI().updateLevelValues(altitude, nave.getCombustivel(), velocidadeActual);
                startLevel();
            } else {
                JOptionPane.showMessageDialog(getGUI(), "Desistes pa?!", "Loser!", JOptionPane.DEFAULT_OPTION);
            }
        }
    }

    /**
     * @return Valor da velocidade máxima para se conseguir passar de nível.
     */
    public int getVelocidadeMaxima() {
        return velocidadeMaxima;
    }
}
