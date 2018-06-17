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
package org.netxms.client.objects;

import org.netxms.base.NXCPMessage;
import org.netxms.client.NXCSession;

/**
 * Generic NetXMS object class (for built-in classes)
 */
public class GenericObject extends AbstractObject {
	/**
	 * Create dummy object of GENERIC class
	 * 
	 * @param id
	 *            object ID to set
	 * @param session
	 *            associated session
	 */
	protected GenericObject(final long id, final NXCSession session) {
		super(id, session);
	}

	/**
	 * Create object from NXCP message
	 * 
	 * @param msg
	 *            Message to create object from
	 * @param session
	 *            Associated client session
	 */
	public GenericObject(final NXCPMessage msg, final NXCSession session) {
		super(msg, session);
	}
}
