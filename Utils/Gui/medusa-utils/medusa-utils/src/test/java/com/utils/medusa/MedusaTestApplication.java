package com.utils.medusa;

import com.utils.medusa.speedometer_gauge.SpeedometerGauge;
import com.utils.medusa.speedometer_gauge.data.Sizes;
import com.utils.medusa.speedometer_gauge.data.Values;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MedusaTestApplication extends Application {

	@Override
	public void start(
			final Stage primaryStage) {

		primaryStage.setTitle("GUI Controls Test Application");

		final StackPane primaryPane = new StackPane();

		final Gauge speedometerGauge = createSpeedometerGauge();
		primaryPane.getChildren().add(speedometerGauge);

		final Scene scene = new Scene(primaryPane, 500, 360);
		primaryStage.setScene(scene);

		primaryStage.show();
	}

	private static Gauge createSpeedometerGauge() {

		final Sizes sizes = new Sizes(200, 200);
		final Values values = new Values(0, 100, 64);
		final Section[] sectionArray = {
				new Section(0, 64, Color.DARKORANGE),
				new Section(64, 100, Color.BLACK)
		};
		return SpeedometerGauge.create(sizes, values, sectionArray);
	}
}
