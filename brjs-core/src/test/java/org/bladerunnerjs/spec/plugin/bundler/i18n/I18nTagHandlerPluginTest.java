package org.bladerunnerjs.spec.plugin.bundler.i18n;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.Aspect;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.junit.Before;
import org.junit.Test;


public class I18nTagHandlerPluginTest extends SpecTest
{

	private App app;
	private Aspect aspect;
	private StringBuffer response = new StringBuffer();
	
	@Before
	public void initTestObjects() throws Exception
	{
		given(brjs).automaticallyFindsBundlers()
			.and(brjs).automaticallyFindsMinifiers()
			.and(brjs).hasBeenCreated();
			app = brjs.app("app1");
			aspect = app.aspect("default");
	}

	@Test
	public void i18nTokenPluginContainsJsRequest() throws Exception {
		given(aspect).indexPageHasContent("<@i18n.bundle@/>");
		when(aspect).indexPageLoadedInDev(response, "en_GB");
		then(response).containsText("<script type=\"text/javascript\" src=\"i18n/en_GB.js\"></script>");
	}
	
}
