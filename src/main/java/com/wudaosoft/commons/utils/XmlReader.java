/**
 *    Copyright 2009-2018 Wudao Software Studio(wudaosoft.com)
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.wudaosoft.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StreamUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.wudaosoft.commons.xml.XmlException;

/** 
 * @author Changsoul Wu
 * 
 */
public class XmlReader {
	
	private static final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class<?>, JAXBContext>(64);
	
	private static final Logger logger = LoggerFactory.getLogger(XmlReader.class);
	
	public static String toXml(Object o) {
		try {
			StringWriter writer = new StringWriter();
			writeToResult(o, writer);
			
			return writer.toString();
		} catch (IOException ex) {
			logger.warn("Could not write to result", ex);
		}
		
		return null;
	}
	
	public static <T> T readFromXmlString(Class<? extends T> clazz, String xml) throws IOException {
		return readFromSource(clazz, new StreamSource(new StringReader(xml)));
	}
	
	public static <T> T readFromInputStream(Class<? extends T> clazz, InputStream inputStream) throws IOException {
		return readFromSource(clazz, new StreamSource(inputStream));
	}
	
	public static <T> T readFromByteArray(Class<? extends T> clazz, byte[] bytes) throws IOException {
		return readFromSource(clazz, new StreamSource(new ByteArrayInputStream(bytes)));
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readFromXMLStreamReader(Class<? extends T> clazz, XMLStreamReader streamReader) throws IOException {
		try {
			moveToNextElement(streamReader);
			Unmarshaller unmarshaller = createUnmarshaller(clazz);
			if (clazz.isAnnotationPresent(XmlRootElement.class)) {
				return (T)unmarshaller.unmarshal(streamReader);
			}
			else {
				JAXBElement<?> jaxbElement = unmarshaller.unmarshal(streamReader, clazz);
				return (T)jaxbElement.getValue();
			}
		}
		catch (NullPointerException ex) {
			throw new XmlException("NPE while unmarshalling. " +
					"This can happen on JDK 1.6 due to the presence of DTD " +
					"declarations, which are disabled.", ex);
		}
		catch (UnmarshalException ex) {
			throw new XmlException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
		}
		catch (JAXBException ex) {
			throw new XmlException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
		catch (XMLStreamException ex) {
			throw new XmlException(ex.getMessage(), ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T readFromSource(Class<? extends T> clazz, Source source) throws IOException {
		try {
			source = processSource(source);
			Unmarshaller unmarshaller = createUnmarshaller(clazz);
			if (clazz.isAnnotationPresent(XmlRootElement.class)) {
				return (T)unmarshaller.unmarshal(source);
			}
			else {
				JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, clazz);
				return (T)jaxbElement.getValue();
			}
		}
		catch (NullPointerException ex) {
			throw new XmlException("NPE while unmarshalling. " +
					"This can happen on JDK 1.6 due to the presence of DTD " +
					"declarations, which are disabled.", ex);
		}
		catch (UnmarshalException ex) {
			throw new XmlException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
			
		}
		catch (JAXBException ex) {
			throw new XmlException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
	}

	protected static Source processSource(Source source) {
		if (source instanceof StreamSource) {
			StreamSource streamSource = (StreamSource) source;
			InputSource inputSource = new InputSource(streamSource.getInputStream());
			try {
				XMLReader xmlReader = XMLReaderFactory.createXMLReader();
				xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
				String featureName = "http://xml.org/sax/features/external-general-entities";
				xmlReader.setFeature(featureName, false);
				xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
				return new SAXSource(xmlReader, inputSource);
			}
			catch (SAXException ex) {
				logger.warn("Processing of external entities could not be disabled", ex);
				return source;
			}
		}
		else {
			return source;
		}
	}
	
	/**
	 * Create a new {@link Marshaller} for the given class.
	 * @param clazz the class to create the marshaller for
	 * @return the {@code Marshaller}
	 * @throws XmlException in case of JAXB errors
	 */
	protected static final Marshaller createMarshaller(Class<?> clazz) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			Marshaller marshaller = jaxbContext.createMarshaller();
			customizeMarshaller(marshaller);
			return marshaller;
		}
		catch (JAXBException ex) {
			throw new XmlException(
					"Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Customize the {@link Marshaller} created by this
	 * message converter before using it to write the object to the output.
	 * @param marshaller the marshaller to customize
	 * @since 4.0.3
	 * @see #createMarshaller(Class)
	 */
	protected static void customizeMarshaller(Marshaller marshaller) {
	}

	/**
	 * Create a new {@link Unmarshaller} for the given class.
	 * @param clazz the class to create the unmarshaller for
	 * @return the {@code Unmarshaller}
	 * @throws XmlException in case of JAXB errors
	 */
	protected static final Unmarshaller createUnmarshaller(Class<?> clazz) throws JAXBException {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			customizeUnmarshaller(unmarshaller);
			return unmarshaller;
		}
		catch (JAXBException ex) {
			throw new XmlException(
					"Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Customize the {@link Unmarshaller} created by this
	 * message converter before using it to read the object from the input.
	 * @param unmarshaller the unmarshaller to customize
	 * @since 4.0.3
	 * @see #createUnmarshaller(Class)
	 */
	protected static void customizeUnmarshaller(Unmarshaller unmarshaller) {
	}

	protected static void writeToResult(Object o, Writer writer) throws IOException {
		try {
			Class<?> clazz = ClassUtils.getUserClass(o);
			Marshaller marshaller = createMarshaller(clazz);
			setCharset("utf-8", marshaller);
			marshaller.marshal(o, writer);
		}
		catch (MarshalException ex) {
			throw new XmlException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
		}
		catch (JAXBException ex) {
			throw new XmlException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Return a {@link JAXBContext} for the given class.
	 * @param clazz the class to return the context for
	 * @return the {@code JAXBContext}
	 * @throws XmlException in case of JAXB errors
	 */
	protected static final JAXBContext getJaxbContext(Class<?> clazz) {
		Assert.notNull(clazz, "'clazz' must not be null");
		JAXBContext jaxbContext = jaxbContexts.get(clazz);
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(clazz);
				jaxbContexts.putIfAbsent(clazz, jaxbContext);
			}
			catch (JAXBException ex) {
				throw new XmlException(
						"Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}

	private static void setCharset(String charSet, Marshaller marshaller) throws PropertyException {
		if (charSet != null) {
			marshaller.setProperty(Marshaller.JAXB_ENCODING, charSet);
		}
	}
	
	public static XMLStreamReader createXMLStreamReader(InputStream body) throws XMLStreamException {
			XMLStreamReader streamReader = createXmlInputFactory().createXMLStreamReader(body);
			return streamReader;
	}
	
	public static String getElementText(String key, InputStream body) throws IOException{
		try {
			return getElementValue(String.class, key, createXMLStreamReader(body));
		} catch (XMLStreamException ex) {
			throw new XmlException(ex.getMessage(), ex);
		}
	}
	
	public static String getElementText(String key, byte[] bytes) throws IOException{
		try {
			return getElementValue(String.class, key, createXMLStreamReader(new ByteArrayInputStream(bytes)));
		} catch (XMLStreamException ex) {
			throw new XmlException(ex.getMessage(), ex);
		}
	}
	
	public static String getElementText(String key, XMLStreamReader streamReader) throws IOException{
		return getElementValue(String.class, key, streamReader);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getElementValue(Class<? extends T> clazz, String key, XMLStreamReader streamReader) throws IOException {

		try {
			
			T result = null;
			int event = moveToFirstChildOfRootElement(streamReader);

			while (event != XMLStreamReader.END_DOCUMENT) {
				String name = streamReader.getName().toString();
				if (key.equals(name)) {
					if (clazz.isAnnotationPresent(XmlRootElement.class)) {
						result = (T) createUnmarshaller(clazz).unmarshal(streamReader);
					} else if (clazz.isAnnotationPresent(XmlType.class)) {
						result = (T) createUnmarshaller(clazz).unmarshal(streamReader, clazz).getValue();
					} else {
						
						//streamReader.next();
						
						String value = streamReader.getElementText();
						if (String.class == clazz) {
							result = (T) value;
						} else if (Integer.class == clazz) {
							result = (T) Integer.valueOf(value.trim());
						} else if (Long.class == clazz) {
							result = (T) Long.valueOf(value.trim());
						} else if (Float.class == clazz) {
							result = (T) Float.valueOf(value.trim());
						} else if (Double.class == clazz) {
							result = (T) Integer.valueOf(value.trim());
						} else if (BigDecimal.class == clazz) {
							result = (T) new BigDecimal(value.trim());
						} else {
							result = (T) value;
						}
					}
					
					break;
				}else {
					streamReader.next();
				}
				event = moveToNextElement(streamReader);
			}
			return result;
		} catch (NumberFormatException ex) {
			throw new XmlException("Could not convert element text to [" + clazz + "]: " + ex.getMessage(), ex);
		} catch (ClassCastException ex) {
			throw new XmlException("Could not convert java.lang.String to [" + clazz + "]: " + ex.getMessage(), ex);
		} catch (UnmarshalException ex) {
			throw new XmlException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
		} catch (JAXBException ex) {
			throw new XmlException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
		} catch (XMLStreamException ex) {
			throw new XmlException(ex.getMessage(), ex);
		}
	}
	
	private static int moveToFirstChildOfRootElement(XMLStreamReader streamReader) throws XMLStreamException {
		// root
		int event = streamReader.next();
		while (event != XMLStreamReader.START_ELEMENT) {
			event = streamReader.next();
		}

		// first child
		event = streamReader.next();
		while ((event != XMLStreamReader.START_ELEMENT) && (event != XMLStreamReader.END_DOCUMENT)) {
			event = streamReader.next();
		}
		return event;
	}

	private static int moveToNextElement(XMLStreamReader streamReader) throws XMLStreamException {
		int event = streamReader.getEventType();
		while (event != XMLStreamReader.START_ELEMENT && event != XMLStreamReader.END_DOCUMENT) {
			event = streamReader.next();
		}
		return event;
	}
	
	protected static XMLInputFactory createXmlInputFactory() {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		inputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		inputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
		return inputFactory;
	}
	
	private static final EntityResolver NO_OP_ENTITY_RESOLVER = new EntityResolver() {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) {
			return new InputSource(new StringReader(""));
		}
	};
	
	private static final XMLResolver NO_OP_XML_RESOLVER = new XMLResolver() {
		@Override
		public Object resolveEntity(String publicID, String systemID, String base, String ns) {
			return StreamUtils.emptyInput();
		}
	};
}
