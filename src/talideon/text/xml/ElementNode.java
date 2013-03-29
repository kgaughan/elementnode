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

import java.util.Map;
import java.util.List;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Lightweight representation of an XML element or document.
 *
 * @author kgaughan
 */
public class ElementNode extends Node {

	private final String _name;
	private final Map    _attributes = new TreeMap();
	private final List   _children   = new LinkedList();

	/**
	 * Creates an new <code>ElementNode</code>.
	 * 
	 * 
	 * @param  name  Its name.
	 */
	public ElementNode(final String name) {
		if (!XmlUtils.isValidName(name)) {
			throw new XmlException("Bad element name: " + name);
		}
		_name = name;
	}

	/**
	 * Creates an new <code>ElementNode</code>.
	 * 
	 * 
	 * @param  name        Its name.
	 * @param  attributes  Initial attributes.
	 */
	public ElementNode(final String name, final Map attributes) {
		this(name);
		try {
			_attributes.putAll(attributes);
		} catch (NullPointerException npex) {
			throw new XmlException("Null attribute map passed.", npex);
		}
	}

	/**
	 * Adds an attribute to this node.
	 *
	 * @param  name   Attribute name.
	 * @param  value  Its value.
	 *
	 * @return Self.
	 */
	public ElementNode addAttribute(final String name, final String value) {
		if (!XmlUtils.isValidName(name) || value == null) {
			throw new XmlException("Bad element attribute.");
		}
		_attributes.put(name, value);
		return this;
	}

	/**
	 * Creates and appends a new text node within this element.
	 *
	 * @param  text  Contents of text node.
	 *
	 * @return Self.
	 */
	public ElementNode addText(final String text) {
		if (text != null && text.length() > 0) {
			_children.add(new TextNode(text));
		}
		return this;
	}

	/**
	 * Creates and appends a new child node.
	 * 
	 * @param  name  Element name.
	 *
	 * @return New child node.
	 */
	public ElementNode addChild(final String name) {
		final ElementNode child = new ElementNode(name);
		_children.add(child);
		return child;
	}

	/**
	 * Creates and appends a new child node.
	 * 
	 * @param  name        Element name.
	 * @param  attributes  Attributes it should have.
	 *
	 * @return New child node.
	 */
	public ElementNode addChild(final String name, final Map attributes) {
		final ElementNode child = new ElementNode(name, attributes);
		_children.add(child);
		return child;
	}

	/**
	 * Creates and appends a new child node.
	 * 
	 * @param  name  Element name.
	 * @param  text  Text it contains.
	 *
	 * @return New child node.
	 */
	public ElementNode addChild(final String name, final String text) {
		final ElementNode child = new ElementNode(name);
		child.addText(text);
		_children.add(child);
		return child;
	}

	/**
	 * Creates and appends a new child node.
	 *
	 * @param  name        Element name.
	 * @param  text        Text it contains.
	 * @param  attributes  Attributes it should have.
	 *
	 * @return New child node.
	 */
	public ElementNode addChild(final String name, final String text, final Map attributes) {
		final ElementNode child = new ElementNode(name, attributes);
		child.addText(text);
		_children.add(child);
		return child;
	}

	/**
	 * Shortcut method to allow a simple child element containing text to
	 * be added.
	 *
	 * @param  name  Child name.
	 * @param  text  Text it contains.
	 *
	 * @return Self.
	 */
	public ElementNode addTextChild(final String name, final String text) {
		addChild(name, text);
		return this;
	}

	/**
	 * Adds a DOM node as a child.
	 *
	 * @param  node  DOM node to embed.
	 *
	 * @return Self.
	 */
	public ElementNode addChild(final org.w3c.dom.Node node) {
		if (node == null) {
			throw new XmlException("Null DOM Node.");
		}
		_children.add(node);
		return this;
	}

	/**
	 * Converts this node into a DOM node.
	 */
	public org.w3c.dom.Node toXml() {
		org.w3c.dom.Document doc = XmlUtils.newDocument();
		doc.appendChild(toXml(doc));
		doc.normalize();
		return doc;
	}

	org.w3c.dom.Node toXml(final org.w3c.dom.Document root) {
		final org.w3c.dom.Element self = root.createElement(_name);

		// Convert attributes.
		Iterator iter = _attributes.keySet().iterator();
		while (iter.hasNext()) {
			final String name = iter.next().toString();
			self.setAttribute(name, _attributes.get(name).toString());
		}

		// Convert children.
		iter = _children.iterator();
		while (iter.hasNext()) {
			final Node child = (Node) iter.next();
			self.appendChild(child.toXml(root));
		}

		return self;
	}

	/**
	 * Converts this node into an XML string.
	 */
	public String toString() {
		// Now, in a sane world, I'd be able to do all this just by
		// calling toString() on the node retutned by toXml(), but
		// noooo...
		return XmlUtils.toString(toXml());
	}
}

/**
 * Representation of a lightweight arbitrary XML node. This would be an
 * interface if it was possible to have interfaces with package-only
 * visibility.
 *
 * @author kgaughan
 */
abstract class Node {

	/**
	 * Converts this text node into a DOM text node.
	 *
	 * @param  root  DOM Document into which this node will eventually be
	 *               embedded.
	 *
	 * @return This node as a DOM node.
	 */
	abstract org.w3c.dom.Node toXml(final org.w3c.dom.Document root);
}

/**
 * Lightweight representation of an XML text node.
 *
 * @author kgaughan
 */
class TextNode extends Node {

	private final String _text;

	/**
	 * Creates a new text node.
	 */
	TextNode(final String text) {
		_text = text;
	}

	org.w3c.dom.Node toXml(final org.w3c.dom.Document root) {
		return root.createTextNode(_text);
	}
}

/**
 * Wrapper for a DOM node.
 */
class DOMNode extends Node {

	private final org.w3c.dom.Node _node;

	/**
	 * Creates a new DOM Node wrapper.
	 */
	DOMNode(final org.w3c.dom.Node node) {
		_node = node;
	}

	org.w3c.dom.Node toXml(final org.w3c.dom.Document root) {
		return _node;
	}
}