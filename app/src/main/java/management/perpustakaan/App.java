package management.perpustakaan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import Models.Library;

public class App extends Application {
    private static Scene scene;
    public static Library library;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("HomeView"), 800, 600);
        stage.setTitle("Sistem Manajemen Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        PersistenceService.saveData();
        System.out.println("Aplikasi ditutup, data berhasil disimpan.");
        super.stop();
    }
    
    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        library = PersistenceService.loadData();
        if (library.items.isEmpty() && library.members.isEmpty()) {
            System.out.println("Memulai dengan data kosong. File data akan dibuat saat ada perubahan.");
        } else {
            System.out.println("Data berhasil dimuat dari file JSON.");
        }
        launch(args);
    }
}