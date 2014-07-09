/**
 * Este package fornece todas as classes para o funcionamento do jogo.
 */
package pootpstarwarslunarlandergame;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Classe que por si cria todo o ambiente grafico do utilizador e inicializa
 * todas as variáveis e instância todas as classes necessárias para o bom
 * funcionamento do jogo em si. A classe GUI extende o JFrame da javax.swing e
 * tem um construtor obrigatório. Contem três classes internas privadas em que
 * cada uma delas também extende uma classe da javax.swing e tem uma utilidade
 * personalizada e para poderem garantir o bom funcionamento da classe
 * principal.
 *
 * @author C.M.P.Soares
 */
public class GUI extends JFrame {

    /**
     * Variável constante do tipo Dimension que define qual o tamanho do ecrã.
     *
     * @see Dimension
     * @see Toolkit#getDefaultToolkit()
     */
    final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * Apontador da instância do GUIMenuBar em utilização.
     *
     * @see GUIMenuBar
     */
    private GUIMenuBar guiMenuBar;
    /**
     * Apontador da instância do GUIDashBoard em utilização.
     *
     * @see GUIDashBoard
     */
    private GUIDashBoard guiDashBoard;
    /**
     * Apontador da instância do GUIAnimation em utilização.
     *
     * @see GUIAnimation
     */
    private GUIAnimation guiAnimation;
    /**
     * Apontador da instância do Nivel em utilização.
     *
     * @see Nivel
     */
    private Nivel nivelJogado;
    /**
     * Dificuldade do Nivel a ser jogado.
     */
    private int dificuldadeJogo = Nave.NAVE_EASY;
    /**
     * Apontador para uma instância de um Clip utilizado para poder colocar
     * musica.
     *
     * @see Clip
     */
    private Clip clip;

    /**
     * Construtor Obrigatório que inicializa o jogo para ela poder funcionar de
     * forma independente.
     */
    GUI() {
        super("StarWars Lunar Lander!");

        //Definir logo nivel 'Default'.
        this.nivelJogado = new Nivel(dificuldadeJogo, this);

        //Adicionar uma musiquinha...
        try {
            File soundFile = new File("StarWarsTheme.wav");
            //Proximo comando para possíveis debugs.
            //System.out.println(soundFile.getCanonicalPath());
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error initializing music");
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, e);
        }
        //Fim da pequena brincadeira!

        //Definir que assume o LookAndFeel de cada SO.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Definir propriedades principais do GUI(JFrame).
        setPreferredSize(new Dimension((SCREEN_DIMENSION.width * 7 / 14), (SCREEN_DIMENSION.height * 8 / 9)));
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Adicionar os componentes do GUI aop mesmo.
        guiMenuBar = new GUIMenuBar();
        setJMenuBar(guiMenuBar);

        guiDashBoard = new GUIDashBoard();
        guiDashBoard.setPreferredSize(new Dimension(SCREEN_DIMENSION.width * 6 / 25, SCREEN_DIMENSION.height * 7 / 8));

        guiAnimation = new GUIAnimation();
        guiAnimation.setPreferredSize(new Dimension(SCREEN_DIMENSION.width / 3, SCREEN_DIMENSION.height * 7 / 8));

        add(guiAnimation, BorderLayout.CENTER);
        add(guiDashBoard, BorderLayout.EAST);

        //Colocar tudo nos tamanho definidos
        pack();
        //Centrar e mostrar no ecrã.
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Metodo criado para poder utilizar o apontador do GUI em classes anónimas.
     *
     * @return O apontador do GUI actual.
     * @see GUI
     */
    private GUI getGUI() {
        return this;
    }

    /**
     * Classe interna privada que constroi um JMenuBar personalizado para o
     * jogo.
     *
     * @see JMenuBar
     */
    private class GUIMenuBar extends JMenuBar {

        /**
         * As variáveis JMenu necessárias para criar este JMenuBar. O menuConfig
         * é para escolher a dificuldade do nível e o som para seleccionar se o
         * som está activo ou não.
         *
         * @see JMenu
         */
        JMenu menu, menuConfig, som;
        /**
         * As variáveis JMenuItem necessárias para criar este JMenuBar.
         *
         * @see JMenuItem
         */
        JMenuItem menuItemInic, menuItemSair;
        /**
         * Os ButtonGroups são necessários para poder ser feita uma selecção da
         * dificuldade e do som.
         *
         * @see ButtonGroup
         */
        ButtonGroup configGroup, selectSom;
        /**
         * Os JRadioButtonMenuItem são inserido num ButtonGroup para se saber
         * qual a selecção possivel.
         *
         * @see JRadioButtonMenuItem
         */
        JRadioButtonMenuItem easy, medium, hard, veryHard, somOn, somOff;

