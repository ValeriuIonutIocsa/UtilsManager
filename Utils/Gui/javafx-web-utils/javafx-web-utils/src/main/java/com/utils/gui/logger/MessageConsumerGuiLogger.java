package com.utils.gui.logger;

import com.utils.log.AbstractMessageConsumer;
import com.utils.log.Logger;
import com.utils.log.MessageConsumer;
import com.utils.log.MessageLevel;

class MessageConsumerGuiLogger extends AbstractMessageConsumer {

	private final MessageConsumer oldMessageConsumer;

	private final CustomWebViewGuiLogger customWebViewGuiLogger;

	MessageConsumerGuiLogger(
			final MessageConsumer oldMessageConsumer,
			final CustomWebViewGuiLogger customWebViewGuiLogger) {

		this.oldMessageConsumer = oldMessageConsumer;

		this.customWebViewGuiLogger = customWebViewGuiLogger;
	}

	@Override
	public void printMessageSpecific(
			final MessageLevel messageLevel,
			final String message) {

		oldMessageConsumer.printMessageSpecific(messageLevel, message);

		try {
			final String text;
			if (messageLevel == MessageLevel.PROGRESS || messageLevel == MessageLevel.STATUS) {
				text = "<b>" + message + "</b><br>";
			} else if (messageLevel == MessageLevel.WARNING) {
				text = "<b><font color=\"DarkOrange\">" + message + "</font></b><br>";
			} else if (messageLevel == MessageLevel.ERROR || messageLevel == MessageLevel.THROWABLE) {
				text = "<b><font color=\"red\">" + message + "</font></b><br>";
			} else {
				text = message + "<br>";
			}
			customWebViewGuiLogger.load(text);

		} catch (final Throwable throwable) {
			oldMessageConsumer.printMessageSpecific(
					MessageLevel.ERROR, "error occurred while logging GUI line");
			final String throwableString = Logger.throwableToString(throwable);
			oldMessageConsumer.printMessageSpecific(MessageLevel.THROWABLE, throwableString);
		}
	}
}
