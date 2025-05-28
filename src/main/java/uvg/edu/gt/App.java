package uvg.edu.gt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    // Paleta de colores moderna y elegante
    private static final Color PRIMARY_DARK = new Color(26, 35, 46);
    private static final Color PRIMARY_LIGHT = new Color(52, 73, 93);
    private static final Color ACCENT_BLUE = new Color(74, 144, 226);
    private static final Color ACCENT_PURPLE = new Color(155, 89, 182);
    private static final Color SUCCESS_GREEN = new Color(46, 204, 113);
    private static final Color WARNING_ORANGE = new Color(230, 126, 34);
    private static final Color BACKGROUND_MAIN = new Color(248, 249, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private static final Color BORDER_LIGHT = new Color(236, 240, 241);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnimeRecommenderGUI().setVisible(true);
        });
    }

    static class AnimeRecommenderGUI extends JFrame {
        private List<Anime> allAnimes;
        private BaseDeDatos baseDeDatos;

        // Componentes para recomendaciones
        private JTextField nombreField;
        private Map<JCheckBox, Genero> generoCheckboxes;
        private Map<Anime, JSlider> animeSliders;
        private JTextArea recomendacionesArea;
        private JButton processButton;

        // Componentes para búsqueda
        private JTextField busquedaAnimeField;
        private JTextArea detallesAnimeArea;
        private JButton buscarAnimeButton;

        public AnimeRecommenderGUI() {
            initializeData();
            setupModernGUI();
        }

        private void initializeData() {
            Map<String, Float> caracteristicasNaruto = new HashMap<>();
            caracteristicasNaruto.put("violencia", 0.8f);
            caracteristicasNaruto.put("humor", 0.3f);

            Map<String, Float> caracteristicasKimiNoNaWa = new HashMap<>();
            caracteristicasKimiNoNaWa.put("romance", 0.9f);
            caracteristicasKimiNoNaWa.put("drama", 0.7f);

            Map<String, Float> caracteristicasSAO = new HashMap<>();
            caracteristicasSAO.put("fantasia", 0.9f);
            caracteristicasSAO.put("accion", 0.6f);

            Anime naruto = new Anime("A1", "Naruto", Arrays.asList(Genero.ACCION), caracteristicasNaruto);
            Anime kimiNoNaWa = new Anime("A2", "Kimi no Na wa", Arrays.asList(Genero.ROMANCE), caracteristicasKimiNoNaWa);
            Anime sao = new Anime("A3", "Sword Art Online", Arrays.asList(Genero.FANTASIA), caracteristicasSAO);

            allAnimes = Arrays.asList(naruto, kimiNoNaWa, sao);

            baseDeDatos = new BaseDeDatos();
            Usuario luis = new Usuario("U2", "Luis");
            luis.calificarAnime(naruto, 4);
            luis.calificarAnime(kimiNoNaWa, 3);
            baseDeDatos.agregarRating(luis, naruto, 4);
            baseDeDatos.agregarRating(luis, kimiNoNaWa, 3);
        }

        private void setupModernGUI() {
            setTitle("Sistema de Recomendación de Anime");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1200, 900);
            setLocationRelativeTo(null);
            setResizable(false);

            getContentPane().setBackground(BACKGROUND_MAIN);

            generoCheckboxes = new HashMap<>();
            animeSliders = new HashMap<>();

            createModernComponents();
            layoutModernComponents();
            addWindowShadow();
        }

        private void createModernComponents() {
            nombreField = createModernTextField("Ingrese su nombre aquí...");
            busquedaAnimeField = createModernTextField("Buscar anime por título...");

            recomendacionesArea = new JTextArea(12, 60);
            recomendacionesArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
            recomendacionesArea.setBackground(PRIMARY_DARK);
            recomendacionesArea.setForeground(SUCCESS_GREEN);
            recomendacionesArea.setCaretColor(SUCCESS_GREEN);
            recomendacionesArea.setBorder(new EmptyBorder(20, 20, 20, 20));
            recomendacionesArea.setEditable(false);
            recomendacionesArea.setLineWrap(true);
            recomendacionesArea.setWrapStyleWord(true);

            detallesAnimeArea = new JTextArea(12, 60);
            detallesAnimeArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            detallesAnimeArea.setBackground(PRIMARY_DARK);
            detallesAnimeArea.setForeground(new Color(255, 215, 0));
            detallesAnimeArea.setCaretColor(new Color(255, 215, 0));
            detallesAnimeArea.setBorder(new EmptyBorder(20, 20, 20, 20));
            detallesAnimeArea.setEditable(false);
            detallesAnimeArea.setLineWrap(true);
            detallesAnimeArea.setWrapStyleWord(true);

            processButton = createGradientButton("GENERAR RECOMENDACIONES");
            processButton.addActionListener(new ProcessRecommendationsListener());

            buscarAnimeButton = createSecondaryButton("BUSCAR ANIME");
            buscarAnimeButton.addActionListener(new BuscarAnimeListener());
        }

        private JTextField createModernTextField(String placeholder) {
            JTextField field = new JTextField(25) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(CARD_BACKGROUND);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                    g2.setColor(BORDER_LIGHT);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            field.setOpaque(false);
            field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            field.setForeground(TEXT_PRIMARY);
            field.setBorder(new EmptyBorder(15, 20, 15, 20));
            field.setToolTipText(placeholder);

            return field;
        }

        private JButton createSecondaryButton(String text) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    GradientPaint gradient = new GradientPaint(
                            0, 0, WARNING_ORANGE,
                            getWidth(), getHeight(), new Color(231, 76, 60)
                    );
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(new Dimension(200, 45));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setFocusPainted(false);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setFont(new Font("Segoe UI", Font.BOLD, 15));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
                }
            });

            return button;
        }

        private JButton createGradientButton(String text) {
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    GradientPaint gradient = new GradientPaint(
                            0, 0, ACCENT_BLUE,
                            getWidth(), getHeight(), ACCENT_PURPLE
                    );
                    g2.setPaint(gradient);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 23, 23);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setPreferredSize(new Dimension(300, 55));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setFocusPainted(false);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setFont(new Font("Segoe UI", Font.BOLD, 17));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setFont(new Font("Segoe UI", Font.BOLD, 16));
                }
            });

            return button;
        }

        private void layoutModernComponents() {
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new BorderLayout(0, 30));
            mainPanel.setBackground(BACKGROUND_MAIN);
            mainPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

            JPanel headerPanel = createModernHeader();

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(BACKGROUND_MAIN);

            contentPanel.add(createUserInfoCard());
            contentPanel.add(Box.createVerticalStrut(25));

            contentPanel.add(createBusquedaAnimeCard());
            contentPanel.add(Box.createVerticalStrut(25));

            contentPanel.add(createGenreCard());
            contentPanel.add(Box.createVerticalStrut(25));

            contentPanel.add(createRatingCard());
            contentPanel.add(Box.createVerticalStrut(30));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(BACKGROUND_MAIN);
            buttonPanel.add(processButton);
            contentPanel.add(buttonPanel);

            contentPanel.add(Box.createVerticalStrut(25));

            contentPanel.add(createResultsCard());

            contentPanel.add(Box.createVerticalStrut(25));

            contentPanel.add(createDetallesAnimeCard());

            mainPanel.add(headerPanel, BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBackground(BACKGROUND_MAIN);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            add(mainPanel);
        }

        private JPanel createModernHeader() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(BACKGROUND_MAIN);
            panel.setPreferredSize(new Dimension(0, 120));

            JLabel titleLabel = new JLabel("SISTEMA DE RECOMENDACIÓN DE ANIME", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    GradientPaint gradient = new GradientPaint(
                            0, 0, ACCENT_BLUE,
                            getWidth(), 0, ACCENT_PURPLE
                    );
                    g2.setPaint(gradient);

                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2;
                    g2.drawString(getText(), x, y);

                    g2.dispose();
                }
            };
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            titleLabel.setPreferredSize(new Dimension(0, 60));

            JLabel subtitleLabel = new JLabel("Descubre tu próximo anime favorito con IA avanzada", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            subtitleLabel.setForeground(TEXT_SECONDARY);

            panel.add(titleLabel, BorderLayout.CENTER);
            panel.add(subtitleLabel, BorderLayout.SOUTH);

            return panel;
        }

        private JPanel createUserInfoCard() {
            JPanel card = createModernCard("Información Personal");

            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
            contentPanel.setBackground(CARD_BACKGROUND);

            JLabel nameLabel = new JLabel("Tu nombre:");
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            nameLabel.setForeground(TEXT_PRIMARY);

            contentPanel.add(nameLabel);
            contentPanel.add(nombreField);

            card.add(contentPanel, BorderLayout.CENTER);
            return card;
        }

        private JPanel createBusquedaAnimeCard() {
            JPanel card = createModernCard("Búsqueda Detallada de Anime");

            JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
            contentPanel.setBackground(CARD_BACKGROUND);

            JLabel searchLabel = new JLabel("Nombre del anime:");
            searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            searchLabel.setForeground(TEXT_PRIMARY);

            contentPanel.add(searchLabel);
            contentPanel.add(busquedaAnimeField);
            contentPanel.add(buscarAnimeButton);

            card.add(contentPanel, BorderLayout.CENTER);
            return card;
        }

        private JPanel createGenreCard() {
            JPanel card = createModernCard("Selecciona tus Géneros Favoritos");

            JPanel contentPanel = new JPanel(new GridLayout(2, 3, 15, 15));
            contentPanel.setBackground(CARD_BACKGROUND);
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

            String[] genreNames = {"Acción", "Comedia", "Romance", "Fantasía", "Slice of Life"};
            Genero[] generos = {Genero.ACCION, Genero.COMEDIA, Genero.ROMANCE, Genero.FANTASIA, Genero.SLICE_OF_LIFE};
            Color[] genreColors = {WARNING_ORANGE, SUCCESS_GREEN, new Color(231, 76, 60), ACCENT_PURPLE, new Color(241, 196, 15)};

            for (int i = 0; i < genreNames.length; i++) {
                JCheckBox checkbox = createModernCheckbox(genreNames[i], genreColors[i]);
                generoCheckboxes.put(checkbox, generos[i]);
                contentPanel.add(checkbox);
            }

            if (genreNames.length % 2 != 0) {
                contentPanel.add(new JPanel());
            }

            card.add(contentPanel, BorderLayout.CENTER);
            return card;
        }

        private JCheckBox createModernCheckbox(String text, Color accentColor) {
            JCheckBox checkbox = new JCheckBox(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isRollover()) {
                        g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    }

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            checkbox.setOpaque(false);
            checkbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            checkbox.setForeground(TEXT_PRIMARY);
            checkbox.setBorder(new EmptyBorder(12, 15, 12, 15));
            checkbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            checkbox.setFocusPainted(false);

            return checkbox;
        }

        private JPanel createRatingCard() {
            JPanel card = createModernCard("Califica estos Animes (0-10)");

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(CARD_BACKGROUND);
            contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

            for (Anime anime : allAnimes) {
                JPanel animePanel = createModernAnimeRatingPanel(anime);
                contentPanel.add(animePanel);
                contentPanel.add(Box.createVerticalStrut(20));
            }

            card.add(contentPanel, BorderLayout.CENTER);
            return card;
        }

        private JPanel createModernAnimeRatingPanel(Anime anime) {
            JPanel panel = new JPanel(new BorderLayout(20, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(new Color(248, 249, 250));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(BORDER_LIGHT);
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            panel.setOpaque(false);
            panel.setBorder(new EmptyBorder(20, 25, 20, 25));
            panel.setPreferredSize(new Dimension(0, 80));

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(anime.getNombre());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLabel.setForeground(TEXT_PRIMARY);

            JLabel genreLabel = new JLabel("(" + String.join(", ", anime.getGeneros()) + ")");
            genreLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            genreLabel.setForeground(TEXT_SECONDARY);

            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(genreLabel);

            JSlider slider = createModernSlider();
            animeSliders.put(anime, slider);

            JLabel valueLabel = new JLabel("0") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(ACCENT_BLUE);
                    g2.fillOval(5, 5, 30, 30);

                    g2.setColor(Color.WHITE);
                    FontMetrics fm = g2.getFontMetrics();
                    String text = getText();
                    int x = (getWidth() - fm.stringWidth(text)) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2 - 2;
                    g2.drawString(text, x, y);

                    g2.dispose();
                }
            };
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            valueLabel.setPreferredSize(new Dimension(40, 40));
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

            slider.addChangeListener(e -> valueLabel.setText(String.valueOf(slider.getValue())));

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
            rightPanel.setOpaque(false);
            rightPanel.add(slider);
            rightPanel.add(valueLabel);

            panel.add(infoPanel, BorderLayout.WEST);
            panel.add(rightPanel, BorderLayout.EAST);

            return panel;
        }

        private JSlider createModernSlider() {
            JSlider slider = new JSlider(0, 10, 0);
            slider.setOpaque(false);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setMajorTickSpacing(2);
            slider.setMinorTickSpacing(1);
            slider.setPreferredSize(new Dimension(250, 40));
            slider.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            slider.setForeground(TEXT_SECONDARY);

            return slider;
        }

        private JPanel createResultsCard() {
            JPanel card = createModernCard("Tus Recomendaciones Personalizadas");

            JScrollPane scrollPane = new JScrollPane(recomendacionesArea);
            scrollPane.setBorder(null);
            scrollPane.setBackground(PRIMARY_DARK);
            scrollPane.getViewport().setBackground(PRIMARY_DARK);

            card.add(scrollPane, BorderLayout.CENTER);
            return card;
        }

        private JPanel createDetallesAnimeCard() {
            JPanel card = createModernCard("Información Detallada del Anime");

            JScrollPane scrollPane = new JScrollPane(detallesAnimeArea);
            scrollPane.setBorder(null);
            scrollPane.setBackground(PRIMARY_DARK);
            scrollPane.getViewport().setBackground(PRIMARY_DARK);

            card.add(scrollPane, BorderLayout.CENTER);
            return card;
        }

        private JPanel createModernCard(String title) {
            JPanel card = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2.setColor(new Color(0, 0, 0, 10));
                    g2.fillRoundRect(4, 4, getWidth()-4, getHeight()-4, 20, 20);

                    g2.setColor(CARD_BACKGROUND);
                    g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 20, 20);

                    g2.setColor(BORDER_LIGHT);
                    g2.drawRoundRect(0, 0, getWidth()-5, getHeight()-5, 20, 20);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            card.setOpaque(false);
            card.setBorder(new EmptyBorder(5, 5, 5, 5));

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            headerPanel.setBackground(CARD_BACKGROUND);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(TEXT_PRIMARY);
            titleLabel.setBorder(new EmptyBorder(15, 20, 10, 20));

            headerPanel.add(titleLabel);
            card.add(headerPanel, BorderLayout.NORTH);

            return card;
        }

        private void addWindowShadow() {
            getRootPane().putClientProperty("apple.awt.windowShadow", Boolean.TRUE);
        }

        private class BuscarAnimeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreAnime = busquedaAnimeField.getText().trim();
                if (nombreAnime.isEmpty()) {
                    showModernDialog("Atención", "Por favor, ingresa el nombre del anime a buscar.", WARNING_ORANGE);
                    return;
                }

                Optional<Map<String, String>> resultado = Busqueda.buscarPorTitulo(nombreAnime);

                if (resultado.isPresent()) {
                    String detallesAnime = formatearResultadoBusqueda(resultado.get());
                    detallesAnimeArea.setText(detallesAnime);
                    showModernDialog("Búsqueda Exitosa", "Anime encontrado en la base de datos!", SUCCESS_GREEN);
                } else {
                    String mensajeError = "═══════════════════════════════════════════════════════════════\n" +
                            "ANIME NO ENCONTRADO: " + nombreAnime.toUpperCase() + "\n" +
                            "═══════════════════════════════════════════════════════════════\n\n" +
                            "El anime '" + nombreAnime + "' no fue encontrado en la base de datos.\n\n" +
                            "Sugerencias:\n" +
                            "- Verifica la ortografía del nombre\n" +
                            "- Prueba con nombres alternativos o en inglés\n" +
                            "- Intenta con palabras clave del título\n" +
                            "- Asegúrate de que el archivo anime.csv esté en el directorio correcto\n\n" +
                            "Ejemplos de búsquedas válidas:\n" +
                            "- 'Naruto'\n" +
                            "- 'Attack on Titan'\n" +
                            "- 'One Piece'\n" +
                            "- 'Death Note'";

                    detallesAnimeArea.setText(mensajeError);
                    showModernDialog("Anime No Encontrado", "No se encontró el anime en la base de datos.", WARNING_ORANGE);
                }
            }
        }

        private String formatearResultadoBusqueda(Map<String, String> info) {
            StringBuilder sb = new StringBuilder();

            String titulo = info.getOrDefault("title", "ANIME ENCONTRADO");

            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append("INFORMACIÓN DETALLADA: ").append(titulo.toUpperCase()).append("\n");
            sb.append("═══════════════════════════════════════════════════════════════\n\n");

            String[] camposImportantes = {
                    "anime_id", "anime_url", "title", "synopsis", "main_pic",
                    "type", "source_type", "num_episodes", "status", "start_date",
                    "end_date", "season", "studios", "genres", "score", "score_count",
                    "score_rank", "popularity_rank", "members_count", "favorites_count",
                    "watching_count", "completed_count", "on_hold_count", "dropped_count",
                    "plan_to_watch_count", "total_count"
            };

            for (String campo : camposImportantes) {
                if (info.containsKey(campo)) {
                    String valor = info.get(campo);
                    if (valor != null && !valor.trim().isEmpty()) {
                        if (campo.equals("synopsis") && valor.length() > 150) {
                            valor = valor.substring(0, 150) + "...";
                        }
                        sb.append(String.format("%-20s: %s\n", campo.toUpperCase(), valor));
                    }
                }
            }

            sb.append("\n--- DISTRIBUCIÓN DE PUNTUACIONES ---\n");
            String[] scoreFields = {
                    "score_10_count", "score_09_count", "score_08_count", "score_07_count",
                    "score_06_count", "score_05_count", "score_04_count", "score_03_count",
                    "score_02_count", "score_01_count"
            };

            for (String scoreField : scoreFields) {
                if (info.containsKey(scoreField)) {
                    String valor = info.get(scoreField);
                    if (valor != null && !valor.trim().isEmpty()) {
                        sb.append(String.format("%-20s: %s\n", scoreField.toUpperCase(), valor));
                    }
                }
            }

            sb.append("\n--- INFORMACIÓN ADICIONAL ---\n");
            if (info.containsKey("clubs")) {
                sb.append(String.format("%-20s: %s\n", "CLUBS", info.get("clubs")));
            }
            if (info.containsKey("pics")) {
                sb.append(String.format("%-20s: %s\n", "PICS", info.get("pics")));
            }

            sb.append("\n═══════════════════════════════════════════════════════════════\n");
            sb.append("DATOS OBTENIDOS DE: anime.csv\n");
            sb.append("SISTEMA DE BÚSQUEDA REAL v2.0 - Powered by Busqueda.java\n");
            sb.append("═══════════════════════════════════════════════════════════════");

            return sb.toString();
        }

        private class ProcessRecommendationsListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                processButton.setText("PROCESANDO...");
                processButton.setEnabled(false);

                SwingUtilities.invokeLater(() -> {
                    try {
                        Thread.sleep(500);

                        String nombre = nombreField.getText().trim();
                        if (nombre.isEmpty()) {
                            showModernDialog("Atención", "Por favor, ingresa tu nombre para continuar.", WARNING_ORANGE);
                            resetButton();
                            return;
                        }

                        Usuario usuario = new Usuario("U" + (baseDeDatos.getRatings().size() + 1), nombre);

                        for (Map.Entry<JCheckBox, Genero> entry : generoCheckboxes.entrySet()) {
                            if (entry.getKey().isSelected()) {
                                usuario.agregarPreferencia(entry.getValue(), 5);
                            }
                        }

                        boolean hasRatings = false;
                        for (Map.Entry<Anime, JSlider> entry : animeSliders.entrySet()) {
                            Anime anime = entry.getKey();
                            int rating = entry.getValue().getValue();

                            if (rating == 0) continue;
                            if (rating < 0 || rating > 10) continue;

                            usuario.calificarAnime(anime, rating);
                            baseDeDatos.agregarRating(usuario, anime, rating);
                            hasRatings = true;
                        }

                        if (!hasRatings) {
                            showModernDialog("Atención", "Por favor, califica al menos un anime para obtener recomendaciones.", WARNING_ORANGE);
                            resetButton();
                            return;
                        }

                        String recommendations = generateModernRecommendations(usuario);
                        recomendacionesArea.setText(recommendations);

                        showModernDialog("Éxito", "Recomendaciones generadas exitosamente!", SUCCESS_GREEN);
                        resetButton();

                    } catch (Exception ex) {
                        resetButton();
                        ex.printStackTrace();
                    }
                });
            }

            private void resetButton() {
                processButton.setText("GENERAR RECOMENDACIONES");
                processButton.setEnabled(true);
            }
        }

        private void showModernDialog(String title, String message, Color accentColor) {
            JDialog dialog = new JDialog(this, title, true);
            dialog.setSize(400, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);

            JPanel panel = new JPanel(new BorderLayout(20, 20));
            panel.setBackground(CARD_BACKGROUND);
            panel.setBorder(new EmptyBorder(30, 30, 30, 30));

            JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
            messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            messageLabel.setForeground(TEXT_PRIMARY);

            JButton okButton = new JButton("OK");
            okButton.setBackground(accentColor);
            okButton.setForeground(Color.WHITE);
            okButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            okButton.setBorder(new EmptyBorder(10, 20, 10, 20));
            okButton.setFocusPainted(false);
            okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            okButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setBackground(CARD_BACKGROUND);
            buttonPanel.add(okButton);

            panel.add(messageLabel, BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(panel);
            dialog.setVisible(true);
        }

        private String generateModernRecommendations(Usuario usuario) {
            StringBuilder sb = new StringBuilder();

            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append("RECOMENDACIONES PERSONALIZADAS PARA: ").append(usuario.getNombre().toUpperCase()).append("\n");
            sb.append("═══════════════════════════════════════════════════════════════\n\n");

            RecomendadorColaborativo rc = new RecomendadorColaborativo(0.5f, baseDeDatos);
            Map<Usuario, Float> similarUsers = new HashMap<>();
            for (Usuario otherUser : baseDeDatos.getRatings().keySet()) {
                if (!otherUser.getId().equals(usuario.getId())) {
                    float similitud = rc.calcularSimilaridadUsuarios(usuario, otherUser);
                    if (similitud >= 0.5f) {
                        similarUsers.put(otherUser, similitud);
                    }
                }
            }

            Map<String, Float> pesosCaracteristicas = new HashMap<>();
            pesosCaracteristicas.put("violencia", 1.0f);
            pesosCaracteristicas.put("humor", 0.5f);
            pesosCaracteristicas.put("romance", 1.0f);
            pesosCaracteristicas.put("drama", 0.8f);
            pesosCaracteristicas.put("fantasia", 0.7f);
            pesosCaracteristicas.put("accion", 0.9f);

            RecomendadorContenido rcont = new RecomendadorContenido(pesosCaracteristicas);
            Map<Anime, Float> similarAnimes = new HashMap<>();
            for (Anime ratedAnime : usuario.getCalificaciones().keySet()) {
                if (usuario.getCalificaciones().get(ratedAnime) >= 4) {
                    for (Anime otherAnime : allAnimes) {
                        if (!otherAnime.getId().equals(ratedAnime.getId()) && !usuario.getCalificaciones().containsKey(otherAnime)) {
                            float similitud = rcont.calcularSimilaridadAnimes(ratedAnime, otherAnime);
                            if (similitud > 0.3f) {
                                similarAnimes.put(otherAnime, similitud);
                            }
                        }
                    }
                }
            }

            if (!similarUsers.isEmpty()) {
                sb.append("RECOMENDACIONES COLABORATIVAS\n");
                sb.append("Basado en usuarios con gustos similares:\n\n");
                for (Map.Entry<Usuario, Float> entry : similarUsers.entrySet()) {
                    Usuario similarUser = entry.getKey();
                    sb.append("Usuario similar: ").append(similarUser.getNombre())
                            .append(" (similitud: ").append(String.format("%.1f%%", entry.getValue() * 100)).append(")\n");

                    for (Map.Entry<Anime, Integer> rating : similarUser.getCalificaciones().entrySet()) {
                        Anime anime = rating.getKey();
                        if (!usuario.getCalificaciones().containsKey(anime)) {
                            sb.append("   ").append(anime.getNombre())
                                    .append(" - Rating: ").append(rating.getValue()).append("/10\n");
                            sb.append("      Géneros: ").append(String.join(", ", anime.getGeneros())).append("\n");
                        }
                    }
                    sb.append("\n");
                }
            } else {
                sb.append("RECOMENDACIONES COLABORATIVAS\n");
                sb.append("No se encontraron usuarios con gustos suficientemente similares.\n");
                sb.append("   Tip: Califica más animes para encontrar usuarios afines!\n\n");
            }

            sb.append("───────────────────────────────────────────────────────────────\n\n");

            if (!similarAnimes.isEmpty()) {
                sb.append("RECOMENDACIONES POR CONTENIDO\n");
                sb.append("Basado en animes similares a los que te gustaron:\n\n");
                for (Map.Entry<Anime, Float> entry : similarAnimes.entrySet()) {
                    Anime anime = entry.getKey();
                    sb.append(anime.getNombre()).append("\n");
                    sb.append("   Similitud: ").append(String.format("%.1f%%", entry.getValue() * 100)).append("\n");
                    sb.append("   Géneros: ").append(String.join(", ", anime.getGeneros())).append("\n\n");
                }
            } else {
                sb.append("RECOMENDACIONES POR CONTENIDO\n");
                sb.append("No se encontraron animes con características suficientemente similares.\n");
                sb.append("   Tip: Califica con puntuaciones más altas (4+) para mejores resultados!\n\n");
            }

            sb.append("═══════════════════════════════════════════════════════════════\n");
            sb.append("SISTEMA DE IA AVANZADA - PRECISION ALGORITHM v2.0\n");
            sb.append("Disfruta tus nuevas recomendaciones de anime!\n");
            sb.append("═══════════════════════════════════════════════════════════════");

            return sb.toString();
        }
    }
}