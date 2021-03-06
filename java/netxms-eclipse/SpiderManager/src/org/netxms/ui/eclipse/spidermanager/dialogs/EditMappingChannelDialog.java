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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.netxms.client.NXCException;
import org.netxms.client.NXCSession;
import org.netxms.ui.eclipse.shared.ConsoleSharedData;
import org.spider.base.SpiderCodes;
import org.spider.client.ClusterObject;
import org.spider.client.HomeChannelObject;
import org.spider.client.MappingChannelObject;
import org.spider.client.MonitorChannelObject;
import org.spider.client.SpiderDefine;
import org.spider.client.SpiderDefine.*;
import org.spider.ui.eclipse.spidermanager.helper.ReadRenderConfigFile;
import org.spider.ui.eclipse.spidermanager.helper.WriteRenderConfigFile;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;

import spider.org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.wb.swt.SWTResourceManager;

public class EditMappingChannelDialog extends Dialog {
	private Text txtTimeSync;
	private Combo cbHome;
	private Combo cbStatus;
	private Combo cbDownload;
	private Label lblDownloadCluster;
	private Label lblRenderCid;
	private Combo cbRender;
	private Label lblUploadCid;
	private Combo cbUpload;
	Link linkHomeChanel;
	Link linkMonitorChanel;
	Object [] cHomeObject;
	Object [] cMoniorObject;
	Object [] downloadClusters;
	Object [] renderClusters;
	Object [] uploadClusters;
	private NXCSession session;
	private TabFolder tabFolder;
	private TabItem tbtmUploadConfig;
	private TabItem tbtmMappingConfig;
	private Group grpAbc;
	private Text txtTitle;
	private Label lblDescTemplate;
	private Button cbDesc;
	private Label lblTagsTemplate;
	private Button cbTag;
	private Text txtDesc;
	private Text txtTags;
	private TabItem tbtmRenderConfig;
	private Group grpRender;
	Button cbTitle;
	Label lbMonitorContent;

	private final int MONITOR_CHANNEL		= 0;
	private final int MONITOR_PLAYLIST		= 1;
	private final int MONITOR_KEYWORK		= 2;
	private final int LIST_ONLINE_VIDEO		= 3;
	private final int LIST_OFFLINE_VIDEO	= 4;

	SpiderDefine spiderDefine = new SpiderDefine();
	MappingConfig mappingConfig = spiderDefine.new MappingConfig();
	RenderConfig renderConfig = spiderDefine.new RenderConfig();
	UploadConfig uploadConfig = spiderDefine.new UploadConfig();

	MappingChannelObject object;
	private Label lblMappingType;
	private Combo cbMappingType;
	private Text txtMonitorContent;
	private Button btnImportMonitor;
	private StyledText txtRenderCmd;
	private ScrolledComposite scrolledComposite;

	public EditMappingChannelDialog(Shell parentShell, MappingChannelObject object) {
		super(parentShell);
		session = ConsoleSharedData.getSession();
		this.object = object;
		this.mappingConfig = object.getMappingConfig();
		this.renderConfig = object.getRenderConfig();
		this.uploadConfig = object.getUploadConfig();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);

		GridData gridData;
		gridData = new GridData(GridData.VERTICAL_ALIGN_END);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		gridData = new GridData(GridData.VERTICAL_ALIGN_END);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		dialogArea.setLayout(null);

		tabFolder = new TabFolder(dialogArea, SWT.NONE);
		tabFolder.setBounds(10, 10, 521, 464);

		tbtmMappingConfig = new TabItem(tabFolder, SWT.NONE);
		tbtmMappingConfig.setImage(ResourceManager.getPluginImage("org.spider.ui.eclipse.spidermanager", "icons/settings_16x16.png"));
		tbtmMappingConfig.setText("Mapping Config");

		Group grpCreateNewAccount = new Group(tabFolder, SWT.NONE);
		tbtmMappingConfig.setControl(grpCreateNewAccount);
		grpCreateNewAccount.setText("Create new mapping channel");

