This project aims to ajaxify Apache OFBiz, which is an open source enterprise automation software, using Google GWT

To know more about :<br />
Apache OFBiz - http://ofbiz.apache.org/<br />
Google Web Toolkit - http://code.google.com/webtoolkit/<br />

Sometime back I had a look at the GWT project, saw the showcase and thought wouldn't it be nice to have GWT front-end for OFBiz applications, to have richer user interfaces. That's when I thought of integrating GWT with OFBiz.

Using GWT one can have richer user interfaces and also all the framework features as well as the business functionality OFBiz provides.

There are 3 ways to have communication in GWT :<br />
1) XML<br />
2) JSON<br />
3) GWT-RPC<br />

I choose to integrate OFBiz & GWT using the native GWT-RPC protocol as it was much easier to use and productive, as java objects are passed around, then instead of using XML & JSON and parsing the messages.

The best thing using ofbizify is, the ofbiz component structure stays the same, you put all your request in controller file, invoke your Java Events, Groovy scripts, Services as you use to and get back the response in gwt app.

And yes, apart from building pure gwt application you can also embed gwt widget/component into existing screens/ftl pages.

To know more about how to configure ofbizify, how to call java events, groovy scripts and services, download the code & example, you can visit the project wiki at http://code.google.com/p/ofbizify/wiki/GettingStarted

and guys let me know if there is anything to improve the project, you can email me at abdullah.shaikh.1@gmail.com