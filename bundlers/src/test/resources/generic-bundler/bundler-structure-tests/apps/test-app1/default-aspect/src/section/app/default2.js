caplin.thirdparty("lib2")
caplin.thirdparty("jquery")
caplin.thirdparty("knockout")
caplin.thirdparty("lib1")
section.app.default2 = function() { }
caplin.extend("section.app.default2","section.app.default1");
section.app.default2.prototype.start = function() {
	write("hello from section.app.default2");
}