package org.bladerunnerjs.plugin.plugins.commands.standard;

import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.plugin.CommandPlugin;

public class InvalidBundlableNodeException extends CommandArgumentsException {
	private static final long serialVersionUID = 1L;
	
	public InvalidBundlableNodeException(String path, CommandPlugin commandPlugin) {
		super("The directory '" + path + "' is not itself a bundlable directory, nor resides within a bundlable directory.", commandPlugin);
	}
}
