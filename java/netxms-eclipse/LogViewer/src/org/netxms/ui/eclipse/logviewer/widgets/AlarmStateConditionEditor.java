/**
 * NetXMS - open source network management system
 * Copyright (C) 2003-2012 Victor Kirhenshtein
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
package org.netxms.ui.eclipse.logviewer.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.netxms.client.constants.ColumnFilterType;
import org.netxms.client.events.Alarm;
import org.netxms.client.log.ColumnFilter;
import org.netxms.ui.eclipse.logviewer.Activator;
import org.netxms.ui.eclipse.logviewer.Messages;
import org.netxms.ui.eclipse.logviewer.views.helpers.LogLabelProvider;
import org.netxms.ui.eclipse.widgets.ImageCombo;

/**
 * Condition editor for alarm state columns
 */
public class AlarmStateConditionEditor extends ConditionEditor {
	private static final String[] OPERATIONS = {
			Messages.get().AlarmStateConditionEditor_Is,
			Messages.get().AlarmStateConditionEditor_IsNot };

	private ImageCombo state;

	/**
	 * @param parent
	 * @param toolkit
	 * @param column
	 * @param parentElement
	 */
	public AlarmStateConditionEditor(Composite parent, FormToolkit toolkit) {
		super(parent, toolkit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netxms.ui.eclipse.logviewer.widgets.ConditionEditor#getOperations()
	 */
	@Override
	protected String[] getOperations() {
		return OPERATIONS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netxms.ui.eclipse.logviewer.widgets.ConditionEditor#createContent
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createContent(Composite parent, ColumnFilter initialFilter) {
		final Image[] stateImages = new Image[4];
		stateImages[Alarm.STATE_OUTSTANDING] = Activator.getImageDescriptor(
				"icons/outstanding.png").createImage(); //$NON-NLS-1$
		stateImages[Alarm.STATE_ACKNOWLEDGED] = Activator.getImageDescriptor(
				"icons/acknowledged.png").createImage(); //$NON-NLS-1$
		stateImages[Alarm.STATE_RESOLVED] = Activator.getImageDescriptor(
				"icons/resolved.png").createImage(); //$NON-NLS-1$
		stateImages[Alarm.STATE_TERMINATED] = Activator.getImageDescriptor(
				"icons/terminated.png").createImage(); //$NON-NLS-1$

		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				for (Image i : stateImages)
					i.dispose();
			}
		});

		state = new ImageCombo(this, SWT.READ_ONLY | SWT.BORDER);
		toolkit.adapt(state);
		for (int i = Alarm.STATE_OUTSTANDING; i <= Alarm.STATE_TERMINATED; i++)
			state.add(stateImages[i], LogLabelProvider.ALARM_STATE_TEXTS[i]);
		state.select(0);
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		state.setLayoutData(gd);

		if ((initialFilter != null)
				&& (initialFilter.getType() == ColumnFilterType.EQUALS)) {
			setSelectedOperation(initialFilter.isNegated() ? 1 : 0);
			state.select((int) initialFilter.getNumericValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.netxms.ui.eclipse.logviewer.widgets.ConditionEditor#createFilter()
	 */
	@Override
	public ColumnFilter createFilter() {
		ColumnFilter filter = new ColumnFilter(ColumnFilterType.EQUALS,
				state.getSelectionIndex());
		filter.setNegated(getSelectedOperation() == 1);
		return filter;
	}
}
