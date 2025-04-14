import javax.swing.*;
import java.sql.*;

public class ManageMovies extends JFrame {
    private int selectedCinema = -1;
    private JLabel selectedLabel;
    private JTextField movieInput;
    private JTextArea movieListArea;

    public ManageMovies() {
        setTitle("Manage Movies");
        setSize(450, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel heading = new JLabel("Select a Cinema:");
        heading.setBounds(30, 10, 200, 30);
        add(heading);

        // Cinema Buttons
        for (int i = 0; i < 5; i++) {
            int index = i;
            JButton cinemaBtn = new JButton("Cinema " + (i + 1));
            cinemaBtn.setBounds(30 + (i * 80), 50, 75, 30);
            cinemaBtn.addActionListener(e -> selectCinema(index));
            add(cinemaBtn);
        }

        selectedLabel = new JLabel("Selected Cinema: None");
        selectedLabel.setBounds(30, 90, 300, 30);
        add(selectedLabel);

        movieInput = new JTextField();
        movieInput.setBounds(30, 130, 200, 30);
        add(movieInput);

        // Action buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(250, 130, 70, 30);
        addBtn.addActionListener(e -> addMovie());

        JButton editBtn = new JButton("Edit");
        editBtn.setBounds(330, 130, 70, 30);
        editBtn.addActionListener(e -> editMovie());

        JButton deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(250, 170, 150, 30);
        deleteBtn.addActionListener(e -> deleteMovie());

        add(addBtn);
        add(editBtn);
        add(deleteBtn);

        movieListArea = new JTextArea();
        movieListArea.setEditable(false);
        movieListArea.setBounds(30, 220, 370, 100);
        add(movieListArea);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(350, 330, 70, 30);
        backBtn.addActionListener(e -> dispose());
        add(backBtn);

        refreshMovieList();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void selectCinema(int index) {
        selectedCinema = index;
        String movie = getMovieFromDatabase(index + 1);
        selectedLabel.setText("Selected Cinema: " + (index + 1) + " — " + (movie.isEmpty() ? "[EMPTY]" : movie));
        movieInput.setText(movie);
    }

    private void addMovie() {
        if (selectedCinema == -1) {
            showMessage("Please select a cinema first.");
            return;
        }

        String title = movieInput.getText().trim();
        if (!title.isEmpty()) {
            addMovieToDatabase(selectedCinema + 1, title);
            showMessage("Movie added!");
            refreshMovieList();
            selectCinema(selectedCinema); // update label
        } else {
            showMessage("Please enter a movie title.");
        }
    }

    private void editMovie() {
        if (selectedCinema == -1) {
            showMessage("Please select a cinema first.");
            return;
        }

        String title = movieInput.getText().trim();
        if (!title.isEmpty()) {
            updateMovieInDatabase(selectedCinema + 1, title);
            showMessage("Movie updated!");
            refreshMovieList();
            selectCinema(selectedCinema);
        } else {
            showMessage("Please enter a movie title.");
        }
    }

    private void deleteMovie() {
        if (selectedCinema == -1) {
            showMessage("Please select a cinema first.");
            return;
        }

        deleteMovieFromDatabase(selectedCinema + 1);
        showMessage("Movie deleted.");
        refreshMovieList();
        selectCinema(selectedCinema);
    }

    private void refreshMovieList() {
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            String movie = getMovieFromDatabase(i + 1);
            list.append("Cinema ").append(i + 1).append(": ")
                .append(movie.isEmpty() ? "[EMPTY]" : movie).append("\n");
        }
        movieListArea.setText(list.toString());
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    private String getMovieFromDatabase(int cinemaId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT movie FROM cinemas WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, cinemaId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("movie");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void addMovieToDatabase(int cinemaId, String movie) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE cinemas SET movie = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, movie);
            pst.setInt(2, cinemaId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMovieInDatabase(int cinemaId, String movie) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE cinemas SET movie = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, movie);
            pst.setInt(2, cinemaId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMovieFromDatabase(int cinemaId) {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE cinemas SET movie = '' WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, cinemaId);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ManageMovies(); // For testing only
    }
}
