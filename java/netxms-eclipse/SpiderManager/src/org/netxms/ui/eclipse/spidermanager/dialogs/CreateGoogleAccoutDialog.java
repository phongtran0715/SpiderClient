/**
 * NetXMS - open source network management system
 * Copyright (C) 2003-2009 Victor Kirhenshtein
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.netxms.ui.eclipse.spidermanager.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.spider.base.SpiderCodes;

/**
 * User database object creation dialog
 * 
 */
public class CreateGoogleAccoutDialog extends Dialog {
	private Text txtUserName;
	private Text txtClientSecret;
	private Text txtClientId;
	private Text txtAppName;
	private Text txtApiKey;
	Combo cbAccountType;

	private String userName;
	private String clientSecret;
	private String clientId;
	private String appName;
	private String apiKey;
	private int accountType;

	public CreateGoogleAccoutDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		GridData gridData;
		dialogArea.setLayout(null);
		gridData = new GridData(GridData.VERTICAL_ALIGN_END);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		gridData = new GridData(GridData.VERTICAL_ALIGN_END);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;

		Group grpCreateNewAccount = new Group(dialogArea, SWT.NONE);
		grpCreateNewAccount.setText("Create new google account");
		grpCreateNewAccount.setBounds(5, 10, 438, 233);

		Label label = new Label(grpCreateNewAccount, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setText("Email");
		label.setBounds(10, 31, 95, 17);

		txtUserName = new Text(grpCreateNewAccount, SWT.BORDER);
		txtUserName.setTextLimit(145);
		txtUserName.setBounds(111, 26, 310, 27);

		Label label_1 = new Label(grpCreateNewAccount, SWT.NONE);
		label_1.setAlignment(SWT.RIGHT);
		label_1.setText("Client Secret");
		label_1.setBounds(10, 133, 95, 17);

		Label label_2 = new Label(grpCreateNewAccount, SWT.NONE);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setText("App Name");
		label_2.setBounds(10, 163, 95, 17);

		txtAppName = new Text(grpCreateNewAccount, SWT.BORDER);
		txtAppName.setTextLimit(40);
		txtAppName.setBounds(111, 158, 310, 27);

		Label label_3 = new Label(grpCreateNewAccount, SWT.NONE);
		label_3.setAlignment(SWT.RIGHT);
		label_3.setText("API Key");
		label_3.setBounds(10, 64, 95, 17);

		txtApiKey = new Text(grpCreateNewAccount, SWT.BORDER);
		txtApiKey.setTextLimit(145);
		txtApiKey.setBounds(111, 59, 310, 27);

		Label lblAccountType = new Label(grpCreateNewAccount, SWT.NONE);
		lblAccountType.setAlignment(SWT.RIGHT);
		lblAccountType.setText("Account Type");
		lblAccountType.setBounds(10, 197, 95, 17);

		cbAccountType = new Combo(grpCreateNewAccount, SWT.NONE);
		cbAccountType.setItems(new String[] {"Helper", "SEO", "Adsend"});
		cbAccountType.setBounds(111, 192, 310, 29);
		cbAccountType.select(0);
		
		Label lblClientId = new Label(grpCreateNewAccount, SWT.NONE);
		lblClientId.setText("Client ID");
		lblClientId.setAlignment(SWT.RIGHT);
		lblClientId.setBounds(10, 102, 95, 17);
		
		txtClientId = new Text(grpCreateNewAccount, SWT.BORDER);
		txtClientId.setTextLimit(145);
		txtClientId.setBounds(111, 92, 310, 27);
		
				txtClientSecret = new Text(grpCreateNewAccount, SWT.BORDER);
				txtClientSecret.setBounds(111, 125, 310, 27);
				txtClientSecret.setTextLimit(145);
		return dialogArea;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Create new google account");
	}

	@Override
	protected void okPressed() {
		userName = txtUserName.getText();
		clientSecret = txtClientSecret.getText();
		clientId = txtClientId.getText();
		appName = txtAppName.getText();
		apiKey = txtApiKey.getText();

		if(userName == null || userName.isEmpty())
		{
			MessageBox dialog =
					new MessageBox(getShell(), SWT.ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("User name must not empty!");
			dialog.open();
			return;	
		}
		if(cbAccountType.getText().equals("Helper"))
		{
			accountType = SpiderCodes.ACCOUNT_HELPER;
		}else if(cbAccountType.getText().equals("SEO")){
			accountType = SpiderCodes.ACCOUNT_SEO;
		}else{
			accountType = SpiderCodes.ACCOUNT_ADSEND;
		}
		super.okPressed();
	}

	public String getUserName() {
		return userName;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getAppName() {
		return appName;
	}

	public String getApiKey() {
		return apiKey;
	}

	public int getAccountType() {
		return accountType;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
