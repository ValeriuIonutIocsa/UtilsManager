package com.utils.medusa.speedometer_gauge;

import com.utils.medusa.speedometer_gauge.data.Sizes;
import com.utils.medusa.speedometer_gauge.data.Values;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import eu.hansolo.medusa.skins.DashboardSkin;

public final class SpeedometerGauge {

	private SpeedometerGauge() {
	}

	public static Gauge create(
			final Sizes sizes,
			final Values values,
			final Section[] sections) {

		final GaugeBuilder<?> gaugeBuilder = GaugeBuilder.create();
		gaugeBuilder.animated(false).interactive(false);

		if (sizes != null) {
			final double width = sizes.width();
			final double height = sizes.height();
			gaugeBuilder.maxSize(width, height).minSize(width, height);
		}

		if (values != null) {
			final double minValue = values.minValue();
			final double maxValue = values.maxValue();
			final double value = values.value();
			gaugeBuilder.minValue(minValue).maxValue(maxValue);
			gaugeBuilder.value(value);
		}

		if (sections != null) {
			gaugeBuilder.sections(sections);
			gaugeBuilder.sectionsVisible(true);
		}

		final Gauge gauge = gaugeBuilder.build();
		gauge.setSkin(new DashboardSkin(gauge));
		return gauge;
	}
}
