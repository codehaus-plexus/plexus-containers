package org.codehaus.plexus.metadata.merge.support;

/*
 * The MIT License
 *
 * Copyright (c) 2006, The Codehaus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.metadata.merge.MergeException;
import org.codehaus.plexus.metadata.merge.MergeStrategy;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Parent;
import org.jdom2.filter.Filter;

/**
 * @author <a href='mailto:rahul.thakur.xdev@gmail.com'>Rahul Thakur</a>
 */
/**
 * @author khmarbaise
 *
 */
public abstract class AbstractMergeableSupport
    implements Mergeable
{
    /**
     * Wrapped JDOM element.
     */
    protected Element element;

    /**
     * The default merging strategy used.
     */
    private static final MergeStrategy DEFAULT_MERGE_STRATEGY = MergeStrategies.DEEP;

    /**
     * @param element {@link Element}
     */
    public AbstractMergeableSupport( Element element )
    {
        this.element = element;
    }

    /** {@inheritDoc} */
    public abstract void merge( Mergeable me )
        throws MergeException;

    /**
     * Determines if the passed in {@link Mergeable} was of same type as this
     * class.
     *
     * @param me {@link Mergeable} instance to test.
     * @return <code>true</code> if the passed in Mergeable can be merged with
     *         the current Mergeable.
     */
    protected abstract boolean isExpectedElementType( Mergeable me );

    // ----------------------------------------------------------------------
    // Methods delegated on wrapped JDOM element.
    // ----------------------------------------------------------------------

    /**
     * @param collection {@link Collection}
     * @return {@link Element}.
     */
    public Element addContent( Collection collection )
    {
        return element.addContent( collection );
    }

    /**
     * @param child {@link Content}
     * @return {@link Element}.
     */
    public Element addContent( Content child )
    {
        return element.addContent( child );
    }

    /**
     * @param index The index.
     * @param c {@link Collection}
     * @return {@link Element}.
     */
    public Element addContent( int index, Collection c )
    {
        return element.addContent( index, c );
    }

    /**
     * @param index The index.
     * @param child {@link Content}
     * @return {@link Element}.
     */
    public Element addContent( int index, Content child )
    {
        return element.addContent( index, child );
    }

    /**
     * @param str The content to be added.
     * @return {@link Element}.
     */
    public Element addContent( String str )
    {
        return element.addContent( str );
    }

    /**
     * @param additional {@link Namespace}
     */
    public void addNamespaceDeclaration( Namespace additional )
    {
        element.addNamespaceDeclaration( additional );
    }

    public Object clone()
    {
        return element.clone();
    }

    public List cloneContent()
    {
        return element.cloneContent();
    }

    public Content detach()
    {
        return element.detach();
    }

    /** {@inheritDoc} */
    public boolean equals( Object obj )
    {
        return element.equals( obj );
    }

    /**
     * @return list of Namespaces.
     */
    public List getAdditionalNamespaces()
    {
        return element.getAdditionalNamespaces();
    }

    /**
     * @param name The name.
     * @param ns {@link Namespace}
     * @return {@link Attribute}
     */
    public Attribute getAttribute( String name, Namespace ns )
    {
        return element.getAttribute( name, ns );
    }

    /**
     * @param name The name of the attribute.
     * @return {@link Attribute}
     */
    public Attribute getAttribute( String name )
    {
        return element.getAttribute( name );
    }

    /**
     * @return list {@link Attribute}
     */
    public List getAttributes()
    {
        return element.getAttributes();
    }

    /**
     * @see org.jdom2.Element#getAttributeValue(java.lang.String,org.jdom2.Namespace,java.lang.String)
     * @param name The name of the attribute.
     * @param ns The {@link Namespace}
     * @param def the default value.
     * @return The value of the attribute.
     */
    public String getAttributeValue( String name, Namespace ns, String def )
    {
        return element.getAttributeValue( name, ns, def );
    }

    /**
     * @see org.jdom2.Element#getAttributeValue(java.lang.String,org.jdom2.Namespace)
     * @param name The name of the attribute.
     * @param ns The {@link Namespace}
     * @return The value of the attribute.
     */
    public String getAttributeValue( String name, Namespace ns )
    {
        return element.getAttributeValue( name, ns );
    }

    /**
     * @see org.jdom2.Element#getAttributeValue(java.lang.String,java.lang.String)
     * @param name The name of the attribute.
     * @param def the default value.
     * @return The value of the attribute.
     */
    public String getAttributeValue( String name, String def )
    {
        return element.getAttributeValue( name, def );
    }

    /**
     * @see org.jdom2.Element#getAttributeValue(java.lang.String)
     * @param name The name of the attribute.
     * @return The value of the attribute.
     */
    public String getAttributeValue( String name )
    {
        return element.getAttributeValue( name );
    }

    /**
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return {@link Element}
     * @see org.jdom2.Element#getChild(java.lang.String,org.jdom2.Namespace)
     */
    public Element getChild( String name, Namespace ns )
    {
        return element.getChild( name, ns );
    }

    /**
     * @param name The name of the child.
     * @return {@link Element}
     * @see org.jdom2.Element#getChild(java.lang.String)
     */
    public Element getChild( String name )
    {
        return element.getChild( name );
    }

    /**
     * @return list of {@link Element}
     * @see org.jdom2.Element#getChildren()
     */
    public List getChildren()
    {
        return element.getChildren();
    }

    /**
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return list {@link Element}
     * @see org.jdom2.Element#getChildren(java.lang.String,org.jdom2.Namespace)
     */
    public List getChildren( String name, Namespace ns )
    {
        return element.getChildren( name, ns );
    }

    /**
     * @param name The name.
     * @return list {@link Element}
     * @see org.jdom2.Element#getChildren(java.lang.String)
     */
    public List getChildren( String name )
    {
        return element.getChildren( name );
    }

    /**
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return the child text.
     * @see org.jdom2.Element#getChildText(java.lang.String,org.jdom2.Namespace)
     */
    public String getChildText( String name, Namespace ns )
    {
        return element.getChildText( name, ns );
    }

    /**
     * @param name The name of the child.
     * @return the child text.
     * @see org.jdom2.Element#getChildText(java.lang.String)
     */
    public String getChildText( String name )
    {
        return element.getChildText( name );
    }

    /**
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return the child text.
     * @see org.jdom2.Element#getChildTextNormalize(java.lang.String,org.jdom2.Namespace)
     */
    public String getChildTextNormalize( String name, Namespace ns )
    {
        return element.getChildTextNormalize( name, ns );
    }

    /**
     * @param name The name of the child.
     * @return the child text.
     * @see org.jdom2.Element#getChildTextNormalize(java.lang.String)
     */
    public String getChildTextNormalize( String name )
    {
        return element.getChildTextNormalize( name );
    }

    /**
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return the child text.
     * @see org.jdom2.Element#getChildTextTrim(java.lang.String,org.jdom2.Namespace)
     */
    public String getChildTextTrim( String name, Namespace ns )
    {
        return element.getChildTextTrim( name, ns );
    }

    /**
     * @param name The name of the child.
     * @return the child text.
     * @see org.jdom2.Element#getChildTextTrim(java.lang.String)
     */
    public String getChildTextTrim( String name )
    {
        return element.getChildTextTrim( name );
    }

    /**
     * @see org.jdom2.Element#getContent()
     * @return list of content.
     */
    public List getContent()
    {
        return element.getContent();
    }

    /**
     * @param filter {@link Filter}
     * @return list of content.
     * @see org.jdom2.Element#getContent(org.jdom2.filter.Filter)
     */
    public List getContent( Filter filter )
    {
        return element.getContent( filter );
    }

    /**
     * @param index The index.
     * @return the content.
     * @see org.jdom2.Element#getContent(int)
     */
    public Content getContent( int index )
    {
        return element.getContent( index );
    }

    /**
     * @return The content size.
     * @see org.jdom2.Element#getContentSize()
     */
    public int getContentSize()
    {
        return element.getContentSize();
    }

    /**
     * @return {@link Iterator} of descendants.
     * @see org.jdom2.Element#getDescendants()
     */
    public Iterator getDescendants()
    {
        return element.getDescendants();
    }

    /**
     * @param filter {@link Filter}
     * @return {@link Iterator} of descendants.
     * @see org.jdom2.Element#getDescendants(org.jdom2.filter.Filter)
     */
    public Iterator getDescendants( Filter filter )
    {
        return element.getDescendants( filter );
    }

    /**
     * @return the document.
     * @see org.jdom2.Content#getDocument()
     */
    public Document getDocument()
    {
        return element.getDocument();
    }

    /**
     * @return The name of the element.
     * @see org.jdom2.Element#getName()
     */
    public String getName()
    {
        return element.getName();
    }

    /**
     * @return {@link Namespace}
     * @see org.jdom2.Element#getNamespace()
     */
    public Namespace getNamespace()
    {
        return element.getNamespace();
    }

    /**
     * @param prefix The prefix.
     * @return {@link Namespace}
     * @see org.jdom2.Element#getNamespace(java.lang.String)
     */
    public Namespace getNamespace( String prefix )
    {
        return element.getNamespace( prefix );
    }

    /**
     * @return the namespace prefix.
     * @see org.jdom2.Element#getNamespacePrefix()
     */
    public String getNamespacePrefix()
    {
        return element.getNamespacePrefix();
    }

    /**
     * @return the namespace URI.
     * @see org.jdom2.Element#getNamespaceURI()
     */
    public String getNamespaceURI()
    {
        return element.getNamespaceURI();
    }

    /**
     * @return The parent.
     * @see org.jdom2.Content#getParent()
     */
    public Parent getParent()
    {
        return element.getParent();
    }

    /**
     * @return the parent {@link Element}
     * @see org.jdom2.Content#getParentElement()
     */
    public Element getParentElement()
    {
        return element.getParentElement();
    }

    /**
     * @return The qualified name.
     * @see org.jdom2.Element#getQualifiedName()
     */
    public String getQualifiedName()
    {
        return element.getQualifiedName();
    }

    /**
     * @return The text.
     * @see org.jdom2.Element#getText()
     */
    public String getText()
    {
        return element.getText();
    }

    /**
     * @return the normalized text.
     * @see org.jdom2.Element#getTextNormalize()
     */
    public String getTextNormalize()
    {
        return element.getTextNormalize();
    }

    /**
     * @return the trimmed text.
     * @see org.jdom2.Element#getTextTrim()
     */
    public String getTextTrim()
    {
        return element.getTextTrim();
    }

    /**
     * @return the element value.
     * @see org.jdom2.Element#getValue()
     */
    public String getValue()
    {
        return element.getValue();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return element.hashCode();
    }

    /**
     * @param child The child.
     * @return the index.
     * @see org.jdom2.Element#indexOf(org.jdom2.Content)
     */
    public int indexOf( Content child )
    {
        return element.indexOf( child );
    }

    /**
     * @see org.jdom2.Element#isAncestor(org.jdom2.Element)
     * @param element {@link Element}.
     * @return true/false.
     */
    public boolean isAncestor( Element element )
    {
        return element.isAncestor( element );
    }

    /**
     * @see org.jdom2.Element#isRootElement()
     * @return true/false.
     */
    public boolean isRootElement()
    {
        return element.isRootElement();
    }

    /**
     * @see org.jdom2.Element#removeAttribute(org.jdom2.Attribute)
     * @param attribute {@link Attribute}
     * @return true/false.
     */
    public boolean removeAttribute( Attribute attribute )
    {
        return element.removeAttribute( attribute );
    }

    /**
     * @see org.jdom2.Element#removeAttribute(java.lang.String,org.jdom2.Namespace)
     * @param name The name of the attribute.
     * @param ns The {@link Namespace}
     * @return true/false.
     */
    public boolean removeAttribute( String name, Namespace ns )
    {
        return element.removeAttribute( name, ns );
    }

    /**
     * @see org.jdom2.Element#removeAttribute(java.lang.String)
     * @param name The mame of the attribute.
     * @return true/false.
     */
    public boolean removeAttribute( String name )
    {
        return element.removeAttribute( name );
    }

    /**
     * @see org.jdom2.Element#removeChild(java.lang.String,org.jdom2.Namespace)
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return true/false.
     */
    public boolean removeChild( String name, Namespace ns )
    {
        return element.removeChild( name, ns );
    }

    /**
     * @see org.jdom2.Element#removeChild(java.lang.String)
     * @param name The name of the child.
     * @return true/false.
     */
    public boolean removeChild( String name )
    {
        return element.removeChild( name );
    }

    /**
     * @see org.jdom2.Element#removeChildren(java.lang.String,org.jdom2.Namespace)
     * @param name The name of the child.
     * @param ns {@link Namespace}
     * @return true/false.
     */
    public boolean removeChildren( String name, Namespace ns )
    {
        return element.removeChildren( name, ns );
    }

    /**
     * @see org.jdom2.Element#removeChildren(java.lang.String)
     * @param name name of the child.
     * @return true/false.
     */
    public boolean removeChildren( String name )
    {
        return element.removeChildren( name );
    }

    /**
     * @see org.jdom2.Element#removeContent()
     * @return list of elements.
     */
    public List removeContent()
    {
        return element.removeContent();
    }

    /**
     * @see org.jdom2.Element#removeContent(org.jdom2.Content)
     * @param child {@link Content}
     * @return true/false.
     */
    public boolean removeContent( Content child )
    {
        return element.removeContent( child );
    }

    /**
     * @see org.jdom2.Element#removeContent(org.jdom2.filter.Filter)
     * @param filter {@link Filter}.
     * @return list of elements.
     */
    public List removeContent( Filter filter )
    {
        return element.removeContent( filter );
    }

    /**
     * @see org.jdom2.Element#removeContent(int)
     * @param index The index.
     * @return {@link Content}
     */
    public Content removeContent( int index )
    {
        return element.removeContent( index );
    }

    /**
     * @see org.jdom2.Element#removeNamespaceDeclaration(org.jdom2.Namespace)
     * @param additionalNamespace {@link Namespace}.
     */
    public void removeNamespaceDeclaration( Namespace additionalNamespace )
    {
        element.removeNamespaceDeclaration( additionalNamespace );
    }

    /**
     * @param attribute {@link Attribute}
     * @return {@link Element}.
     * @see org.jdom2.Element#setAttribute(org.jdom2.Attribute)
     */
    public Element setAttribute( Attribute attribute )
    {
        return element.setAttribute( attribute );
    }

    /**
     * @see org.jdom2.Element#setAttribute(java.lang.String,java.lang.String,org.jdom2.Namespace)
     * @param name name of the attribute.
     * @param value The value of the attribute.
     * @param ns {@link Namespace}.
     * @return {@link Element}
     */
    public Element setAttribute( String name, String value, Namespace ns )
    {
        return element.setAttribute( name, value, ns );
    }

    /**
     * @param name name of the attribute.
     * @param value The value of the attribute.
     * @return {@link Element}
     * @see org.jdom2.Element#setAttribute(java.lang.String,java.lang.String)
     */
    public Element setAttribute( String name, String value )
    {
        return element.setAttribute( name, value );
    }

    /**
     * @param newAttributes list of new attributes. 
     * @return {@link Element}
     */
    public Element setAttributes( List newAttributes )
    {
        return element.setAttributes( newAttributes );
    }

    /**
     * @param newContent {@link Collection}
     * @return {@link Element}
     * @see org.jdom2.Element#setContent(java.util.Collection)
     */
    public Element setContent( Collection newContent )
    {
        return element.setContent( newContent );
    }

    /**
     * @param child {@link Content}
     * @return {@link Element}
     * @see org.jdom2.Element#setContent(org.jdom2.Content)
     */
    public Element setContent( Content child )
    {
        return element.setContent( child );
    }

    /**
     * @param index The index.
     * @param collection {@link Collection}
     * @return {@link Parent}
     * @see org.jdom2.Element#setContent(int,java.util.Collection)
     */
    public Parent setContent( int index, Collection collection )
    {
        return element.setContent( index, collection );
    }

    /**
     * @param index index.
     * @param child {@link Content}
     * @return {@link Element}
     * @see org.jdom2.Element#setContent(int,org.jdom2.Content)
     */
    public Element setContent( int index, Content child )
    {
        return element.setContent( index, child );
    }

    /**
     * @param name The name of the element.
     * @return {@link Element}
     * @see org.jdom2.Element#setName(java.lang.String)
     */
    public Element setName( String name )
    {
        return element.setName( name );
    }

    /**
     * @param namespace {@link Namespace}
     * @see org.jdom2.Element#setNamespace(org.jdom2.Namespace)
     * @return {@link Element}
     */
    public Element setNamespace( Namespace namespace )
    {
        return element.setNamespace( namespace );
    }

    /**
     * @see org.jdom2.Element#setText(java.lang.String)
     * @param text The text to be set.
     * @return {@link Element}
     */
    public Element setText( String text )
    {
        return element.setText( text );
    }

    /**
     * {@link org.jdom2.Element#toString()}
     * {@inheritDoc}
     */
    public String toString()
    {
        return element.toString();
    }

    /**
     * Returns the wrapped up JDom {@link Element} instance.
     * {@inheritDoc}
     */
    public Element getElement()
    {
        return this.element;
    }

    /**
     * Sub classes should override if they wish to provide a different
     * combination of composite keys for determining conflicts.
     * @param defaultList the default list. 
     * @return the default list.
     */
    protected List getElementNamesForConflictResolution( List defaultList )
    {
        return defaultList;
    }

    /**
     * Returns the default {@link MergeStrategy} instance.
     * @return {@link MergeStrategy}
     */
    protected MergeStrategy getDefaultMergeStrategy()
    {
        return DEFAULT_MERGE_STRATEGY;
    }

}
