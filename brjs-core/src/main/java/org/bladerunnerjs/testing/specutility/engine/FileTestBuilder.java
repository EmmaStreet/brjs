package org.bladerunnerjs.testing.specutility.engine;

import java.io.File;

import org.bladerunnerjs.utility.FileUtil;

public class FileTestBuilder extends SpecTestBuilder {
	private final FileUtil fileUtil;
	private final File file;
	private final BuilderChainer builderChainer;

	public FileTestBuilder(SpecTest specTest, File file) {
		super(specTest);
		this.file = file;
		builderChainer = new BuilderChainer(specTest);
		fileUtil = new FileUtil(specTest.getActiveCharacterEncoding());
	}
	
	public BuilderChainer containsFileWithContents(String filePath, String fileContents) throws Exception {
		fileUtil.write(new File(file, filePath), fileContents);
		
		return builderChainer;
	}
}