		Label lblChannelId = new Label(grpCreateNewAccount, SWT.NONE);
		lblChannelId.setAlignment(SWT.RIGHT);
		lblChannelId.setText("C Home ID");
		lblChannelId.setBounds(10, 31, 109, 17);

		lbMonitorContent = new Label(grpCreateNewAccount, SWT.NONE);
		lbMonitorContent.setAlignment(SWT.RIGHT);
		lbMonitorContent.setText("C Monitor ID");
		lbMonitorContent.setBounds(10, 140, 109, 17);

		Label lblGoogleAccount = new Label(grpCreateNewAccount, SWT.NONE);
		lblGoogleAccount.setAlignment(SWT.RIGHT);
		lblGoogleAccount.setText("Time Sync");
		lblGoogleAccount.setBounds(10, 204, 109, 17);

		txtTimeSync = new Text(grpCreateNewAccount, SWT.BORDER);
		txtTimeSync.setText("600");
		txtTimeSync.setTextLimit(150);
		txtTimeSync.setBounds(131, 199, 290, 27);

		Label lblVideoIntro = new Label(grpCreateNewAccount, SWT.NONE);
		lblVideoIntro.setAlignment(SWT.RIGHT);
		lblVideoIntro.setText("Sync Status");
		lblVideoIntro.setBounds(10, 250, 109, 17);

