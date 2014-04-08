package org.bladerunnerjs.spec.command;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.Aspect;
import org.bladerunnerjs.model.exception.command.ArgumentParsingException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.exception.command.CommandOperationException;
import org.bladerunnerjs.model.exception.command.NodeAlreadyExistsException;
import org.bladerunnerjs.model.exception.name.InvalidDirectoryNameException;
import org.bladerunnerjs.model.exception.name.InvalidRootPackageNameException;
import org.bladerunnerjs.plugin.plugins.commands.standard.ExportApplicationCommand;
import org.bladerunnerjs.plugin.plugins.commands.standard.ImportApplicationCommand;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;

public class ImportApplicationCommandTest extends SpecTest
{
	private App app, existingApp;
	private Aspect aspect;
	
	
	@Before
	public void setUp() throws Exception
	{
		given(brjs).hasCommands(new ImportApplicationCommand(), new ExportApplicationCommand())
			.and(brjs).automaticallyFindsBundlers()
			.and(brjs).automaticallyFindsMinifiers()
			.and(brjs).hasBeenCreated();
		app = brjs.app("app1");
		existingApp = brjs.app("existingApp");
		aspect = app.aspect("default");
		
		// createAppThatCanBeExported
		
		
		
		
	}

	@Test
	public void commandThrowsErrorIfNewAppNameIsNotPassedIn() throws Exception
	{
		when(brjs).runCommand("import-app", "myzip.zip");
		then(exceptions).verifyException(ArgumentParsingException.class, unquoted("Parameter 'new-app-name' is required"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void commandThrowsErrorIfNewAppNamespaceIsNotPassedIn() throws Exception
	{
		when(brjs).runCommand("import-app", "myzip.zip", "mynewapp");
		then(exceptions).verifyException(ArgumentParsingException.class, unquoted("Parameter 'new-app-namespace' is required"))
			.whereTopLevelExceptionIs(CommandArgumentsException.class);
	}
	
	@Test
	public void commandThrowsErrorIfAppZipParameterIsNotAFileOnDisk() throws Exception
	{
		when(brjs).runCommand("import-app", "doesnotexist.zip", "mynewapp", "brx");
		then(exceptions).verifyException(CommandOperationException.class, unquoted("Unable to find app-zip 'doesnotexist.zip'"));
	}
	
	@Test
	public void commandThrowsErrorIfAppZipParameterDoesNotEndWithDotZip() throws Exception
	{
		given(brjs).containsFile("sdk/myfile.foo");
		when(brjs).runCommand("import-app", "myfile.foo", "mynewapp", "brx");
		then(exceptions).verifyException(CommandOperationException.class, unquoted("The provided zip file didn't have a .zip suffix: '" + 
				new File(brjs.file("sdk"), "myfile.foo").getPath() + "'"));
	}
	
	@Test
	public void commandThrowsAnErrorIfNewAppNameMatchesAnAlreadyExistingAppName() throws Exception
	{
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "existingApp", "brx");
		then(exceptions).verifyException(NodeAlreadyExistsException.class, unquoted("App 'existingApp' already exists"));
	}

	// validation
	@Test
	public void importsZippedAppToANewAppNameWithSpecialCharsThrowsException() throws Exception
	{	
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "cr�dit", "brx");
		then(exceptions).verifyException(InvalidDirectoryNameException.class, unquoted("'cr�dit' is not a valid directory name"))
			.whereTopLevelExceptionIs(CommandOperationException.class);
	}

	@Test 
	public void importsZippedAppToANewAppNameWithSpacesThrowsException() throws Exception
	{
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "my app", "brx");
		then(exceptions).verifyException(InvalidDirectoryNameException.class, unquoted("'my app' is not a valid directory name"))
			.whereTopLevelExceptionIs(CommandOperationException.class);
	}
	
//	@Test
//	public void importsZippedAppToANewAppNameWithANumber() throws Exception
//	{
//		given(brjs).containsFile("sdk/import.zip");
//		when(brjs).runCommand("import-app", "import.zip", "myapp2", "brx");
//		TODO assert appName
//	}
//
//	@Test
//	public void importsZippedAppToANewAppNameWithAnUnderscore() throws Exception
//	{
//		importApplicationCommand.doCommand(new String[] { "emptytrader.zip", "novotrader_new", "novox" });
//	}

//	@Test
//	public void importsZippedAppToANewAppNameWithAHyphen() throws Exception
//	{
//		importApplicationCommand.doCommand(new String[] { "emptytrader.zip", "novo-trader", "novox" });
//	}
//	

