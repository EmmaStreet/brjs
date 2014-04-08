package org.bladerunnerjs.plugin.plugins.commands.standard;

import java.io.File;
import java.util.zip.ZipFile;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.BRJS;
import org.bladerunnerjs.model.exception.command.CommandOperationException;
import org.bladerunnerjs.model.exception.command.CommandArgumentsException;
import org.bladerunnerjs.model.exception.command.NodeAlreadyExistsException;
import org.bladerunnerjs.plugin.utility.command.ArgsParsingCommandPlugin;
import org.bladerunnerjs.utility.FileUtility;

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
		String newApplicationName = parsedArgs.getString("new-app-name");
		String newApplicationNamespace = parsedArgs.getString("new-app-namespace");
		
		File appToImport = new File(brjs.file("sdk") + File.separator + appZipPath);
				
		if(!appToImport.exists()) throw new CommandOperationException("Unable to find app-zip '" + appZipPath + "'");
		if(!appToImport.getPath().endsWith(".zip")) throw new CommandOperationException("The provided zip file didn't have a .zip suffix: '" + appToImport.getAbsolutePath() + "'.");
		if(!appAlreadyExists(newApplicationName)) throw new NodeAlreadyExistsException(brjs.app(newApplicationName), this);
		
		try
		{
			ZipFile appZip = new ZipFile(appToImport);
			File tempUnzipDir = FileUtility.createTemporaryDirectory("tempApplicationDir");
			
			FileUtility.unzip(appZip, tempUnzipDir);
//			String currentApplicationName = importApplicationCommandUtility.getCurrentApplicationName(tempUnzipDir);
			System.out.println("oo");
			

//			File temporaryApplicationDir = new File(temporaryDirectoryForNewApplication, currentApplicationName);
//			
//			importApplicationCommandUtility.copyCutlassSDKJavaLibsIntoApplicationWebInfDirectory(sdkBaseDir, temporaryApplicationDir);
			
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
			
//			out.println("Successfully imported '" + new File(applicationZip).getName() + "' as new application '" + newApplicationName + "'");
//			out.println(" " + newApplicationDirectory.getAbsolutePath());
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
}
