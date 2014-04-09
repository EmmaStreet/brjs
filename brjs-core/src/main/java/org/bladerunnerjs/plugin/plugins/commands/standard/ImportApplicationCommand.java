package org.bladerunnerjs.plugin.plugins.commands.standard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.zip.ZipFile;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.model.events.AppImportedEvent;
import org.bladerunnerjs.model.exception.command.CommandOperationException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.exception.command.NodeAlreadyExistsException;
import org.bladerunnerjs.model.exception.name.InvalidDirectoryNameException;
import org.bladerunnerjs.model.exception.name.InvalidRootPackageNameException;
import org.bladerunnerjs.plugin.utility.command.ArgsParsingCommandPlugin;
import org.bladerunnerjs.utility.FileUtility;
import org.bladerunnerjs.utility.NameValidator;

import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.UnflaggedOption;

public class ImportApplicationCommand extends ArgsParsingCommandPlugin
{
	private BRJS brjs;
	
	@Override
	public void setBRJS(BRJS brjs)
	{
		this.brjs = brjs;
	}

	@Override
	protected void configureArgsParser(JSAP argsParser) throws JSAPException {
		argsParser.registerParameter(new UnflaggedOption("app-zip").setRequired(true).setHelp("the path to the application zip to be imported"));
		argsParser.registerParameter(new UnflaggedOption("new-app-name").setRequired(true).setHelp("the application name for the imported app"));
		argsParser.registerParameter(new UnflaggedOption("new-app-namespace").setRequired(true).setHelp("the application namespace for the imported app"));
	}
	
	@Override
	public String getCommandName()
	{
		return "import-app";
	}
	
	@Override
	public String getCommandDescription()
	{
		return "Create a new application by importing a given zipped application.";
	}

	@Override
	public String getCommandHelp() {
		return getCommandUsage();
	}
	
	@Override
	protected void doCommand(JSAPResult parsedArgs) throws CommandArgumentsException, CommandOperationException
	{	
		String appZipPath = parsedArgs.getString("app-zip");
		String newAppName = parsedArgs.getString("new-app-name");
		String newAppNamespace = parsedArgs.getString("new-app-namespace");
		
		// Support absolute and relative-sdk-path
		File appToImport = new File(appZipPath).exists() ? new File(appZipPath) : new File(brjs.sdkDir().dir() + File.separator + appZipPath);
				
		try
		{
			// Validation
			if(!appToImport.exists()) throw new FileNotFoundException("Unable to find app-zip '" + appZipPath + "'");
			if(!appToImport.getPath().endsWith(".zip")) throw new CommandOperationException("The provided zip file didn't have a .zip suffix: '" + appToImport.getAbsolutePath() + "'.");
			if(appAlreadyExists(newAppName)) throw new NodeAlreadyExistsException(brjs.app(newAppName), this);
			if(!NameValidator.isValidDirectoryName(newAppName)) throw new InvalidDirectoryNameException(newAppName);
			if(!NameValidator.isValidPackageName(newAppNamespace)) throw new InvalidRootPackageNameException(newAppNamespace);
			
			// unzip
			ZipFile appZip = new ZipFile(appToImport);
			File tempUnzipDir = FileUtility.createTemporaryDirectory("tempApplicationDir");
			FileUtility.unzip(appZip, tempUnzipDir);

			// populate WEB-INF/lib
			File tempAppDir = new File(tempUnzipDir, getOriginalAppName(tempUnzipDir));
			FileUtility.copyDirectoryContents(brjs.appJars().dir(), new File(tempAppDir, "WEB-INF/lib"));	// TODO use a better way to get an 'unrooted' app's WEB-INF/lib folder
			
			// rename stuff
			
			// copy to BRJS apps folder, deploy and notify observers
			App app = brjs.app(newAppName);
			FileUtility.copyDirectoryContents(tempAppDir, app.dir());
			app.deploy();
			app.notifyObservers(new AppImportedEvent(), app);
			
//			File newApplicationDirectory = importApplicationCommandUtility.createApplicationDirIfItDoesNotAlreadyExist(sdkBaseDir, newApplicationName);
//			File currentApplicationDirectoryInTempDir = temporaryApplicationDir;
//			FileUtility.copyDirectoryContents(currentApplicationDirectoryInTempDir, newApplicationDirectory);
//
//			/* we cant use the directory locator here since the exploded zip isnt inside an sdk structure so they locator wont recognise it
//			 *  - as it happens we know the app.conf is in the root of the app so we can just create a new file object relative to the app path
//			 */
//			File temporaryDirAppConf = new File(temporaryApplicationDir, CutlassConfig.APP_CONF_FILENAME);
//			File newAppDirConf = new File(newApplicationDirectory, CutlassConfig.APP_CONF_FILENAME);
//			FileUtils.copyFile(temporaryDirAppConf, newAppDirConf);
//			
//			String applicationNamespace = RequirePrefixCalculator.getAppRequirePrefix(newApplicationDirectory);
//			Renamer.renameApplication(newApplicationDirectory, applicationNamespace, newApplicationNamespace, currentApplicationName, newApplicationName);
//			
//			RequirePrefixCalculator.purgeCachedApplicationNamespaces();
//			
//			importApplicationCommandUtility.createAutoDeployFileForApp(newApplicationDirectory, brjs.bladerunnerConf().getJettyPort());
			
			System.out.println("Successfully imported '" + appToImport.getName() + "' as new application '" + newAppName + "'");
			System.out.println(" " + brjs.app(newAppName).dir().getAbsolutePath());
		}
		catch (Exception e)
		{
			throw new CommandOperationException("Failed to import application from zip '" + appZipPath + "'.", e);
		}
	}

	
	private boolean appAlreadyExists(String appName)
	{
		for(App app : brjs.apps())
		{
			if(app.getName().equals(appName)) return true; 
		}
		return false;
	}
	
	private String getOriginalAppName(File unzippedAppDirectory) throws CommandOperationException
	{
		if(unzippedAppDirectory.listFiles().length > 1)
		{
			throw new CommandOperationException("More than one folder found at root of application zip.");
		}
		
		return unzippedAppDirectory.listFiles()[0].getName();
	}
}