	// <app-namespace> exception test cases 
	@Test
	public void importsZippedAppToANewNamespaceWithSpecialCharsThrowsException() throws Exception
	{
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "myimportedapp", "novo�");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, unquoted("'novo�' is not a valid root package name"))
			.whereTopLevelExceptionIs(CommandOperationException.class);
	}
	
	@Test 
	public void commandThrowsExceptionIfAppNamespaceHasUnderscore() throws Exception
	{
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "myimportedapp", "br_x");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, unquoted("'br_x' is not a valid root package name"))
			.whereTopLevelExceptionIs(CommandOperationException.class);
	}

	
	@Test 
	public void importsZippedAppToANewNamespaceWithAHyphenThrowsException() throws Exception
	{
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "myimportedapp", "br-x");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, unquoted("'br-x' is not a valid root package name"))
			.whereTopLevelExceptionIs(CommandOperationException.class);
	}
	
	@Test
	public void importsZippedAppToANewNamespaceWithSpacesThrowsException() throws Exception
	{
		given(brjs).containsFile("sdk/import.zip");
		when(brjs).runCommand("import-app", "import.zip", "myimportedapp", "br x");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, unquoted("'br x' is not a valid root package name"))
			.whereTopLevelExceptionIs(CommandOperationException.class);
	}

//	TODO: No longer valid?
//	@Test
//	public void importsZippedAppToANewNamespaceWithReservedCaplinNamespaceThrowsException() throws Exception
//	{
//		given(brjs).containsFile("sdk/import.zip");
//		when(brjs).runCommand("import-app", "import.zip", "myimportedapp", "caplin");
//		then(exceptions).verifyException(InvalidRootPackageNameException.class, unquoted("'caplin' is not a valid root package name"))
//			.whereTopLevelExceptionIs(CommandOperationException.class);
//	}
	
//	@Test
//	public void importsZippedAppToANewNamespaceWithANumberIsAllowed() throws Exception
//	{
//		importApplicationCommand.doCommand(new String[] { "emptytrader.zip", "novotrader", "novox2" });
//	}
//


//
	
	@Test
	public void canImportAnApplication() throws Exception
	{
//		given()
		
	}
//	@Test
//	public void copiesAcrossAppConfFile() throws Exception
//	{
//		File appDirectory = new File(applicationsDirectory, "newtrader");
//		File appConf = new File(appDirectory, "app.conf");
//		
//		importApplicationCommand.doCommand(new String[] { "plaintrader.zip", "newtrader", "novox" });
//
//		assertTrue(appDirectory.exists());
//		assertTrue(appConf.exists());
//		
//		List<String> appConfLines = FileUtils.readLines(appConf);
//		assertEquals("requirePrefix: novox", appConfLines.get(1));
//	}
//

//	@Test 
//	public void zippedApplicationWithParentFolderHasSpacesCanBeImported() throws Exception
//	{
//		sdkBaseDir = new File(tempDir, "folder with spaces/" + CutlassConfig.SDK_DIR);
//		brjs = BRJSTestFactory.createBRJS(sdkBaseDir);
//		
//		File applicationsDirectory = new File(sdkBaseDir.getParent(), CutlassConfig.APPLICATIONS_DIR);
//		File appDirectory = new File(applicationsDirectory, "novotrader");
//		importApplicationCommand = new ImportApplicationCommand(brjs);
//
//		BRJSAccessor.initialize(BRJSTestFactory.createBRJS(sdkBaseDir));
//		
//		assertFalse(appDirectory.exists());
//
//		importApplicationCommand.doCommand(new String[] { "plaintrader.zip", "novotrader", "novox" });
//
//		assertTrue(appDirectory.exists());
//	}
//

	
//	@Ignore //TODO: this test should be in the CT3 plugin
//	@Test
//	public void importsZippedWebcentricApplicationAndCreatesDatabase() throws Exception
//	{
//		File appDirectory = new File(applicationsDirectory, "novotrader");
//		File emptyTraderWEBINFJar = new File(appDirectory, "WEB-INF/lib/testJar.jar");
//		File generatedWebcentricFolder = new File(sdkBaseDir.getParent(), CutlassConfig.WEBCENTRIC_DB_DIR);
//		
//		assertFalse(appDirectory.exists());
//		assertFalse(emptyTraderWEBINFJar.exists());
//		assertFalse(generatedWebcentricFolder.exists());
//
//		importApplicationCommand.doCommand(new String[] { "emptytrader.zip", "novotrader", "novox" });
//
//		assertTrue(appDirectory.exists());
//		assertTrue(emptyTraderWEBINFJar.exists());
//		assertTrue(new File(generatedWebcentricFolder, "novotrader").exists());
//	}

}
