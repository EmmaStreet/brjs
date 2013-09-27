C_DynamicScriptLoader = new function(){var l_oHeadElement=document.getElementsByTagName('head').item(0);
var l_oScriptIdentifierToElementMap=new Object();
var l_oScriptIdentifierToTimeoutMap=new Object();
this.loadScript = function(C,D,B,A){if(typeof B.scriptLoadTimeout!="function"){throw new SL4B_Exception("C_DynamicScriptLoader.loadScript: l_oErrorCallback parameter must implement scriptLoadTimeout() ");
}D+=((D.indexOf("?")==-1) ? "?" : "&")+(new Date()).valueOf();var l_oScriptElement=l_oScriptIdentifierToElementMap[C];
if(typeof l_oScriptElement!="undefined"){l_oHeadElement.removeChild(l_oScriptElement);}l_oScriptElement=document.createElement('script');l_oScriptElement.src=D;l_oScriptElement.type='text/javascript';l_oScriptIdentifierToElementMap[C]=l_oScriptElement;l_oHeadElement.appendChild(l_oScriptElement);var self=this;
var l_nTimeout=setTimeout(function(){self.checkLoaded(C,B);},A);
l_oScriptIdentifierToTimeoutMap[C]=l_nTimeout;};
this.checkLoaded = function(B,A){var l_nTimeout=l_oScriptIdentifierToTimeoutMap[B];
if(typeof l_nTimeout!="undefined"){this.clearScript(B);A.scriptLoadTimeout(B);}};
this.scriptLoaded = function(A){var l_nTimeout=l_oScriptIdentifierToTimeoutMap[A];
if(typeof l_nTimeout!="undefined"){clearTimeout(l_nTimeout);this.clearScript(A);}};
this.clearScript = function(A){delete (l_oScriptIdentifierToElementMap[A]);delete (l_oScriptIdentifierToTimeoutMap[A]);};
};