        /**
         * Construtor obrigatório para criar o GUIMenuBar.
         */
        GUIMenuBar() {
            super();

            //Ficheiro -> Iniciar:
            menuItemInic = new JMenuItem("Iniciar Novo Jogo");

            menuItemInic.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int option = JOptionPane.showConfirmDialog(getGUI(), "Vamos Começar?", nivelJogado.LevelTitleToString(), JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        if (getGUI().nivelJogado.isGameRunning()) {
                            getGUI().nivelJogado.stopLevel();
                        }
                        getGUI().nivelJogado = new Nivel(dificuldadeJogo, getGUI());
                        getGUI().updateGUIDashBoardTitle();
                        getGUI().nivelJogado.startLevel();
                    }
                }
            });
            //End of task

            //Ficherio -> Configurar:
            menuConfig = new JMenu("Configurar Dificuldade");
            configGroup = new ButtonGroup();

            easy = new JRadioButtonMenuItem("Easy: Naboo Starfighter");
            easy.setSelected(true);
            easy.addActionListener(jRadioButtonAListener());
            menuConfig.add(easy);
            configGroup.add(easy);

            medium = new JRadioButtonMenuItem("Medium: Naboo Royal StarShip");
            medium.addActionListener(jRadioButtonAListener());
            menuConfig.add(medium);
            configGroup.add(medium);

            hard = new JRadioButtonMenuItem("Hard: Millenium Falcon");
            hard.addActionListener(jRadioButtonAListener());
            menuConfig.add(hard);
            configGroup.add(hard);

            veryHard = new JRadioButtonMenuItem("Very Hard: ARC-170 Starfighter");
            veryHard.addActionListener(jRadioButtonAListener());
            menuConfig.add(veryHard);
            configGroup.add(veryHard);
            //End of Task

            //Ficheiro -> Som:
            som = new JMenu("Som");
            selectSom = new ButtonGroup();

            somOn = new JRadioButtonMenuItem("On");
            somOn.setSelected(true);
            somOn.addActionListener(selectSomAListener());
            som.add(somOn);
            selectSom.add(somOn);

            somOff = new JRadioButtonMenuItem("Off");
            somOff.addActionListener(selectSomAListener());
            som.add(somOff);
            selectSom.add(somOff);

            //Ficheiro -> Sair:
            menuItemSair = new JMenuItem("Sair");
            menuItemSair.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int option = JOptionPane.showConfirmDialog(getGUI(), "Deseja mesmo Sair?", "Sair", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(getGUI(), "Obrigado por Jogar!\nAté a próxima!", "Adeus!", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
            });
            //End Of Task

            //Inicializar Menu:
            menu = new JMenu("Ficheiro");
            //Adicionar os Componentes todos ao menu:
            menu.add(menuItemInic);
            menu.addSeparator();
            menu.add(menuConfig);
            menu.addSeparator();
            menu.add(som);
            menu.addSeparator();
            menu.add(menuItemSair);
            //Adicionar Menu ao MenuBar:
            add(menu);
        }

        /**
         * @return Um ActionListener que escuta os JRadioMenuButton utilizados
         * para a selecção da dificuldade do nível.
         * @see ActionListener
         */
        private ActionListener jRadioButtonAListener() {
            ActionListener al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object botaoFonte = e.getSource();

                    getGUI().nivelJogado.stopLevel();

                    if (botaoFonte.equals(veryHard)) {
                        dificuldadeJogo = Nave.NAVE_VERY_HARD;
                    } else if (botaoFonte.equals(hard)) {
                        dificuldadeJogo = Nave.NAVE_HARD;
                    } else if (botaoFonte.equals(medium)) {
                        dificuldadeJogo = Nave.NAVE_MEDIUM;
                    } else {
                        dificuldadeJogo = Nave.NAVE_EASY;
                    }

                    getGUI().nivelJogado = new Nivel(dificuldadeJogo, getGUI());
                    getGUI().updateGUIDashBoardTitle();
                }
            };
            return al;
        }

        /**
         * @return Um ActionListener que escuta os JRadioMenuButton utilizados
         * para seleccionar se o som está ligado ou nao.
         * 
         * @see ActionListener
         */
        private ActionListener selectSomAListener() {
            ActionListener al;
            al = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object botaoFonte = e.getSource();

                    if (botaoFonte.equals(somOn)) {
                        if (!clip.isActive()) {
                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                        }
                    } else {
                        if (clip.isActive()) {
                            clip.stop();
                        }
                    }

                    getGUI().nivelJogado = new Nivel(dificuldadeJogo, getGUI());
                    getGUI().updateGUIDashBoardTitle();
                }
            };
            return al;
        }
    }

    /**
     * Classe interna privada que constroi um JPanel personalizado para o jogo.
     * Preparado para criar o "painel de controlo" do jogo.
     *
     * @see JPanel
     */
    private class GUIDashBoard extends JPanel {

        /**
         * Um RoundedPanel transparente para inserir os paineis e itens do
         * painel que mostra a informação e que contém o botão do jato.
         *
         * @see RoundedPanel
         */
        private RoundedPanel DashBoard = new RoundedPanel(15, false);
        /**
         * Um JPanel criado para inserir o painel jPInfo e assim conseguir
         * facilmente criar uma margem.
         *
         * @see JPanel
         */
        private JPanel jPforjPInfo = new JPanel();
        /**
         * Um JPanel criado para inserir o painel os componentes com a
         * informação do nível.
         *
         * @see JPanel
         */
        private JPanel jPInfo = new JPanel();
        /**
         * Um JPanel criado para inserir o painel os botões do jogo.
         *
         * @see JPanel
         */
        private JPanel jPSouthButtons = new JPanel();
        /**
         * Um JLabel utilizado para dispor o título deste 'painel de controlo.
         *
         * @see JLabel
         */
        private JLabel TítuloNivel = new JLabel(nivelJogado.LevelTitleToString());
        /**
         * Utilizado para colocar a String <i>"Velocidade</i> Máxima: " no
         * 'painel de controlo
         *
         * @see JLabel
         */
        private final JLabel VELOCIDADE_MAXIMA = new JLabel("Velocidade Máxima: ");
        /**
         * Utilizado para colocar a String <i>"Altitude: "</i> no 'painel de
         * controlo
         *
         * @see JLabel
         */
        private final JLabel ALTITUDE = new JLabel("Altitude: ");
        /**
         * Utilizado para colocar a String <i>"Velocidade: "</i> no 'painel de
         * controlo
         *
         * @see JLabel
         */
        private final JLabel VELOCIDADE = new JLabel("Velocidade: ");
        /**
         * Utilizado para colocar a String <i>"Combustível: "</i> no 'painel de
         * controlo
         *
         * @see JLabel
         */
        private final JLabel COMBUSTIVEL = new JLabel("Combustível: ");
        /**
         * Utilizado para colocar a String <i>" m/s"</i> no 'painel de controlo
         *
         * @see JLabel
         */
        private final JLabel METROS_POR_SEGUNDO = new JLabel(" m/s");
        /**
         * Segundo JPanel utilizado para colocar a String <i>" m/s"</i> no
         * 'painel de controlo
         *
         * @see JLabel
         */
        private final JLabel METROS_POR_SEGUNDO_SEGUINTE = new JLabel(" m/s");
        /**
         * Utilizado para colocar a String <i>" m"</i> no 'painel de controlo
         *
         * @see JLabel
         */
        private final JLabel METROS = new JLabel(" m");
        /**
         * Utilizado para colocar a String <i>" litros"</i> no 'painel de
         * controlo
         *
         * @see JLabel
         */
        private final JLabel LITROS = new JLabel(" litros");
        /**
         * Utilizado para dispor a <b>velocidade máxima</b> do nível vigente no
         * ecrá.
         *
         * @see JTextField
         */
        private JTextField velocidadeMaximaText = new JTextField("0");
        /**
         * Utilizado para dispor a <b>actual altitude</b> do nível vigente no
         * ecrá.
         *
         * @see JTextField
         */
        private JTextField altitudeText = new JTextField("0");
        /**
         * Utilizado para dispor a <b>actual velocidade</b> do nível vigente no
         * ecrá.
         *
         * @see JTextField
         */
        private JTextField velocidadeText = new JTextField("0");
        /**
         * Utilizado para dispor o <b>combustível</b> do nível vigente no ecrá.
         *
         * @see JTextField
         */
        private JTextField combustivelText = new JTextField("0");
        /**
         * Botão utilizado para quando pressionado se activar o jato se
         * possível.
         *
         * @see JButton
         */
        private JButton jatoButton = new JButton("Jato");
        /**
         * O layout utilizado para o jPInfo e dispor a informação organizada em
         * grelha.
         *
         * @see GridLayout
         */
        private GridLayout gridLayout = new GridLayout(4, 3);

        /**
         * Construtor obrigatório para criar o 'painel de controlo'.
         */
        GUIDashBoard() {

            super();
            setLayout(new FlowLayout(FlowLayout.LEADING, 18, 18));
            setBackground(Color.getHSBColor(0.57f, 0.75f, 0.9f));

            DashBoard.setLayout(new BorderLayout());
            DashBoard.setPreferredSize(new Dimension(SCREEN_DIMENSION.width * 11 / 50, SCREEN_DIMENSION.height * 25 / 32));
            DashBoard.setBackground(Color.BLACK);

            gridLayout.setVgap(15);

            VELOCIDADE_MAXIMA.setLabelFor(velocidadeMaximaText);
            ALTITUDE.setLabelFor(altitudeText);
            VELOCIDADE.setLabelFor(velocidadeText);
            COMBUSTIVEL.setLabelFor(combustivelText);
            METROS_POR_SEGUNDO.setLabelFor(velocidadeMaximaText);
            METROS.setLabelFor(altitudeText);
            METROS_POR_SEGUNDO_SEGUINTE.setLabelFor(velocidadeText);
            LITROS.setLabelFor(combustivelText);

            TítuloNivel.setOpaque(false);

            velocidadeMaximaText.setEnabled(false);
            altitudeText.setEnabled(false);
            velocidadeText.setEnabled(false);
            combustivelText.setEnabled(false);

            velocidadeMaximaText.setHorizontalAlignment(JTextField.RIGHT);
            altitudeText.setHorizontalAlignment(JTextField.RIGHT);
            velocidadeText.setHorizontalAlignment(JTextField.RIGHT);
            combustivelText.setHorizontalAlignment(JTextField.RIGHT);

            jPInfo.setLayout(gridLayout);
            jPInfo.setOpaque(false);

            jPInfo.add(VELOCIDADE_MAXIMA);
            jPInfo.add(velocidadeMaximaText);
            jPInfo.add(METROS_POR_SEGUNDO);

            jPInfo.add(ALTITUDE);
            jPInfo.add(altitudeText);
            jPInfo.add(METROS);

            jPInfo.add(VELOCIDADE);
            jPInfo.add(velocidadeText);
            jPInfo.add(METROS_POR_SEGUNDO_SEGUINTE);

            jPInfo.add(COMBUSTIVEL);
            jPInfo.add(combustivelText);
            jPInfo.add(LITROS);

            jPSouthButtons.setLayout(new FlowLayout());
            jPSouthButtons.setOpaque(false);

            jatoButton.setPreferredSize(new Dimension(SCREEN_DIMENSION.width * 1 / 10, SCREEN_DIMENSION.height * 1 / 15));
            jatoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            jatoButton.setOpaque(false);
            jatoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (nivelJogado.isGameRunning()) {
                        nivelJogado.hitJato();
                    }
                }
            });

            jPSouthButtons.add(jatoButton);

            jPforjPInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
            jPforjPInfo.add(TítuloNivel);
            jPforjPInfo.add(jPInfo);
            jPforjPInfo.setOpaque(false);

            DashBoard.add(jPforjPInfo, BorderLayout.CENTER);
            DashBoard.add(jPSouthButtons, BorderLayout.SOUTH);
            DashBoard.setOpaque(false);

            add(DashBoard);

        }
    }

    /**
     * Classe interna privada que constroi um JPanel personalizado para o jogo.
     * Preparado para criar o "painel de animações" do jogo.
     *
     * @see JPanel
     * @see GamePanel
     */
    private class GUIAnimation extends JPanel {

        /**
         * Painel que contém a animação que é adicionado ao painel actual.
         *
         * @see GamePanel
         */
        private GamePanel actionPanel = new GamePanel(15, true);

        /**
         * Construtor obrigatório para criar o 'painel das animações'.
         */
        GUIAnimation() {
            super();
            setLayout(new FlowLayout(FlowLayout.CENTER, 18, 18));
            setBackground(Color.getHSBColor(0.57f, 0.75f, 0.9f));

            actionPanel.setPreferredSize(new Dimension(SCREEN_DIMENSION.width * 6 / 25, SCREEN_DIMENSION.height * 28 / 36));
            actionPanel.setAlignmentX(CENTER_ALIGNMENT);
            actionPanel.setAlignmentY(CENTER_ALIGNMENT);
            actionPanel.setOpaque(false);

            add(actionPanel);
        }
    }

    /**
     * Classe interna privada que constroi um RoundedPanel personalizado para o
     * jogo. Desenha as diferentes animações do jogo.
     *
     * @see GUIAnimation
     * @see RoundedPanel
     */
    private class GamePanel extends RoundedPanel {

        /**
         * Variável utilizada para definir se o painel de jogo tem a nave
         * desenhada.
         */
        private boolean shipIsActivated;
        /**
         * Percentagem da altura do painel a que se encontra o desenho da nave,
         * a contar vindo de cima.
         */
        private double shipHeightPercent;
        /**
         * Variável a partir do qual se sabe qual a nave a desenhar.
         */
        private int shipType;

        /**
         * Construtor que cria um painel de jogo vazio.
         *
         * @param cornerRadius Constante que define qual o raio se utiliza para
         * arredondondar os cantos.
         * @param colorIsBlack Constante adicional utilizado para definir se o
         * painel é preto ou transparente.
         */
        GamePanel(int cornerRadius, boolean colorIsBlack) {
            this(cornerRadius, colorIsBlack, false, (double) 0.0, Nave.NAVE_EASY);
        }

        /**
         * Construtor que cria um painel de jogo com informação da nave. O
         * construtor assume que a nave é para ser desenhada, ou seja que está
         * activa.
         *
         * @param cornerRadius Constante que define qual o raio se utiliza para
         * arredondondar os cantos.
         * @param colorIsBlack Constante adicional utilizado para definir se o
         * painel é preto ou transparente.
         * @param shipHeightPercent Percentagem da altura do painel a que se
         * encontra o desenho da nave, a contar vindo de cima.
         * @param shipType Variável a partir do qual se sabe qual a nave a
         * desenhar.
         */
        GamePanel(int cornerRadius, boolean colorIsBlack, double shipHeightPercent, int shipType) {
            this(cornerRadius, colorIsBlack, true, shipHeightPercent, shipType);
        }

        /**
         * Construtor que é chamado a partir dos outros e possivelmente se
         * quisesse inserir os dados para desenhar a nave mas não a activar. É
         * neste construtor que são inicializados as variáveis.
         *
         * @param cornerRadius Constante que define qual o raio se utiliza para
         * arredondondar os cantos.
         * @param colorIsBlack Constante adicional utilizado para definir se o
         * painel é preto ou transparente.
         * @param shipIsActivated Variável utilizada para definir se o painel de
         * jogo tem a nave desenhada.
         * @param shipHeightPercent Percentagem da altura do painel a que se
         * encontra o desenho da nave, a contar vindo de cima.
         * @param shipType Variável a partir do qual se sabe qual a nave a
         * desenhar.
         */
        GamePanel(int cornerRadius, boolean colorIsBlack, boolean shipIsActivated, double shipHeightPercent, int shipType) {
            super(cornerRadius, colorIsBlack);
            this.shipIsActivated = shipIsActivated;
            this.shipType = shipType;
            this.shipHeightPercent = shipHeightPercent;
        }

        /**
         * Utiliza-se este método para, conforme o tipo de nave definido na
         * instância desta classe, desenhar a respetiva classe.
         *
         * @param g Parâmetro que vai buscar do <code>paintComponent()</code>
         * para desenhar a nave.
         */
        private void designNave(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            if (shipType == Nave.NAVE_VERY_HARD) {
                //Desenho Principal:
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2)), (int) ((this.getHeight() * shipHeightPercent) + 1), 6, 30);
                g2.drawOval((int) ((this.getWidth() / 2)), (int) ((this.getHeight() * shipHeightPercent) + 1), 6, 30);

                g2.setColor(Color.DARK_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2) + 2), (int) ((this.getHeight() * shipHeightPercent) + 4), 2, 20);
                g2.drawOval((int) ((this.getWidth() / 2) + 2), (int) ((this.getHeight() * shipHeightPercent) + 4), 2, 20);

                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2) - 1), (int) ((this.getHeight() * shipHeightPercent) + 9), 8, 30);
                g2.drawOval((int) ((this.getWidth() / 2) - 1), (int) ((this.getHeight() * shipHeightPercent) + 9), 8, 30);
                g2.fillRoundRect((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent) + 10), 4, 30, 5, 5);
                g2.drawRoundRect((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent) + 10), 4, 30, 5, 5);

                g2.setColor(Color.DARK_GRAY);
                g2.drawRoundRect((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent) + 17), 4, 10, 5, 5);

                //Arma Direita:
                g2.setColor(Color.GRAY);
                g2.drawRect((int) ((this.getWidth() / 2) + 24), (int) ((this.getHeight() * shipHeightPercent) + 14), 3, 2);
                g2.fillRoundRect((int) ((this.getWidth() / 2) + 25), (int) ((this.getHeight() * shipHeightPercent)), 2, 16, 2, 2);

                //Asa Direita:
                g2.setColor(Color.RED);
                g2.drawRoundRect((int) ((this.getWidth() / 2) + 8), (int) ((this.getHeight() * shipHeightPercent) + 12), 4, 3, 2, 2);
                g2.fillRoundRect((int) ((this.getWidth() / 2) + 8), (int) ((this.getHeight() * shipHeightPercent) + 12), 4, 3, 2, 2);
                g2.drawRoundRect((int) ((this.getWidth() / 2) + 24), (int) ((this.getHeight() * shipHeightPercent) + 15), 4, 3, 2, 2);
                g2.fillRoundRect((int) ((this.getWidth() / 2) + 24), (int) ((this.getHeight() * shipHeightPercent) + 15), 4, 3, 2, 2);

                g2.setColor(Color.LIGHT_GRAY);
                GeneralPath gp = new GeneralPath();
                gp.moveTo((float) ((this.getWidth() / 2) + 8), (float) ((this.getHeight() * shipHeightPercent) + 14));
                gp.lineTo((float) ((this.getWidth() / 2) + 25), (float) (this.getHeight() * shipHeightPercent) + 15);
                gp.lineTo((float) ((this.getWidth() / 2) + 25), (float) (this.getHeight() * shipHeightPercent) + 19);
                gp.lineTo((float) ((this.getWidth() / 2) + 8), (float) (this.getHeight() * shipHeightPercent) + 23);
                gp.closePath();
                g2.draw(gp);
                g2.fill(gp);

                g2.setColor(Color.GRAY);
                g2.drawRect((int) ((this.getWidth() / 2) + 9), (int) ((this.getHeight() * shipHeightPercent) + 17), 2, 8);
                g2.fillRoundRect((int) ((this.getWidth() / 2) + 9), (int) ((this.getHeight() * shipHeightPercent) + 17), 2, 10, 2, 2);

                //Arma Esquerda:
                g2.setColor(Color.GRAY);
                g2.drawRect((int) ((this.getWidth() / 2) - 20), (int) ((this.getHeight() * shipHeightPercent) + 14), 3, 2);
                g2.fillRoundRect((int) ((this.getWidth() / 2) - 19), (int) ((this.getHeight() * shipHeightPercent)), 2, 16, 2, 2);

                //Asa Esquerda:
                g2.setColor(Color.RED);
                g2.drawRoundRect((int) ((this.getWidth() / 2) - 6), (int) ((this.getHeight() * shipHeightPercent) + 12), 4, 3, 2, 2);
                g2.fillRoundRect((int) ((this.getWidth() / 2) - 6), (int) ((this.getHeight() * shipHeightPercent) + 12), 4, 3, 2, 2);
                g2.drawRoundRect((int) ((this.getWidth() / 2) - 22), (int) ((this.getHeight() * shipHeightPercent) + 15), 4, 3, 2, 2);
                g2.fillRoundRect((int) ((this.getWidth() / 2) - 22), (int) ((this.getHeight() * shipHeightPercent) + 15), 4, 3, 2, 2);

                g2.setColor(Color.LIGHT_GRAY);
                gp = new GeneralPath();
                gp.moveTo((float) ((this.getWidth() / 2) - 2), (float) ((this.getHeight() * shipHeightPercent) + 14));
                gp.lineTo((float) ((this.getWidth() / 2) - 19), (float) (this.getHeight() * shipHeightPercent) + 15);
                gp.lineTo((float) ((this.getWidth() / 2) - 19), (float) (this.getHeight() * shipHeightPercent) + 19);
                gp.lineTo((float) ((this.getWidth() / 2) - 2), (float) (this.getHeight() * shipHeightPercent) + 23);
                gp.closePath();
                g2.draw(gp);
                g2.fill(gp);

                g2.setColor(Color.GRAY);
                g2.drawRect((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 17), 2, 8);
                g2.fillRoundRect((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 17), 2, 10, 2, 2);

                //Details:
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect((int) ((this.getWidth() / 2) + 8), (int) ((this.getHeight() * shipHeightPercent) + 14), 4, 3);
                g2.drawRect((int) ((this.getWidth() / 2) - 6), (int) ((this.getHeight() * shipHeightPercent) + 14), 4, 3);

                g2.setColor(Color.GRAY);
                gp = new GeneralPath();
                gp.moveTo((float) ((this.getWidth() / 2) + 5), (float) ((this.getHeight() * shipHeightPercent) + 27));
                gp.lineTo((float) ((this.getWidth() / 2) + 6), (float) ((this.getHeight() * shipHeightPercent) + 29));
                gp.lineTo((float) ((this.getWidth() / 2) + 4), (float) ((this.getHeight() * shipHeightPercent) + 40));
                gp.closePath();
                g2.draw(gp);

                gp = new GeneralPath();
                gp.moveTo((float) ((this.getWidth() / 2) + 1), (float) ((this.getHeight() * shipHeightPercent) + 27));
                gp.lineTo((float) ((this.getWidth() / 2)), (float) ((this.getHeight() * shipHeightPercent) + 29));
                gp.lineTo((float) ((this.getWidth() / 2) + 2), (float) ((this.getHeight() * shipHeightPercent) + 40));
                gp.closePath();
                g2.draw(gp);

            } else if (shipType == Nave.NAVE_HARD) {
                //Desenho Principal: 
                g2.setColor(Color.GRAY);
                g2.fillRect((int) ((this.getWidth() / 2) + 7), (int) ((this.getHeight() * shipHeightPercent) + 8), 7, 2);
                g2.drawRect((int) ((this.getWidth() / 2) + 7), (int) ((this.getHeight() * shipHeightPercent) + 8), 7, 2);

                GeneralPath gp = new GeneralPath();
                gp.moveTo((float) ((this.getWidth() / 2) - 5), (float) ((this.getHeight() * shipHeightPercent) + 10));
                gp.lineTo((float) ((this.getWidth() / 2) - 1), (float) (this.getHeight() * shipHeightPercent));
                gp.lineTo((float) ((this.getWidth() / 2) + 4), (float) (this.getHeight() * shipHeightPercent));
                gp.lineTo((float) ((this.getWidth() / 2) + 4), (float) (this.getHeight() * shipHeightPercent) + 22);
                gp.lineTo((float) ((this.getWidth() / 2) - 5), (float) ((this.getHeight() * shipHeightPercent) + 22));
                gp.closePath();
                g2.draw(gp);
                g2.fill(gp);

                gp = new GeneralPath();
                gp.moveTo((float) ((this.getWidth() / 2) + 26), ((float) (this.getHeight() * shipHeightPercent) + 10));
                gp.lineTo((float) ((this.getWidth() / 2) + 22), (float) (this.getHeight() * shipHeightPercent));
                gp.lineTo((float) ((this.getWidth() / 2) + 17), (float) (this.getHeight() * shipHeightPercent));
                gp.lineTo((float) ((this.getWidth() / 2) + 17), (float) (this.getHeight() * shipHeightPercent) + 22);
                gp.lineTo((float) ((this.getWidth() / 2) + 26), (float) ((this.getHeight() * shipHeightPercent) + 22));
                g2.draw(gp);
                g2.fill(gp);

                g2.fillOval((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 10), 30, 30);
                g2.drawOval((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 10), 30, 30);

                //CockPit:
                g2.fillOval((int) ((this.getWidth() / 2) + 28), (int) ((this.getHeight() * shipHeightPercent) + 8), 4, 9);
                g2.drawOval((int) ((this.getWidth() / 2) + 28), (int) ((this.getHeight() * shipHeightPercent) + 8), 4, 9);
                g2.fillOval((int) ((this.getWidth() / 2) + 24), (int) ((this.getHeight() * shipHeightPercent) + 13), 7, 5);
                g2.drawOval((int) ((this.getWidth() / 2) + 24), (int) ((this.getHeight() * shipHeightPercent) + 13), 7, 5);

                //Detalhe Cockpit:
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2) + 29), (int) ((this.getHeight() * shipHeightPercent) + 9), 3, 2);
                g2.drawOval((int) ((this.getWidth() / 2) + 29), (int) ((this.getHeight() * shipHeightPercent) + 9), 3, 2);

                //Details:
                //Bolinhas de baixo:
                g2.setColor(Color.DARK_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2) + 3), (int) ((this.getHeight() * shipHeightPercent) + 31), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 3), (int) ((this.getHeight() * shipHeightPercent) + 31), 3, 3);
                g2.fillOval((int) ((this.getWidth() / 2) + 9), (int) ((this.getHeight() * shipHeightPercent) + 32), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 9), (int) ((this.getHeight() * shipHeightPercent) + 32), 3, 3);
                g2.fillOval((int) ((this.getWidth() / 2) + 15), (int) ((this.getHeight() * shipHeightPercent) + 31), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 15), (int) ((this.getHeight() * shipHeightPercent) + 31), 3, 3);

                //Bolinhas do meio:
                g2.fillOval((int) ((this.getWidth() / 2) + 4), (int) ((this.getHeight() * shipHeightPercent) + 26), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 4), (int) ((this.getHeight() * shipHeightPercent) + 26), 3, 3);
                g2.fillOval((int) ((this.getWidth() / 2) + 9), (int) ((this.getHeight() * shipHeightPercent) + 27), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 9), (int) ((this.getHeight() * shipHeightPercent) + 27), 3, 3);
                g2.fillOval((int) ((this.getWidth() / 2) + 14), (int) ((this.getHeight() * shipHeightPercent) + 26), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 14), (int) ((this.getHeight() * shipHeightPercent) + 26), 3, 3);

                //Bolinhas do lado Esquerdo:
                g2.fillOval((int) ((this.getWidth() / 2)), (int) ((this.getHeight() * shipHeightPercent) + 3), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2)), (int) ((this.getHeight() * shipHeightPercent) + 3), 3, 3);
                g2.fillOval((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent) + 13), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent) + 13), 3, 3);

                //Bolinhas do lado Direito:
                g2.fillOval((int) ((this.getWidth() / 2) + 18), (int) ((this.getHeight() * shipHeightPercent) + 3), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 18), (int) ((this.getHeight() * shipHeightPercent) + 3), 3, 3);
                g2.fillOval((int) ((this.getWidth() / 2) + 17), (int) ((this.getHeight() * shipHeightPercent) + 13), 3, 3);
                g2.drawOval((int) ((this.getWidth() / 2) + 17), (int) ((this.getHeight() * shipHeightPercent) + 13), 3, 3);

                //Circulo Central:
                g2.drawOval((int) ((this.getWidth() / 2) + 7), (int) ((this.getHeight() * shipHeightPercent) + 17), 7, 7);

            } else if (shipType == Nave.NAVE_MEDIUM) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval((int) (this.getWidth() / 2), (int) (this.getHeight() * shipHeightPercent), 14, 40);
                g2.drawOval((int) (this.getWidth() / 2), (int) (this.getHeight() * shipHeightPercent), 14, 40);
                g2.fillOval((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 30), 5, 20);
                g2.drawOval((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 30), 5, 20);
                g2.fillOval((int) ((this.getWidth() / 2) + 15), (int) ((this.getHeight() * shipHeightPercent) + 30), 5, 20);
                g2.drawOval((int) ((this.getWidth() / 2) + 15), (int) ((this.getHeight() * shipHeightPercent) + 30), 5, 20);
                g2.fillOval((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 30), 25, 15);
                g2.drawOval((int) ((this.getWidth() / 2) - 5), (int) ((this.getHeight() * shipHeightPercent) + 30), 25, 15);
                g2.setColor(Color.WHITE);
                g2.fillOval((int) ((this.getWidth() / 2) + 3), (int) ((this.getHeight() * shipHeightPercent) + 15), 8, 2);
                g2.drawOval((int) ((this.getWidth() / 2) + 3), (int) ((this.getHeight() * shipHeightPercent) + 15), 8, 2);

            } else {
                g2.setColor(Color.YELLOW);
                g2.fillOval((int) (this.getWidth() / 2), (int) (this.getHeight() * shipHeightPercent), 9, 40);
                g2.drawOval((int) (this.getWidth() / 2), (int) (this.getHeight() * shipHeightPercent), 9, 40);
                g2.fillOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 25);
                g2.drawOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 25);
                g2.fillOval((int) ((this.getWidth() / 2) + 12), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 25);
                g2.drawOval((int) ((this.getWidth() / 2) + 12), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 25);
                g2.fillOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 10), 25, 5);
                g2.drawOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 10), 25, 5);

                g2.setColor(Color.LIGHT_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent)), 7, 10);
                g2.drawOval((int) ((this.getWidth() / 2) + 1), (int) ((this.getHeight() * shipHeightPercent)), 7, 10);
                g2.fillOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 10);
                g2.drawOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 10);
                g2.fillOval((int) ((this.getWidth() / 2) + 12), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 10);
                g2.drawOval((int) ((this.getWidth() / 2) + 12), (int) ((this.getHeight() * shipHeightPercent) + 3), 5, 10);
                g2.fillOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 7), 25, 2);
                g2.drawOval((int) ((this.getWidth() / 2) - 8), (int) ((this.getHeight() * shipHeightPercent) + 7), 25, 2);

                g2.setColor(Color.DARK_GRAY);
                g2.fillOval((int) ((this.getWidth() / 2) + 2), (int) ((this.getHeight() * shipHeightPercent) + 6), 5, 14);
                g2.drawOval((int) ((this.getWidth() / 2) + 2), (int) ((this.getHeight() * shipHeightPercent) + 6), 5, 14);

            }
        }

        /**
         * Quando é chamado este método o painel actualiza o seu desenho
         * conforme o valor dos parâmetros.
         *
         * @param shipHeightPercent Percentagem da altura do painel a que se
         * encontra o desenho da nave, a contar vindo de cima.
         * @param shipType Variável a partir do qual se sabe qual a nave a
         * desenhar.
         */
        public void resetPainting(double shipHeightPercent, int shipType) {
            shipIsActivated = true;
            this.shipType = shipType;
            if (shipType == Nave.NAVE_MEDIUM) {
                this.shipHeightPercent = 0.912 * shipHeightPercent;
            } else {
                this.shipHeightPercent = 0.93 * shipHeightPercent;
            }
            repaint();
        }

        /**
         * O método
         * <code>paintComponent(Graphics g)</code> overriden. Vai buscar o
         * método da classe RoundedPanel e se a nave estiver activa chama o
         * método que desenha a nave.
         *
         * @param g Parâmetro base do metodo <code>paintComponent()</code>.
         * @see JPanel#paintComponent(java.awt.Graphics)
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (shipIsActivated) {

                designNave(g);

            }
        }
    }

    /**
     * Recebe o titulo do nivel e actualiza o DashBoard da GUI.
     */
    public void updateGUIDashBoardTitle() {
        guiDashBoard.TítuloNivel.setText(nivelJogado.LevelTitleToString());
    }

    /**
     * Recebe os valores do DashBoard e actualiza o painel.
     *
     * @param altitude Altitude vigente da nave.
     * @param combustivel Combustível que a nave ainda tem disponível.
     * @param velocidade Velocidade vigente da nave.
     */
    public void updateLevelValues(int altitude, int combustivel, int velocidade) {
        guiDashBoard.altitudeText.setText(Integer.toString(altitude));
        guiDashBoard.combustivelText.setText(Integer.toString(combustivel));
        guiDashBoard.velocidadeText.setText(Integer.toString(velocidade));
        guiDashBoard.velocidadeMaximaText.setText(Integer.toString(nivelJogado.getVelocidadeMaxima()));
    }

    /**
     * Recebe o valor de conclusão do nivel e actualiza a posição da nave no
     * Painel e/ou se mudar de Nave (Dificuldade), muda o desenho da Nave. A
     * nave fica visivel no painel entre, aproximadamente, os valores 0 e 1.
     *
     * @param percentNivel Percentagem da altura a qual a nave está de aterrar,
     * ou seja, finalizar o nivel.
     */
    void updateSpaceShip(double percentNivel) {
        guiAnimation.actionPanel.resetPainting(percentNivel, dificuldadeJogo);

    }
}
