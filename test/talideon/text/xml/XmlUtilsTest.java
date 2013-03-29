/*
 * Copyright (c) Keith Gaughan, 2006. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0, which accompanies this
 * distribution and is available at http://www.opensource.org/licenses/cpl1.0.php
 */

package talideon.text.xml;

import java.io.StringWriter;
import junit.framework.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class XmlUtilsTest extends TestCase {

	public XmlUtilsTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
	}

	public void testDocumentCreated() {
		assertNotNull(XmlUtils.newDocument());
	}

	public void testTransformCreated() {
		try {
			XmlUtils.transform(null, null);
			fail("Should have thrown an exception.");
		} catch (Exception ex) {
			// Success!
		}
	}

	public void testSingleChild() throws Exception {
		Document document = XmlUtils.newDocument();
		document.appendChild(document.createElement("child"));
		String asString = XmlUtils.toString(document);
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><child/>", asString);
	}
}
