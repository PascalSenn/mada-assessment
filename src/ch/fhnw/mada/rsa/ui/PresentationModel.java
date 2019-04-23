package ch.fhnw.mada.rsa.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PresentationModel {
	public final StringProperty path = new SimpleStringProperty();
	public final StringProperty pathPri = new SimpleStringProperty();
	public final StringProperty pathPub = new SimpleStringProperty();
	public final StringProperty rsaN = new SimpleStringProperty();
	public final StringProperty rsaE = new SimpleStringProperty();
	public final StringProperty rsaD = new SimpleStringProperty();
}
