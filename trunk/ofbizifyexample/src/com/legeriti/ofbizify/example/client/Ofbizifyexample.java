/*
 * Copyright 2010 Abdullah Shaikh
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.legeriti.ofbizify.example.client;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.legeriti.ofbizify.gwt.gwtrpc.rpc.client.GwtRpcService;
import com.legeriti.ofbizify.gwt.gwtrpc.rpc.client.GwtRpcServiceAsync;
import com.legeriti.ofbizify.gwt.gwtrpc.rpc.client.util.ServiceUtil;
import com.legeriti.ofbizify.gwt.gwtrpc.rpc.client.util.WidgetUtil;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Ofbizifyexample implements EntryPoint {
	
	HashMap<String, String> formData = new HashMap<String, String>();
	
	String partyId = "DemoCustomer";
	String CONTEXT_URL = "/ofbizifyexample/control/";
	
	GwtRpcServiceAsync gwtrcpService = GWT.create(GwtRpcService.class);
	ServiceDefTarget endpoint = (ServiceDefTarget) gwtrcpService;
	
	private DockPanel mainPanel;
	
	//start - form fields
	private TextBox name = new TextBox();
	private TextBox email = new TextBox();
	private ListBox country = new ListBox();
	private ListBox state = new ListBox();
	private TextBox mobile = new TextBox();
	private TextBox phone = new TextBox();

	private Hidden emailContactMechId = new Hidden();
	private Hidden addressContactMechId = new Hidden();
	private Hidden phoneContactMechId = new Hidden();
	private Hidden mobileContactMechId = new Hidden();
	//end - form fields
	
	//start - table fields
	final FlexTable customerTable = new FlexTable();
	//end - table fields
	
	public Ofbizifyexample() {
		
		mainPanel = new DockPanel();
		mainPanel.setStyleName("mainPanel");
	    mainPanel.setSpacing(0);
		mainPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		mainPanel.setVerticalAlignment(DockPanel.ALIGN_TOP);
		
		//Widget headerPanel = new Label("Header Panel");
		HorizontalPanel headerPanel = new HorizontalPanel();
		mainPanel.add(headerPanel, DockPanel.NORTH);

		headerPanel.add(createTitle("ofbizify - Ajaxifying Ofbiz, using GWT", false));

		//Widget footerPanel = new Label("Footer Panel");
		VerticalPanel footerPanel = new VerticalPanel();
		footerPanel.setStyleName("centerPanel");
		mainPanel.add(footerPanel, DockPanel.SOUTH);
		
		//rightPanel = createRightPanel();
		//mainPanel.add(rightPanel, DockPanel.EAST);

		//leftPanel = createLeftPanel();
		//mainPanel.add(leftPanel, DockPanel.WEST);

		//--
		VerticalPanel centerPanel = new VerticalPanel();
		centerPanel.setStyleName("centerPanel");
		mainPanel.add(centerPanel, DockPanel.CENTER);
		centerPanel.add(createForm());
		//--

		//--
		footerPanel.add(createTable());
		//--
		
		mainPanel.setCellWidth(headerPanel, "100%");
		mainPanel.setCellHeight(headerPanel, "40%");

		//mainPanel.setCellWidth(rightPanel, "12%");
		//mainPanel.setCellHeight(rightPanel, "55%");
		
		//mainPanel.setCellWidth(leftPanel, "12%");
		//mainPanel.setCellHeight(leftPanel, "55%");
		
		mainPanel.setCellWidth(centerPanel, "76%");
		mainPanel.setCellHeight(centerPanel, "55%");
		
		mainPanel.setCellWidth(footerPanel, "100%");
		mainPanel.setCellHeight(footerPanel, "5%");
		
	}
	
	public Widget createTable() {
		
		customerTable.setTitle("Customer List (click on the record to update)");
		
		customerTable.addClickHandler(
    			new ClickHandler() {

    				public void onClick(ClickEvent event) {

						FlexTable ft = (FlexTable) event.getSource();
						Cell c = ft.getCellForEvent(event);

						if(c != null) {

							 int cellIndex = c.getCellIndex();
							 int rowIndex = c.getRowIndex();

							 if(cellIndex == 6) {

							 } else {
								 	setFormData(formData);
								 	getStates();
							 }
						 }
					}
    			});

		customerTable.setBorderWidth(1);
		
		customerTable.setText(0, 0, "Name");
		customerTable.setText(0, 1, "Email");
		customerTable.setText(0, 2, "Country");
		customerTable.setText(0, 3, "State");
		customerTable.setText(0, 4, "Mobile");
		customerTable.setText(0, 5, "Phone");
		customerTable.setText(0, 6, "");

		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.add(createTitle("Customer List (click on the record to update)", true));
		panel.add(customerTable);
		
		return panel;
	}
	
	public Widget createForm() {

		FlexTable table = new FlexTable();

		Label nameLbl = new Label("Name : ");
		table.setWidget(1, 0, nameLbl);

		table.setWidget(1, 1, name);
		
		Label emailLbl = new Label("Email Address : ");
		table.setWidget(2, 0, emailLbl);
		
		
		table.setWidget(2, 1, email);
		
		Label countryLbl = new Label("Country : ");
		table.setWidget(3, 0, countryLbl);
		table.setWidget(3, 1, country);

		ChangeHandler countryChangeHandler = new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				getStates();
			}
		};
		
		country.addChangeHandler(countryChangeHandler);

		Label curLocLbl = new Label("Current Location : ");
		table.setWidget(4, 0, curLocLbl);

		table.setWidget(4, 1, state);
		state.addItem("select", "select");

		Label conNosLbl = new Label("Contact Numbers: ");
		table.setWidget(5, 0, conNosLbl);
		
		Label conNosLblText = new Label("Out of two phone numbers, one is compulsory.");
		table.setWidget(5, 1, conNosLblText);
		
		Label mobileLbl = new Label("Mobile : ");
		table.setWidget(6, 0, mobileLbl);
		
		table.setWidget(6, 1, mobile);
		
		Label phoneLbl = new Label("Phone : ");
		table.setWidget(7, 0, phoneLbl);

		table.setWidget(7, 1, phone);

		final Button saveButton = new Button("Save Changes");
		saveButton.setStyleName("saveChangesButtonStyle");
		
		saveButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				//you can do the validation over here
				boolean status = false;
				
				if(addressContactMechId.getValue().isEmpty() || emailContactMechId.getValue().isEmpty() ||
						mobileContactMechId.getValue().isEmpty() || phoneContactMechId.getValue().isEmpty()) {

					Window.alert("You need to click the record, you want to update, from the table below");
					status = false;
					return;
				}
				
				if(name.getText().isEmpty() || email.getText().isEmpty() || 
						country.getValue(country.getSelectedIndex()).equals("select") || 
						state.getValue(state.getSelectedIndex()).equals("select") ||
						mobile.getText().isEmpty() || phone.getText().isEmpty()) {
					Window.alert("Please fill all the customer details");
					status = false;
					return;
				} else {
					status = true;
				}

				if(status) {
					
					
					String command = saveButton.getText();
					
					if("Create".equals(command)) {
					} else
					if("Save Changes".equals(command)){
						
						//start - save contact details
						showLoadingMessage("saving contact details ....");
					
						HashMap<String, String> parameters = getFormData();
					
						AsyncCallback<HashMap<String, Object>> callback = new AsyncCallback<HashMap<String, Object>>() {
						
							public void onFailure(Throwable caught) {
							
								Window.alert("SERVER_ERROR " + caught.toString());
							}
						
							public void onSuccess(HashMap<String, Object> result) {

								if(ServiceUtil.isSuccess(result)) {

									HashMap<String, String> updateResult = (HashMap<String, String>)result.get("result");
	
									String personalDetailsUpdate = updateResult.get("personalDetailsUpdate");
									
									String emailUpdate = updateResult.get("emailUpdate");
									String emailContactMechId = updateResult.get("emailContactMechId");
									
									String addressUpdate = updateResult.get("addressUpdate");
									String addressContactMechId = updateResult.get("addressContactMechId");
									
									String mobileUpdate = updateResult.get("mobileUpdate");
									String mobileContactMechId = updateResult.get("mobileContactMechId");
									
									String phoneUpdate = updateResult.get("phoneUpdate");
									String phoneContactMechId = updateResult.get("phoneContactMechId");
								
									if(personalDetailsUpdate.equals("success") && emailUpdate.equals("success") &&
										addressUpdate.equals("success") && mobileUpdate.equals("success") && phoneUpdate.equals("success")) {
										
										HashMap<String, String> fields = new HashMap<String, String>();
										fields.put("emailContactMechId", emailContactMechId);
										fields.put("addressContactMechId", addressContactMechId);
										fields.put("mobileContactMechId", mobileContactMechId);
										fields.put("phoneContactMechId", phoneContactMechId);
										
										setHiddenFields(fields);
	
										hideLoadingMessage();
										
										getContactDetails();
										
									} else {
										//display some message to the user
									}
								}
							}
					};
					
					String SAVE_CONTACT_DETAILS = CONTEXT_URL + "saveContactDetails";
					endpoint.setServiceEntryPoint(SAVE_CONTACT_DETAILS);
					gwtrcpService.processRequest(parameters, callback);
					//end - save contact details
				}

				}
			}
		});

		Button cancelButton = new Button("Clear");
		cancelButton.setStyleName("backButtonStyle");

		HorizontalPanel btnPanel = new HorizontalPanel();
		btnPanel.add(saveButton);
		btnPanel.add(cancelButton);
		//
		
		table.setWidget(8, 1, btnPanel);

		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("100%");
		panel.add(createTitle("Customer Details", true));
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth("100%");
		panel.add(hp);
		
		hp.add(table);
		
		HTML msg = new HTML();
		msg.setHTML("<h3>This example demonstrates the integration of OFBiz with GWT (using GWT-RPC protocol), <br/>" +
					"because of time constraint I could only provide the read & update functionality,<br/>" +
					"but will be providing the create & delete functionality soon." +
					"<br/>" +
					"It would have been more simple if I had created a custom table and do CRUD operations on it,<br/>" +
					"but to show that already existing services can be called but providing a simple wrapper around them, as choose this way." +
					"<br/>" +
					"I have use groovy for calling use but you can choose groovy, java events or even services</h3>");
		hp.add(msg);
		//
		
		//hidden fields
		panel.add(emailContactMechId);
		panel.add(addressContactMechId);
		panel.add(phoneContactMechId);
		panel.add(mobileContactMechId);
		//hidden fields

		//gwtrpc call to get the countries
		getContactDetails();
		getCountries();
		//getStates();

		return panel;
	}

	public void onModuleLoad() {
		RootPanel.get().add(mainPanel);
	}
	
	public Widget createTitle(String text, boolean logo) {
		
		Image titleImg = new Image("images/user_reg_4.jpeg");
		titleImg.setStyleName("imgStyle");
		//titleImg.setSize("30px", "30px");

		Label title = new Label(text);
		title.setStyleName("formTitle");

		FlexTable table = new FlexTable();
		table.setStyleName("fpStyle");
		table.setWidth("100%");

		if(logo) {
			table.getFlexCellFormatter().setWidth(0, 0, "20px");
			table.setWidget(0, 0, titleImg);
		}
		
		table.setWidget(0, 1, title);
		
		table.setBorderWidth(0);
		
		return table; 
	}
	
	//start - util methods
	public void setHiddenFields(HashMap<String, String> fields) {

		emailContactMechId.setValue(fields.get("emailContactMechId"));
		addressContactMechId.setValue(fields.get("addressContactMechId"));
		phoneContactMechId.setValue(fields.get("phoneContactMechId"));
		mobileContactMechId.setValue(fields.get("mobileContactMechId"));
	}
	
	public void setCountries(List<HashMap<String, String>> countries) {
		
		if(country.getSelectedIndex() == -1) {
			WidgetUtil.fillListBox(country, "geoId", "geoName", countries);
		} else {
			String countryGeoId = country.getValue(country.getSelectedIndex());
			WidgetUtil.fillListBox(country, countryGeoId, "geoId", "geoName", countries);
		}
	}
	
	public void setStates(List<HashMap<String, String>> states) {

		if(state.getSelectedIndex() == -1) {
			WidgetUtil.fillListBox(state, "geoId", "geoName", states);
		} else {
			String stateProvinceGeoId = state.getValue(state.getSelectedIndex());
			WidgetUtil.fillListBox(state, stateProvinceGeoId, "geoId", "geoName", states);
		}
	}
	
	private PopupPanel popup = new PopupPanel(false, true);
	
	public void showLoadingMessage(String msg) {

		popup.clear();

		Image loadingImg = new Image("images/data-loader.gif");
		Label lbl = new Label(msg);

		FlowPanel fp = new FlowPanel();
		fp.add(loadingImg);
		fp.add(lbl);
		
		popup.add(fp);
		popup.center();
	}
	
	public void hideLoadingMessage() {
		popup.clear();
		popup.hide();
	}
	
	public HashMap<String, String> getFormData() {

		HashMap<String, String> formData = new HashMap<String, String>();
		formData.put("firstName", name.getValue());
		
		formData.put("emailContactMechId", emailContactMechId.getValue());
		formData.put("emailId", email.getValue());
		
		formData.put("addressContactMechId", addressContactMechId.getValue());
		
		if(state.getSelectedIndex() != -1) {
			formData.put("stateProvinceGeoId", state.getValue(state.getSelectedIndex()));
		}
		
		if(country.getSelectedIndex() != -1) {
			formData.put("countryGeoId", country.getValue(country.getSelectedIndex()));
		}

		formData.put("phoneContactMechId", phoneContactMechId.getValue());
		formData.put("phoneNo", phone.getValue());
		
		formData.put("mobileContactMechId", mobileContactMechId.getValue());
		formData.put("mobileNo", mobile.getValue());

		return formData;
	}
	
	public void setFormData(HashMap<String, String> formData) {
		
		name.setText(formData.get("firstName"));
		
		emailContactMechId.setValue(formData.get("emailContactMechId"));
		email.setText(formData.get("emailId"));

		addressContactMechId.setValue(formData.get("addressContactMechId"));

		if(state.getItemCount() == 1 && state.getValue(0).equals("select")) {
			state.addItem(formData.get("stateProvinceGeoName"), formData.get("stateProvinceGeoId"));
			WidgetUtil.setListBoxSelectedIndex(state, formData.get("stateProvinceGeoId"));
		} else {
			WidgetUtil.setListBoxSelectedIndex(state, formData.get("stateProvinceGeoId"));
		}

		if(country.getItemCount() == 1 && country.getValue(0).equals("select")) {
			country.addItem(formData.get("countryGeoName"), formData.get("countryGeoId"));
			WidgetUtil.setListBoxSelectedIndex(country, formData.get("countryGeoId"));
		} else {
			WidgetUtil.setListBoxSelectedIndex(country, formData.get("countryGeoId"));
		}

		phoneContactMechId.setValue(formData.get("phoneContactMechId"));
		phone.setText(formData.get("phoneNo"));
		
		mobileContactMechId.setValue(formData.get("mobileContactMechId"));
		mobile.setText(formData.get("mobileNo"));
	}
	//end - util methods
	
	//start - code for gwtrpc calls
	private void getContactDetails() {

		showLoadingMessage("loading contact details ....");

		//start - get contact details
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("userName", partyId);

		AsyncCallback<HashMap<String, Object>> callback = new AsyncCallback<HashMap<String, Object>>() {
			
			public void onFailure(Throwable caught) {
				
				Window.alert("SERVER_ERROR " + caught.toString());
			}
			
			public void onSuccess(HashMap<String, Object> result) {

				if(ServiceUtil.isSuccess(result)) {

					HashMap<String, String> res = (HashMap<String, String>)result.get("result");

					//HashMap<String, String> formData = new HashMap<String, String>();
					formData.put("firstName", res.get("firstName"));
					
					formData.put("emailContactMechId", res.get("emailContactMechId"));
					formData.put("emailId", res.get("emailId"));
					
					formData.put("addressContactMechId", res.get("addressContactMechId"));
					formData.put("stateProvinceGeoId", res.get("stateProvinceGeoId"));
					formData.put("stateProvinceGeoName", res.get("stateProvinceGeoName"));
					formData.put("countryGeoId", res.get("countryGeoId"));
					formData.put("countryGeoName", res.get("countryGeoName"));
					
					formData.put("phoneContactMechId", res.get("phoneContactMechId"));
					formData.put("phoneNo", res.get("phoneNo"));
					
					formData.put("mobileContactMechId", res.get("mobileContactMechId"));
					formData.put("mobileNo", res.get("mobileNo"));

					//setFormData(formData);
					
					//getStates();
					
					//
					customerTable.setText(1, 0, formData.get("firstName"));
					customerTable.setText(1, 1, formData.get("emailId"));
					customerTable.setText(1, 2, formData.get("countryGeoName"));
					customerTable.setText(1, 3, formData.get("stateProvinceGeoName"));
					customerTable.setText(1, 4, formData.get("phoneNo"));
					customerTable.setText(1, 5, formData.get("mobileNo"));
					Button btnDelete = new Button("Delete");
					btnDelete.setEnabled(false);
					customerTable.setWidget(1, 6, btnDelete);
					//

					hideLoadingMessage();
				}
			}
		};

		String GET_CONTACT_DETAILS = CONTEXT_URL + "getContactDetails";
		endpoint.setServiceEntryPoint(GET_CONTACT_DETAILS);
		gwtrcpService.processRequest(parameters, callback);
		//end - get contact details
	}
	
	private void getCountries() {

		//start - for countries
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("fields", "geoId,geoName");

		AsyncCallback<HashMap<String, Object>> callback = new AsyncCallback<HashMap<String, Object>>() {
			
			public void onFailure(Throwable caught) {
				Window.alert("SERVER_ERROR " + caught.toString());
			}
			
			public void onSuccess(HashMap<String, Object> result) {

				if(ServiceUtil.isSuccess(result)) {
					
					if(result.get("result") instanceof List) {

						List<HashMap<String, String>> countries = (List<HashMap<String, String>>)result.get("result");
						setCountries(countries);
					}
				}
			}
		};

		String GET_COUNTRIES = CONTEXT_URL + "countries";
		endpoint.setServiceEntryPoint(GET_COUNTRIES);
		gwtrcpService.processRequest(parameters, callback);
		//end - for countries
	}
	
	private void getStates() {

		if(country.getSelectedIndex() != -1) {

			//start - for states
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("countryId", country.getValue(country.getSelectedIndex()));
			parameters.put("fields", "geoId,geoName");
	
			AsyncCallback<HashMap<String, Object>> callback = new AsyncCallback<HashMap<String, Object>>() {
				
				public void onFailure(Throwable caught) {
					Window.alert("SERVER_ERROR " + caught.toString());
				}
				
				public void onSuccess(HashMap<String, Object> result) {
	
					if(ServiceUtil.isSuccess(result)) {
						
						if(result.get("result") instanceof List) {
	
							List<HashMap<String, String>> states = (List<HashMap<String, String>>)result.get("result");
							setStates(states);
						}
					}
				}
			};
	
			String GET_STATES = CONTEXT_URL + "states";
			endpoint.setServiceEntryPoint(GET_STATES);
			gwtrcpService.processRequest(parameters, callback);
			//end - for states
		}
	}
	//end - code for gwtrpc calls
}
