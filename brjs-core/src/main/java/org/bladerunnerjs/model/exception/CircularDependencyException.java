package org.bladerunnerjs.model.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bladerunnerjs.model.SourceModule;

import com.google.common.base.Joiner;

public class CircularDependencyException extends ModelOperationException {
	private static final long serialVersionUID = 1L;
	
	private final Set<SourceModule> unprocessedSourceModules;
	
	public CircularDependencyException(Set<SourceModule> unprocessedSourceModules) {
		super("");
		this.unprocessedSourceModules = unprocessedSourceModules;
	}
	
	@Override
	public String getMessage() {
		return "Circular dependency involving the classes " + (Joiner.on(", ").join(getRequirePaths(unprocessedSourceModules)) + " prevented the bundle from being written.");
	}
	
	private List<String> getRequirePaths(Set<SourceModule> sourceModules) {
		List<String> requirePaths = new ArrayList<>();
		
		for (SourceModule sourceModule : sourceModules) {
			requirePaths.add("'" + sourceModule.getRequirePath() + "'");
		}
		
		return requirePaths;
	}
}
