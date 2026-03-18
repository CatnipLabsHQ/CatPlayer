import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class PlayerGUI {

    static JFrame mainFrame;
    static JList songList;
    static DefaultListModel<String> songListModel;
    static JToolBar toolBar;
    static JButton playButton;
    static JButton addSongButton;
    static JButton removeSongButton;

    /* AI Help Part !!!!!!! i dident code this */
    static String playlistFile = "playlist.txt";

    public static void main(String[] args) {
        mainWindow();
    }

    public static  void mainWindow(){
        mainFrame = new JFrame();
        songList = new JList();
        songListModel = new DefaultListModel<>();
        toolBar = new JToolBar();
        playButton = new JButton();
        addSongButton = new JButton();
        removeSongButton = new JButton();

        // mainFrame Configuration
        mainFrame.setTitle("Cat Player");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800,600);

        // Glue Configuration Sticks Things Together
        mainFrame.add(songList, BorderLayout.NORTH);
        mainFrame.add(toolBar, BorderLayout.SOUTH);

        songList.setModel(songListModel);

        /* AI Help Part !!!!!!! i dident code this */
        loadPlaylist();

        toolBar.add(playButton);
        toolBar.add(addSongButton);
        toolBar.add(removeSongButton);

        // songList Configuration
        songList.setVisible(true);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // AI Help Part !!!!!!! i dident code this
        songList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof String) {
                    setText(new File((String) value).getName());
                }
                return this;
            }
        });

        // toolbar Configuration
        toolBar.setBackground(Color.LIGHT_GRAY);
        toolBar.setVisible(true);

        // PlayButton Configuration
        playButton.setText("Play");
        playButton.setBackground(Color.gray);
        playButton.addActionListener(e -> playSong());
        playButton.setVisible(true);

        // addSongButton configuration
        addSongButton.setText("Add Song");
        addSongButton.setBackground(Color.gray);
        addSongButton.addActionListener(e -> addSong());
        addSongButton.setVisible(true);


        // removeSongButton configuration
        removeSongButton.setText("Remove Song");
        removeSongButton.setBackground(Color.gray);
        removeSongButton.addActionListener(e -> removeSong());
        removeSongButton.setVisible(true);

        /* AI Help Part !!!!!!! i dident code this */
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                savePlaylist();
                System.exit(0);
            }
        });

        mainFrame.setVisible(true);
    }

    public static  void playSong() {
        // AI Help Part !!!!!!! i dident code this

        int selectedIndex = songList.getSelectedIndex();

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Bitte wähle zuerst einen Song aus!",
                    "Kein Song ausgewählt",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String songPath = songListModel.getElementAt(selectedIndex);
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("celluloid", songPath);

        try{
            processBuilder.start();
            System.out.println("Celluloid gestartet mit: " + songPath);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Fehler: Celluloid konnte nicht gestartet werden!\n" +
                            "Stelle sicher, dass Celluloid installiert ist.",
                    "Fehler beim Abspielen",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static  void addSong(){
        JFileChooser songchooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Music", "mp3", "mp4", "webm");


        // Chooser Configuration
        songchooser.setFileFilter(filter);
        int result = songchooser.showOpenDialog(mainFrame);



        // Check if open or close pressed
        if (result == JFileChooser.APPROVE_OPTION){
            File selectedFile = songchooser.getSelectedFile();
            String filePath = selectedFile.getPath();
            songListModel.addElement(filePath);
        }
        else {

        }

    }

    public static  void removeSong(){
        int selectedIndex = songList.getSelectedIndex();

        if (selectedIndex == -1) {
            // Nichts tun oder Fehlermeldung
            return;
        }

        JFrame removeFrame = new JFrame();
        JLabel removeLabel = new JLabel();
        JPanel Panel = new JPanel();
        JButton yesButton = new JButton();
        JButton noButton = new JButton();

        // removeFrame Configuration
        removeFrame.setTitle("Remove Song?");
        removeFrame.setSize(400,200);
        removeFrame.add(BorderLayout.NORTH, removeLabel);
        removeFrame.add(Panel);
        Panel.add(yesButton);
        Panel.add(noButton);


        // RemoveLabel Configuration
        removeLabel.setText("do you want to Remove this Song?");

        // noButton Configuration
        noButton.setText("NO");
        noButton.addActionListener(e -> removeFrame.dispose());


        // yesButton Configuration
        yesButton.setText("YES");
        yesButton.addActionListener(e -> {
            removeFrame.dispose();
            songListModel.remove(selectedIndex);
        });


        removeFrame.setVisible(true);
    }

    /* AI Help Part !!!!!!! i dident code this */
    public static void savePlaylist() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playlistFile))) {
            for (int i = 0; i < songListModel.getSize(); i++) {
                writer.write(songListModel.getElementAt(i));
                writer.newLine();
            }
            System.out.println("Playlist gespeichert!");
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern: " + e.getMessage());
        }
    }

    /* AI Help Part !!!!!!! i dident code this */
    public static void loadPlaylist() {
        File file = new File(playlistFile);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                songListModel.addElement(line);
            }
            System.out.println("Playlist geladen: " + songListModel.getSize() + " Songs");
        } catch (IOException e) {
            System.err.println("Fehler beim Laden: " + e.getMessage());
        }
    }
}