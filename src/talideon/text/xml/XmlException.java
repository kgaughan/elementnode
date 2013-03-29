/*
 * ElementNode
 * by Keith Gaughan <kmgaughan@eircom.net>
 *
 * Copyright (c) Keith Gaughan, 2006. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0, which accompanies this
 * distribution and is available at http://www.opensource.org/licenses/cpl1.0.php
 */

package talideon.text.xml;

/**
 * Represents an error in ElementNode or XmlUtils.
 *
 * @author kgaughan
 */
public class XmlException extends RuntimeException {

	public XmlException() {
		super();
	}

	public XmlException(final String msg) {
		super(msg);
	}

	public XmlException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public XmlException(final Throwable cause) {
		super(cause);
	}
}
