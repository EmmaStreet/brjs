caplinx.dashboard.app.service.window.WindowOpenerProvider = function()
{
};
caplin.implement(caplinx.dashboard.app.service.window.WindowOpenerProvider, caplinx.dashboard.app.service.window.WindowOpenerService);

caplinx.dashboard.app.service.window.WindowOpenerProvider.prototype.openWindow = function(sUrl)
{
	window.open(sUrl, "_blank");
};