		cbHome = new Combo(grpCreateNewAccount, SWT.READ_ONLY);
		cbHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String cName = getHomeChannelName(cbHome.getText());
				linkHomeChanel.setText("Go to <a href=\"https://www.youtube.com/channel\">" + cName + "</a> channel." );
			}
		});
		cbHome.setItems(new String[] {});
		cbHome.setBounds(132, 19, 289, 29);

		cbStatus = new Combo(grpCreateNewAccount, SWT.NONE);
		cbStatus.setItems(new String[] {"disable", "enable"});
		cbStatus.setBounds(132, 245, 289, 29);
		cbStatus.select(0);

		linkHomeChanel = new Link(grpCreateNewAccount, SWT.NONE);
		linkHomeChanel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cbHome.getText().isEmpty() == false)
				{
					Program.launch("https://www.youtube.com/channel/" + cbHome.getText());	
				}
			}
		});
		linkHomeChanel.setBounds(131, 54, 290, 17);
		linkHomeChanel.setText("<a></a>");

		linkMonitorChanel = new Link(grpCreateNewAccount, 0);
		linkMonitorChanel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(txtMonitorContent.getText().isEmpty() == false)
				{
					Program.launch("https://www.youtube.com/channel/" + txtMonitorContent.getText());	
				}
			}
		});
		linkMonitorChanel.setText("<a></a>");
		linkMonitorChanel.setBounds(131, 165, 290, 17);

		cbDownload = new Combo(grpCreateNewAccount, SWT.NONE);
		cbDownload.setItems(new String[] {});
		cbDownload.setBounds(132, 291, 289, 29);

		lblDownloadCluster = new Label(grpCreateNewAccount, SWT.NONE);
		lblDownloadCluster.setText("Download CID");
		lblDownloadCluster.setAlignment(SWT.RIGHT);
		lblDownloadCluster.setBounds(10, 303, 109, 17);

		lblRenderCid = new Label(grpCreateNewAccount, SWT.NONE);
		lblRenderCid.setText("Render CID");
		lblRenderCid.setAlignment(SWT.RIGHT);
		lblRenderCid.setBounds(10, 349, 109, 17);

		cbRender = new Combo(grpCreateNewAccount, SWT.NONE);
		cbRender.setItems(new String[] {});
		cbRender.setBounds(132, 337, 289, 29);

		lblUploadCid = new Label(grpCreateNewAccount, SWT.NONE);
		lblUploadCid.setText("Upload CID");
		lblUploadCid.setAlignment(SWT.RIGHT);
		lblUploadCid.setBounds(10, 392, 109, 17);

		cbUpload = new Combo(grpCreateNewAccount, SWT.NONE);
		cbUpload.setItems(new String[] {});
		cbUpload.setBounds(132, 380, 289, 29);

		lblMappingType = new Label(grpCreateNewAccount, SWT.NONE);
		lblMappingType.setText("Mapping Type");
		lblMappingType.setAlignment(SWT.RIGHT);
		lblMappingType.setBounds(10, 91, 109, 17);

		cbMappingType = new Combo(grpCreateNewAccount, SWT.DROP_DOWN | SWT.READ_ONLY);
		cbMappingType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = cbMappingType.getSelectionIndex();
				switch (index) {
				case MONITOR_CHANNEL:
					lbMonitorContent.setText("CMonitor ID");
					btnImportMonitor.setVisible(false);
					break;
				case MONITOR_PLAYLIST:
					lbMonitorContent.setText("Playlist ID");
					btnImportMonitor.setVisible(false);
					break;
				case MONITOR_KEYWORK:
					lbMonitorContent.setText("Keywork");
					btnImportMonitor.setVisible(false);
					break;
				case LIST_ONLINE_VIDEO:
					lbMonitorContent.setText("Video list");
					btnImportMonitor.setVisible(true);
					break;
				case LIST_OFFLINE_VIDEO:
					lbMonitorContent.setText("Video location");
					btnImportMonitor.setVisible(true);
					break;
				default:
					break;
				}
			}
		});
		cbMappingType.setItems(new String[] {"Monitor Channel", "Monitor Playlist", "Monitor Keyword", "List Online Video", "List Offline Video"});
		cbMappingType.setBounds(132, 79, 289, 29);
		cbMappingType.select(0);

		txtMonitorContent = new Text(grpCreateNewAccount, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		txtMonitorContent.setEditable(false);
		txtMonitorContent.setTextLimit(150);
		txtMonitorContent.setBounds(131, 130, 290, 27);

		btnImportMonitor = new Button(grpCreateNewAccount, SWT.NONE);
		btnImportMonitor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getShell(), SWT.SINGLE);
				dlg.setText("Select video list file");
				String[] filterExt = { "*.txt", "*.*" };
				dlg.setFilterExtensions(filterExt);
				if (dlg.open() != null) {
					String filePath = dlg.getFilterPath();
					String fileName = dlg.getFileName();
					String fileData = "";
					File file = new File(filePath + "/" + fileName);
					Scanner sc;
					try {
						sc = new Scanner(file);
						while (sc.hasNextLine())
							fileData += sc.nextLine() + ";";
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
					txtMonitorContent.setText(fileData);
				}
			}
		});
		btnImportMonitor.setBounds(423, 128, 84, 29);
		btnImportMonitor.setText("Import");

		tbtmRenderConfig = new TabItem(tabFolder, SWT.NONE);
		tbtmRenderConfig.setImage(ResourceManager.getPluginImage("org.spider.ui.eclipse.spidermanager", "icons/render.png"));
		tbtmRenderConfig.setText("Render Config");

		grpRender = new Group(tabFolder, SWT.NONE);
		grpRender.setText("Render");
		tbtmRenderConfig.setControl(grpRender);
		grpRender.setLayout(null);

		Button btnImport = new Button(grpRender, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getShell(), SWT.SINGLE);
				dlg.setText("Open render config template file");
				String[] filterExt = { "*.xml", "*.*" };
				dlg.setFilterExtensions(filterExt);
				if (dlg.open() != null) {
					String filePath = dlg.getFilterPath();
					String names = dlg.getFileName();

					System.out.println(names);
					ReadRenderConfigFile readXML = new ReadRenderConfigFile();
					RenderConfig renderCfg = readXML.read(filePath + "/" + names);
					txtRenderCmd.setText(renderCfg.renderCmd);
				}
			}
		});
		btnImport.setBounds(10, 394, 95, 29);
		btnImport.setText("Import");

		Button btnExport = new Button(grpRender, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog
				.setFilterNames(new String[] { "XML file (.xml)", "All Files (*.*)" });
				dialog.setFilterExtensions(new String[] { "*.xml", "*.*" }); 
				dialog.setFileName("config.xml");
				String filePath = dialog.open();
				WriteRenderConfigFile writeXML = new WriteRenderConfigFile();
				RenderConfig renderCfg = new SpiderDefine().new RenderConfig(txtRenderCmd.getText());
				writeXML.write(filePath, renderCfg);
			}
		});
		btnExport.setBounds(124, 394, 95, 29);
		btnExport.setText("Export");
		
		scrolledComposite = new ScrolledComposite(grpRender, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		scrolledComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		scrolledComposite.setBounds(10, 24, 497, 364);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		txtRenderCmd = new StyledText(scrolledComposite, SWT.BORDER | SWT.WRAP);
		txtRenderCmd.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		txtRenderCmd.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		scrolledComposite.setContent(txtRenderCmd);
		scrolledComposite.setMinSize(txtRenderCmd.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		tbtmUploadConfig = new TabItem(tabFolder, SWT.NONE);
		tbtmUploadConfig.setImage(ResourceManager.getPluginImage("org.spider.ui.eclipse.spidermanager", "icons/upload.png"));
		tbtmUploadConfig.setText("Upload Config");

		grpAbc = new Group(tabFolder, SWT.NONE);
		grpAbc.setText("Upload");
		tbtmUploadConfig.setControl(grpAbc);
		grpAbc.setLayout(null);

		Label lblNewLabel = new Label(grpAbc, SWT.CENTER);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(23, 74, 109, 17);
		lblNewLabel.setText("Title template");

		txtTitle = new Text(grpAbc, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtTitle.setBounds(138, 31, 301, 102);

		cbTitle = new Button(grpAbc, SWT.CHECK);
		cbTitle.setBounds(445, 78, 26, 24);

		lblDescTemplate = new Label(grpAbc, SWT.CENTER);
		lblDescTemplate.setText("Desc template");
		lblDescTemplate.setAlignment(SWT.RIGHT);
		lblDescTemplate.setBounds(23, 192, 109, 17);

		cbDesc = new Button(grpAbc, SWT.CHECK);
		cbDesc.setBounds(445, 192, 26, 24);

		lblTagsTemplate = new Label(grpAbc, SWT.CENTER);
		lblTagsTemplate.setText("Tags template");
		lblTagsTemplate.setAlignment(SWT.RIGHT);
		lblTagsTemplate.setBounds(23, 319, 109, 17);

		cbTag = new Button(grpAbc, SWT.CHECK);
		cbTag.setBounds(445, 312, 26, 24);

		txtDesc = new Text(grpAbc, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtDesc.setBounds(138, 148, 301, 102);

		txtTags = new Text(grpAbc, SWT.BORDER | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		txtTags.setBounds(138, 271, 301, 102);

		initialData();
		return dialogArea;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Create new mapping channel");
	}

	@Override
	protected void okPressed() {
		mappingConfig.cHomeId = cbHome.getText();
		if(mappingConfig.cHomeId == null || mappingConfig.cHomeId.isEmpty())
		{
			MessageBox dialog =
					new MessageBox(getShell(), SWT.ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("Home channel ID must not empty!");
			dialog.open();
			return;
		}

		mappingConfig.monitorContent = txtMonitorContent.getText();
		if(mappingConfig.monitorContent == null || mappingConfig.monitorContent.isEmpty())
		{
			MessageBox dialog =
					new MessageBox(getShell(), SWT.ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("Monitor channel ID must not empty!");
			dialog.open();
			return;
		}

		String strTime = txtTimeSync.getText();
		if(strTime == null || strTime.isEmpty())
		{
			MessageBox dialog =
					new MessageBox(getShell(), SWT.ERROR | SWT.OK);
			dialog.setText("Error");
			dialog.setMessage("Time sync interval must not empty!");
			dialog.open();
			return;
		}
		mappingConfig.timeSync = Integer.parseInt(strTime);

		if(cbStatus.getText().equals("disable"))
		{
			mappingConfig.statusSync = 0;
		}
		else{
			mappingConfig.statusSync = 1;
		}
		mappingConfig.downloadClusterId = cbDownload.getText();
		mappingConfig.renderClusterId = cbRender.getText();
		mappingConfig.uploadClusterId = cbUpload.getText();
		mappingConfig.mappingType = cbMappingType.getSelectionIndex();
		renderConfig.renderCmd = txtRenderCmd.getText();
		uploadConfig.titleTemp = txtTitle.getText();
		uploadConfig.descTemp = txtDesc.getText();
		uploadConfig.tagTemp = txtTags.getText();
		uploadConfig.enableTitle = cbTitle.getSelection();
		uploadConfig.enableDesc = cbDesc.getSelection();
		uploadConfig.enableTag = cbTag.getSelection();

		super.okPressed();
	}

	private void initialData()
	{
		
		//Initial data
		try {
			if(cHomeObject == null)
			{
				cHomeObject = session.getHomeChannel();
				setHomeChannelData();
			}
		} catch (IOException | NXCException e1) {
			e1.printStackTrace();
		}

		if(downloadClusters == null)
		{
			try {
				downloadClusters = session.getCluster(SpiderCodes.CLUSTER_DOWNLOAD);
				setDownloadCluster();
			} catch (IOException | NXCException e) {
				e.printStackTrace();
			}
		}
		if(renderClusters == null)
		{
			try {
				renderClusters = session.getCluster(SpiderCodes.CLUSTER_RENDER);
				setRenderCluster();
			} catch (IOException | NXCException e) {
				e.printStackTrace();
			}
		}
		if(uploadClusters == null)
		{
			try {
				uploadClusters = session.getCluster(SpiderCodes.CLUSTER_UPLOAD);
				setUploadCluster();
			} catch (IOException | NXCException e) {
				e.printStackTrace();
			}
		}
		//Set data for field
		cbHome.setText(object.getMappingConfig().cHomeId);
		String cHomeName = getHomeChannelName(object.getMappingConfig().cHomeId);
		linkHomeChanel.setText("Go to <a href=\"https://www.youtube.com/channel\">" + cHomeName + "</a> channel." );
		txtMonitorContent.setText(mappingConfig.monitorContent);
		//String cMonitorName = getMonitorChannelName(object.getMappingConfig().monitorContent);
		//linkMonitorChanel.setText("Go to <a href=\"https://www.youtube.com/channel\">" + cMonitorName + "</a> channel." );

		txtTimeSync.setText(Long.toString(object.getMappingConfig().timeSync));
		if(object.getMappingConfig().statusSync == 0)
		{
			cbStatus.setText("disable");
		}else{
			cbStatus.setText("enable");
		}
		cbDownload.setText(object.getMappingConfig().downloadClusterId);
		cbRender.setText(object.getMappingConfig().renderClusterId);
		cbUpload.setText(object.getMappingConfig().uploadClusterId);
		cbMappingType.select(object.getMappingConfig().mappingType);

		RenderConfig renderConfig = object.getRenderConfig();
		txtRenderCmd.setText(renderConfig.renderCmd);
		
		UploadConfig uploadConfig = object.getUploadConfig();
		txtTitle.setText(uploadConfig.titleTemp);
		txtDesc.setText(uploadConfig.descTemp);
		txtTags.setText(uploadConfig.tagTemp);
		cbTitle.setSelection(uploadConfig.enableTitle);
		cbDesc.setSelection(uploadConfig.enableDesc);
		cbTag.setSelection(uploadConfig.enableTag);
		
		switch (object.getMappingConfig().mappingType) {
		case MONITOR_CHANNEL:
			lbMonitorContent.setText("CMonitor Id");
			btnImportMonitor.setVisible(false);
			break;
		case MONITOR_PLAYLIST:
			lbMonitorContent.setText("Playlist Id");
			btnImportMonitor.setVisible(false);
			break;
		case MONITOR_KEYWORK:
			lbMonitorContent.setText("Keyword");
			btnImportMonitor.setVisible(false);
			break;
		case LIST_ONLINE_VIDEO:
		case LIST_OFFLINE_VIDEO:
			lbMonitorContent.setText("List video");
			btnImportMonitor.setVisible(true);
			btnImportMonitor.setEnabled(false);
			break;
		default:
			break;
		}
	}

	private void setHomeChannelData()
	{
		if(cHomeObject != null)
		{
			for(int i = 0; i < cHomeObject.length; i++)
			{
				Object homeObj = cHomeObject[i];
				if(homeObj instanceof HomeChannelObject)
				{
					cbHome.add(((HomeChannelObject) homeObj).getChannelId());
				}
			}
		}
	}
	/*
	private void setMonitorChannelData()
	{
		if(cMoniorObject != null)
		{
			for(int i = 0; i < cMoniorObject.length; i++)
			{
				Object monitorObj = cMoniorObject[i];
				if(monitorObj instanceof MonitorChannelObject)
				{
					cbMonitor.add(((MonitorChannelObject) monitorObj).getChannelId());
				}
			}
		}
	}
	 */

	private void setDownloadCluster()
	{
		if(downloadClusters != null)
		{
			for(int i = 0; i < downloadClusters.length; i++)
			{
				Object object = downloadClusters[i];
				if(object instanceof ClusterObject)
				{
					cbDownload.add(((ClusterObject) object).getClusterId());
				}
			}
		}
	}

	private void setRenderCluster()
	{
		if(renderClusters != null)
		{
			for(int i = 0; i < renderClusters.length; i++)
			{
				Object object = renderClusters[i];
				if(object instanceof ClusterObject)
				{
					cbRender.add(((ClusterObject) object).getClusterId());
				}
			}
		}
	}

	private void setUploadCluster()
	{
		if(uploadClusters != null)
		{
			for(int i = 0; i < uploadClusters.length; i++)
			{
				Object object = uploadClusters[i];
				if(object instanceof ClusterObject)
				{
					cbUpload.add(((ClusterObject) object).getClusterId());
				}
			}
		}
	}

	private String getHomeChannelName(String cHomeID)
	{
		String result = "";
		for (Object it : cHomeObject) {
			if(it instanceof HomeChannelObject)
			{
				if(((HomeChannelObject) it).getChannelId().equals(cHomeID))
				{
					result = ((HomeChannelObject) it).getChannelName();
				}
			}
		}
		return result;
	}
	/*
	private String getMonitorChannelName(String cMonitorID)
	{
		String result = "";
		for (Object it : cMoniorObject) {
			if(it instanceof MonitorChannelObject)
			{
				if(((MonitorChannelObject) it).getChannelId().equals(cMonitorID))
				{
					result = ((MonitorChannelObject) it).getChannelName();
				}
			}
		}
		return result;
	}
	 */

	public MappingConfig getMappingConfig() {
		return mappingConfig;
	}

	public void setMappingConfig(MappingConfig mappingConfig) {
		this.mappingConfig = mappingConfig;
	}

	public RenderConfig getRenderConfig() {
		return renderConfig;
	}

	public void setRenderConfig(RenderConfig renderConfig) {
		this.renderConfig = renderConfig;
	}

	public UploadConfig getUploadConfig() {
		return uploadConfig;
	}

	public void setUploadConfig(UploadConfig uploadConfig) {
		this.uploadConfig = uploadConfig;
	}
}
