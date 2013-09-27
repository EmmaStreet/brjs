package org.bladerunnerjs.model;

import java.util.List;

import org.bladerunnerjs.model.exception.ModelOperationException;


public interface SourceFile extends LinkedAssetFile {
	String getRequirePath();
	Resources getResources();
	List<SourceFile> getOrderDependentSourceFiles() throws ModelOperationException;
}
