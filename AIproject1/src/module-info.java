module AIproject1 {
	requires javafx.controls;
    requires java.desktop; // <-- Add this line

	opens application to javafx.graphics, javafx.fxml;
}
