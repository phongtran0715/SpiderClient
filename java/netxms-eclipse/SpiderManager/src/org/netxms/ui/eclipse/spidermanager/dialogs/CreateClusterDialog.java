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
import org.netxms.client.NXCSession;
import org.netxms.ui.eclipse.shared.ConsoleSharedData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * User database object creation dialog
 * 
 */
public class CreateClusterDialog extends Dialog {
	private String clusterId;
	private String clusterName;
	private String ipAddress;
	private int port;
	private String userName;
	private String password;
	
	private Text txtClusterId;
	private Text txtClusterName;
	private Text txtIpAddress;
	private Text txtPort;
	private Text txtUserName;
	private Text txtPassword;
	
	private String formTitle;
	
	public CreateClusterDialog(Shell parentShell, String title) {
		super(parentShell);
		this.formTitle = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
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
		grpCreateNewAccount.setText("Create cluster");
		grpCreateNewAccount.setBounds(5, 10, 427, 225);

		Label lblChannelId = new Label(grpCreateNewAccount, SWT.NONE);
		lblChannelId.setAlignment(SWT.RIGHT);
		lblChannelId.setText("Cluters ID");
		lblChannelId.setBounds(10, 31, 109, 17);

		Label lblChannelName = new Label(grpCreateNewAccount, SWT.NONE);
		lblChannelName.setAlignment(SWT.RIGHT);
		lblChannelName.setText("Cluster Name");
		lblChannelName.setBounds(10, 66, 109, 17);

		Label lblGoogleAccount = new Label(grpCreateNewAccount, SWT.NONE);
		lblGoogleAccount.setAlignment(SWT.RIGHT);
		lblGoogleAccount.setText("IP Address");
		lblGoogleAccount.setBounds(10, 99, 109, 17);
		
		txtClusterId = new Text(grpCreateNewAccount, SWT.BORDER);
		txtClusterId.setTextLimit(40);
		txtClusterId.setBounds(131, 21, 290, 27);
		
		txtClusterName = new Text(grpCreateNewAccount, SWT.BORDER);
		txtClusterName.setTextLimit(145);
		txtClusterName.setBounds(131, 56, 290, 27);
		
		txtIpAddress = new Text(grpCreateNewAccount, SWT.BORDER);
		txtIpAddress.setTextLimit(150);
		txtIpAddress.setBounds(131, 89, 290, 27);
		
		txtPort = new Text(grpCreateNewAccount, SWT.BORDER);
		txtPort.setTextLimit(40);
		txtPort.setBounds(131, 122, 290, 27);
		
		Label lblPort = new Label(grpCreateNewAccount, SWT.NONE);
		lblPort.setText("Port");
		lblPort.setAlignment(SWT.RIGHT);
		lblPort.setBounds(10, 132, 109, 17);
		
		Label lblUsername = new Label(grpCreateNewAccount, SWT.NONE);
		lblUsername.setText("UserName");
		lblUsername.setAlignment(SWT.RIGHT);
		lblUsername.setBounds(10, 165, 109, 17);
		
		txtUserName = new Text(grpCreateNewAccount, SWT.BORDER | SWT.PASSWORD);
		txtUserName.setTextLimit(40);
		txtUserName.setBounds(131, 155, 290, 27);
		
		Label lblPassword = new Label(grpCreateNewAccount, SWT.NONE);
		lblPassword.setText("Password");
		lblPassword.setAlignment(SWT.RIGHT);
		lblPassword.setBounds(10, 198, 109, 17);
		
		txtPassword = new Text(grpCreateNewAccount, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setTextLimit(150);
		txtPassword.setBounds(131, 188, 290, 27);
		
		final Button cbShowPass = new Button(dialogArea, SWT.CHECK);
		cbShowPass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cbShowPass.getSelection())
				{
					txtUserName.setEchoChar((char)0);
					txtPassword.setEchoChar((char)0);
				}else{
					txtUserName.setEchoChar('*');
					txtPassword.setEchoChar('*');
				}
			}
		});
		cbShowPass.setBounds(438, 0, 26, 24);

		initialData();
		return dialogArea;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(this.formTitle);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		clusterId = txtClusterId.getText();
		clusterName = txtClusterName.getText();
		if(clusterId == null || clusterId.isEmpty())
		{
			MessageBox dialog =
					new MessageBox(getShell(), SWT.ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("Cluster ID must not empty!");
			dialog.open();
			return;
		}
		
		ipAddress = txtIpAddress.getText();
		if(ipAddress == null || ipAddress.isEmpty())
		{
			MessageBox dialog =
					new MessageBox(getShell(), SWT.ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("IP Address must not empty!");
			dialog.open();
			return;
		}
		userName = txtUserName.getText();
		password = txtPassword.getText();
		super.okPressed();
	}

	private void initialData()
	{
	}

	public String getClusterId() {
		return clusterId;
	}

	public String getClusterName() {
		return clusterName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public int getPort() {
		return port;
	}
	
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}
}
