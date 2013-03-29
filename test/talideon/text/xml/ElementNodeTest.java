/*
 * Copyright (c) Keith Gaughan, 2006. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Common Public License v1.0, which accompanies this
 * distribution and is available at http://www.opensource.org/licenses/cpl1.0.php
 */

package talideon.text.xml;

import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import org.w3c.dom.Node;

public class ElementNodeTest extends TestCase {

	private static final String P = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private ElementNode root;
	private ElementNode rootWithAttributes;

	public ElementNodeTest(String testName) {
		super(testName);
	}

	protected void setUp() throws Exception {
		root = new ElementNode("root");

		Map attrs = new HashMap();
		attrs.put("zoop", "zing");
		rootWithAttributes = new ElementNode("root", attrs);
	}

	public void testConstruction() {
		assertEquals(P + "<root/>", root.toString());
	}

	public void testCreateWithAttributes() {
		assertEquals(P + "<root zoop=\"zing\"/>", rootWithAttributes.toString());
	}

	public void testAddText() {
		root.addText("This is ").addText("the text ").addText(null).addText("I'm adding.");
		assertEquals(P + "<root>This is the text I'm adding.</root>", root.toString());
	}

	public void testAttributes() {
		root.addAttribute("lang", "fr").addAttribute("word", "bond");
		assertEquals(P + "<root lang=\"fr\" word=\"bond\"/>", root.toString());
	}

	public void testChild() {
		root.addChild("branch");
		assertEquals(P + "<root><branch/></root>", root.toString());
	}

	public void testChildWithAttributes() {
		Map attrs = new HashMap();
		attrs.put("word", "bond");
		root.addChild("branch", attrs);
		assertEquals(P + "<root><branch word=\"bond\"/></root>", root.toString());
	}

	public void testTextChildWithAttributes() {
		Map attrs = new HashMap();
		attrs.put("word", "bond");
		root.addChild("branch", "Foo", attrs);
		assertEquals(P + "<root><branch word=\"bond\">Foo</branch></root>", root.toString());
	}

	public void testMultipleChildren() {
		root.addChild("branch");
		root.addChild("another-branch");
		assertEquals(P + "<root><branch/><another-branch/></root>", root.toString());
	}

	public void testTextChild() {
		root.addChild("branch", "Not chopsticks, spoons!");
		assertEquals(P + "<root><branch>Not chopsticks, spoons!</branch></root>", root.toString());
	}

	public void testInlineTextChild() {
		root.addTextChild("branch", "a").addTextChild("another-branch", "b");
		assertEquals(P + "<root><branch>a</branch><another-branch>b</another-branch></root>", root.toString());
	}

	public void testChildAttributes() {
		root.addChild("branch")
			.addAttribute("lang", "fr")
			.addAttribute("word", "bond");
		assertEquals(P + "<root><branch lang=\"fr\" word=\"bond\"/></root>", root.toString());
	}

	public void testExceptions() {
		try {
			ElementNode shouldThrow = new ElementNode(null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addAttribute(null, null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addAttribute("bad attribute", "");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild((String) null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild((org.w3c.dom.Node) null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild("");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild("bad tag");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addAttribute(null, null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addAttribute("", "");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild("t", (Map) null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild("", "");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild(null, null, null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addChild("a", "b", null);
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addTextChild(null, "a");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		try {
			root.addTextChild("", "b");
			fail("Should have thrown exception.");
		} catch (Exception ex) {
			// Success!
		}

		// Make sure the ElementNode was never modified.
		assertEquals(P + "<root/>", root.toString());
	}
}
