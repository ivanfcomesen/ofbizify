## How to make ofbizify calls ##

Ofbizify has :
  1. Event Handlers
    * GwtRpcGroovyEventHandler(gwtgroovy) - To call groovy scripts
    * GwtRpcJavaEventHandler' (gwtjava)   - To call java events
    * GwtRpcServiceEventHandler (gwtservice) - To call services

> 2. GwtRpcServiceImpl (RemoteServiceServlet)

> 3. Utility classes
    * GwtRpcPayload
    * GwtRpcPayloadUtil
    * GwtRpcUtil

<br />
### Creating your ofbiz gwt component : ###
1) Create ofbiz component using the ant script

2) Add the below entry to the component's build.xml local.class.path

`<fileset dir="../../hot-deploy/ofbizify/build/lib" includes="*.jar"/>`

3) Include the ofbizify controller in your controller

`<include location="component://ofbizify/webapp/ofbizify/WEB-INF/controller.xml"/>`

4) Add the below servlet mapping to your component web.xml

```
<servlet>
 <servlet-name>gwtrpc</servlet-name>
 <servlet-class>com.legeriti.ofbizify.gwt.gwtrpc.rpc.server.GwtRpcServiceImpl</servlet-class>
</servlet>

<servlet-mapping>
 <servlet-name>gwtrpc</servlet-name>
 <url-pattern>/gwtrpc</url-pattern>
</servlet-mapping>
```

5) Create the GWT Web application using the GWT Eclipse plugin

6) Copy the ofbizify-client.jar into the gwt application's lib directory

7) Copy the gwt generated files to your component's webapp/yourcomponent directory

<br />
## How to make calls ##
### 1. Groovy ###
> <b>1.1)</b> Create a groovy script which will return the required data, use the GwtRpcPayloadUtil.returnSuccessWithPayload method to send the data back as response
```
List<GenericValue> users = delegator.findList("OfbizifyExampleUsers", null, null, null, null, true );

return GwtRpcPayloadUtil.returnSuccessWithPayload(users);
```

> <b>1.2)</b> Create the request mapping for the groovy file
```
<request-map uri="getUserDetails">
<event type="gwtgroovy" path="component://ofbizifyexample/webapp/ofbizifyexample/WEB-INF/actions/" invoke="GetUserDetails.groovy"/>
   <response name="success" type="none"/>
   <response name="error" type="none"/>
</request-map>
```

> <b>1.3)</b> Make the call from gwt
```
HashMap<String, String> parameters = new HashMap<String, String>();

AsyncCallback<HashMap<String, Object>> callback = new AsyncCallback<HashMap<String, Object>>() {
   public void onFailure(Throwable caught) {
     Window.alert("server error " + caught.toString());
   }

   public void onSuccess(HashMap<String, Object> result) {
     if(ServiceUtil.isSuccess(result)) {

       ArrayList<HashMap<String, String>> users = (ArrayList<HashMap<String, String>>)result.get("payload");

       Window.alert("got data " + users);

     }
   }
};

GwtRpcServiceAsync gwtrcpService = GWT.create(GwtRpcService.class);
ServiceDefTarget endpoint = (ServiceDefTarget) gwtrcpService;
endpoint.setServiceEntryPoint("/ofbizifyexample/control/getUserDetails");
gwtrcpService.processRequest(parameters, callback);
```

### 2. Java ###
> <b>2.1)</b> Create a java event which will return the required data, set the "result" request attribute and use the GwtRpcPayloadUtil.returnSuccessWithPayload method to send the data back as response
```
List<GenericValue> users = delegator.findList("OfbizifyExampleUsers", null, null, null, null, true );

Map<String, Object> result = GwtRpcPayloadUtil.returnSuccessWithPayload(users);
request.setAttribute("result", result);

return "success";
```

> <b>2.2)</b> Create the request mapping for the java event
```
<request-map uri="getUserDetails">
   <event type="gwtjava" path="com.legeriti.ofbizify.example.event.UserDetailEvent" invoke="getUserDetails"/>
   <response name="success" type="none"/>
   <response name="error" type="none"/>
</request-map>
```

> <b>2.3)</b> Make the call from gwt (this will be same as done for groovy)

#### 3. Service ####
> <b>3.1)</b> Define the service which will return the required data, set the "result" as attribute, which will be the data that is to be returned back as response
```
Service Definition :
<service name="getUserDetails" engine="java" auth="false" location="com.legeriti.ofbizify.example.event.UserDetailServices" invoke="getUserDetails">
   <attribute mode="OUT" name="result" optional="false" type="java.util.Map"/>
</service>
```
```
Service Implementation :
List<GenericValue> users = delegator.findList("OfbizifyExampleUsers", null, null, null, null, true );

Map<String, Object> result = GwtRpcPayloadUtil.returnSuccessWithPayload(users);

Map<String, Object> returnMap = new HashMap<String, Object>();
returnMap.put("result", result);

return returnMap;
```

> <b>3.2)</b> Create the request mapping for the service
```
<request-map uri="serviceGetUserDetails">
   <event type="gwtservice" invoke="getUserDetails"/>
   <response name="success" type="none"/>
   <response name="error" type="none"/>
</request-map>
```

> <b>3.3)</b> Make the call from gwt (this will be same as done for groovy)


I have taken the ofbizify example code to explain, you can have a look at the code from http://ofbizify.googlecode.com/svn/trunk/ofbizifyexample/