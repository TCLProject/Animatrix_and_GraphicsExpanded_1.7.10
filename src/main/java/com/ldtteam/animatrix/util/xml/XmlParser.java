package com.ldtteam.animatrix.util.xml;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * Reads an XML file and stores all the data in {@link XmlNode} objects,
 * allowing for easy access to the data contained in the XML file.
 * 
 * @author Karl
 *
 */
@SideOnly(Side.CLIENT)
public class XmlParser {

	private static final Pattern DATA = Pattern.compile(">(.+?)<");
	private static final Pattern START_TAG = Pattern.compile("<(.+?)>");
	private static final Pattern ATTR_NAME = Pattern.compile("(.+?)=");
	private static final Pattern ATTR_VAL = Pattern.compile("\"(.+?)\"");
	private static final Pattern CLOSED = Pattern.compile("(</|/>)");

	/**
	 * Reads an XML file and stores all the data in {@link XmlNode} objects,
	 * allowing for easy access to the data contained in the XML file.
	 * 
	 * @param file - the XML file
	 * @return The root node of the XML structure.
	 */
	public static XmlNode loadXmlFile(final ResourceLocation file) throws Exception {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(file).getInputStream()));
        reader.readLine();
        final XmlNode node = loadNode(reader);
        reader.close();
        return node;
	}

	private static XmlNode loadNode(final BufferedReader reader) throws Exception {
		final String line = reader.readLine().trim();
		if (line.startsWith("</")) {
			return null;
		}
		final String[] startTagParts = getStartTag(line).split(" ");
		final XmlNode node = new XmlNode(startTagParts[0].replace("/", ""));
		addAttributes(startTagParts, node);
		addData(line, node);
		if (CLOSED.matcher(line).find()) {
			return node;
		}
		XmlNode child = null;
		while ((child = loadNode(reader)) != null) {
			node.addChild(child);
		}
		return node;
	}

	private static void addData(final String line, final XmlNode node) {
		final Matcher matcher = DATA.matcher(line);
		if (matcher.find()) {
			node.setData(matcher.group(1));
		}
	}

	private static void addAttributes(final String[] titleParts, final XmlNode node) {
		for (int i = 1; i < titleParts.length; i++) {
			if (titleParts[i].contains("=")) {
				addAttribute(titleParts[i], node);
			}
		}
	}

	private static void addAttribute(final String attributeLine, final XmlNode node) {
		final Matcher nameMatch = ATTR_NAME.matcher(attributeLine);
		nameMatch.find();
		final Matcher valMatch = ATTR_VAL.matcher(attributeLine);
		valMatch.find();
		node.addAttribute(nameMatch.group(1), valMatch.group(1));
	}

	private static String getStartTag(final String line) {
		final Matcher match = START_TAG.matcher(line);
		match.find();
		return match.group(1);
	}

}
